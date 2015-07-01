package com.applovin.plugin.cordova;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import android.annotation.TargetApi;
import android.app.Activity;
import android.util.Log;

import android.widget.RelativeLayout;

import android.view.View;
import android.view.ViewGroup;
import com.applovin.adview.*;
import com.applovin.sdk.*;

import java.util.*;//Random


public class ApplovinPlugin extends CordovaPlugin {
	private static final String LOG_TAG = "ApplovinPlugin";
	private static AppLovinAdView adView;
	private static RelativeLayout adViewLayout;
	private static CordovaWebView webView;
	private static AppLovinIncentivizedInterstitial incent;
	private static String incentId;
	private CallbackContext callbackContextGeneral;
	
	
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);
		ApplovinPlugin.webView = webView;
    }

	@Override
	public void onPause(boolean multitasking) {
		super.onPause(multitasking);
	}
	
	@Override
	public void onResume(boolean multitasking) {
		super.onResume(multitasking);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		
		Log.d(LOG_TAG, "===============Calling " + action + "===============");
		
		
		if (action.equals("initializeSdk")) {
			initializeSdk(action, args, callbackContext);
			return true;
		}
		else if(action.equals("interstitialShow")) {
			interstitialShow(action, args, callbackContext);
			return true;
		}
		else if(action.equals("setUserId")) {
			setUserId(action, args, callbackContext);
			return true;
		}
		else if(action.equals("bannerShow")) {
			bannerShow(action, args, callbackContext);
			return true;
		}
		else if(action.equals("bannerRemove")) {
			bannerRemove(action, args, callbackContext);
			return true;
		}
		else if(action.equals("incentivLoad")) {
			incentivLoad(action, args, callbackContext);
			return true;
		}
		else if(action.equals("incentivShow")) {
			incentivShow(action, args, callbackContext);
			return true;
		}
		
		return false; // Returning false results in a "MethodNotFound" error.
		
	}
	
	
	private void initializeSdk(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		callbackContextGeneral = callbackContext;
		AppLovinSdk.initializeSdk(cordova.getActivity().getApplicationContext());
	}
	
	private void interstitialShow(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		AppLovinInterstitialAd.show(cordova.getActivity());
	}
	
	private void setUserId(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
      	incentId = args.optString(0);
		
	}
	
	private void incentivLoad(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		final Activity cordAct = cordova.getActivity();
		incent = AppLovinIncentivizedInterstitial.create(cordAct);
		// incent.preload(null);
		
		if(incentId != null){
			incent.setUserIdentifier(incentId);
		}
		
		incent.preload(new AppLovinAdLoadListener(){
			@Override
			public void adReceived(AppLovinAd ad){

				PluginResult pr = new PluginResult(PluginResult.Status.OK, "onIncentivLoaded");
				pr.setKeepCallback(true);
				callbackContextGeneral.sendPluginResult(pr);

			}
			
			@Override
			public void failedToReceiveAd(int errorCode){
				try{
					JSONObject results = new JSONObject();
					results.put("Type", "IncentivLoadFail");
					results.put("ErrorCode", errorCode);
					
					PluginResult pr = new PluginResult(PluginResult.Status.OK, results);
					pr.setKeepCallback(true);
					callbackContextGeneral.sendPluginResult(pr);
				}
				catch (JSONException e) {
					
				}
			}
		});
	}	
	
	
	private void incentivShow(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		
		final Activity cordAct = cordova.getActivity();
		incent.show(cordAct,
			new AppLovinAdRewardListener(){
				// This method is called when the user selected "no" when asked if they want to view an ad. (Only if pre-video prompt is enabled).
				@Override
				public void userDeclinedToViewAd(AppLovinAd ad) {}

				// This method is called when the user over already received the maximum allocated rewards for the day.
				@Override
				public void userOverQuota(AppLovinAd ad, Map response) {}

				// This method is called when the user's reward was detected as fraudulent.
				@Override
				public void userRewardRejected(AppLovinAd ad, Map response) {}

				// This method will be invoked if we contacted AppLovin successfully. This means that we will be pinging your currency endpoint shortly, so you may wish to refresh the user's coins from your server.
				@Override
				public void userRewardVerified(AppLovinAd ad, Map response) {

					Double amount = Double.parseDouble((String) response.get("amount"));
					String currency = (String) response.get("currency");
					
					try{
						JSONObject results = new JSONObject();
						results.put("Type", "RewardSuccess");
						results.put("amount", amount);
						results.put("currency", currency);
						
						PluginResult pr = new PluginResult(PluginResult.Status.OK, results);
						pr.setKeepCallback(true);
						callbackContextGeneral.sendPluginResult(pr);
					}
					catch (JSONException e) {
						
					}

				}

				// This method will be invoked if we were unable to contact AppLovin, therefore no ping will be heading to your server.
				@Override
				public void validationRequestFailed(AppLovinAd ad, int errorCode) {
					
					try{
						JSONObject results = new JSONObject();
						results.put("Type", "RewardSuccess");
						results.put("ErrorCode", errorCode);
						
						PluginResult pr = new PluginResult(PluginResult.Status.OK, results);
						pr.setKeepCallback(true);
						callbackContextGeneral.sendPluginResult(pr);
					}
					catch (JSONException e) {
						
					}
				}
			},
		
			new AppLovinAdVideoPlaybackListener(){
				@Override
				public void videoPlaybackBegan(AppLovinAd ad){
					Log.d(LOG_TAG, "===============Playback Began===============");
					
				}
				
				@Override
				public void videoPlaybackEnded(AppLovinAd ad, double percentViewed, boolean fullyWatched){
					Log.d(LOG_TAG, "===============Playback Ended===============");
					
					String message;
					
					if(fullyWatched){
						message = "onVideoPlaybackEndFullyWatched";
					}
					else{
						message = "onVideoPlaybackEndInterrupted";
					}
					
					PluginResult pr = new PluginResult(PluginResult.Status.OK, message);
					pr.setKeepCallback(true);
					callbackContextGeneral.sendPluginResult(pr);
					
				}
			},
			
			new AppLovinAdDisplayListener(){
				@Override
				public void	adDisplayed(AppLovinAd ad){
					Log.d(LOG_TAG, "===============Ad Displayed===============");
					
				}
				
				@Override
				public void adHidden(AppLovinAd ad){
					Log.d(LOG_TAG, "===============Ad Hidden===============");
					
					PluginResult pr = new PluginResult(PluginResult.Status.OK, "onVideoPlaybackWasHidden");
					pr.setKeepCallback(true);
					callbackContextGeneral.sendPluginResult(pr);
				
				}
			}
		);

		incent = null;
	}
	
	private void bannerShow(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
	    
		final Activity cordAct = cordova.getActivity();
      	final boolean isBottom = Boolean.parseBoolean(args.optString(0));
		

	    cordova.getActivity().runOnUiThread(new Runnable(){
			@Override
			public void run(){
				
            	ViewGroup parentView = (ViewGroup) webView.getView().getParent();
				
				if(adView != null){
					parentView.removeView(adView);
					adView.destroy();
					adView = null;
				}
				
				if (adViewLayout == null) {
					adViewLayout = new RelativeLayout(cordova.getActivity());
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
					parentView.addView(adViewLayout, params);
				}
				
            	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            	
            	if(isBottom){
              		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            	}
            	else{
              		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            	}
				
				
				adView = new AppLovinAdView(AppLovinAdSize.BANNER, cordAct);
				
				adView.loadNextAd();
				adViewLayout.addView(adView, params);
				parentView.bringToFront();
				
			}
		});
	}
	
	
	private void bannerRemove(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
	    cordova.getActivity().runOnUiThread(new Runnable(){
			@Override
			public void run(){
		    	ViewGroup parentView = (ViewGroup) webView.getView().getParent();
				if(adView != null){
					parentView.removeView(adView);
					adView.destroy();
					adView = null;
				}
			}
		});
	}
	
}