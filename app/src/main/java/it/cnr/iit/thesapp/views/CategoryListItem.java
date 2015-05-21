package it.cnr.iit.thesapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.devspark.robototextview.widget.RobotoTextView;

import it.cnr.iit.thesapp.R;
import it.cnr.iit.thesapp.model.Category;

public class CategoryListItem extends FrameLayout {

	private Category              category;
	private RobotoTextView        categoryName;
	private CategoryClickListener mListener;

	public CategoryListItem(Context context) {
		super(context);
		init();
	}

	public CategoryListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CategoryListItem(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public void init() {
		inflate(getContext(), R.layout.panel_domain_list_item, this);
		categoryName = (RobotoTextView) findViewById(R.id.domain_name);
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

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
		categoryName.setText(category.getDescriptor());
	}

	public interface CategoryClickListener {

		void onCategoryClicked(Category category);
	}
}
