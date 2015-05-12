package it.cnr.iit.thesapp.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

import com.devspark.robototextview.widget.RobotoAutoCompleteTextView;

import de.greenrobot.event.EventBus;
import it.cnr.iit.thesapp.R;
import it.cnr.iit.thesapp.event.SetSearchDelayEvent;
import it.cnr.iit.thesapp.utils.Logs;

public class DelayedAutoCompleteTextView extends RobotoAutoCompleteTextView {

	private static final int          MESSAGE_TEXT_CHANGED       = 100;
	private static final int          DEFAULT_AUTOCOMPLETE_DELAY = 500;
	private final        DelayHandler mHandler                   = new DelayHandler();
	private              int          mAutoCompleteDelay         = DEFAULT_AUTOCOMPLETE_DELAY;
	private ProgressBar mLoadingIndicator;

	public DelayedAutoCompleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setLoadingIndicator(ProgressBar progressBar) {
		mLoadingIndicator = progressBar;
	}

	public void setAutoCompleteDelay(long autoCompleteDelay) {
		if (autoCompleteDelay > 0) {
			Logs.retrofit("Autosearch delay setted at: " + autoCompleteDelay);
			mAutoCompleteDelay = (int) autoCompleteDelay;
		}
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		EventBus.getDefault().register(this);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		EventBus.getDefault().unregister(this);
	}

	public void onEvent(SetSearchDelayEvent event) {
		setAutoCompleteDelay(event.getDelay());
	}

	@Override
	protected void performFiltering(CharSequence text, int keyCode) {
		setError(null);
		if (mLoadingIndicator != null) {
			mLoadingIndicator.setVisibility(View.VISIBLE);
		}

		if (mAutoCompleteDelay < 10) {
			mHandler.removeMessages(MESSAGE_TEXT_CHANGED);
			super.performFiltering(text, keyCode);
		} else {
			mHandler.removeMessages(MESSAGE_TEXT_CHANGED);
			mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_TEXT_CHANGED, text),
					mAutoCompleteDelay);
		}
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


	public class DelayHandler extends Handler {
		public DelayHandler() {
		}

		@Override
		public void handleMessage(Message msg) {
			DelayedAutoCompleteTextView.super.performFiltering((CharSequence) msg.obj, msg.arg1);
		}
	}
}