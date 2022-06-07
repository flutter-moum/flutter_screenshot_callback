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

public class ScreenshotCallbackPlugin implements MethodCallHandler {
    ScreenshotCallbackPlugin(Context context) {
        this.context = context;
    }

    private static MethodChannel channel;
    private static final String ttag = "screenshot_callback";

    private final Context context;

    private Handler handler;
    private ScreenshotDetector detector;
    private String lastScreenshotName;

    public static void registerWith(Registrar registrar) {
        channel = new MethodChannel(registrar.messenger(), "flutter.moum/screenshot_callback");
        channel.setMethodCallHandler(new ScreenshotCallbackPlugin(registrar.context()));
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {

        if (call.method.equals("initialize")) {
            handler = new Handler(Looper.getMainLooper());

            detector = new ScreenshotDetector(context, new Function1<String, Unit>() {
                @Override
                public Unit invoke(String screenshotName) {
                    if (!screenshotName.equals(lastScreenshotName)) {
                        lastScreenshotName = screenshotName;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                channel.invokeMethod("onCallback", null);
                            }
                        });
                    }
                    return null;
                }
            });
            detector.start();

            result.success("initialize");
        } else if (call.method.equals("dispose")) {
            detector.stop();
            detector = null;
            lastScreenshotName = null;

            result.success("dispose");
        } else {
            result.notImplemented();
        }
    }
}
