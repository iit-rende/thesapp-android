package it.cnr.iit.thesapp.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import it.cnr.iit.thesapp.R;
import it.cnr.iit.thesapp.fragments.TermFragment;
import it.cnr.iit.thesapp.model.Term;

public class TermExplorerAdapter extends FragmentPagerAdapter implements ViewPager
																				 .OnPageChangeListener,
																		 TermFragment
																				 .PageListener {


	private final List<Term> terms;
	private final float      pageWidth;
	private final ViewPager  pager;
	SparseArray<TermFragment> registeredFragments = new SparseArray<TermFragment>();
	private int count = -1;

	public TermExplorerAdapter(Context context, FragmentManager fm, List<Term> terms,
							   ViewPager pager) {
		super(fm);
		if (terms != null) this.terms = terms;
		else this.terms = new ArrayList<>();
		TypedValue outValue = new TypedValue();
		context.getResources().getValue(R.dimen.pager_page_width, outValue, true);
		//pageWidth = outValue.getFloat();
		pageWidth = 1f;
		this.pager = pager;
		pager.setOnPageChangeListener(this);
	}

	@Override
	public float getPageWidth(int position) {
		return pageWidth;
	}

	public void onTermFetched(Term term) {
		terms.get(getPositionForTerm(term)).copy(term);
	}

	public int addTerm(String termDescriptor, String termDomain, String termLanguage) {
		Term term = new Term(termDescriptor, termDomain, termLanguage);
		return addTerm(term);
	}

	public int addTerm(Term term) {
		if (term != null) {
			Log.d("Thesaurus", "Adding " + term.getDescriptor() + " to explorer");
			if (!isTermInList(term)) {
				terms.add(term);
				count = -1;
				notifyDataSetChanged();
			}
			return getPositionForTerm(term);
		}
		return -1;
	}

	private int getPositionForTerm(Term targetWord) {
		int i = 0;
		for (Term word : terms) {
			if (word.equals(targetWord)) return i;
			i++;
		}
		return -1;
	}

	private boolean isTermInList(Term word) {
		for (Term word1 : terms) {
			if (word.equals(word1)) return true;
		}
		return false;
	}

	public void removeWords(long lastWordToShow) {
		//TODO
	}

	public Term getTerm(String termDescriptor, String termDomain, String termLanguage) {
		for (Term word : terms) {
			if (word.getDescriptor().equals(termDescriptor) && word.getDomain().equals(
					termDomain) && word.getLanguage().equals(termLanguage)) return word;
		}
		return null;
	}

	public Term getTerm(int position) {
		return terms.get(position);
	}

	@Override
	public Fragment getItem(int position) {
		final TermFragment termFragment = TermFragment.newInstance(terms.get(position));
		termFragment.setPageListener(this, position);
		return termFragment;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		TermFragment fragment = (TermFragment) super.instantiateItem(container, position);
		registeredFragments.put(position, fragment);
		return fragment;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		registeredFragments.remove(position);
		super.destroyItem(container, position, object);
	}

	public TermFragment getRegisteredFragment(int position) {
		return registeredFragments.get(position);
	}

	@Override
	public int getCount() {
		if (count == -1) {
			if (terms != null) {
				count = terms.size();
			} else {
				return 0;
			}
		}
		return count;
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

	@Override
	public void onPageSelected(int position) {
		Log.d("ViewPager", "Page selected: " + position);

		int key;
		for (int i = 0; i < registeredFragments.size(); i++) {
			key = registeredFragments.keyAt(i);
			TermFragment termFragment = registeredFragments.get(key);
			if (termFragment != null) {
				if (i != position) termFragment.elevate(false);
				else {
					termFragment.elevate(true);
					termFragment.scrollToTop();
				}
			}
		}
	}

	@Override
	public void onPageScrollStateChanged(int i) {}

	@Override
	public void onPageClicked(int position) {
		pager.setCurrentItem(position);
	}
}
