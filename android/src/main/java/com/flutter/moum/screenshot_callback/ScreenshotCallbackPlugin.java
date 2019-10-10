package com.flutter.moum.screenshot_callback;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import android.os.Environment;
import android.os.FileObserver;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;

public class ScreenshotCallbackPlugin implements MethodCallHandler {
  private static MethodChannel channel;

  private Handler handler;
  private FileObserver fileObserver;
  private String TAG = "tag";

  public static void registerWith(Registrar registrar) {
    channel = new MethodChannel(registrar.messenger(), "screenshot_callback");
    channel.setMethodCallHandler(new ScreenshotCallbackPlugin());
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("initialize")) {
      handler = new Handler(Looper.getMainLooper());
      for(Path path : Path.values()){
        fileObserver = new FileObserver(path.getPath(), FileObserver.CREATE) {
          @Override
          public void onEvent(int event, String path) {
            Log.d(TAG,"onEvent");
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
      result.success("initialize");
    } else if (call.method.equals("dispose")) {
      fileObserver.stopWatching();
      result.success("dispose");
    } else {
      result.notImplemented();
    }
  }
}
