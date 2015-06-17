package it.cnr.iit.thesapp.fragments;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.devspark.robototextview.widget.RobotoTextView;

import java.util.List;

import it.cnr.iit.thesapp.App;
import it.cnr.iit.thesapp.R;
import it.cnr.iit.thesapp.model.CategoryList;
import it.cnr.iit.thesapp.model.Domain;
import it.cnr.iit.thesapp.model.DomainSearch;
import it.cnr.iit.thesapp.model.TimelineElement;
import it.cnr.iit.thesapp.utils.Logs;
import it.cnr.iit.thesapp.utils.PrefUtils;
import it.cnr.iit.thesapp.views.DomainListItem;
import it.cnr.iit.thesapp.views.ErrorView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DomainListFragment extends TimelineElementFragment implements DomainListItem.DomainClickListener {

	private RobotoTextView termTitle;
	private RobotoTextView termDescription;
	private ScrollView     scrollView;
	private RobotoTextView termSubtitle;
	private View           titleContainer;
	private LinearLayout   domainContainer;

	public DomainListFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_domain_list, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		errorView = (ErrorView) view.findViewById(R.id.error_view);
		errorView.setCallback(new ErrorView.ErrorViewListener() {
			@Override
			public void onViewClicked() {
				fetchElement();
			}
		});

		toolbar = (Toolbar) view.findViewById(R.id.card_toolbar);
		toolbar.setNavigationIcon(getActivity().getResources().getDrawable(
				R.drawable.ic_navigation_arrow_back));
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mListener != null) mListener.onUpPressed();
			}
		});

		if (page == 0 && toolbar != null) {
			toolbar.setNavigationIcon(null);
			toolbar.setNavigationOnClickListener(null);
		}
		titleContainer = view.findViewById(R.id.title_container);
		cardContent = view.findViewById(R.id.card_content);
		domainContainer = (LinearLayout) view.findViewById(R.id.domain_list_container);

		termTitle = (RobotoTextView) view.findViewById(R.id.term_title);
		termSubtitle = (RobotoTextView) view.findViewById(R.id.term_subtitle);

		scrollView = (ScrollView) view.findViewById(R.id.scrollView);
		scrollView.setVerticalScrollBarEnabled(false);
		scrollView.setHorizontalScrollBarEnabled(false);

		CardView cardView = (CardView) view.findViewById(R.id.cardView);
		cardView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("CardView", "Card Clicked, position " + page);
				if (pageListener != null) pageListener.onPageClicked(page);
			}
		});
	}


	public void fetchElement() {
		setUiLoading(true);
		tryGetDomainsFromCache();
		Logs.retrofit(
				"Fetching domains: " + termDescriptor + " in " + termDomain + " (" + termLanguage +
				")");
		App.getApi().getService().domains(termLanguage, new Callback<DomainSearch>() {
			@Override
			public void success(DomainSearch domainSearch, Response response) {
				if (response.getStatus() == 200 && domainSearch != null) {
					Logs.retrofit("Domains fetched: " + domainSearch.getDomains().size());
					prepareDomainListElement(domainSearch);
					setUiLoading(false);
				} else {
					Logs.retrofit("Error fetching domainSearch: " + response.getStatus() + " - " +
					              response.getReason());
					showError(response);
				}
			}

			@Override
			public void failure(RetrofitError error) {
				error.printStackTrace();
				showError(error);
			}
		});
	}

	public void reloadUi(TimelineElement element) {
		setUiLoading(false);
		if (element instanceof DomainSearch) {
			DomainSearch domainSearch = (DomainSearch) element;
			domainSearch.fillMissingInfo();

			Logs.retrofit("Loading UI for " + domainSearch);
			termTitle.setText(getString(R.string.domain_list_title));
			termSubtitle.setText(getString(R.string.domain_list_subtitle));

			domainContainer.removeAllViews();
			addTermsContainer(domainSearch.getDomains());
		}
	}

	public void scrollToTop() {
		scrollView.fullScroll(View.FOCUS_UP);
	}

	private void tryGetDomainsFromCache() {
		final List<Domain> domains = PrefUtils.loadDomains(getActivity());
		if (domains != null) {
			DomainSearch domainSearch = new DomainSearch(Domain.getDefault(getActivity()),
					termLanguage);
			domainSearch.setDomains(domains);
			prepareDomainListElement(domainSearch);
		}
	}

	private void prepareDomainListElement(DomainSearch domainSearch) {
		domainSearch.setLanguage(PrefUtils.loadLanguage(getActivity()));
		domainSearch.fillMissingInfo();
		PrefUtils.saveDomains(getActivity(), domainSearch.getDomains());
		reloadUi(domainSearch);
		persistElement(domainSearch);
	}

	private void addTermsContainer(List<Domain> domains) {
		if (domains != null && domains.size() > 0) {
			for (Domain domain : domains) {
				DomainListItem container = new DomainListItem(getActivity());

				container.setDomain(domain);
				container.setDomainClickListener(this);
				domainContainer.addView(container);
			}
		}
	}

	@Override
	public void onDomainClicked(Domain domain) {
		CategoryList categoryList = new CategoryList(domain);
		if (mListener != null) mListener.onElementClicked(categoryList.getDescriptor(),
				categoryList.getDomainDescriptor(), domain.getLanguage(),
				categoryList.getElementKind(), page);
	}
}
