package com.flutter.moum.screenshot_callback;

import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

import android.os.FileObserver;
import android.util.Log;

public class FileObserverImpl extends FileObserver {
    public String TAG = "ScreenshotCallbackPlugin";
    private MethodChannel channel;

    public FileObserverImpl(String path, int mask, MethodChannel channel) {
        super(path, mask);
        this.channel = channel;
    }

    @Override
    public void onEvent(int event, String path) {
        if (event == FileObserver.CREATE) {
            channel.invokeMethod("onCallback", null);
            Log.d(TAG, "create screenshot file");
        }
    }

}
