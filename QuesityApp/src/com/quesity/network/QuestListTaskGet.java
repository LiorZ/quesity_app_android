package com.quesity.network;

import java.util.List;

import android.app.Dialog;
import android.content.Context;

import com.quesity.app.R;
import com.quesity.activities.QuesityMain;
import com.quesity.fragments.SimpleDialogs;
import com.quesity.models.Quest;
import com.quesity.network.MultipleImagesLoader.ImagesLoaded;

public class QuestListTaskGet<Result> extends FetchJSONTaskGet<Result> {

	private Context _context;
	
	@Override
	protected void onPostExecute(Result r) {
		Quest[] result = (Quest[])r;
		if ( result == null ) {
			if ( _progress != null )
				_progress.dismiss();
			if ( _error_status_code > -1 ) {
				handleError();
			}
			return;
		}else {
			loadImages((Quest[])result);
		}
	}

	public QuestListTaskGet(Class<Result> c, Context ctx) {
		super(c, ctx);
		_context = ctx;
		setNetworkErrorHandler( new SimpleNetworkErrorHandler(_context, -1, -1, R.string.err_connection) );
	}

	
	private void loadImages(final Quest[] quests) {
		String[] images = new String[quests.length];
		for (int i = 0; i < images.length; i++) {
			List<String> images_list = quests[i].getImages();
			if ( images_list.size() == 0 ) {
				images[i] ="";
			}else {
				images[i] = images_list.get(0);
			}
		}
		_post_execute.apply(quests);
//		ImagesLoaded listener = new ImagesLoaded() {
//			
//			@Override
//			public void done() {
//				if ( _progress != null )
//					_progress.dismiss();
//				
//			}
//		};
		
//		MultipleImagesLoader loader = new MultipleImagesLoader(images,listener);
//		loader.load();
	}
	

}
