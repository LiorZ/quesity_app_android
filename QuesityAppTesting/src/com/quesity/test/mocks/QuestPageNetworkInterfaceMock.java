package com.quesity.test.mocks;

import java.io.InputStream;

import android.content.Context;

import com.quesity.R;
import com.quesity.general.Config;
import com.quesity.models.JSONModel;
import com.quesity.models.QuestPage;

public class QuestPageNetworkInterfaceMock extends GeneralMockNetworkInterface {

	public QuestPageNetworkInterfaceMock(InputStream jsonStream, Context c) {
		super(jsonStream,c);
	}

	@Override
	protected void constructURL(JSONModel m) {
		QuestPage p = (QuestPage)m;
		Context context = getContext();
		String url = String.format(context.getString(R.string.request_page), p.getQuestId(), p.getId());
		pointURLToModel(Config.SERVER_URL + url, p);
		if ( p.getPageNumber() == 1 ) {
			String first_page_url = String.format(context.getString(R.string.request_first_page), p.getQuestId());
			pointURLToModel(Config.SERVER_URL + first_page_url, p);
		}
	}

	@Override
	protected Class<?> getModelClass() {
		return QuestPage[].class;
	}
	

}
