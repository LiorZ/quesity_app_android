package com.quesity.network.dagger_modules;

import com.quesity.activities.QuesityMain;
import com.quesity.activities.QuestPageActivity;
import com.quesity.activities.QuestsListViewActivity;
import com.quesity.activities.SplashScreen;
import com.quesity.models.QuestPage;
import com.quesity.network.FetchJSONTaskGet;
import com.quesity.network.IFetchJSONTask;
import com.quesity.network.INetworkInterface;
import com.quesity.network.NetworkInterface;

import dagger.Module;
import dagger.Provides;


@Module(
		injects = {
				SplashScreen.class,
				QuesityMain.class,
				QuestPageActivity.class,
				QuestsListViewActivity.class
		},
		library = true
)
public class NetworkInterfaceModule {
	@Provides INetworkInterface provideNetworkInterface(){
		return NetworkInterface.getInstance();
	}
	
	@Provides IFetchJSONTask<QuestPage> provideQuestPageJSONTask() {
		return new FetchJSONTaskGet<QuestPage>(QuestPage.class);
	}
}
