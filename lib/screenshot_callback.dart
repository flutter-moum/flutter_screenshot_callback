import 'dart:io';
import 'dart:async';

import 'package:flutter/scheduler.dart';
import 'package:flutter/services.dart';
import 'package:permission_handler/permission_handler.dart';

class ScreenshotCallback {
  static const MethodChannel _channel =
      const MethodChannel('flutter.moum/screenshot_callback');

  /// Functions to execute when callback fired.
  List<VoidCallback> onCallbacks = <VoidCallback>[];

  /// If `true`, the user will be asked to grant storage permissions when
  /// callback is added.
  ///
  /// Defaults to `true`.
  bool requestPermissions;

  ScreenshotCallback({
    this.requestPermissions = true,
  }) {
    initialize();
  }

  /// Initializes screenshot callback plugin.
  Future<void> initialize() async {
    if (Platform.isAndroid && requestPermissions) {
      await checkPermission();
    }
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

  /// Checks if user has granted permissions for storage.
  ///
  /// If permission is not granted, it'll be requested.
  Future<void> checkPermission() async => await Permission.storage.request();
}
