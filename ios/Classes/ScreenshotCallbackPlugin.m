#import "ScreenshotCallbackPlugin.h"
#import <screenshot_callback/screenshot_callback-Swift.h>

@implementation ScreenshotCallbackPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftScreenshotCallbackPlugin registerWithRegistrar:registrar];
}
@end
