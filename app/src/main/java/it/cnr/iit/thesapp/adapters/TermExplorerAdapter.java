package it.cnr.iit.thesapp.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.ViewGroup;

import java.util.List;

import it.cnr.iit.thesapp.App;
import it.cnr.iit.thesapp.R;
import it.cnr.iit.thesapp.fragments.TermFragment;
import it.cnr.iit.thesapp.model.Term;
import it.cnr.iit.thesapp.utils.Logs;

public class TermExplorerAdapter extends FragmentPagerAdapter implements ViewPager
																				 .OnPageChangeListener,
																		 TermFragment
																				 .PageListener {


	private final float     pageWidth;
	private final ViewPager pager;
	SparseArray<TermFragment> registeredFragments = new SparseArray<TermFragment>();
	private int count = -1;
	private int baseId;

	public TermExplorerAdapter(Context context, FragmentManager fm, List<Term> terms,
							   ViewPager pager) {
		super(fm);
		if (terms != null) App.terms = terms;

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
		term.setCompletelyFetched(true);
		final int positionForTerm = getPositionForTerm(term);
		if (positionForTerm >= 0) App.terms.get(positionForTerm).copy(term);
	}

	public int addTerm(String termDescriptor, String termDomain, String termLanguage,
					   int clickedFromPage) {
		Term term = new Term(termDescriptor, termDomain, termLanguage);
		return addTerm(term, clickedFromPage);
	}

	public int addTerm(Term term, int clickedFromPage) {
		if (term != null) {
			Log.d("Thesaurus", "Adding " + term.getDescriptor() + " to explorer");
			if (!isTermInList(term)) {
				removeTerms(clickedFromPage);
				App.terms.add(term);
				count = -1;
				notifyChangeInPosition(1);
				notifyDataSetChanged();
			}
			return getPositionForTerm(term);
		}
		return -1;
	}

	private int getPositionForTerm(Term targetTerm) {
		int i = 0;
		for (Term term : App.terms) {
			if (term.equals(targetTerm)) return i;
			i++;
		}
		return -1;
	}

	private boolean isTermInList(Term term) {
		for (Term word1 : App.terms) {
			if (term.equals(word1)) return true;
		}
		return false;
	}

	public void removeTerms(int lastPositionToKeep) {
		if (lastPositionToKeep >= 0) {
			for (int i = getCount() - 1; i > lastPositionToKeep; i--) {
				App.terms.remove(i);
				registeredFragments.remove(i);
			}
		}
	}

	public Term getTerm(String termDescriptor, String termDomain, String termLanguage) {
		for (Term term : App.terms) {
			Logs.cache(term.toString());
			if (term.getDescriptor().equals(termDescriptor) && term.getDomainDescriptor().equals(
					termDomain) && term.getLanguage().equals(termLanguage)) return term;
		}
		return null;
	}

	public Term getTerm(int position) {
		return App.terms.get(position);
	}

	public List<Term> getTerms() {
		return App.terms;
	}

	public void setTerms(List<Term> terms) {
		App.terms = terms;
		notifyChangeInPosition(terms.size());
		count = -1;
		notifyDataSetChanged();
	}

	@Override
	public Fragment getItem(int position) {
		final TermFragment termFragment = TermFragment.newInstance(App.terms.get(position));
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

	@Override
	public int getItemPosition(Object object) {
		return PagerAdapter.POSITION_NONE;
	}


	@Override
	public long getItemId(int position) {
		// give an ID different from position when position has been changed
		return baseId + position;
	}

	/**
	 * Notify that the position of a fragment has been changed.
	 * Create a new ID for each position to force recreation of the fragment
	 *
	 * @param n number of items which have been changed
	 */
	public void notifyChangeInPosition(int n) {
		// shift the ID returned by getItemId outside the range of all previous fragments
		baseId += getCount() + n;
	}

	public TermFragment getRegisteredFragment(int position) {
		return registeredFragments.get(position);
	}

	@Override
	public int getCount() {
		if (count == -1) {
			if (App.terms != null) {
				count = App.terms.size();
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
