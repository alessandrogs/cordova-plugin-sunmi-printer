package br.com.ifabrica.sunmi.sunmiprinter;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;

import android.content.Context;
import android.util.Log;
import android.util.Base64;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.google.zxing.BarcodeFormat;
import com.sunmi.controller.ICallback;
import com.sunmi.impl.V1Printer;

public class SunmiPrinter extends CordovaPlugin {

    private V1Printer printer;
    private ICallback callback;
//    private Bitmap mBitmap;
    private Context context;
    private static String TAG = "SunmiPrinter";

    private enum Option {
        printText,
        printQRCode,
        printBarCode,
        printImage,
        printerTest
    }

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        context = cordova.getActivity().getApplicationContext();
        printer = new V1Printer(context);

        callback = new ICallback() {

            @Override
            public void onRunResult(boolean isSuccess) {
                Log.i(TAG, "onRunResult:" + isSuccess);
            }

            @Override
            public void onReturnString(String result) {
                Log.i(TAG, "onReturnString:" + result);
            }

            @Override
            public void onRaiseException(int code, String msg) {
                Log.i(TAG, "onRaiseException:" + code + ":" + msg);
            }
        };
        printer.setCallback(callback);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Option option = null;
        try {
            option = Option.valueOf(action);
        } catch (Exception e) {
            return false;
        }
        final JSONArray arr = args;
        switch (option) {
            case printText:
                ThreadPoolManager.getInstance().executeTask(new Runnable() {

                    @Override
                    public void run() {

                        String text;
                        int fontSize;
                        int align;
                        int LF;

                        printer.beginTransaction();
                        printer.printerInit();

                        try {
//                            JSONArray jsonArray = new JSONArray(arr);
//                        JSONArray jsonPersonData = jsonArray.getJSONArray(1);
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject item = arr.getJSONObject(i);
                                text = item.getString("text");
                                fontSize = item.getInt("fontSize");
                                align = item.getInt("align");
                                LF = item.getInt("LF");
                                printer.setAlignment(align);
                                printer.setFontSize(fontSize);
                                printer.printText(text);
                                printer.lineWrap(LF);
                            }
                        } catch (JSONException e) {
                        }

                        printer.commitTransaction();
                    }
                });
                break;

            case printQRCode:
                ThreadPoolManager.getInstance().executeTask(new Runnable() {

                    @Override
                    public void run() {

                        String text;
                        int width;
                        int height;
                        int align;
                        int LF;

                        printer.beginTransaction();
                        printer.printerInit();

                        try {
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject item = arr.getJSONObject(i);
                                text = item.getString("text");
                                width = item.getInt("width");
                                align = item.getInt("align");
                                LF = item.getInt("LF");

                                printer.setAlignment(align);
//                                printer.printBarCode(text, BarcodeFormat.QR_CODE, width, height);
                                printer.printQRCode(text, width);
//                                printer.printDoubleQRCode("http://www.sunmi.com", "http://www.baidu.com", 180);
                                printer.lineWrap(LF);
                            }
                        } catch (JSONException e) {
                        }
                        printer.commitTransaction();
                    }
                });
                break;

            case printBarCode:
                ThreadPoolManager.getInstance().executeTask(new Runnable() {

                    @Override
                    public void run() {

                        String text;
                        int width;
                        int height;
                        int align;
                        int LF;

                        printer.beginTransaction();
                        printer.printerInit();

                        try {
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject item = arr.getJSONObject(i);
                                text = item.getString("text");
                                width = item.getInt("width");
                                height = item.getInt("height");
                                align = item.getInt("align");
                                LF = item.getInt("LF");

                                printer.setAlignment(align);
                                printer.printBarCode(text, BarcodeFormat.EAN_13, width, height);
                                printer.lineWrap(LF);
                            }
                        } catch (JSONException e) {
                        }
                        printer.commitTransaction();
                    }
                });
                break;

            case printImage:
                ThreadPoolManager.getInstance().executeTask(new Runnable() {

                    @Override
                    public void run() {

                        Bitmap image;
                        int align;
                        int LF;

                        printer.beginTransaction();
                        printer.printerInit();

                        try {
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject item = arr.getJSONObject(i);
                                byte[] decodedString = Base64.decode(item.getString("image"), Base64.DEFAULT);
                                image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                align = item.getInt("align");
                                LF = item.getInt("LF");

                                printer.setAlignment(align);
                                printer.printBitmap(image);
                                printer.lineWrap(LF);
                            }
                        } catch (JSONException e) {
                        }
                        printer.commitTransaction();
                    }
                });
                break;

            case printerTest:
                ThreadPoolManager.getInstance().executeTask(new Runnable() {

                    @Override
                    public void run() {
                        printer.beginTransaction();
                        printer.printerSelfChecking();
                        printer.commitTransaction();
                    }
                });
                break;
        }
        return true;
    }
}
