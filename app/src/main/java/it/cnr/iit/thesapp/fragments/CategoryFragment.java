package it.cnr.iit.thesapp.fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
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
import it.cnr.iit.thesapp.views.TermsContainerWithAlphabet;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CategoryFragment extends TimelineElementFragment {

	private RobotoTextView termTitle;
	private ScrollView     scrollView;
	private LinearLayout   hierarchyContainer;

	public CategoryFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_category, container, false);
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
		cardContent = view.findViewById(R.id.card_content);
		hierarchyContainer = (LinearLayout) view.findViewById(R.id.hierarchy_container);

		termTitle = (RobotoTextView) view.findViewById(R.id.term_title);

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
		Logs.retrofit("Fetching Category: " + termDescriptor + " in " + termDomain + " (" +
		              termLanguage +
		              ")");
		App.getApi()
		   .getService()
		   .category(termDescriptor, termDomain, termLanguage, new Callback<Category>() {
			   @Override
			   public void success(Category category, Response response) {
				   if (response.getStatus() == 200 && category != null) {
					   Logs.retrofit("Category fetched: " + category);
					   category.fillMissingInfo();
					   reloadUi(category);
					   persistElement(category);
					   setUiLoading(false);
				   } else {
					   Logs.retrofit("Error fetching category: " + response.getStatus() + " - " +
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
		if (element instanceof Category) {
			Category category = (Category) element;

			Logs.ui("Loading UI for " + category);
			termTitle.setText(category.getDescriptor());

			//setUiColor(Color.parseColor(category.getDomain().getColor()));
			boolean useItalian = PrefUtils.IT.equals(element.getLanguage());
			hierarchyContainer.removeAllViews();
			addTermsContainer(category.getTerms(), getString(
							useItalian ? R.string.category_terms_it : R.string.category_terms_en),
					R.drawable.term_label_selector, R.color.term_label_text_selector);

			setWindowToolbar(category.getDomain());
		}
	}

	public void scrollToTop() {
		scrollView.fullScroll(View.FOCUS_UP);
	}

	private void addTermsContainer(final List<Term> terms, String containerTitle,
	                               @DrawableRes final int drawableId, @ColorRes final int
			                               colorId) {
		if (terms != null && terms.size() > 0) {
			final TermsContainerWithAlphabet container = new TermsContainerWithAlphabet(
					getActivity());
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
			hierarchyContainer.addView(container);
		}
	}
}
