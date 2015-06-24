#import <UIKit/UIKit.h>
#import "ALAdView.h"
#import "ALAdLoadDelegate.h"
@interface AppLovinBannerManager : UIViewController<ALAdLoadDelegate>
{
    ALAdView *adView;
    ALAd* loadedAd;
    double x;
    double y;
}
@property UIViewController *g_controller;
@property UIView *g_glView;

-(void)setView:(UIViewController*)_controller uiView:(UIView*)_view;
/**
 *  Removes all banners if any.
 */
-(void)bannerRemove;

/**
 *  Sets the x and y target coordinates in points of the banner.
 *  When the banner is loaded it will be placed at these coordinates.
 */
-(void)bannerSetPosition:(double)_x yCoord:(double)_y;

/**
 *  Returns height of current banner in points (not pixels).
 */
-(double)bannerGetHeight;

@end