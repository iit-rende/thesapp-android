package it.cnr.iit.thesapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.devspark.robototextview.widget.RobotoTextView;

import it.cnr.iit.thesapp.R;

public class ErrorView extends FrameLayout {
	private RobotoTextView    mSmallError;
	private RobotoTextView    mBigError;
	private ErrorViewListener callback;

	public ErrorView(Context context) {
		super(context);
		init();
	}

	public ErrorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		View view = inflate(getContext(), R.layout.panel_error_view, this);
		mSmallError = (RobotoTextView) view.findViewById(R.id.small_error);
		mBigError = (RobotoTextView) view.findViewById(R.id.big_error);
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (callback != null) callback.onViewClicked();
			}
		});
	}

	public void setError(String bigError, String smallError) {
		mSmallError.setText(smallError);
		mBigError.setText(bigError);
	}

	public void setCallback(ErrorViewListener callback) {
		this.callback = callback;
	}

	public interface ErrorViewListener {
		void onViewClicked();
	}
}
