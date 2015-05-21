package it.cnr.iit.thesapp.fragments;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
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
import it.cnr.iit.thesapp.model.CategoryList;
import it.cnr.iit.thesapp.model.TimelineElement;
import it.cnr.iit.thesapp.utils.Logs;
import it.cnr.iit.thesapp.views.CategoryListItem;
import it.cnr.iit.thesapp.views.ErrorView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CategoryListFragment extends TimelineElementFragment implements CategoryListItem
		.CategoryClickListener {

	private RobotoTextView termTitle;
	private RobotoTextView termDescription;
	private ScrollView     scrollView;
	private RobotoTextView termSubtitle;
	private View           titleContainer;
	private LinearLayout   domainContainer;

	public CategoryListFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_domain_list, container, false);
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

		if (page == 0 && toolbar != null) {
			toolbar.setNavigationIcon(null);
			toolbar.setNavigationOnClickListener(null);
		}
		titleContainer = view.findViewById(R.id.title_container);
		cardContent = view.findViewById(R.id.card_content);
		domainContainer = (LinearLayout) view.findViewById(R.id.domain_list_container);

		termTitle = (RobotoTextView) view.findViewById(R.id.term_title);
		termSubtitle = (RobotoTextView) view.findViewById(R.id.term_subtitle);

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
		Logs.retrofit("Fetching categories: " + termDescriptor + " in " + termDomain + " (" +
		              termLanguage +
		              ")");
		App.getApi().getService().categoryList(termDomain, termLanguage,
				new Callback<CategoryList>() {
					@Override
					public void success(CategoryList domainSearch, Response response) {
						if (response.getStatus() == 200 && domainSearch != null) {
							Logs.retrofit(
									"CategoryList fetched: " + domainSearch.getCategories().size
											());
							prepareCategoryListElement(domainSearch);
							setUiLoading(false);
						} else {
							Logs.retrofit(
									"Error fetching CategoryList: " + response.getStatus() + " -" +
									" " +
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
		setUiLoading(false);
		if (element instanceof CategoryList) {
			CategoryList categoryList = (CategoryList) element;
			categoryList.fillMissingInfo();

			Logs.retrofit("Loading UI for " + categoryList);
			termTitle.setText(getString(R.string.category_list_title));
			termSubtitle.setText(getString(R.string.category_list_subtitle,
					categoryList.getDomainDescriptor()));

			domainContainer.removeAllViews();
			addTermsContainer(categoryList.getCategories());
		}
	}

	public void scrollToTop() {
		scrollView.fullScroll(View.FOCUS_UP);
	}

	private void prepareCategoryListElement(CategoryList categoryList) {
		categoryList.fillMissingInfo();
		reloadUi(categoryList);
		persistElement(categoryList);
	}

	private void addTermsContainer(List<Category> categories) {
		if (categories != null && categories.size() > 0) {
			for (Category category : categories) {
				CategoryListItem container = new CategoryListItem(getActivity());

				container.setCategory(category);
				container.setCategoryClickListener(this);
				domainContainer.addView(container);
			}
		}
	}

	@Override
	public void onCategoryClicked(Category category) {
		if (mListener != null) mListener.onElementClicked(category.getDescriptor(),
				category.getDomainDescriptor(), category.getLanguage(), category.getElementKind(),
				page);
	}
}
