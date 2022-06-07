package com.flutter.moum.screenshot_callback;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ScreenshotCallbackPlugin implements MethodCallHandler, FlutterPlugin {
    private static MethodChannel channel;
    private static final String ttag = "screenshot_callback";

    private Context applicationContext;

    private Handler handler;
    private ScreenshotDetector detector;
    private String lastScreenshotName;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        onAttachedToEngine(binding.getApplicationContext(), binding.getBinaryMessenger());
    }

    private void onAttachedToEngine(Context applicationContext, BinaryMessenger messenger) {
        this.applicationContext = applicationContext;
        channel = new MethodChannel(messenger, "flutter.moum/screenshot_callback");
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        applicationContext = null;
        channel.setMethodCallHandler(null);
        channel = null;
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {

        if (call.method.equals("initialize")) {
            handler = new Handler(Looper.getMainLooper());

            detector = new ScreenshotDetector(applicationContext, new Function1<String, Unit>() {
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
