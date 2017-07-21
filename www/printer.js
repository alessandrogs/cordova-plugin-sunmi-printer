var exec = require('cordova/exec');

var printer = {
    platforms: ['android'],

    isSupported: function () {
        if (window.device) {
            var platform = window.device.platform;
            if ((platform !== undefined) && (platform !== null)) {
                return (this.platforms.indexOf(platform.toLowerCase()) >= 0);
            }
        }
        return false;
    },
    printText: function (arr, onSuccess, onError) {
        exec(onSuccess, onError, 'SunmiPrinter', 'printText', arr);
    },
    printQRCode: function (arr, onSuccess, onError) {
        exec(onSuccess, onError, 'SunmiPrinter', 'printQRCode', arr);
    },
    printBarCode: function (arr, onSuccess, onError) {
        exec(onSuccess, onError, 'SunmiPrinter', 'printBarCode', arr);
    },
    printImage: function (arr, onSuccess, onError) {
        exec(onSuccess, onError, 'SunmiPrinter', 'printImage', arr);
    },
    printerTest: function (arr, onSuccess, onError) {
        exec(onSuccess, onError, 'SunmiPrinter', 'printerTest', arr);
    }
};
module.exports = printer;
