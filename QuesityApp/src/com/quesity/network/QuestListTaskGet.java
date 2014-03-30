package com.quesity.network;

import java.util.List;

import android.app.Dialog;
import android.content.Context;

import com.quesity.R;
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
			Dialog errorDialog = SimpleDialogs.getErrorDialog(_context.getString(R.string.lbl_err_load_quest),_context);
			errorDialog.show();
			return;
		}
		if ( result != null )
			loadImages((Quest[])result);
	}

	public QuestListTaskGet(Class<Result> c, Context ctx) {
		super(c, ctx);
		_context = ctx;
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
		ImagesLoaded listener = new ImagesLoaded() {
			
			@Override
			public void done() {
				if ( _progress != null )
					_progress.dismiss();
				_post_execute.apply(quests);
			}
		};
		
		MultipleImagesLoader loader = new MultipleImagesLoader(images,listener);
		loader.load();
	}
	


}
