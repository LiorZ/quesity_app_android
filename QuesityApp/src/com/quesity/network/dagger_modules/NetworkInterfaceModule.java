package com.quesity.network.dagger_modules;

import com.quesity.network.FetchJSONTaskGet;
import com.quesity.network.INetworkInterface;
import com.quesity.network.NetworkInterface;

import dagger.Module;
import dagger.Provides;


@Module(
		library = true 
)
public class NetworkInterfaceModule {
	@Provides INetworkInterface provideNetworkInterface(){
		return NetworkInterface.getInstance();
	}
}
