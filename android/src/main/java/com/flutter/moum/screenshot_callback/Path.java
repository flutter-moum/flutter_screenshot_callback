package com.flutter.moum.screenshot_callback;

import android.os.Environment;

import java.io.File;

public enum Path {
    DCIM(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator + "Screenshots" + File.separator),
    PICTURES(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES ) + File.separator + "Screenshots" + File.separator);

    final private String path;

    public String getPath() {
        return path;
    }

    private Path(String path) {
        this. path = path;

    }
}
