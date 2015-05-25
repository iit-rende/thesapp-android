package it.cnr.iit.thesapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import it.cnr.iit.thesapp.R;

public class SearchFacetsContainer extends FrameLayout {

	public SearchFacetsContainer(Context context) {
		super(context);
		init();
	}

	public SearchFacetsContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SearchFacetsContainer(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		inflate(getContext(), R.layout.item_term_search_suggested_categories, this);
	}
}
