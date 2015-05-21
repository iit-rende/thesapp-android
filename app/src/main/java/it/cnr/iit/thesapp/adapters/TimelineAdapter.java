package it.cnr.iit.thesapp.adapters;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.ViewGroup;

import it.cnr.iit.thesapp.App;
import it.cnr.iit.thesapp.R;
import it.cnr.iit.thesapp.fragments.TermFragment;
import it.cnr.iit.thesapp.fragments.TimelineElementFragment;
import it.cnr.iit.thesapp.model.Category;
import it.cnr.iit.thesapp.model.CategoryList;
import it.cnr.iit.thesapp.model.Domain;
import it.cnr.iit.thesapp.model.DomainSearch;
import it.cnr.iit.thesapp.model.Term;
import it.cnr.iit.thesapp.model.TimelineElement;
import it.cnr.iit.thesapp.utils.Logs;

public class TimelineAdapter extends FragmentPagerAdapter implements TermFragment.PageListener {


	private final float     pageWidth;
	private final ViewPager pager;
	private final Context context;
	private int count = -1;
	private int baseId;

	public TimelineAdapter(Context context, FragmentManager fm, ViewPager pager) {
		super(fm);
		this.context = context;
		TypedValue outValue = new TypedValue();
		context.getResources().getValue(R.dimen.pager_page_width, outValue, true);
		//pageWidth = outValue.getFloat();
		pageWidth = 1f;
		this.pager = pager;
	}

	public void onElementFetched(TimelineElement element) {
		element.setCompletelyFetched(true);
		final int positionForElement = getPositionForElement(element);
		if (positionForElement >= 0) App.timelineElements.get(positionForElement).copy(element);
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

	public int addCategoryList(String termDomain, String termLanguage, int clickedFromPage) {
		CategoryList category = new CategoryList(termDomain, termLanguage);
		return addCategoryList(category, clickedFromPage);
	}

	public int addDomainList(String language) {
		DomainSearch domainSearch = new DomainSearch(Domain.getDefault(context), language);
		return addDomainList(domainSearch, -1);
	}

	public int addTerm(Term term, int clickedFromPage) {
		if (term != null) {
			Logs.thesaurus("Adding term " + term.getDescriptor() + " to explorer");
			if (!isTermInList(term)) {
				removeTerms(clickedFromPage);
				App.timelineElements.add(term);

				notifyChangeInPosition(getCount());
				count = -1;
				notifyDataSetChanged();
			}
			return getPositionForElement(term);
		}
		return -1;
	}

	public int addCategory(Category category, int clickedFromPage) {
		if (category != null) {
			Logs.thesaurus("Adding category to explorer: " + category);
			if (!isTermInList(category)) {
				removeTerms(clickedFromPage);
				App.timelineElements.add(category);

				notifyChangeInPosition(getCount());
				count = -1;
				notifyDataSetChanged();
			}
			return getPositionForElement(category);
		}
		return -1;
	}

	public int addDomainList(DomainSearch domainSearch, int clickedFromPage) {
		if (domainSearch != null) {
			Logs.thesaurus("Adding domainSearch " + domainSearch.getDescriptor() + " to explorer");
			if (!isTermInList(domainSearch)) {
				removeTerms(clickedFromPage);
				App.timelineElements.add(domainSearch);
				count = -1;
				notifyChangeInPosition(App.timelineElements.size());
				notifyDataSetChanged();
			}
			return getPositionForElement(domainSearch);
		}
		return -1;
	}

	public int addCategoryList(CategoryList categoryList, int clickedFromPage) {
		if (categoryList != null) {
			Logs.thesaurus("Adding categoryList " + categoryList.getDescriptor() + " to explorer");
			if (!isTermInList(categoryList)) {
				removeTerms(clickedFromPage);
				App.timelineElements.add(categoryList);
				count = -1;
				notifyChangeInPosition(App.timelineElements.size());
				notifyDataSetChanged();
			}
			return getPositionForElement(categoryList);
		}
		return -1;
	}

	private int getPositionForElement(TimelineElement targetElement) {
		int i = 0;
		for (TimelineElement timelineElement : App.timelineElements) {
			if (timelineElement.equals(targetElement)) {
				Logs.cache("Position found for : " + targetElement);
				return i;
			}
			i++;
		}
		Logs.cache("Can't find position for : " + targetElement);
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
			}
		}
	}

	public void goHome() {
		switch (getCount()) {
			case 0:
			case 1:
			case 2:
				return;
			default:
				pager.setCurrentItem(1, true);
				//delayedDeleteHistory();
		}
	}

	private void delayedDeleteHistory() {
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				deleteHistory();
			}
		}, 300);
	}

	private void deleteHistory() {
		removeTerms(1);
		notifyChangeInPosition(getCount());
		count = -1;
		notifyDataSetChanged();
	}

	public TimelineElement getTerm(String elementDescriptor, String elementDomain,
	                               String elementLanguage, int elementKind) {
		for (TimelineElement element : App.timelineElements) {
			//Logs.cache(element.toString());
			if (element.getDescriptor().equals(elementDescriptor) &&
			    element.getDomainDescriptor().equals(elementDomain) && element.getLanguage()
			                                                                  .equals(elementLanguage) &&
			    element.getElementKind() == elementKind) return element;
		}
		return null;
	}

	public TimelineElement getTerm(int position) {
		return App.timelineElements.get(position);
	}

	@Override
	public Fragment getItem(int position) {
		TimelineElementFragment timelineElementFragment = TimelineElementFragment.newInstance(
				App.timelineElements.get(position));

		if (timelineElementFragment != null) timelineElementFragment.setPageListener(this,
				position);
		return timelineElementFragment;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		return (Fragment) super.instantiateItem(container, position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
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
	public int getItemPosition(Object object) {
		return PagerAdapter.POSITION_NONE;
	}

	@Override
	public float getPageWidth(int position) {
		return pageWidth;
	}

	@Override
	public void onPageClicked(int position) {
		pager.setCurrentItem(position);
	}
}
