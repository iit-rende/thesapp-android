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

import com.devspark.robototextview.util.RobotoTextViewUtils;
import com.devspark.robototextview.util.RobotoTypefaceManager;
import com.devspark.robototextview.widget.RobotoTextView;
import com.github.clemp6r.futuroid.Async;
import com.github.clemp6r.futuroid.Future;
import com.github.clemp6r.futuroid.SuccessCallback;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

import it.cnr.iit.thesapp.R;
import it.cnr.iit.thesapp.fragments.TimelineElementFragment;
import it.cnr.iit.thesapp.model.Term;
import it.cnr.iit.thesapp.utils.Logs;

public class TermsContainerWithAlphabet extends LinearLayout {

	private RobotoTextView title;
	private FlowLayout     container;

	public TermsContainerWithAlphabet(Context context) {
		super(context);
		init();
	}

	public TermsContainerWithAlphabet(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TermsContainerWithAlphabet(Context context, AttributeSet attrs, int defStyleAttr) {
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
			new TextViewService().prepareTermTextViews(terms, colorId, drawableId, margin,
					callback,
					page).addSuccessCallback(

					new SuccessCallback<List<View>>() {
						@Override
						public void onSuccess(List<View> result) {
							for (View robotoTextView : result) {
								container.addView(robotoTextView);
							}
						}
					});
		}
	}

	public RobotoTextView createTextView(@ColorRes int colorId, @DrawableRes int drawableId,
	                                     int margin) {
		RobotoTextView tv = new RobotoTextView(getContext());
		tv.setTextColor(getResources().getColorStateList(colorId));
		tv.setTypeface(null, Typeface.BOLD);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(
				R.dimen.element_card_label_text_size));
		if (getWidth() > 0) tv.setMaxWidth(this.getWidth() - margin * 20);
		tv.setBackgroundResource(drawableId);
		final FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
				FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(margin, margin, margin, margin);
		tv.setLayoutParams(params);
		return tv;
	}

	public RobotoTextView createHeader() {
		RobotoTextView tv = new RobotoTextView(getContext());
		tv.setTextColor(getResources().getColor(R.color.category_label_header));

		Typeface typeface = RobotoTypefaceManager.obtainTypeface(getContext(),
				RobotoTypefaceManager.Typeface.ROBOTO_LIGHT);
		RobotoTextViewUtils.setTypeface(tv, typeface);

		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(
				R.dimen.element_card_header_text_size));
		int padding = getResources().getDimensionPixelSize(R.dimen.element_card_header_padding);
		tv.setPadding(padding, padding * 2, padding, padding);

		final FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
				FlowLayout.LayoutParams.MATCH_PARENT, FlowLayout.LayoutParams.WRAP_CONTENT);

		tv.setLayoutParams(params);
		return tv;
	}

	public class TextViewService {

		public Future<List<View>> prepareTermTextViews(final List<Term> terms,
		                                               @ColorRes final int colorId,
		                                               @DrawableRes final int drawableId,
		                                               final int margin,
		                                               final TimelineElementFragment
				                                               .TimelineElementFragmentCallback
				                                               callback,

		                                               final int page) {
			return Async.submit(new Callable<List<View>>() {
				@Override
				public List<View> call() {
					Collections.sort(terms, new Comparator<Term>() {
						@Override
						public int compare(Term lhs, Term rhs) {
							String s1 = lhs.getDescriptor();
							String s2 = rhs.getDescriptor();

							String s1Trimmed;
							if (Character.isDigit(s1.charAt(0))) s1Trimmed = s1.substring(
									s1.indexOf(' ') + 1);
							else s1Trimmed = s1;

							String s2Trimmed;
							if (Character.isDigit(s2.charAt(0))) s2Trimmed = s2.substring(
									s2.indexOf(' ') + 1);
							else s2Trimmed = s2;

							final int i = s1Trimmed.toLowerCase().compareTo(
									s2Trimmed.toLowerCase());
							if (i != 0) return i;
							else return s1.toLowerCase().compareTo(s2.toLowerCase());
						}
					});

					List<View> tvs = new ArrayList<View>();
					char previousChar = '\u0000';
					for (final Term term : terms) {
						final String descr = term.getDescriptor().toUpperCase();
						String descrTrimmed;
						if (Character.isDigit(descr.charAt(0))) descrTrimmed = descr.substring(
								descr.indexOf(' ') + 1);
						else descrTrimmed = descr;
						char c = descrTrimmed.charAt(0);

						if (Character.isDigit(c)) c = '#';

						if (c != previousChar) {
							previousChar = c;
							RobotoTextView header = createHeader();
							header.setText(String.valueOf(previousChar));
							tvs.add(header);
						}
						RobotoTextView tv = createTextView(colorId, drawableId, margin);
						tv.setText(term.getDescriptor());

						tv.setOnClickListener(new OnClickListener() {
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
						tvs.add(tv);
					}
					return tvs;
				}
			});
		}

		public Future<List<RobotoTextView>> prepareHeaderViews(final List<Term> terms,
		                                                       @ColorRes final int colorId,
		                                                       @DrawableRes final int drawableId,
		                                                       final int margin,
		                                                       final TimelineElementFragment
				                                                       .TimelineElementFragmentCallback callback,

		                                                       final int page) {
			return Async.submit(new Callable<List<RobotoTextView>>() {
				@Override
				public List<RobotoTextView> call() {
					List<RobotoTextView> tvs = new ArrayList<RobotoTextView>();
					for (final Term term : terms) {
						RobotoTextView tv = createTextView(colorId, drawableId, margin);
						tv.setText(term.getDescriptor());

						tv.setOnClickListener(new OnClickListener() {
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
						tvs.add(tv);
					}
					return tvs;
				}
			});
		}
	}
}
