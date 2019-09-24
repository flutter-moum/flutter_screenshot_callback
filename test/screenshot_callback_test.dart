import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:screenshot_callback/screenshot_callback.dart';

void main() {
  const MethodChannel channel = MethodChannel('screenshot_callback');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await ScreenshotCallback.platformVersion, '42');
  });
}
