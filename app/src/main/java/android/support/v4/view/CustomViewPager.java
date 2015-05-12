package android.support.v4.view;

import android.content.Context;
import android.util.AttributeSet;

public class CustomViewPager extends ViewPager {

	// The speed of the scroll used by setCurrentItem()
	private static final int VELOCITY = 1;

	public CustomViewPager(Context context) {
		super(context);
	}

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	void setCurrentItemInternal(int item, boolean smoothScroll, boolean always) {
		setCurrentItemInternal(item, smoothScroll, always, VELOCITY);
	}
}
