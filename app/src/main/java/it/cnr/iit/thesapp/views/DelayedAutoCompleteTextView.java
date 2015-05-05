package it.cnr.iit.thesapp.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

import com.devspark.robototextview.widget.RobotoAutoCompleteTextView;

import java.lang.ref.WeakReference;

import it.cnr.iit.thesapp.R;

public class DelayedAutoCompleteTextView extends RobotoAutoCompleteTextView {

	private static final int          MESSAGE_TEXT_CHANGED       = 100;
	private static final int          DEFAULT_AUTOCOMPLETE_DELAY = 750;
	private final        DelayHandler mHandler                   = new DelayHandler(this);
	private              int          mAutoCompleteDelay         = DEFAULT_AUTOCOMPLETE_DELAY;
	private ProgressBar mLoadingIndicator;

	public DelayedAutoCompleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setLoadingIndicator(ProgressBar progressBar) {
		mLoadingIndicator = progressBar;
	}

	public void setAutoCompleteDelay(int autoCompleteDelay) {
		mAutoCompleteDelay = autoCompleteDelay;
	}

	@Override
	protected void performFiltering(CharSequence text, int keyCode) {
		setError(null);
		if (mLoadingIndicator != null) {
			mLoadingIndicator.setVisibility(View.VISIBLE);
		}
		mHandler.removeMessages(MESSAGE_TEXT_CHANGED);
		mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_TEXT_CHANGED, text),
				mAutoCompleteDelay);
	}

	@Override
	public void onFilterComplete(int count) {
		if (count < 0) {
			setError(getContext().getString(R.string.error_searching));
		}
		if (mLoadingIndicator != null) {
			mLoadingIndicator.setVisibility(View.GONE);
		}
		super.onFilterComplete(count);
	}

	public static class DelayHandler extends Handler {

		public WeakReference<DelayedAutoCompleteTextView> delayedAutoCompleteTextViewWeakReference;

		public DelayHandler(DelayedAutoCompleteTextView delayedAutoCompleteTextView) {
			this.delayedAutoCompleteTextViewWeakReference =
					new WeakReference<DelayedAutoCompleteTextView>(delayedAutoCompleteTextView);
		}

		@Override
		public void handleMessage(Message msg) {
			delayedAutoCompleteTextViewWeakReference.get().performFiltering((CharSequence) msg.obj,
					msg.arg1);
		}
	}
}