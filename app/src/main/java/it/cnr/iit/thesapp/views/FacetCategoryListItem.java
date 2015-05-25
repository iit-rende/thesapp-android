package it.cnr.iit.thesapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.devspark.robototextview.widget.RobotoTextView;

import it.cnr.iit.thesapp.R;
import it.cnr.iit.thesapp.model.FacetCategory;

public class FacetCategoryListItem extends FrameLayout {

	private FacetCategory         category;
	private RobotoTextView        categoryName;
	private CategoryClickListener mListener;
	private RobotoTextView        categorySize;

	public FacetCategoryListItem(Context context) {
		super(context);
		init();
	}

	public FacetCategoryListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public FacetCategoryListItem(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public void init() {
		inflate(getContext(), R.layout.item_facet_category, this);
		categoryName = (RobotoTextView) findViewById(R.id.category_name);
		categorySize = (RobotoTextView) findViewById(R.id.category_size);
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) mListener.onCategoryClicked(getCategory());
			}
		});
	}

	public void setCategoryClickListener(CategoryClickListener listener) {
		mListener = listener;
	}

	public FacetCategory getCategory() {
		return category;
	}

	public void setCategory(FacetCategory category) {
		this.category = category;
		categoryName.setText(category.getDescriptor());
		categorySize.setText("" + category.getCount());
	}

	public interface CategoryClickListener {

		void onCategoryClicked(FacetCategory category);
	}
}
