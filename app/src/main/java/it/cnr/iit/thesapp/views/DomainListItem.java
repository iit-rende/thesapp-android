package it.cnr.iit.thesapp.views;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.devspark.robototextview.widget.RobotoTextView;

import it.cnr.iit.thesapp.R;
import it.cnr.iit.thesapp.model.Domain;
import it.cnr.iit.thesapp.model.DomainLocalization;
import it.cnr.iit.thesapp.utils.PrefUtils;

public class DomainListItem extends FrameLayout {

	private Domain              domain;
	private RobotoTextView      domainName;
	private DomainClickListener mListener;
	private RobotoTextView domainDescription;
	private RobotoTextView domainTermCount;

	public DomainListItem(Context context) {
		super(context);
		init();
	}

	public DomainListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DomainListItem(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public void init() {
		inflate(getContext(), R.layout.panel_domain_list_item, this);
		domainName = (RobotoTextView) findViewById(R.id.domain_name);
		domainDescription = (RobotoTextView) findViewById(R.id.domain_description);
		domainTermCount = (RobotoTextView) findViewById(R.id.domain_term_count);
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mListener != null) mListener.onDomainClicked(getDomain());
			}
		});
	}

	public void setDomainClickListener(DomainClickListener listener) {
		mListener = listener;
	}

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;

		DomainLocalization loc = null;
		String language = PrefUtils.loadLanguage(getContext());
		for (DomainLocalization domainLocalization : domain.getLocalizations()) {
			if (language.equalsIgnoreCase(domainLocalization.getLanguage()))
				loc = domainLocalization;
		}
		if (loc == null && domain.getLocalizations() != null &&
		    domain.getLocalizations().size() > 0) loc = domain.getLocalizations().get(0);
		if (loc != null) {
			domainName.setText(loc.getDescriptor());
			domainDescription.setText(loc.getDescription());
			domainTermCount.setText("" + loc.getTermCount());
		} else {
			if (TextUtils.isEmpty(domain.getLocalization())) domainName.setText(
					domain.getDescriptor());
			else domainName.setText((domain.getLocalization()));
		}
	}

	public interface DomainClickListener {

		void onDomainClicked(Domain domain);
	}
}
