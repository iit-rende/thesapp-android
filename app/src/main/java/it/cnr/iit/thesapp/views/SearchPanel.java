package it.cnr.iit.thesapp.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import java.util.List;

import it.cnr.iit.thesapp.R;
import it.cnr.iit.thesapp.adapters.DomainSpinnerAdapter;
import it.cnr.iit.thesapp.adapters.SimpleSectionedRecyclerViewAdapter;
import it.cnr.iit.thesapp.adapters.TermSearchRecAdapter;
import it.cnr.iit.thesapp.model.Domain;
import it.cnr.iit.thesapp.model.FacetCategory;
import it.cnr.iit.thesapp.model.Term;

public class SearchPanel extends FrameLayout implements TermSearchRecAdapter.TermClickListener {

	private TermSearchRecAdapter        mAdapter;
	private SearchBox                   searchBox;
	private SearchBox.SearchBoxListener mListener;
	private int                         mBoundedWidth;
	private int                         mBoundedHeight;
	private View fakeToolbar;

	public SearchPanel(Context context) {
		super(context);
		init();
		mBoundedWidth = 0;
		mBoundedHeight = 0;
	}

	public SearchPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BoundedView);
		mBoundedWidth = a.getDimensionPixelSize(R.styleable.BoundedView_bounded_width, 0);
		mBoundedHeight = a.getDimensionPixelSize(R.styleable.BoundedView_bounded_height, 0);
		a.recycle();
		init();
	}

	public SearchPanel(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BoundedView);
		mBoundedWidth = a.getDimensionPixelSize(R.styleable.BoundedView_bounded_width, 0);
		mBoundedHeight = a.getDimensionPixelSize(R.styleable.BoundedView_bounded_height, 0);
		a.recycle();
		init();
	}

	private void init() {
		inflate(getContext(), R.layout.panel_search_panel, this);
		fakeToolbar = findViewById(R.id.fake_toolbar);
		searchBox = (SearchBox) findViewById(R.id.search_box);

		mAdapter = new TermSearchRecAdapter(null, this, getContext());
		SimpleSectionedRecyclerViewAdapter mSectionedAdapter =
				new SimpleSectionedRecyclerViewAdapter(getContext(),
						R.layout.item_search_section_header,
						R.id.section_text, mAdapter);
		mAdapter.setSectionAdapter(mSectionedAdapter);
		searchBox.setTermSearchAdapter(mAdapter);

		DomainSpinnerAdapter domainSpinnerAdapter = new DomainSpinnerAdapter(getContext(), null);
		searchBox.setDomainSpinnerAdapter(domainSpinnerAdapter);

		CustomRecyclerView mList = (CustomRecyclerView) findViewById(R.id.search_list);
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
		mList.setLayoutManager(mLayoutManager);
		//mList.setAdapter(mAdapter);
		mList.setAdapter(mSectionedAdapter);
	}


	@Override
	public void onTermClicked(Term term) {
		searchBox.hideKeyboard();
		if (mListener != null) mListener.onTermSelected(term.getDescriptor(),
				term.getDomainDescriptor(), term.getLanguage());
	}

	@Override
	public void onTermLongClicked(Term monster) {

	}

	@Override
	public void onCategoryClicked(FacetCategory category) {
		searchBox.reperformSearch();
	}

	@Override
	public void onFacetContainerOpened() {
		hideKeyboard();
	}

	public void setSearchListener(SearchBox.SearchBoxListener searchListener) {
		this.mListener = searchListener;
	}

	public void setDomains(List<Domain> domains) {
		searchBox.setDomains(domains);
	}

	public void setFakeToolbarColor(int color) {
		fakeToolbar.setBackgroundColor(color);
	}

	public void showKeyboard() {
		searchBox.showKeyboard();
	}

	public void hideKeyboard() {
		searchBox.hideKeyboard();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// Adjust width as necessary
		int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
		if (mBoundedWidth > 0 && mBoundedWidth < measuredWidth) {
			int measureMode = MeasureSpec.getMode(widthMeasureSpec);
			widthMeasureSpec = MeasureSpec.makeMeasureSpec(mBoundedWidth, measureMode);
		}
		// Adjust height as necessary
		int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
		if (mBoundedHeight > 0 && mBoundedHeight < measuredHeight) {
			int measureMode = MeasureSpec.getMode(heightMeasureSpec);
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(mBoundedHeight, measureMode);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public void setLanguage(String language) {
		searchBox.setLanguage(language);
	}

	public void setDomain(Domain domain) {
		searchBox.setDomain(domain);
		mAdapter.setDomain(domain);
	}
}
