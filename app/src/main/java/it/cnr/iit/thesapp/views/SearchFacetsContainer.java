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

public class SearchFacetsContainer extends FrameLayout {

	private LinearLayout         categoryContainer;
	private RobotoCompoundButton actionButton;
	private boolean              showingFacets;
	private FacetContainer       facets;

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

		actionButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				onActionClicked();
			}
		});
	}

	public void setFacets(FacetContainer facetContainer) {
		this.facets = facetContainer;
		for (FacetCategory facetCategory : facets.getCategories()) {
			//TODO add a line
		}
	}

	private void onActionClicked() {
		if (showingFacets) {
			showingFacets = false;
			actionButton.setText(getContext().getString(R.string.filter_by_category));
			categoryContainer.setVisibility(GONE);
			//TODO Hide facets and begin search
		} else {
			showingFacets = true;
			actionButton.setText(getContext().getString(R.string.filter_by_all_categories));
			categoryContainer.setVisibility(VISIBLE);
			//TODO Show facets
		}
	}
}
