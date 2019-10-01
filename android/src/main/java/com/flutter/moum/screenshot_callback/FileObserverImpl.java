package com.flutter.moum.screenshot_callback;

import android.os.FileObserver;
import android.util.Log;

public class FileObserverImpl extends FileObserver {

    public String TAG = "FileObserverImpl";

    public FileObserverImpl(String path, int mask) {
        super(path, mask);
    }

    @Override
    public void onEvent(int event, String path) {
        if (event == FileObserver.CREATE) {
            Log.d(TAG, "onEvent");

        } else if (event == FileObserver.CLOSE_WRITE) {
            Log.d(TAG, "close_write");
        }

    }

}
