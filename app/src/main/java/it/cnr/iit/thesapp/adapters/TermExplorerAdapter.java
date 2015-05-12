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
import it.cnr.iit.thesapp.fragments.TimelineElementFragment;
import it.cnr.iit.thesapp.model.Category;
import it.cnr.iit.thesapp.model.Term;
import it.cnr.iit.thesapp.model.TimelineElement;
import it.cnr.iit.thesapp.utils.Logs;

public class TermExplorerAdapter extends FragmentPagerAdapter implements ViewPager
																				 .OnPageChangeListener,
																		 TermFragment
																				 .PageListener {


	private final float     pageWidth;
	private final ViewPager pager;
	SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
	private int count = -1;
	private int baseId;

	public TermExplorerAdapter(Context context, FragmentManager fm, List<TimelineElement> terms,
							   ViewPager pager) {
		super(fm);
		if (terms != null) App.timelineElements = terms;

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

	public void onTermFetched(TimelineElement term) {
		term.setCompletelyFetched(true);
		final int positionForTerm = getPositionForTerm(term);
		if (positionForTerm >= 0) App.timelineElements.get(positionForTerm).copy(term);
	}

	public int addTerm(String termDescriptor, String termDomain, String termLanguage,
					   int clickedFromPage) {
		Term term = new Term(termDescriptor, termDomain, termLanguage);
		return addTerm(term, clickedFromPage);
	}

	public int addCategory(String termDescriptor, String termDomain, String termLanguage,
						   int clickedFromPage) {
		Category category = new Category(termDescriptor, termDomain, termLanguage);
		return addCategory(category, clickedFromPage);
	}

	public int addTerm(Term term, int clickedFromPage) {
		if (term != null) {
			Logs.thesaurus("Adding term " + term.getDescriptor() + " to explorer");
			if (!isTermInList(term)) {
				removeTerms(clickedFromPage);
				App.timelineElements.add(term);
				count = -1;
				notifyChangeInPosition(App.timelineElements.size());
				notifyDataSetChanged();
			}
			return getPositionForTerm(term);
		}
		return -1;
	}

	public int addCategory(Category category, int clickedFromPage) {
		if (category != null) {
			Logs.thesaurus("Adding category " + category.getDescriptor() + " to explorer");
			if (!isTermInList(category)) {
				removeTerms(clickedFromPage);
				App.timelineElements.add(category);
				count = -1;
				notifyChangeInPosition(App.timelineElements.size());
				notifyDataSetChanged();
			}
			return getPositionForTerm(category);
		}
		return -1;
	}

	private int getPositionForTerm(TimelineElement targetElement) {
		int i = 0;
		for (TimelineElement timelineElement : App.timelineElements) {
			if (timelineElement.equals(targetElement)) return i;
			i++;
		}
		return -1;
	}

	private boolean isTermInList(TimelineElement element) {
		for (TimelineElement timelineElement : App.timelineElements) {
			if (element.equals(timelineElement)) return true;
		}
		return false;
	}

	public void removeTerms(int lastPositionToKeep) {
		if (lastPositionToKeep >= 0) {
			for (int i = getCount() - 1; i > lastPositionToKeep; i--) {
				App.timelineElements.remove(i);
				registeredFragments.remove(i);
			}
		}
	}

	public TimelineElement getTerm(String termDescriptor, String termDomain, String termLanguage) {
		for (TimelineElement term : App.timelineElements) {
			Logs.cache(term.toString());
			if (term.getDescriptor().equals(termDescriptor) && term.getDomainDescriptor().equals(
					termDomain) && term.getLanguage().equals(termLanguage)) return term;
		}
		return null;
	}

	public TimelineElement getTerm(int position) {
		return App.timelineElements.get(position);
	}


	@Override
	public Fragment getItem(int position) {
		TimelineElementFragment timelineElementFragment;
		if (getTerm(position).getElementKind() == TimelineElement.KIND_TERM)
			timelineElementFragment = TimelineElementFragment.newInstance(App.timelineElements.get(
					position));
		else timelineElementFragment = TimelineElementFragment.newInstance(App.timelineElements
				.get(
				position));
		if (timelineElementFragment != null) timelineElementFragment.setPageListener(this,
				position);
		return timelineElementFragment;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Fragment fragment = (Fragment) super.instantiateItem(container, position);
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

	public void notifyChangeInPosition(int n) {
		// shift the ID returned by getItemId outside the range of all previous fragments
		baseId += getCount() + n;
	}

	public Fragment getRegisteredFragment(int position) {
		return registeredFragments.get(position);
	}

	@Override
	public int getCount() {
		if (count == -1) {
			if (App.timelineElements != null) {
				count = App.timelineElements.size();
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

		/*
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
		*/
	}

	@Override
	public void onPageScrollStateChanged(int i) {}

	@Override
	public void onPageClicked(int position) {
		pager.setCurrentItem(position);
	}
}
