module.exports = {
    // Applovin functions start
	initializeSdk: function() {
        var self = this;
        var callbackGeneral = function(result){
            if(typeof result == "string") {
               switch(result){
                case "onInitializeSdk":
                	if(self.onInitializeSdk){
                		self.onInitializeSdk();
                	}
                    break;

                case "onIncentivLoaded":
                	if(self.onIncentivLoaded){
                    	self.onIncentivLoaded();
                	}
                    break;
                case "onVideoPlaybackEndFullyWatched":
                    if(self.onVideoPlaybackEndFullyWatched){
                        self.onVideoPlaybackEndFullyWatched();
                    }
                    break;
                case "onVideoPlaybackEndInterrupted":
                    if(self.onVideoPlaybackEndInterrupted){
                        self.onVideoPlaybackEndInterrupted();
                    }
                    break;
                case "onVideoPlaybackWasHidden":
                    if(self.onVideoPlaybackWasHidden){
                        self.onVideoPlaybackWasHidden();
                    }
                    break;
                }
            }
            else{
                switch(result.Type){
                    case "RewardSuccess":
                    	if(self.onRewardSuccess){
                        	self.onRewardSuccess(result.amount, result.currency);
                    	}
                        break;
               
                    case "RewardFail":
                    	if(self.onRewardFail){
                        	self.onRewardFail(result.ErrorCode);
                    	}
                        break;
               
                    case "IncentivLoadFail":
                    	if(self.onIncentivLoadFail){
                        	self.onIncentivLoadFail(result.ErrorCode);
                    	}
                        break;
                }
            }
        }
        cordova.exec(
            callbackGeneral,
            null,
            'ApplovinPlugin',
            'initializeSdk',
            []
        );
	},
	interstitialShow: function() {
		cordova.exec(
			null,
			null,
			'ApplovinPlugin',
			'interstitialShow',
			[]
		);
	},
	incentivShow: function() {
		cordova.exec(
			null,
			null,
			'ApplovinPlugin',
			'incentivShow',
			[]
		);
	},
	incentivLoad: function() {
		cordova.exec(
			null,
			null,
			'ApplovinPlugin',
			'incentivLoad',
			[]
		);
	},
	bannerShow: function(isBottom) {
		cordova.exec(
			null,
			null,
			'ApplovinPlugin',
			'bannerShow',
			[isBottom]
		);
	},
	bannerRemove: function() {
		cordova.exec(
			null,
			null,
			'ApplovinPlugin',
			'bannerRemove',
			[]
		);
	},
	setUserId: function(userId) {
		cordova.exec(
			null,
			null,
			'ApplovinPlugin',
			'setUserId',
			[userId]	// userId string
		);
	},
	// Applovin functions end
    // Initialize function names in case no corresponding ones defined by user
    onInitializeSdk: null,
    onIncentivLoaded: null,
    onIncentivLoadFail: null,
    onRewardSuccess: null,
    onRewardFail: null,
    onVideoPlaybackEndFullyWatched: null,
    onVideoPlaybackEndInterrupted: null,
    onVideoPlaybackWasHidden: null

};