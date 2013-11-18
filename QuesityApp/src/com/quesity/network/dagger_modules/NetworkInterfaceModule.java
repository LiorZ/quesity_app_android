package com.quesity.network.dagger_modules;

import com.quesity.activities.BaseActivity;
import com.quesity.network.INetworkInterface;
import com.quesity.network.NetworkInterface;

import dagger.Module;
import dagger.Provides;


@Module(
		injects = {
				BaseActivity.class
		},
		library=true
)
public class NetworkInterfaceModule {
	@Provides INetworkInterface provideNetworkInterface(){
		return NetworkInterface.getInstance();
	}
}
