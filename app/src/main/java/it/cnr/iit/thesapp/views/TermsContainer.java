package it.cnr.iit.thesapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.devspark.robototextview.widget.RobotoTextView;

import org.apmem.tools.layouts.FlowLayout;

import java.util.List;

import it.cnr.iit.thesapp.R;
import it.cnr.iit.thesapp.fragments.TermFragment;
import it.cnr.iit.thesapp.model.Term;

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

	public void setTerms(List<Term> terms, int termColor,
						 final TermFragment.TermFragmentCallbacks callback) {
		int padding = getResources().getDimensionPixelSize(R.dimen.padding_small);
		int textColor = getResources().getColor(R.color.white);
		if (terms != null) for (final Term term : terms) {
			RobotoTextView tv = new RobotoTextView(getContext());
			tv.setText(term.getDescriptor());
			tv.setTextColor(textColor);
			tv.setPadding(padding, padding, padding, padding);
			tv.setBackgroundColor(termColor);

			final FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
					FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
			params.setMargins(padding / 2, padding / 2, padding / 2, padding / 2);
			tv.setLayoutParams(params);

			tv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (callback != null) callback.onTermClicked(term.getDescriptor(),
							term.getDomainDescriptor(), term.getLanguage());
				}
			});
			container.addView(tv);
		}
	}
}
