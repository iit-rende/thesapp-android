package it.cnr.iit.thesapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.devspark.robototextview.widget.RobotoTextView;

import java.util.List;

import it.cnr.iit.thesapp.App;
import it.cnr.iit.thesapp.R;
import it.cnr.iit.thesapp.model.Domain;
import it.cnr.iit.thesapp.model.FacetCategory;
import it.cnr.iit.thesapp.model.Term;
import it.cnr.iit.thesapp.model.TermSearch;
import it.cnr.iit.thesapp.utils.Logs;

public class TermSearchRecAdapter extends RecyclerView.Adapter<TermSearchRecAdapter.TermResultHolder> implements
		Filterable {

	private final String highlightColor;
	int count = -1;
	private TermClickListener clickListener;

	private List<Term>          items;
	private Domain              domain;
	private String              language;
	private FilterCallbacks     mCallbacks;
	private FacetCategory       category;
	private String              searchedTerm;
	private List<FacetCategory> suggestedCategories;

	public TermSearchRecAdapter(List<Term> modelData, TermClickListener clickListener,
	                            Context context) {
		this.items = modelData;
		this.clickListener = clickListener;
		this.domain = new Domain();
		this.category = new FacetCategory();
		this.highlightColor = String.format("#%06X", (0xFFFFFF & context.getResources().getColor(
				R.color.accent)));
	}

	public void setTerms(List<Term> items) {
		this.items = items;
		count = -1;
		notifyDataSetChanged();
	}


	@Override
	public TermResultHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		View itemView = LayoutInflater.
				                              from(viewGroup.getContext()).
				inflate(R.layout.item_term_search, viewGroup, false);
		return new TermResultHolder(itemView, viewType);
	}


	@Override
	public void onBindViewHolder(final TermResultHolder viewHolder, int position) {
		Term term = items.get(position);
		if (!term.isSemantic()) {
			String line = highlightSearchedTerm(searchedTerm, term.getDescriptor());
			Logs.ui("Syntactic term: " + line);
			viewHolder.termDescriptor.setText(Html.fromHtml(line));
		} else {
			Logs.ui("Semantic term: " + term.getDescriptor());
			viewHolder.termDescriptor.setText(term.getDescriptor());
		}
		viewHolder.setClickListener(new TermResultHolder.ClickListener() {
			@Override
			public void onClick(View v, int pos, boolean isLongClick) {
				if (!isLongClick) {
					Logs.ui("Term " + pos + " clicked!");
					if (clickListener != null) clickListener.onTermClicked(getTerm(pos));
				} else {
					Logs.ui("Term " + pos + " long clicked!");
					if (clickListener != null) clickListener.onTermClicked(getTerm(pos));
				}
			}
		});
	}

	@Override
	public int getItemCount() {
		if (count >= 0) return count;
		if (items != null) {
			count = items.size() + 1;
			return count;
		} else {
			count = 0;
			return count;
		}
	}

	public Term getTerm(int position) {
		return items.get(position - 1);
	}


	public void setDomain(Domain domain) {
		Logs.ui("Setting domain for search: " + domain.getDescriptor());
		this.domain = domain;
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
						Logs.retrofit("Searching for " + constraint.toString() + " in " +
						              domain.getDescriptor() + " (" + language + ")");
						TermSearch search = App.getApi().getService().query(constraint.toString(),
								domain.getDescriptor(), category.getDescriptor(), language);

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
				Logs.ui("Result published for " + contraint);
				setTerms(((TermSearch) results.values).getSuggestions());
				setSearchedTerm(((TermSearch) results.values).getQuery());
				setSuggestedCategories(((TermSearch) results.values).getFacets().getCategories());
				if (mCallbacks != null) mCallbacks.onFilterComplete(results.count);
			}

			@Override
			public CharSequence convertResultToString(final Object resultValue) {
				return "";
			}
		};
	}

	private String highlightSearchedTerm(String termSearched, String textToHighlight) {
		return textToHighlight.replaceAll("(?i)(" + termSearched + ")",
				"<font color=" + highlightColor + ">$1</font>");
	}

	public void setSearchedTerm(String searchedTerm) {
		this.searchedTerm = searchedTerm;
	}

	public void setSuggestedCategories(List<FacetCategory> suggestedCategories) {
		this.suggestedCategories = suggestedCategories;
	}

	public interface TermClickListener {

		public void onTermClicked(Term monster);

		public void onTermLongClicked(Term monster);
	}

	public interface FilterCallbacks {

		void onFilterComplete(int count);
	}

	public final static class TermResultHolder extends RecyclerView.ViewHolder implements View
			.OnClickListener,
			View.OnLongClickListener {


		public  RobotoTextView termDescriptor;
		private ClickListener  clickListener;

		public TermResultHolder(View itemView) {
			super(itemView);
		}

		public TermResultHolder(View itemView, int viewType) {
			super(itemView);
			termDescriptor = (RobotoTextView) itemView.findViewById(R.id.word);
			itemView.setOnClickListener(this);
			itemView.setOnLongClickListener(this);
		}

		public void setClickListener(ClickListener clickListener) {
			this.clickListener = clickListener;
		}

		@Override
		public void onClick(View v) {
			clickListener.onClick(v, getPosition(), false);
		}

		@Override
		public boolean onLongClick(View v) {
			clickListener.onClick(v, getPosition(), true);
			return true;
		}

		public interface ClickListener {

			void onClick(View v, int position, boolean isLongClick);
		}
	}
}
