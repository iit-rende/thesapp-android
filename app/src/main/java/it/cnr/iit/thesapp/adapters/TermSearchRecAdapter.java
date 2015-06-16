package it.cnr.iit.thesapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.devspark.robototextview.widget.RobotoTextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import it.cnr.iit.thesapp.App;
import it.cnr.iit.thesapp.R;
import it.cnr.iit.thesapp.model.Domain;
import it.cnr.iit.thesapp.model.FacetCategory;
import it.cnr.iit.thesapp.model.FacetContainer;
import it.cnr.iit.thesapp.model.Term;
import it.cnr.iit.thesapp.model.TermSearch;
import it.cnr.iit.thesapp.utils.ColorUtils;
import it.cnr.iit.thesapp.utils.Logs;
import it.cnr.iit.thesapp.views.SearchFacetsContainer;

public class TermSearchRecAdapter extends RecyclerView.Adapter<TermSearchRecAdapter
		.SearchItemHolder> implements
		Filterable {

	private static final int FACET_HOLDER      = 1;
	private static final int TERM_HOLDER       = 2;
	private static final int FILTER_ITEM_DELTA = 1;
	private final Context context;
	int count = -1;
	private int               highlightColorDark;
	private String            highlightColor;
	private TermClickListener clickListener;
	private List<Term>                         items;
	private Domain                             domain;
	private String                             language;
	private FilterCallbacks                    mCallbacks;
	private FacetCategory                      category;
	private String                             searchedTerm;
	private FacetContainer                     facets;
	private SimpleSectionedRecyclerViewAdapter sectionAdapter;
	private int sectionHeaderPosition = -1;

	public TermSearchRecAdapter(List<Term> modelData, TermClickListener clickListener,
	                            Context context) {
		this.items = modelData;
		this.clickListener = clickListener;
		this.domain = new Domain();
		this.category = new FacetCategory();
		this.highlightColor = String.format("#%06X", (0xFFFFFF & context.getResources().getColor(
				R.color.accent)));
		this.highlightColorDark = context.getResources().getColor(R.color.primaryDark);
		this.context = context;
	}

	public void setTerms(List<Term> items) {
		Collections.sort(items, new Comparator<Term>() {
			@Override
			public int compare(Term t1, Term t2) {
				return (t1.isSemantic() == t2.isSemantic() ? 0 : (t1.isSemantic() ? 1 : -1));
			}
		});
		this.items = items;
		count = -1;
		setSections();
		notifyDataSetChanged();
	}

	private void setSections() {
		int i = FILTER_ITEM_DELTA;
		boolean found = false;
		for (Term item : items) {
			if (item.isSemantic()) {
				found = true;
				break;
			} else i++;
		}

		SimpleSectionedRecyclerViewAdapter.Section[] sections;
		if (found) {
			sections =
					new SimpleSectionedRecyclerViewAdapter.Section[]{new
							SimpleSectionedRecyclerViewAdapter.Section(
							i, context.getString(R.string.semating_search_header))};
			sectionHeaderPosition = i;
		} else {
			sections = new SimpleSectionedRecyclerViewAdapter.Section[0];
			sectionHeaderPosition = -1;
		}
		sectionAdapter.setSections(sections);
	}

	public void setSectionAdapter(SimpleSectionedRecyclerViewAdapter adapter) {
		this.sectionAdapter = adapter;
	}

	@Override
	public SearchItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		switch (viewType) {
			case TERM_HOLDER:
				View termHolder = LayoutInflater.
						                                from(viewGroup.getContext()).
						                                inflate(R.layout.item_term_search,
								                                viewGroup, false);
				return new TermResultHolder(termHolder);
			case FACET_HOLDER:
				SearchFacetsContainer facetHolder = new SearchFacetsContainer(
						viewGroup.getContext());
				ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				facetHolder.setLayoutParams(params);
				return new FacetsHolder(facetHolder);
		}
		return null;
	}

	@Override
	public void onBindViewHolder(final SearchItemHolder viewHolder, int position) {
		Logs.ui("Binding view for position: " + position);
		if (position == 0) {
			FacetsHolder holder = (FacetsHolder) viewHolder;
			holder.setFacets(facets, category);
		} else {
			TermResultHolder holder = (TermResultHolder) viewHolder;
			Term term = getTerm(position, false);
			holder.onBindViewHolder(term);
		}
	}

	@Override
	public int getItemViewType(int position) {
		switch (position) {
			case 0:
				return FACET_HOLDER;
			default:
				return TERM_HOLDER;
		}
	}

	@Override
	public int getItemCount() {
		if (count >= 0) return count;
		if (items != null) {
			final int size = items.size();
			if (size >= 0) count = size + FILTER_ITEM_DELTA;
			else count = 0;
			return count;
		} else {
			count = 0;
			return count;
		}
	}

	public Term getTerm(int position, boolean withPositionDelta) {
		int sectionDelta;
		if (sectionHeaderPosition >= 0 && withPositionDelta)
			sectionDelta = position > sectionHeaderPosition ? 1 : 0;
		else sectionDelta = 0;
		final int i = position - FILTER_ITEM_DELTA - sectionDelta;
		Logs.ui("Getting item for position " + position + ", returning item at " + i);
		return items.get(i);
	}


	public void setDomain(Domain domain) {
		Logs.ui("Setting domain for search: " + domain.getDescriptor());
		this.domain = domain;
		highlightColor = domain.getColor();
		highlightColorDark = ColorUtils.darkerColor(Color.parseColor(domain.getColor()));
		notifyItemChanged(0);
	}

	public void setLanguage(String language) {
		Logs.ui("Setting language for search: " + language);
		this.language = language;
	}

	public void setCategory(FacetCategory category) {
		this.category = category;
	}

	public void setFilterCallbacks(FilterCallbacks callbacks) {
		this.mCallbacks = callbacks;
	}

	@Override
	public Filter getFilter() {
		return new Filter() {
			@Override
			protected FilterResults performFiltering(final CharSequence constraint) {
				final FilterResults filterResults = new FilterResults();
				if (constraint != null && constraint.length() > 0) {
					try {
						String c = null;
						if (category != null) c = category.getDescriptor();
						Logs.retrofit("Searching for " + constraint.toString() + " in " +
						              domain.getDescriptor() + " (" + language +
						              ") with category " + c);
						TermSearch search = App.getApi().getService().query(constraint.toString(),
								domain.getDescriptor(), c, language);

						if (search != null) {
							Logs.retrofit("Suggestion received for " + search.getQuery() + ": " +
							              search.getSuggestions().size());
							for (Term term : search.getSuggestions()) {
								term.setDomainDescriptor(search.getDomain());
							}
							filterResults.values = search;
							filterResults.count = search.getSuggestions().size();
						} else {
							Logs.retrofit("Suggestion search for " + constraint + " failed");
							filterResults.values = null;
							filterResults.count = -1;
						}
					} catch (Exception e) {
						Logs.retrofit("Suggestion search for " + constraint + " failed");
						e.printStackTrace();
						filterResults.values = null;
						filterResults.count = -1;
					}
				}
				return filterResults;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(final CharSequence contraint,
			                              final FilterResults results) {
				if (results != null) {
					Logs.ui("Result published for " + contraint);
					setTerms(((TermSearch) results.values).getSuggestions());
					setSearchedTerm(((TermSearch) results.values).getQuery());
					setFacets(((TermSearch) results.values).getFacets());
					if (mCallbacks != null) mCallbacks.onFilterComplete(results.count);
				} else {
					if (mCallbacks != null) mCallbacks.onFilterComplete(0);
				}
			}

			@Override
			public CharSequence convertResultToString(final Object resultValue) {
				return "";
			}
		};
	}


	public void setSearchedTerm(String searchedTerm) {
		this.searchedTerm = searchedTerm;
	}

	public void setFacets(FacetContainer suggestedCategories) {
		this.facets = suggestedCategories;
		notifyItemChanged(0);
	}

	public interface TermClickListener {

		void onTermClicked(Term monster);
		void onTermLongClicked(Term monster);
		void onCategoryClicked(FacetCategory category);
		void onFacetContainerOpened();
	}

	public interface FilterCallbacks {

		void onFilterComplete(int count);
	}


	public static class SearchItemHolder extends RecyclerView.ViewHolder {

		private ClickListener clickListener;

		public SearchItemHolder(View itemView) {
			super(itemView);
		}

		public void setClickListener(ClickListener clickListener) {
			this.clickListener = clickListener;
		}


		public interface ClickListener {

			void onClick(View v, int position, boolean isLongClick);
		}
	}

	public class FacetsHolder extends SearchItemHolder {

		public SearchFacetsContainer facetsContainer;

		public FacetsHolder(View itemView) {
			super(itemView);
			facetsContainer = (SearchFacetsContainer) itemView;
			facetsContainer.setColor(highlightColorDark);
			facetsContainer.setSearchFacetListener(new SearchFacetsContainer.SearchFacetListener
					() {
				@Override
				public void onCategoryClicked(FacetCategory category) {
					setCategory(category);
					if (clickListener != null) clickListener.onCategoryClicked(category);
				}

				@Override
				public void onContainerOpened() {
					if (clickListener != null) clickListener.onFacetContainerOpened();
				}
			});
		}

		public void setFacets(FacetContainer facets, FacetCategory selectedCategory) {
			facetsContainer.setFacets(facets, selectedCategory);
		}
	}


	public class TermResultHolder extends SearchItemHolder implements View.OnClickListener,
			View.OnLongClickListener {

		public  RobotoTextView termDescriptor;
		private ClickListener  termClickListener;

		public TermResultHolder(View itemView) {
			super(itemView);
			termDescriptor = (RobotoTextView) itemView.findViewById(R.id.word);
			itemView.setOnClickListener(this);
			itemView.setOnLongClickListener(this);
		}

		public void onBindViewHolder(Term term) {
			if (!term.isSemantic()) {
				String line = highlightSearchedTerm(searchedTerm, term.getDescriptor());
				Logs.ui("Syntactic term: " + line);
				termDescriptor.setText(Html.fromHtml(line));
			} else {
				Logs.ui("Semantic term: " + term.getDescriptor());
				termDescriptor.setText(term.getDescriptor());
			}
			setClickListener(new ClickListener() {
				@Override
				public void onClick(View v, int pos, boolean isLongClick) {
					if (!isLongClick) {
						Logs.ui("Term " + pos + " clicked!");
						if (clickListener != null) clickListener.onTermClicked(getTerm(pos, true));
					} else {
						Logs.ui("Term " + pos + " long clicked!");
						if (clickListener != null) clickListener.onTermClicked(getTerm(pos, true));
					}
				}
			});
		}

		public void setClickListener(ClickListener clickListener) {
			this.termClickListener = clickListener;
		}

		@Override
		public void onClick(View v) {
			termClickListener.onClick(v, getPosition(), false);
		}

		@Override
		public boolean onLongClick(View v) {
			termClickListener.onClick(v, getPosition(), true);
			return true;
		}

		private String highlightSearchedTerm(String termSearched, String textToHighlight) {
			return textToHighlight.replaceAll("(?i)(" + termSearched + ")",
					"<font color=" + highlightColor + ">$1</font>");
		}
	}
}
