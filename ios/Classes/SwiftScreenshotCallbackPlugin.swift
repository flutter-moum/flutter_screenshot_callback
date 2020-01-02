import Flutter
import UIKit

public class SwiftScreenshotCallbackPlugin: NSObject, FlutterPlugin {
  static var channel: FlutterMethodChannel?

  public static func register(with registrar: FlutterPluginRegistrar) {
    channel  = FlutterMethodChannel(name: "flutter.moum/screenshot_callback", binaryMessenger: registrar.messenger())

    let instance = SwiftScreenshotCallbackPlugin()
    if let channel = channel {
      registrar.addMethodCallDelegate(instance, channel: channel)
    }
    
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    if(call.method == "initialize"){
      NotificationCenter.default.addObserver(
          forName: UIApplication.userDidTakeScreenshotNotification,
          object: nil,
          queue: .main) { notification in
          if let channel = SwiftScreenshotCallbackPlugin.channel {
            channel.invokeMethod("onCallback", arguments: nil)
          }

          result("screen shot called")
      }
      result("initialize")
    }else if(call.method == "dispose"){
      result("dispose")
    }else{
      result("")
    }
  }
    
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
}
