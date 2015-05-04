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
import it.cnr.iit.thesapp.model.Term;
import it.cnr.iit.thesapp.model.TermSearch;
import it.cnr.iit.thesapp.model.Word;

public class TermSearchAdapter extends ArrayAdapter<Term> implements Filterable {

	private final LayoutInflater inflater;
	private       List<Term>     words;
	private int count = -1;

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
		Filter myFilter = new Filter() {
			@Override
			protected FilterResults performFiltering(final CharSequence constraint) {
				TermSearch search = App.getApi().getService().query(constraint.toString(),
						"Turismo", "it");

				final FilterResults filterResults = new FilterResults();
				filterResults.values = search.getSuggestions();
				filterResults.count = search.getSuggestions().size();

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
				return resultValue == null ? "" : ((Word) resultValue).getWord();
			}
		};
		return myFilter;
	}

	// somewhere else in your class definition
	static class ViewHolder {
		RobotoTextView nameText;
	}
}
