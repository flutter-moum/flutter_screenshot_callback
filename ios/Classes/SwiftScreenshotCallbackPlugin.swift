import Flutter
import UIKit

public class SwiftScreenshotCallbackPlugin: NSObject, FlutterPlugin {
  var channel: FlutterMethodChannel? = nil

  public static func register(with registrar: FlutterPluginRegistrar) {
    channel = FlutterMethodChannel(name: "screenshot_callback", binaryMessenger: registrar.messenger())
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
          result("screen shot called")

          if let channel = channel {
            channel.invokeMethod("onCallback", arguments: args, result: {(r:Any?) -> () in

                      })
          }
      }
    }else if(call.method == "dispose"){
      result("dispose")
    }else{
      result("")
    }
  }


}
