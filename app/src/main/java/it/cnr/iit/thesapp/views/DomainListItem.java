package it.cnr.iit.thesapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.devspark.robototextview.widget.RobotoTextView;

import it.cnr.iit.thesapp.R;
import it.cnr.iit.thesapp.model.Domain;

public class DomainListItem extends FrameLayout {
	private Domain              domain;
	private RobotoTextView      domainName;
	private DomainClickListener mListener;

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
		domainName.setText(domain.getDescriptor());
	}

	public interface DomainClickListener {
		void onDomainClicked(Domain domain);
	}
}
