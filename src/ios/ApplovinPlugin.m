#import "ApplovinPlugin.h"
#import "ALSdk.h"
#import "ALInterstitialAd.h"
#import "ALIncentivizedInterstitialAd.h"
#import "ALAdView.h"
#import "AppLovinBannerManager.h"
#import "AppLovinIncentivManager.h"

@interface ApplovinPlugin()
@property (strong, nonatomic) IBOutlet UILabel *adLoadStatus;
@end

@implementation ApplovinPlugin

- (void) testFunction: (CDVInvokedUrlCommand*)command{
    NSLog(@"testFunction called");
}
- (void) initializeSdk:(CDVInvokedUrlCommand *)command{
    NSLog(@"Attempting to call initializeSdk");
    inc = [[AppLovinIncentivManager alloc] init];
    inc.plugin = self;
    [ALSdk initializeSdk];
    ban = [[AppLovinBannerManager alloc] init];
    self.viewController.modalPresentationStyle = UIModalPresentationCustom;
    [ban  setView:self.viewController uiView:self.webView];
    
    callbackGeneral = command.callbackId;
    [self cordovaCallback:@"onInitializeSdk"];
}
-(void)interstitialShow:(CDVInvokedUrlCommand *)command{
    NSLog(@"Attempting to call interstitialShow");
    [ALInterstitialAd show];
}
-(double)interstitialIsReady:(CDVInvokedUrlCommand *)command{
    double isReady = [ALInterstitialAd isReadyForDisplay];
    NSLog(@"%f", isReady);
    return isReady;
}
-(void)bannerShow:(CDVInvokedUrlCommand *)command{
    NSLog(@"About to call loadNextAd (banner)");
    BOOL isBottom = [[command.arguments objectAtIndex:0] boolValue];
    if(isBottom){
        [ban bannerSetPosition:0 yCoord:ban.view.frame.size.height-50];
    }
    else{
        [ban bannerSetPosition:0 yCoord:0];
    }
    [ban viewDidLoad];
}
-(void)bannerRemove:(CDVInvokedUrlCommand *)command{
    [ban bannerRemove];
}
-(void)incentivLoad:(CDVInvokedUrlCommand *)command{
    NSLog(@"About to call Incetivized preloadAndNotify");
    [inc loadRewardedVideo];
}

-(void)incentivShow:(CDVInvokedUrlCommand *)command{
    NSLog(@"About to call Incetivized showRewardedVideo");
    [inc showRewardedVideo];
}

-(double)incentivIsReady:(CDVInvokedUrlCommand *)command{
    return [ALIncentivizedInterstitialAd isReadyForDisplay];
}

-(void)setUserId:(CDVInvokedUrlCommand *)command{
    NSString *userId = [command.arguments objectAtIndex:0];
    [ALIncentivizedInterstitialAd setUserIdentifier:userId];
}
-(void)cordovaCallback:(NSString*)message{
    CDVPluginResult* pr = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:message];
    [pr setKeepCallbackAsBool:YES];
    [self.commandDelegate sendPluginResult:pr callbackId:callbackGeneral];
}
-(void)cordovaCallbackDict:(NSDictionary*)dict{
    CDVPluginResult* pr = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:dict];
    [pr setKeepCallbackAsBool:YES];
    [self.commandDelegate sendPluginResult:pr callbackId:callbackGeneral];
}
@end