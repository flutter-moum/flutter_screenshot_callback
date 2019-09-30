import 'dart:async';

import 'package:flutter/scheduler.dart';
import 'package:flutter/services.dart';
import 'package:meta/meta.dart';

class ScreenshotCallback {
  static const MethodChannel _channel =
      const MethodChannel('screenshot_callback');

  List<VoidCallback> onCallbacks;

  Future<void> addListener(VoidCallback callback) async {
    if (callback == null) throw ("No Callback!");

    onCallbacks.add(callback);

    await _channel.invokeMethod("initialize");
  }

  void _handleMethod(MethodCall call) {
    switch (call.method) {
      case 'onCallback':
        for (var callback in onCallbacks) callback();
        break;
      default:
        throw ('method not defined');
    }
  }

  Future<void> dispose() async {
    await _channel.invokeMethod("dispose");
  }

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
