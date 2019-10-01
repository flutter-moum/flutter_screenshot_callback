import 'dart:async';

import 'package:flutter/scheduler.dart';
import 'package:flutter/services.dart';
import 'package:meta/meta.dart';

class ScreenshotCallback {
  static const MethodChannel _channel =
      const MethodChannel('screenshot_callback');

  List<VoidCallback> onCallbacks
  = [];

  ScreenshotCallback() {
    initialize();
  }

  Future<void> initialize() async {
    await _channel.invokeMethod("initialize");
  }

  void addListener(VoidCallback callback) {
    if (callback == null) throw ("No Callback!");
    onCallbacks.add(callback);
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
}
