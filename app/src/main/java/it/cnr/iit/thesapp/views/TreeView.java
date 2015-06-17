package it.cnr.iit.thesapp.views;

import android.content.Context;
import android.content.res.ColorStateList;
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
		int i = 1;
		int margin = getResources().getDimensionPixelSize(R.dimen.padding_small) / 2;

		for (Term parent : term.getHierarchy()) {
			addView(createTextViewV2(parent, i, margin, true));
			i++;
		}

		//Adding the term as non clickable
		addView(createTextViewV2(term, i, margin, false));
		i++;

		//Adding the narrower terms
		for (Term narrower : term.getNarrowerTerms()) {
			addView(createTextViewV2(narrower, i, margin, true));
		}
	}

	private RobotoTextView createTextView(final Term term, int position, int margin,
	                                      boolean clickable) {
		int padding = getResources().getDimensionPixelSize(R.dimen.tree_view_base_padding);

		RobotoTextView tv = new RobotoTextView(getContext());
		tv.setText(term.getDescriptor());

		final ColorStateList colorStateList = clickable ? getResources().getColorStateList(
				R.color.hierarchy_label_text_selector) : getResources().getColorStateList(
				R.color.hierarchy_label_text_selector_not_clickable);
		tv.setTextColor(colorStateList);
		tv.setTypeface(null, clickable ? Typeface.BOLD : Typeface.ITALIC);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(
				R.dimen.element_card_label_text_size));
		tv.setBackgroundResource(clickable ? R.drawable.label_background_hierarchy :
		                         R.drawable.label_background_hierarchy_not_clickable);
		tv.setPadding(padding, padding, padding, padding);

		final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(margin * position * 4, margin, margin, margin);
		tv.setLayoutParams(params);

		if (clickable) tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onTermClicked(term);
			}
		});
		return tv;
	}

	private RobotoTextView createTextViewV2(final Term term, int position, int margin,
	                                        boolean clickable) {
		int padding = getResources().getDimensionPixelSize(R.dimen.tree_view_base_padding);

		RobotoTextView tv = new RobotoTextView(getContext());
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < position; i++) {
			sb.append("\u2022 ");
		}
		sb.append(" ");
		sb.append(term.getDescriptor());
		tv.setText(sb.toString());

		final ColorStateList colorStateList = clickable ? getResources().getColorStateList(
				R.color.hierarchy_label_text_selector) : getResources().getColorStateList(
				R.color.hierarchy_label_text_selector_not_clickable);
		tv.setTextColor(colorStateList);
		tv.setTypeface(null, Typeface.BOLD);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(
				R.dimen.element_card_label_text_size));
		//tv.setBackgroundResource(clickable ? R.drawable.label_background_hierarchy : R.drawable
		// .label_background_hierarchy_not_clickable);
		tv.setPadding(padding, padding, padding, padding);

		final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(margin, margin, margin, margin);
		tv.setLayoutParams(params);

		if (clickable) tv.setOnClickListener(new OnClickListener() {
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
