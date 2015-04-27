package it.cnr.iit.thesapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class SearchBox extends FrameLayout {
	public SearchBox(Context context) {
		super(context);
		init();
	}

	public SearchBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SearchBox(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}


	public void init() {}
}
