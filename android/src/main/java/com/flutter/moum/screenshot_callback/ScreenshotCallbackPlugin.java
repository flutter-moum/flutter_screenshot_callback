package com.flutter.moum.screenshot_callback;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

//import android.util.Log;

public class ScreenshotCallbackPlugin implements MethodCallHandler {
    ScreenshotCallbackPlugin(Context context) {
        _context = context;
    }

    private static MethodChannel _channel;
    private static final String _tag = "screenshot_callback";

    private final Context _context;

    private Handler _handler;
    private ScreenshotDetector _detector;
    private String _lastScreenshotName;

    public static void registerWith(Registrar registrar) {
        _channel = new MethodChannel(registrar.messenger(), "flutter.moum/screenshot_callback");
        _channel.setMethodCallHandler(new ScreenshotCallbackPlugin(registrar.context()));
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        if (call.method.equals("initialize")) {
            _handler = new Handler(Looper.getMainLooper());

            _detector = new ScreenshotDetector(_context, new Function1<String, Unit>() {
                @Override
                public Unit invoke(String screenshotName) {
//                    Log.d(_tag, "onScreenshotDetected: " + screenshotName);

                    if (!screenshotName.equals(_lastScreenshotName)) {
                        _lastScreenshotName = screenshotName;
                        _handler.post(new Runnable() {
                            @Override
                            public void run() {
//                                Log.d(_tag, "onCallback: ");
                                _channel.invokeMethod("onCallback", null);
                            }
                        });
                    }

                    return null;
                }
            });
            _detector.start();

            result.success("initialize");
        } else if (call.method.equals("dispose")) {
            _detector.stop();
            _detector = null;
            _lastScreenshotName = null;

            result.success("dispose");
        } else {
            result.notImplemented();
        }
    }
}
