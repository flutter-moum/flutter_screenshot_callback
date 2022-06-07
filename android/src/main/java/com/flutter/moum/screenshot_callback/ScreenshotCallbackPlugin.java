package com.flutter.moum.screenshot_callback;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import android.os.Build;
import android.os.FileObserver;

import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class ScreenshotCallbackPlugin implements MethodCallHandler {
    private static MethodChannel channel;

    private Handler handler;
    private FileObserver fileObserver;

    public static void registerWith(Registrar registrar) {
        channel = new MethodChannel(registrar.messenger(), "flutter.moum/screenshot_callback");
        channel.setMethodCallHandler(new ScreenshotCallbackPlugin());
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {

        if (call.method.equals("initialize")) {
            handler = new Handler(Looper.getMainLooper());
            if (Build.VERSION.SDK_INT >= 29) {
                List<File> files = new ArrayList<File>();
                for (Path path : Path.values()) {
                    files.add(new File(path.getPath()));
                }

                fileObserver = new FileObserver(files, FileObserver.CREATE) {
                    @Override
                    public void onEvent(int event, String path) {
                        if (event == FileObserver.CREATE) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    channel.invokeMethod("onCallback", null);
                                }
                            });
                        }
                    }
                };
                fileObserver.startWatching();
            } else {
                for (Path path : Path.values()) {
                    fileObserver = new FileObserver(path.getPath(), FileObserver.CREATE) {
                        @Override
                        public void onEvent(int event, String path) {
                            if (event == FileObserver.CREATE) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        channel.invokeMethod("onCallback", null);
                                    }
                                });
                            }
                        }
                    };
                    fileObserver.startWatching();
                }
            }
            result.success("initialize");
        } else if (call.method.equals("dispose")) {
            fileObserver.stopWatching();
            result.success("dispose");
        } else {
            result.notImplemented();
        }
    }
}
