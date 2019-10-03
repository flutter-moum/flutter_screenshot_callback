package com.flutter.moum.screenshot_callback;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import android.os.Environment;
import android.os.FileObserver;
import java.io.File;

import android.os.Handler;
import android.os.Looper;

public class ScreenshotCallbackPlugin implements MethodCallHandler {
  private static final String SCREENSHOT_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator + "Screenshots" + File.separator;
  private Handler handler;
  private FileObserver fileObserver;
  private static MethodChannel channel;

  public static void registerWith(Registrar registrar) {
    channel = new MethodChannel(registrar.messenger(), "screenshot_callback");
    channel.setMethodCallHandler(new ScreenshotCallbackPlugin());
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("initialize")) {
      handler = new Handler(Looper.getMainLooper());
      fileObserver = new FileObserver(SCREENSHOT_PATH, FileObserver.CREATE){
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
      result.success("initialize");
    } else if (call.method.equals("dispose")) {
      fileObserver.stopWatching();
      result.success("dispose");
    } else {
      result.notImplemented();
    }
  }
}
