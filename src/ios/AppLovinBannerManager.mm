#import "AppLovinBannerManager.h"
#import "ALInterstitialAd.h"
#import "ApplovinPlugin.h"

@interface AppLovinBannerManager ()
@end

@implementation AppLovinBannerManager
-(id)init
{
    NSLog(@"Banner Init");
    x = 0;
    y = 0;
    return self;
}

-(void)setView:(UIViewController*)_controller uiView:(UIView*)_uiview
{
    NSLog(@"SetView called for bannerMgr");
    self.g_glView = _uiview;
    self.g_controller = _controller;
}

- (void)viewDidLoad
{
    NSLog(@"ViewDidLoad called");
    [[[ALSdk shared] adService] loadNextAd: [ALAdSize sizeBanner] andNotify: self];
}

-(void) adService:(ALAdService *)adService didLoadAd:(ALAd *)ad
{
    // In case an adView already exists remove it
    [self bannerRemove];
    
    NSLog(@"Calling adService, showing banner");
    loadedAd = ad;
    adView = [[ALAdView alloc]initWithFrame:CGRectMake(x, y, self.view.frame.size.width, 50)];

    [adView render:loadedAd];
    adView.tag  = 66;   // Added to identify it later
    adView.parentController = self.g_controller;
    [self.g_glView addSubview:adView];
}

-(void)bannerSetPosition:(double)_x yCoord:(double)_y
{
    x = _x;
    y = _y;
}

-(void) adService:(ALAdService *)adService didFailToLoadAdWithError:(int)code
{
    if(code == kALErrorCodeNoFill)
    {
        NSLog(@"kALErrorCodeNoFill (No Fill)");
    }
    else
    {
        NSLog(@"Unable to reach AppLovin");
    }
}

-(void) bannerRemove
{
    // For some reason [adView removeFromSuperview] doesn't work, so I loop through all subviews
    // and remove the one with the tag I know I assigned to the adView
    NSLog(@"bannerRemoveCalled");
    for (UIView *subview in self.g_glView.subviews)
    {
        NSLog(@"viewN");
        if(subview.tag == 66){
            [subview removeFromSuperview];
        }
    }
}

-(double)bannerGetHeight
{
    return adView.bounds.size.height;
}
@end