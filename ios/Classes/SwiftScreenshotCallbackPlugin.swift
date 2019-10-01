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
      result("initialize")
    }else if(call.method == "dispose"){
      result("dispose")
    }else{
      result("")
    }
  }
}
