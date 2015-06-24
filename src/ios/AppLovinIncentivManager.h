#import <UIKit/UIKit.h>

@class ApplovinPlugin;

@interface AppLovinIncentivManager : UIViewController
@property ApplovinPlugin* plugin;
/**
 *  Preloads incentivized video with delegate attached. Must be called
 *  before trying to show an incentivized video.
 */
-(void)loadRewardedVideo;

/**
 *  Shows incentivized video offer if one is loaded already.
 */
-(void)showRewardedVideo;
@end