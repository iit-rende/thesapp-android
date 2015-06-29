package it.cnr.iit.thesapp.views;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

public class CustomRecyclerView extends RecyclerView {

	private static final String TAG = "CustomRecyclerView";

	public CustomRecyclerView(android.content.Context context) {
		super(context);
	}

	public CustomRecyclerView(android.content.Context context, android.util.AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomRecyclerView(android.content.Context context, android.util.AttributeSet attrs,
	                          int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void scrollTo(int x, int y) {
		Log.e(TAG, "CustomRecyclerView does not support scrolling to an absolute position.");
		// Either don't call super here or call just for some phones, or try catch it. From
		// default implementation we have removed the Runtime Exception trown
	}
}

