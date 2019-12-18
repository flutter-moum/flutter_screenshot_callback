import 'dart:io';
import 'dart:async';

import 'package:flutter/scheduler.dart';
import 'package:flutter/services.dart';
import 'package:permission_handler/permission_handler.dart';

class ScreenshotCallback {
  static const MethodChannel _channel =
      const MethodChannel('flutter.moum/screenshot_callback');

  /// Functions to execute when callback fired.
  List<VoidCallback> onCallbacks = [];

  /// If `true`, the user will be asked to grant storage permissions when
  /// callback is added.
  ///
  /// Defaults to `true`.
  bool requestPermissions;

  ScreenshotCallback({ this.requestPermissions }) {
    requestPermissions ??= true;
    initialize();
  }

  /// init screenshot callback plugin.
  Future<void> initialize() async {
    if (Platform.isAndroid && requestPermissions) await checkPermission();
    _channel.setMethodCallHandler(_handleMethod);
    await _channel.invokeMethod("initialize");
  }

  /// add void callback.
  void addListener(VoidCallback callback) {
    if (callback == null) throw ("No Callback!");
    onCallbacks.add(callback);
  }

  Future<dynamic> _handleMethod(MethodCall call) async {
    switch (call.method) {
      case 'onCallback':
        for (var callback in onCallbacks) callback();
        break;
      default:
        throw ('method not defined');
    }
  }

  /// remove callback listener.
  Future<void> dispose() async {
    await _channel.invokeMethod("dispose");
  }

  Future<void> checkPermission() async {
    PermissionStatus status = await PermissionHandler()
        .checkPermissionStatus(PermissionGroup.storage);

    if (status != PermissionStatus.granted) {
      await PermissionHandler().requestPermissions([PermissionGroup.storage]);
    }
  }
}
