import Flutter
import UIKit

public class SwiftScreenshotCallbackPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "screenshot_callback", binaryMessenger: registrar.messenger())
    let instance = SwiftScreenshotCallbackPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    if(call.method == "initialize"){
      print("iOS screenshotcallback initialized")
      result("initialize")
      NotificationCenter.default.addObserver(
          forName: NSNotification.Name.UIApplicationUserDidTakeScreenshot,
          object: nil,
          queue: .main) { notification in
          print("iOS 스크린샷 콜백함수 호출")
              //executes after screenshot
      }
    }else if(call.method == "dispose"){
      result("dispose")
    }else{
      result("")
    }
  }

  // channel.invokeMethod("onCallback", arguments: args, result: {(r:Any?) -> () in
  //   // this will be called with r = "some string" (or FlutterMethodNotImplemented)
  // })
}
