package com.flutter.moum.screenshot_callback;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import android.os.Environment;
import android.os.FileObserver;
import java.io.File;
import android.util.Log;

/** ScreenshotCallbackPlugin */
public class ScreenshotCallbackPlugin implements MethodCallHandler {
  private static final String TAG = "ScreenshotCallbackPlugin";
  private static final String absolutePath = Environment.getExternalStorageDirectory() + File.separator
      + Environment.DIRECTORY_PICTURES + File.separator + "Screenshots" + File.separator;
  // private static final String absolutePath =
  // Environment.getRootDirectory().getPath();
  private static FileObserver fileObserver;

  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "screenshot_callback");
    channel.setMethodCallHandler(new ScreenshotCallbackPlugin());

    fileObserver = new FileObserverImpl(absolutePath, FileObserver.CREATE, );

    // registrar.activeContext().registerReceiver(fileObserver);
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("initialize")) {
      Log.d(TAG, "initialize: service is initialized");
      Log.d(TAG, absolutePath);
      fileObserver.startWatching();
      result.success("initialize");

    } else if (call.method.equals("dispose")) {
      Log.d(TAG, "dispose: service is destroyed");
      fileObserver.stopWatching();
      // registrar.activeContext().unregisterReceiver(fileObserver);
      result.success("dispose");
    } else {
      result.notImplemented();
    }
  }

  // channel.invokeMethod("onCallback", arguments, new Result() {
  // @Override
  // public void success(Object o) {
  // // this will be called with o = "some string"
  // }

  // @Override
  // public void error(String s, String s1, Object o) {}

  // @Override
  // public void notImplemented() {}
  // });
}
