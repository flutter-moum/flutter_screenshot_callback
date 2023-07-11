import 'dart:async';

import 'package:flutter/services.dart';

class ScreenshotCallback {
  static const MethodChannel _channel =
      const MethodChannel('flutter.moum/screenshot_callback');

  /// Functions to execute when callback fired.
  List<VoidCallback> onCallbacks = <VoidCallback>[];

  ScreenshotCallback() {
    initialize();
  }

  /// Initializes screenshot callback plugin.
  Future<void> initialize() async {
    _channel.setMethodCallHandler(_handleMethod);
    await _channel.invokeMethod('initialize');
  }

  /// Add void callback.
  void addListener(VoidCallback callback) {
    onCallbacks.add(callback);
  }

  Future<dynamic> _handleMethod(MethodCall call) async {
    switch (call.method) {
      case 'onCallback':
        for (final callback in onCallbacks) {
          callback();
        }
        break;
      default:
        throw ('method not defined');
    }
  }

  /// Remove callback listener.
  Future<void> dispose() async => await _channel.invokeMethod('dispose');
}
