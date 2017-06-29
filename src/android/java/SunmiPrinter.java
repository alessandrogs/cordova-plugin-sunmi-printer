package br.com.ifabrica.sunmi.sunmiprinter;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

//import android.graphics.Bitmap;
import com.sunmi.controller.ICallback;
import com.sunmi.impl.V1Printer;

public class SunmiPrinter extends CordovaPlugin {

    private V1Printer printer;
    private ICallback callback;
//    private Bitmap mBitmap;
    private Context context;
    private static String TAG = "SunmiPrinter";

    private enum Option {
        printText
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
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        printer.setCallback(callback);

        Option option = null;
        try {
            option = Option.valueOf(action);
        } catch (Exception e) {
            return false;
        }
        switch (option) {
            case printText:
                final String text = args.getString(0);
                ThreadPoolManager.getInstance().executeTask(new Runnable() {

                    @Override
                    public void run() {

                        printer.beginTransaction();

                        printer.printerInit();

                        printer.setFontSize(24);
                        printer.printText("APCAP\n");
                        printer.printText("CAD 239843298423\n");
                        printer.printText("===============================\n");
                        printer.printText("Transação 03456123243343\n");
                        printer.printText("CARRO COS-0086\n");
                        printer.printText("27/06/2017 14:45\n");
                        printer.printText("------------------------------- \n");
                        printer.printText("2016040621001004150224503623\n\n");
                        printer.printText("cetsp.com.br\n");
                        printer.printText("--------------------------------\n");
                        printer.printText("Válido até          27/06/2017 17:29\n");
                        printer.printText("--------------------------------\n");
                        printer.printText("Obrigado\n");
                        printer.printText(text + "\n");
                        printer.setFontSize(32);
                        printer.printText("http://www.apcap.com.br\n");
                        printer.printOriginalText("http://www.ideams.com.br\n");
                        printer.setFontSize(24);
                        printer.printText("contato@apcap.com.br\n");
                        printer.printOriginalText("Volte Sempre\n");
                        printer.lineWrap(6);

                        printer.commitTransaction();
                    }
                });

                break;
        }
        return true;
    }
}
