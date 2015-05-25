package it.cnr.iit.thesapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.devspark.robototextview.widget.RobotoCompoundButton;

import it.cnr.iit.thesapp.R;
import it.cnr.iit.thesapp.model.FacetCategory;
import it.cnr.iit.thesapp.model.FacetContainer;

public class SearchFacetsContainer extends FrameLayout implements FacetCategoryListItem
		.CategoryClickListener {


	private LinearLayout         categoryContainer;
	private RobotoCompoundButton actionButton;
	private boolean              showingFacets;
	private FacetContainer       facets;
	private SearchFacetListener mListener;

	public SearchFacetsContainer(Context context) {
		super(context);
		init();
	}

	public SearchFacetsContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SearchFacetsContainer(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		inflate(getContext(), R.layout.item_term_search_suggested_categories, this);
		this.categoryContainer = (LinearLayout) findViewById(R.id.categories_container);
		this.actionButton = (RobotoCompoundButton) findViewById(R.id.facet_action);
	}

	public void setFacets(FacetContainer facetContainer) {
		this.facets = facetContainer;
		close();
		categoryContainer.removeAllViews();
		for (FacetCategory facetCategory : facets.getCategories()) {
			FacetCategoryListItem line = new FacetCategoryListItem(getContext());
			line.setCategory(facetCategory);
			line.setCategoryClickListener(this);
			categoryContainer.addView(line);
		}
		if (facetContainer.getCategories().size() == 1) {
			actionButton.setText(getContext().getString(
					R.string.filter_by_all_category_with_selected,
					facetContainer.getCategories().get(0).getDescriptor()));
			actionButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					if (mListener != null) mListener.onCategoryClicked(null);
				}
			});
		} else {
			actionButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					onActionClicked();
				}
			});
		}
	}

	private void onActionClicked() {
		if (showingFacets) {
			if (mListener != null) mListener.onCategoryClicked(null);
			close();
		} else {
			open();
		}
	}

	private void open() {
		showingFacets = true;
		actionButton.setText(getContext().getString(R.string.filter_by_all_categories));
		categoryContainer.setVisibility(VISIBLE);
	}

	private void close() {
		showingFacets = false;
		actionButton.setText(getContext().getString(R.string.filter_by_category));
		categoryContainer.setVisibility(GONE);
	}

	@Override
	public void onCategoryClicked(FacetCategory category) {
		close();
		if (mListener != null) mListener.onCategoryClicked(category);
	}

	public void setSearchFacetListener(SearchFacetListener listener) {
		this.mListener = listener;
	}

	public interface SearchFacetListener {

		void onCategoryClicked(FacetCategory category);
	}
}
