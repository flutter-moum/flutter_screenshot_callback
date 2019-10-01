package com.flutter.moum.screenshot_callback;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import android.util.Log;

/** ScreenshotCallbackPlugin */
public class ScreenshotCallbackPlugin implements MethodCallHandler {
  private static final String TAG = "ScreenshotCallbackPlugin";

  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "screenshot_callback");
    channel.setMethodCallHandler(new ScreenshotCallbackPlugin());
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("initialize")) {
      Log.d(TAG, "initialize: service is initialized");
      result.success("initialize");
    } else if (call.method.equals("dispose")) {
      Log.d(TAG, "dispose: service is destroyed");
      result.success("dispose");
    } else {
      result.notImplemented();
    }
  }
}
