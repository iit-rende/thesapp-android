package it.cnr.iit.thesapp.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.devspark.robototextview.widget.RobotoTextView;

import it.cnr.iit.thesapp.R;
import it.cnr.iit.thesapp.model.Term;

public class TreeView extends LinearLayout {
	private TreeViewCallbacks mCallbacks;

	public TreeView(Context context) {
		super(context);
		init();
	}

	public TreeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TreeView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		setOrientation(VERTICAL);
	}

	public void setHierarchy(Term term) {
		removeAllViews();
		int i = 0;
		int margin = getResources().getDimensionPixelSize(R.dimen.padding_small) / 2;

		for (Term parent : term.getHierarchy()) {
			addView(createTextView(parent, i, margin));
			i++;
		}
	}

	private RobotoTextView createTextView(final Term term, int position, int margin) {
		int padding = getResources().getDimensionPixelSize(R.dimen.tree_view_base_padding);

		RobotoTextView tv = new RobotoTextView(getContext());
		tv.setText(term.getDescriptor());

		tv.setTextColor(getResources().getColorStateList(R.color.hierarchy_label_text_selector));
		tv.setTypeface(null, Typeface.BOLD);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(
				R.dimen.element_card_label_text_size));
		tv.setBackgroundResource(R.drawable.label_background_hierarchy);
		tv.setPadding(padding, padding, padding, padding);

		final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(margin * position * 4, margin, margin, margin);
		tv.setLayoutParams(params);

		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onTermClicked(term);
			}
		});
		return tv;
	}


	private void onTermClicked(Term term) {
		if (mCallbacks != null) mCallbacks.onTermClicked(term);
	}

	public void setCallbacks(TreeViewCallbacks callbacks) {
		this.mCallbacks = callbacks;
	}

	public interface TreeViewCallbacks {
		void onTermClicked(Term term);
	}
}
