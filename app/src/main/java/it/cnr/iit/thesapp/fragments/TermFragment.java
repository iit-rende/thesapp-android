package it.cnr.iit.thesapp.fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
import it.cnr.iit.thesapp.utils.PrefUtils;
import it.cnr.iit.thesapp.utils.SdkUtils;
import it.cnr.iit.thesapp.views.ErrorView;
import it.cnr.iit.thesapp.views.TermsContainer;
import it.cnr.iit.thesapp.views.TreeView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TermFragment extends TimelineElementFragment {

	private RobotoTextView termTitle;
	private RobotoTextView termDescription;
	private ScrollView     scrollView;
	private RobotoTextView termSubtitle;
	private View           titleContainer;
	private LinearLayout   infoContainer;
	private TreeView       termTreeView;
	private RobotoTextView treeViewHeader;
	private boolean        withTree;
	private RobotoTextView termDescriptionLabel;

	public TermFragment() {}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
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
		toolbar.setNavigationIcon(getActivity().getResources()
		                                       .getDrawable(R.drawable.ic_navigation_arrow_back));
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mListener != null) mListener.onUpPressed();
			}
		});

		if (page == 0 && toolbar != null) {
			toolbar.setNavigationIcon(null);
			toolbar.setNavigationOnClickListener(null);
		}
		titleContainer = view.findViewById(R.id.title_container);
		cardContent = view.findViewById(R.id.card_content);
		infoContainer = (LinearLayout) view.findViewById(R.id.hierarchy_container);

		termTitle = (RobotoTextView) view.findViewById(R.id.term_title);
		termSubtitle = (RobotoTextView) view.findViewById(R.id.term_subtitle);
		termDescription = (RobotoTextView) view.findViewById(R.id.term_description);
		termDescriptionLabel = (RobotoTextView) view.findViewById(R.id.term_description_label);

		treeViewHeader = (RobotoTextView) view.findViewById(R.id.tree_view_header);
		termTreeView = (TreeView) view.findViewById(R.id.tree_view);
		withTree = treeViewHeader != null;
		if (withTree) termTreeView.setCallbacks(new TreeView.TreeViewCallbacks() {
			@Override
			public void onTermClicked(Term term) {
				if (mListener != null) mListener.onElementClicked(term.getDescriptor(),
						term.getDomainDescriptor(), term.getLanguage(), TimelineElement.KIND_TERM,
						page);
			}
		});

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
		App.getApi()
		   .getService()
		   .term(termDescriptor, termDomain, termLanguage, new Callback<Term>() {
			   @Override
			   public void success(Term term, Response response) {
				   if (response.getStatus() == 200 && term != null) {
					   Logs.retrofit("Term fetched: " + term);
					   term.fillMissingInfo();
					   reloadUi(term);
					   persistElement(term);
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
		if (isDetached()) return;
		setUiLoading(false);
		if (element instanceof Term) {
			Term term = (Term) element;
			term.fillMissingInfo();

			boolean useItalian = PrefUtils.IT.equals(element.getLanguage());
			Logs.retrofit("Loading UI for " + term);
			termTitle.setText(term.getDescriptor());
			if (term.getUsedFor() != null) {
				termSubtitle.setVisibility(View.VISIBLE);
				termSubtitle.setText(term.getUsedFor()
				                         .getDescriptor());
			} else {
				termSubtitle.setVisibility(View.GONE);
			}
			if (!TextUtils.isEmpty(term.getScopeNote())) {
				termDescription.setVisibility(View.VISIBLE);
				termDescriptionLabel.setVisibility(View.VISIBLE);
				termDescription.setText(term.getScopeNote());
				termDescriptionLabel.setText(useItalian ? R.string.term_description_label_it :
				                             R.string.term_description_label_en);
			} else {
				termDescription.setVisibility(View.GONE);
				termDescriptionLabel.setVisibility(View.GONE);
			}
			//setUiColor(Color.parseColor(term.getDomain().getColor()));

			if (withTree) {
				termTreeView.setVisibility(View.VISIBLE);
				treeViewHeader.setVisibility(View.VISIBLE);
				termTreeView.setHierarchy(term);
			}

			infoContainer.removeAllViews();
			addCategoryContainer(term.getCategories(), getResources().getQuantityString(
							useItalian ? R.plurals.categories_terms_it :
							R.plurals.categories_terms_en, term.getCategories()
							                                   .size()),
					R.drawable.label_background_category, R.color.category_label_text_selector);

			addTermsContainer(term.getLocalizations(), getResources().getQuantityString(
							useItalian ? R.plurals.translations_terms_it :
							R.plurals.translations_terms_en, term.getLocalizations()
							                                     .size()),
					R.drawable.label_background_localization,
					R.color.localization_label_text_selector);

			addTermsContainer(term.getUseFor(), getResources().getQuantityString(
							useItalian ? R.plurals.synonyms_terms_it : R.plurals.synonyms_terms_en,
							term.getUseFor()
							    .size()), R.drawable.label_background_synonym,
					R.color.synonym_label_text_selector);

			addTermsContainer(term.getBroaderTerms(), getResources().getQuantityString(
							useItalian ? R.plurals.broader_terms_it : R.plurals.broader_terms_en,
							term.getBroaderTerms()
							    .size()), R.drawable.label_background_broader,
					R.color.broader_label_text_selector);

			addTermsContainer(term.getNarrowerTerms(), getResources().getQuantityString(
							useItalian ? R.plurals.narrower_terms_it : R.plurals.narrower_terms_en,
							term.getNarrowerTerms()
							    .size()), R.drawable.label_background_narrower,
					R.color.narrower_label_text_selector);

			addTermsContainer(term.getRelatedTerms(), getResources().getQuantityString(
							useItalian ? R.plurals.related_terms_it : R.plurals.related_terms_en,
							term.getRelatedTerms()
							    .size()), R.drawable.label_background_related,
					R.color.related_label_text_selector);

			setWindowToolbar(term.getDomain());
		}
	}

	public void scrollToTop() {
		scrollView.fullScroll(View.FOCUS_UP);
	}

	private void addTermsContainer(final List<Term> terms, String containerTitle,
	                               @DrawableRes final int drawableId, @ColorRes final int
			                               colorId) {
		if (terms != null && terms.size() > 0) {
			final TermsContainer container = new TermsContainer(getActivity());
			container.setTitle(containerTitle);
			container.getViewTreeObserver()
			         .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

				         @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
				         @Override
				         public void onGlobalLayout() {
					         if (SdkUtils.isPreSDK16()) {
						         container.getViewTreeObserver()
						                  .removeGlobalOnLayoutListener(this);
					         } else {
						         container.getViewTreeObserver()
						                  .removeOnGlobalLayoutListener(this);
					         }
					         container.setTerms(terms, drawableId, colorId, mListener, page);
				         }
			         });
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams
					.WRAP_CONTENT);
			container.setLayoutParams(params);
			infoContainer.addView(container);
		}
	}

	private void addCategoryContainer(final List<Category> categories, String containerTitle,
	                                  @DrawableRes final int drawableId,
	                                  @ColorRes final int colorId) {
		if (categories != null && categories.size() > 0) {
			final TermsContainer container = new TermsContainer(getActivity());
			container.setTitle(containerTitle);
			container.getViewTreeObserver()
			         .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

				         @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
				         @Override
				         public void onGlobalLayout() {
					         if (SdkUtils.isPreSDK16()) {
						         container.getViewTreeObserver()
						                  .removeGlobalOnLayoutListener(this);
					         } else {
						         container.getViewTreeObserver()
						                  .removeOnGlobalLayoutListener(this);
					         }
					         container.setCategories(categories, drawableId, colorId, mListener,
							         page);
				         }
			         });
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams
					.WRAP_CONTENT);
			container.setLayoutParams(params);
			infoContainer.addView(container);
		}
	}
}
