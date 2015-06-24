#import <Cordova/CDV.h>
#import "AppLovinBannerManager.h"
#import "AppLovinIncentivManager.h"

@interface ApplovinPlugin : CDVPlugin{
    AppLovinIncentivManager* inc;
    AppLovinBannerManager* ban;
    @public NSString* callbackGeneral;
}

- (void) initializeSdk: (CDVInvokedUrlCommand*)command;
- (void) interstitialShow: (CDVInvokedUrlCommand*)command;
- (double)interstitialIsReady:(CDVInvokedUrlCommand *)command;
/**
 *  Shows banner at banner's currently set position (0,0 by default).
 *  Will be centered by default.
 */
- (void)bannerShow:(CDVInvokedUrlCommand *)command;
/**
 * Removes all banners (if any)
 */
-(void)bannerRemove:(CDVInvokedUrlCommand *)command;
/**
 *  Preloads incentivized video with delegate attached. Must be called
 *  before trying to show an incentivized video.
 */
-(void)incentivLoad:(CDVInvokedUrlCommand *)command;

/**
 *  Shows incentivized video offer if one is loaded already.
 */
-(void)incentivShow:(CDVInvokedUrlCommand *)command;

/**
 *  Whether or not the incentivized video is ready to show.
 *
 *  Returns BOOL (double for GMS compatibility)
 */
-(double)incentivIsReady:(CDVInvokedUrlCommand *)command;
/**
 *  Set User Id for Incentivized Video
 */
-(void)setUserId:(CDVInvokedUrlCommand *)command;
/**
 *  Used to send callbacks events
 */
-(void)cordovaCallback:(NSString*)message;
-(void)cordovaCallbackDict:(NSDictionary*)dict;
@end
