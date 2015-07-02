package it.cnr.iit.thesapp.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.devspark.robototextview.style.RobotoTypefaceSpan;
import com.devspark.robototextview.util.RobotoTypefaceManager;
import com.devspark.robototextview.widget.RobotoButton;

import it.cnr.iit.thesapp.R;
import it.cnr.iit.thesapp.model.FacetCategory;
import it.cnr.iit.thesapp.model.FacetContainer;
import it.cnr.iit.thesapp.utils.Logs;

public class SearchFacetsContainer extends FrameLayout implements FacetCategoryListItem
		.CategoryClickListener {


	private LinearLayout   categoryContainer;
	private RobotoButton   actionButton;
	private boolean        showingFacets;
	private FacetContainer facets;
	private SearchFacetListener mListener;
	private Drawable       filterDrawable;
	private Drawable       unfilterDrawable;
	private Drawable       closePanelDrawable;
	private int            color;
	private View           background;

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
		this.actionButton = (RobotoButton) findViewById(R.id.facet_action);
		this.background = findViewById(R.id.background);
		filterDrawable = getContext().getResources().getDrawable(R.drawable.ic_filter_list);
		unfilterDrawable = getContext().getResources().getDrawable(R.drawable.ic_close);
		closePanelDrawable = getContext().getResources().getDrawable(
				R.drawable.ic_keyboard_arrow_up);
	}

	public void setFacets(FacetContainer facetContainer, FacetCategory selectedCategory) {
		this.facets = facetContainer;
		close();
		categoryContainer.removeAllViews();
		if (facets != null) {

			for (FacetCategory facetCategory : facets.getCategories()) {
				FacetCategoryListItem line = new FacetCategoryListItem(getContext());
				line.setCategory(facetCategory);
				line.setCategoryClickListener(this);
				categoryContainer.addView(line);
			}
			if (facetContainer.getCategories().size() == 0 && selectedCategory != null) {
				setActionText(selectedCategory);
				actionButton.setCompoundDrawablesWithIntrinsicBounds(null, null, unfilterDrawable,
						null);

				actionButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						if (mListener != null) mListener.onCategoryClicked(null);
					}
				});
			} else if (facetContainer.getCategories().size() == 1) {
				setActionText(facetContainer.getCategories().get(0));
				actionButton.setCompoundDrawablesWithIntrinsicBounds(null, null, unfilterDrawable,
						null);

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
	}

	private void setActionText(FacetCategory category) {
		actionButton.setText(category.getDescriptor());
	}

	private void setActionText2(FacetCategory category) {
		SpannableStringBuilder sb = new SpannableStringBuilder();

		String cat = category.getDescriptor();
		String sub = getContext().getString(R.string.filter_by_all_category_with_selected);

		RobotoTypefaceSpan robotoTypefaceSpan = new RobotoTypefaceSpan(getContext(),
				RobotoTypefaceManager.Typeface.ROBOTO_BLACK);
		Spannable spannable = new SpannableString(cat);
		spannable.setSpan(robotoTypefaceSpan, 0, spannable.length() - 1,
				Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		spannable.setSpan(new RelativeSizeSpan(1.5f), 0, spannable.length() - 1,
				Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		Logs.ui("Span: " + spannable.getSpanStart(robotoTypefaceSpan) + "-->" +
		        spannable.getSpanEnd(robotoTypefaceSpan));

		sb.append(spannable).append("\n").append(sub);

		actionButton.setText(sb.toString());
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
		actionButton.setCompoundDrawablesWithIntrinsicBounds(null, null, closePanelDrawable, null);
		categoryContainer.setVisibility(VISIBLE);
		if (mListener != null) mListener.onContainerOpened();
	}

	private void close() {
		showingFacets = false;
		actionButton.setText(getContext().getString(R.string.filter_by_category));
		actionButton.setCompoundDrawablesWithIntrinsicBounds(null, null, filterDrawable, null);

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

	public void setColor(int color) {
		this.color = color;
		background.setBackgroundColor(color);
	}

	public interface SearchFacetListener {

		void onCategoryClicked(FacetCategory category);
		void onContainerOpened();
	}
}
