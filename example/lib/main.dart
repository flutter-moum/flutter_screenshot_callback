import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:screenshot_callback/screenshot_callback.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  ScreenshotCallback screenshotCallback = ScreenshotCallback();

  String text = "ready";

  @override
  void initState() {
    super.initState();

    screenshotCallback.addListener(() {
      setState(() {
        text = "callback!";
      });
    });
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
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Text(text),
        ),
      ),
    );
  }
}
