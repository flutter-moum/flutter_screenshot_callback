import 'dart:io';

import 'package:flutter/material.dart';
import 'package:screenshot_callback/screenshot_callback.dart';
import 'package:permission_handler/permission_handler.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  ScreenshotCallback screenshotCallback;

  String text = "Ready..";

  @override
  void initState() {
    super.initState();

    init();
  }

  void init() async {
    if (Platform.isAndroid) await checkPermission();
    await initScreenshotCallback();
  }

  //This should be executed after granted permission
  Future<void> initScreenshotCallback() async {
    screenshotCallback = ScreenshotCallback();

    screenshotCallback.addListener(() {
      setState(() {
        text = "Screenshot callback Fired!";
      });
    });

    screenshotCallback.addListener(() {
      print("We can add multiple listeners ");
    });
  }

  Future<void> checkPermission() async {
    PermissionStatus status = await PermissionHandler()
        .checkPermissionStatus(PermissionGroup.storage);

    if (status != PermissionStatus.granted) {
      await PermissionHandler().requestPermissions([PermissionGroup.storage]);
    }
  }

  @override
  void dispose() {
    screenshotCallback.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Detect Screenshot Callback Example'),
        ),
        body: Center(
          child: Text(text,
              style: TextStyle(
                fontWeight: FontWeight.bold,
              )),
        ),
      ),
    );
  }
}
