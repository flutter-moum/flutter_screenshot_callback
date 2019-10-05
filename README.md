## screenshot_callback

[![pub package](https://img.shields.io/pub/v/screenshot_callback.svg)](https://pub.dartlang.org/packages/screenshot_callback)

A Flutter plugin for iOS and Android for detecting screenshot.

*Note*: This plugin is still under development, and we will add features such as importing screenshot image. [Feedback welcome](https://github.com/flutter-moum/flutter_screenshot_callback/issues/new) and [Pull Requests](https://github.com/flutter-moum/flutter_screenshot_callback/pulls) are most welcome!

## Usage

### Import the package

To use this plugin, follow the [**plugin installation instructions**](https://pub.dev/packages/screenshot_callback#-installing-tab-).

### Android

Android detects external storage to determine if a screenshot has occurred. So you need permission to external storage. Use the [**following links**](https://pub.dev/packages/permission) if necessary.

### iOS

No action is required. Screenshots can be detected by the plug-in itself.

### Use the plugin

Add the following import to your Dart code:

```dart
import 'package:screenshot_callback/screenshot_callback.dart';
```

Initialize ScreenshotCallback with the scopes you want:

```dart
ScreenshotCallback screenshotCallback = ScreenshotCallback();
```

### addListener

Then invoke <code>addListener</code> method of <code>ScreenshotCallback</code>.
Add custom functions that you want to excute after detect screenshot. e.g:

```dart
screenshotCallback.addListener(() {
  //Void funtions are implemented
  print('detect screenshot');
});
```

### dispose

You need to call <code>dispose</code> method to terminate  <code>ScreenshotCallback</code> before you exit the app e.g:

```dart
screenshotCallback.dispose();
```

## Issues and feedback

Please [**file**](https://github.com/flutter-moum/flutter_screenshot_callback/issues/new) issues to send feedback or report a bug. Thank you !