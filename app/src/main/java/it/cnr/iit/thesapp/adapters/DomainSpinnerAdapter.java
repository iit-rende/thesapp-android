package it.cnr.iit.thesapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.devspark.robototextview.widget.RobotoTextView;

import java.util.List;

import it.cnr.iit.thesapp.R;
import it.cnr.iit.thesapp.model.Domain;

public class DomainSpinnerAdapter extends ArrayAdapter<Domain> implements SpinnerAdapter {
	private final LayoutInflater inflater;
	private       List<Domain>   domains;
	private int count = -1;

	public DomainSpinnerAdapter(Context context, List<Domain> domains) {
		super(context, android.R.layout.simple_dropdown_item_1line, domains);
		this.domains = domains;
		this.inflater = LayoutInflater.from(context);
	}

	public void setDomains(List<Domain> domains) {
		if (domains != null) {
			this.domains = domains;
			count = -1;
			notifyDataSetChanged();
		} else {
			this.domains = null;
			count = -1;
			notifyDataSetInvalidated();
		}
	}

	@Override
	public int getCount() {
		if (count == -1) {
			if (domains != null) count = domains.size();
			else return 0;
		}
		return count;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RobotoTextView label = (RobotoTextView) inflater.inflate(R.layout.item_domain, parent,
				false);
		label.setText(domains.get(position).getDescriptor());
		return label;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		RobotoTextView label = (RobotoTextView) inflater.inflate(R.layout.item_domain, parent,
				false);
		label.setText(domains.get(position).getDescriptor());
		return label;
	}

	public int getPositionFromDescriptor(String descriptor) {
		if (domains != null) {
			for (int i = 0; i < domains.size(); i++) {
				if (domains.get(i).getDescriptor().equals(descriptor)) return i;
			}
		}
		return -1;
	}

	@Override
	public Domain getItem(int position) {
		return domains.get(position);
	}
}
