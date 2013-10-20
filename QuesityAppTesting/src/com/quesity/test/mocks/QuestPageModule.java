package com.quesity.test.mocks;

import java.io.InputStream;

import javax.inject.Singleton;

import android.content.Context;

import com.quesity.activities.QuestPageActivity;
import com.quesity.models.QuestPage;
import com.quesity.network.IFetchJSONTask;
import com.quesity.network.INetworkInterface;
import com.quesity.network.dagger_modules.NetworkInterfaceModule;

import dagger.Module;
import dagger.Provides;

@Module(
			includes = NetworkInterfaceModule.class,
			injects = QuestPageActivity.class,
			overrides = true
	)
public class QuestPageModule {
		private InputStream _reader;
		private Context _context;
		public QuestPageModule(InputStream r, Context c){
			_reader = r;
			_context = c;
		}
		
		@Provides @Singleton INetworkInterface provideNetworkInterface() {
			return new QuestPageNetworkInterfaceMock(_reader, _context); 
	    }
		
		@Provides @Singleton IFetchJSONTask<QuestPage> provideQuestPageJSONTask() {
			return new FetchJSONTaskMock<QuestPage>(QuestPage.class);
		}
}	