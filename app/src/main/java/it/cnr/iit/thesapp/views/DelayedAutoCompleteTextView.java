package it.cnr.iit.thesapp.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import de.greenrobot.event.EventBus;
import it.cnr.iit.thesapp.R;
import it.cnr.iit.thesapp.event.SetSearchDelayEvent;
import it.cnr.iit.thesapp.utils.Logs;

public class DelayedAutoCompleteTextView extends EditText {

	private static final int          MESSAGE_TEXT_CHANGED       = 100;
	private static final int          DEFAULT_AUTOCOMPLETE_DELAY = 500;
	private static final int MIN_CHAR_FOR_SEARCH = 2;
	private final        DelayHandler mHandler                   = new DelayHandler();
	private              int          mAutoCompleteDelay         = DEFAULT_AUTOCOMPLETE_DELAY;
	private ProgressBar      mLoadingIndicator;
	private OnSearchListener mListener;
	private ImageButton mClearButton;
	private boolean     allowClearButton;

	public DelayedAutoCompleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DelayedAutoCompleteTextView(Context context) {
		super(context);
		init();
	}

	public DelayedAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}


	public void setLoadingIndicator(ProgressBar progressBar) {
		mLoadingIndicator = progressBar;
	}

	public void setClearButton(ImageButton button) {
		mClearButton = button;
		mClearButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setText("");
			}
		});
	}

	private void init() {
		addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void onTextChanged(CharSequence text, int start, int before, int count) {
				performFiltering(text);
			}

			@Override
			public void afterTextChanged(Editable s) {
				allowClearButton = true;
			}
		});

		setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				switch (actionId) {
					case EditorInfo.IME_ACTION_SEARCH:
						performSearchNow(getText());
						break;
				}
				return false;
			}
		});
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

	protected void performFiltering(CharSequence text) {
		setError(null);
		if (text != null && text.length() >= MIN_CHAR_FOR_SEARCH) {
			if (mLoadingIndicator != null) {
				mLoadingIndicator.setVisibility(View.VISIBLE);
			}
			if (mClearButton != null) {
				mClearButton.setVisibility(GONE);
			}

			if (mAutoCompleteDelay < 10) {
				performSearchNow(text);
			} else {
				mHandler.removeMessages(MESSAGE_TEXT_CHANGED);
				mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_TEXT_CHANGED, text),
						mAutoCompleteDelay);
			}
		}
	}

	public void onFilterComplete(int count) {
		if (count < 0) {
			setError(getContext().getString(R.string.error_searching));
		}
		if (mLoadingIndicator != null) {
			mLoadingIndicator.setVisibility(View.INVISIBLE);
		}

		if (mClearButton != null && allowClearButton) {
			mClearButton.setVisibility(VISIBLE);
		}
	}

	public void performSearchNow(CharSequence text) {
		mHandler.removeMessages(MESSAGE_TEXT_CHANGED);
		if (mListener != null) mListener.performSearch(text);
	}

	public void setSearchListener(OnSearchListener listener) {
		this.mListener = listener;
	}


	public interface OnSearchListener {

		void performSearch(CharSequence filter);
	}

	public class DelayHandler extends Handler {

		public DelayHandler() {
		}

		@Override
		public void handleMessage(Message msg) {
			performSearchNow((CharSequence) msg.obj);
		}
	}
}