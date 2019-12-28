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
//import android.util.Log;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class ScreenshotCallbackPlugin implements MethodCallHandler {
    private static MethodChannel channel;

    private Handler handler;
    private FileObserver fileObserver;
    private String TAG = "tag";


    public static void registerWith(Registrar registrar) {
        channel = new MethodChannel(registrar.messenger(), "flutter.moum/screenshot_callback");
        channel.setMethodCallHandler(new ScreenshotCallbackPlugin());
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        //Log.d(TAG, "onMethodCall: ");

        if (call.method.equals("initialize")) {
            handler = new Handler(Looper.getMainLooper());
            if (Build.VERSION.SDK_INT >= 29) {
                //Log.d(TAG, "android x");
                List<File> files = new ArrayList<File>();
                for (Path path : Path.values()) {
                    files.add(new File(path.getPath()));
                }

                fileObserver = new FileObserver(files, FileObserver.CREATE) {
                    @Override
                    public void onEvent(int event, String path) {
                        //Log.d(TAG, "androidX onEvent");
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
                //Log.d(TAG, "android others");
                for (Path path : Path.values()) {
                    //Log.d(TAG, "onMethodCall: "+path.getPath());
                    fileObserver = new FileObserver(path.getPath(), FileObserver.CREATE) {
                        @Override
                        public void onEvent(int event, String path) {
                            //Log.d(TAG, "android others onEvent");
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
