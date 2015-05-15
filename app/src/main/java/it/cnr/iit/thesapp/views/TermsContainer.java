package it.cnr.iit.thesapp.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.devspark.robototextview.widget.RobotoTextView;

import org.apmem.tools.layouts.FlowLayout;

import java.util.List;

import it.cnr.iit.thesapp.R;
import it.cnr.iit.thesapp.fragments.TimelineElementFragment;
import it.cnr.iit.thesapp.model.Category;
import it.cnr.iit.thesapp.model.Term;
import it.cnr.iit.thesapp.utils.Logs;

public class TermsContainer extends LinearLayout {

	private RobotoTextView title;
	private FlowLayout     container;

	public TermsContainer(Context context) {
		super(context);
		init();
	}

	public TermsContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TermsContainer(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		View view = inflate(getContext(), R.layout.panel_terms_container, this);
		title = (RobotoTextView) view.findViewById(R.id.container_title);
		container = (FlowLayout) view.findViewById(R.id.container_terms);
	}

	public void setTitle(String text) {
		title.setText(text);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void setTerms(List<Term> terms, @DrawableRes int drawableId, @ColorRes int colorId,
						 final TimelineElementFragment.TimelineElementFragmentCallback callback,
						 final int page) {
		int margin = getResources().getDimensionPixelSize(R.dimen.padding_small) / 2;

		if (terms != null) {
			for (final Term term : terms) {
				RobotoTextView tv = createTextView(colorId, drawableId, margin);
				tv.setText(term.getDescriptor());

				tv.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (callback != null) {
							Logs.ui("Term clicked: " + term.toString());
							callback.onElementClicked(term.getDescriptor(),
									term.getDomainDescriptor(), term.getLanguage(),
									term.getElementKind(), page);
						}
					}
				});
				container.addView(tv);
			}
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void setCategories(List<Category> categories, @DrawableRes int drawableId,
							  @ColorRes int colorId,
							  final TimelineElementFragment.TimelineElementFragmentCallback callback,
							  final int page) {
		int margin = getResources().getDimensionPixelSize(R.dimen.padding_small) / 2;

		if (categories != null) {
			for (final Category category : categories) {
				RobotoTextView tv = createTextView(colorId, drawableId, margin);
				tv.setText(category.getDescriptor());

				tv.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (callback != null) {
							Logs.ui("Category clicked: " + category.toString());
							callback.onElementClicked(category.getDescriptor(),
									category.getDomainDescriptor(), category.getLanguage(),
									category.getElementKind(), page);
						}
					}
				});
				container.addView(tv);
			}
		}
	}

	public RobotoTextView createTextView(@ColorRes int colorId, @ColorRes int drawableId,
										 int margin) {
		RobotoTextView tv = new RobotoTextView(getContext());
		tv.setTextColor(getResources().getColorStateList(colorId));
		tv.setTypeface(null, Typeface.BOLD);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(
				R.dimen.element_card_label_text_size));
		tv.setBackgroundResource(drawableId);
		final FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
				FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(margin, margin, margin, margin);
		tv.setLayoutParams(params);
		return tv;
	}
}
