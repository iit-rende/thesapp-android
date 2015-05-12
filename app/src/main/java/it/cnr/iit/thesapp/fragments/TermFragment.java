package it.cnr.iit.thesapp.fragments;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.devspark.robototextview.widget.RobotoTextView;

import java.util.List;

import it.cnr.iit.thesapp.App;
import it.cnr.iit.thesapp.R;
import it.cnr.iit.thesapp.model.Category;
import it.cnr.iit.thesapp.model.Term;
import it.cnr.iit.thesapp.model.TimelineElement;
import it.cnr.iit.thesapp.utils.Logs;
import it.cnr.iit.thesapp.views.ErrorView;
import it.cnr.iit.thesapp.views.TermsContainer;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TermFragment extends TimelineElementFragment {
	private RobotoTextView termTitle;
	private RobotoTextView termDescription;
	private ScrollView     scrollView;
	private RobotoTextView termSubtitle;
	private Toolbar        toolbar;
	private View           titleContainer;
	private LinearLayout   hierarchyContainer;

	public TermFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_term, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		errorView = (ErrorView) view.findViewById(R.id.error_view);
		errorView.setCallback(new ErrorView.ErrorViewListener() {
			@Override
			public void onViewClicked() {
				fetchElement();
			}
		});

		toolbar = (Toolbar) view.findViewById(R.id.card_toolbar);
		toolbar.setNavigationIcon(getActivity().getResources().getDrawable(
				R.drawable.ic_navigation_arrow_back));
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mListener != null) mListener.onUpPressed();
			}
		});
		titleContainer = view.findViewById(R.id.title_container);
		cardContent = view.findViewById(R.id.card_content);
		hierarchyContainer = (LinearLayout) view.findViewById(R.id.hierarchy_container);

		termTitle = (RobotoTextView) view.findViewById(R.id.term_title);
		termSubtitle = (RobotoTextView) view.findViewById(R.id.term_subtitle);
		termDescription = (RobotoTextView) view.findViewById(R.id.term_description);

		scrollView = (ScrollView) view.findViewById(R.id.scrollView);
		scrollView.setVerticalScrollBarEnabled(false);
		scrollView.setHorizontalScrollBarEnabled(false);

		CardView cardView = (CardView) view.findViewById(R.id.cardView);
		cardView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("CardView", "Card Clicked, position " + page);
				if (pageListener != null) pageListener.onPageClicked(page);
			}
		});
	}


	public void fetchElement() {
		setUiLoading(true);
		Logs.retrofit(
				"Fetching term: " + termDescriptor + " in " + termDomain + " (" + termLanguage +
				")");
		App.getApi().getService().term(termDescriptor, termDomain, termLanguage,
				new Callback<Term>() {
					@Override
					public void success(Term term, Response response) {
						if (response.getStatus() == 200 && term != null) {
							Logs.retrofit("Term fetched: " + term);
							term.fillMissingInfo();
							reloadUi(term);
							persistTerm(term);
							setUiLoading(false);
						} else {
							Logs.retrofit("Error fetching term: " + response.getStatus() + " - " +
										  response.getReason());
							showError(response);
						}
					}

					@Override
					public void failure(RetrofitError error) {
						error.printStackTrace();
						showError(error);
					}
				});
	}


	public void reloadUi(TimelineElement element) {
		if (element instanceof Term) {
			Term term = (Term) element;
			term.fillMissingInfo();

			Logs.retrofit("Loading UI for " + term);
			termTitle.setText(term.getDescriptor());
			if (term.getUsedFor() != null) {
				termSubtitle.setVisibility(View.VISIBLE);
				termSubtitle.setText(term.getUsedFor().getDescriptor());
			} else {
				termSubtitle.setVisibility(View.GONE);
			}
			if (!TextUtils.isEmpty(term.getScopeNote())) {
				termDescription.setVisibility(View.VISIBLE);
				termDescription.setText(term.getScopeNote());
			} else {
				termDescription.setVisibility(View.GONE);
			}
			//setUiColor(Color.parseColor(term.getDomain().getColor()));

			hierarchyContainer.removeAllViews();
			addTermsContainer(term.getRelatedTerms(), getString(R.string.related_terms),
					getResources().getColor(R.color.material_deep_teal_500));
			addCategoryContainer(term.getCategories(), getString(R.string.categories_terms),
					getResources().getColor(R.color.md_green_500));
			addTermsContainer(term.getBroaderTerms(), getString(R.string.broader_terms),
					getResources().getColor(R.color.md_orange_500));
			addTermsContainer(term.getNarrowerTerms(), getString(R.string.narrower_terms),
					getResources().getColor(R.color.md_amber_500));
			setWindowToolbar(term.getDomain());
		}
	}


	private void setUiColor(int color) {
		titleContainer.setBackgroundColor(color);
		toolbar.setBackgroundColor(color);
	}

	private void addTermsContainer(List<Term> terms, String containerTitle, int termColor) {
		if (terms != null && terms.size() > 0) {
			TermsContainer container = new TermsContainer(getActivity());
			container.setTitle(containerTitle);
			container.setTerms(terms, termColor, mListener, page);
			hierarchyContainer.addView(container);
		}
	}

	private void addCategoryContainer(List<Category> categories, String containerTitle,
									  int termColor) {
		if (categories != null && categories.size() > 0) {
			TermsContainer container = new TermsContainer(getActivity());
			container.setTitle(containerTitle);
			container.setCategories(categories, termColor, mListener, page);
			hierarchyContainer.addView(container);
		}
	}

	public void scrollToTop() {
		scrollView.fullScroll(View.FOCUS_UP);
	}
}
