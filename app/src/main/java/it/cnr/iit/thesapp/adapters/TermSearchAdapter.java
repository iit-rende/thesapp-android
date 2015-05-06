package it.cnr.iit.thesapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.devspark.robototextview.widget.RobotoTextView;

import java.util.List;

import it.cnr.iit.thesapp.App;
import it.cnr.iit.thesapp.R;
import it.cnr.iit.thesapp.model.Domain;
import it.cnr.iit.thesapp.model.Term;
import it.cnr.iit.thesapp.model.TermSearch;
import it.cnr.iit.thesapp.utils.Logs;

public class TermSearchAdapter extends ArrayAdapter<Term> implements Filterable {

	private final LayoutInflater inflater;
	private       List<Term>     words;
	private int count = -1;
	private Domain domain;
	private String language = "it";

	public TermSearchAdapter(Context context, List<Term> objects) {
		super(context, android.R.layout.simple_dropdown_item_1line, objects);
		this.words = objects;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setWords(List<Term> words) {
		this.words = words;
		count = -1;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder; // to reference the child views for later actions

		if (v == null) {

			v = inflater.inflate(R.layout.item_word_search, null);
			// cache view fields into the holder
			holder = new ViewHolder();
			holder.nameText = (RobotoTextView) v.findViewById(R.id.word);
			// associate the holder with the view for later lookup
			v.setTag(holder);
		} else {
			// view already exists, get the holder instance from the view
			holder = (ViewHolder) v.getTag();
		}
		// no local variables with findViewById here

		// use holder.nameText where you were
		// using the local variable nameText before
		holder.nameText.setText(words.get(position).getDescriptor());
		return v;
	}

	@Override
	public int getCount() {
		if (count == -1) {
			if (words != null) {
				count = words.size();
			} else return 0;
		}
		return count;
	}

	@Override
	public Term getItem(int position) {
		if (words != null && words.size() > 0) return words.get(position);
		else return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
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
								domain.getDescriptor(), language);

						if (search != null) {
							Logs.retrofit("Suggestion received for " + search.getQuery() + ": " +
										  search.getSuggestions().size());
							for (Term term : search.getSuggestions()) {
								term.setDomainDescriptor(search.getDomain());
							}
							filterResults.values = search.getSuggestions();
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
				setWords((List<Term>) results.values);
			}

			@Override
			public CharSequence convertResultToString(final Object resultValue) {
				return resultValue == null ? "" : ((Term) resultValue).getDescriptor();
			}
		};
	}

	public void setDomain(Domain domain) {
		Logs.ui("Setting domain for search: " + domain.getDescriptor());
		this.domain = domain;
	}

	public void setLanguage(String language) {
		Logs.ui("Setting language for search: " + language);
		this.language = language;
	}

	// somewhere else in your class definition
	static class ViewHolder {
		RobotoTextView nameText;
	}
}
