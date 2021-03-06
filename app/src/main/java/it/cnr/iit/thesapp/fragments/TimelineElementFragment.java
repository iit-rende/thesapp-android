package it.cnr.iit.thesapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import it.cnr.iit.thesapp.App;
import it.cnr.iit.thesapp.R;
import it.cnr.iit.thesapp.model.Domain;
import it.cnr.iit.thesapp.model.TimelineElement;
import it.cnr.iit.thesapp.utils.Logs;
import it.cnr.iit.thesapp.views.ErrorView;
import retrofit.RetrofitError;
import retrofit.client.Response;

public abstract class TimelineElementFragment extends Fragment {

	private static final String ARG_WORD_DESCRIPTOR   = "word_DESCRIPTOR";
	private static final String ARG_WORD_DOMAIN       = "word_DOMAIN";
	private static final String ARG_WORD_LANGUAGE     = "word_LANGUAGE";
	private static final String ARG_WORD_ELEMENT_KIND = "word_KIND";
	public  TimelineElementFragmentCallback mListener;
	public  PageListener                    pageListener;
	public  int                             page;
	public  String                          termDescriptor;
	public  String                          termDomain;
	public  String                          termLanguage;
	public  ProgressBar                     progressBar;
	public  View                            cardContent;
	public  ErrorView                       errorView;
	public Toolbar toolbar;
	private int                             termKind;
	private boolean alreadyLoadedUi = false;

	public TimelineElementFragment() {
		// Required empty public constructor
	}

	public static TimelineElementFragment newInstance(TimelineElement element) {
		Bundle args = new Bundle();
		args.putString(ARG_WORD_DESCRIPTOR, element.getDescriptor());
		args.putString(ARG_WORD_DOMAIN, element.getDomainDescriptor());
		args.putString(ARG_WORD_LANGUAGE, element.getLanguage());
		args.putInt(ARG_WORD_ELEMENT_KIND, element.getElementKind());

		switch (element.getElementKind()) {
			case TimelineElement.KIND_TERM:
				Logs.ui("Creating TermFragment");
				TermFragment termFragment = new TermFragment();
				termFragment.setArguments(args);
				return termFragment;
			case TimelineElement.KIND_CATEGORY:
				Logs.ui("Creating CategoryFragment");
				CategoryFragment categoryFragment = new CategoryFragment();
				categoryFragment.setArguments(args);
				return categoryFragment;
			case TimelineElement.KIND_DOMAIN_CONTAINER:
				Logs.ui("Creating DomainListFragment");
				DomainListFragment domainListFragment = new DomainListFragment();
				domainListFragment.setArguments(args);
				return domainListFragment;
			case TimelineElement.KIND_CATEGORY_LIST:
				Logs.ui("Creating DomainListFragment");
				CategoryListFragment categoryListFragment = new CategoryListFragment();
				categoryListFragment.setArguments(args);
				return categoryListFragment;
		}
		return null;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (TimelineElementFragmentCallback) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					activity.toString() + " must implement WordFragmentCallbacks");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			termDescriptor = getArguments().getString(ARG_WORD_DESCRIPTOR);
			termDomain = getArguments().getString(ARG_WORD_DOMAIN);
			termLanguage = getArguments().getString(ARG_WORD_LANGUAGE);
			termKind = getArguments().getInt(ARG_WORD_ELEMENT_KIND);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		loadElement();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	public void loadElement() {
		if (mListener != null) {
			final TimelineElement element = mListener.getElement(termDescriptor, termDomain,
					termLanguage, termKind);
			if (element != null && element.isCompletelyFetched()) {
				Logs.thesaurus("Element is cached: " + element);
				setUiLoading(false);
				reloadUi(element);
			} else {
				Logs.thesaurus("Element is not cached: " + element);
				fetchElement();
			}
		}
	}

	public abstract void fetchElement();

	public void showError(Response response) {
		alreadyLoadedUi = false;
		progressBar.setVisibility(View.GONE);
		cardContent.setVisibility(View.GONE);
		errorView.setVisibility(View.VISIBLE);
		String errorString;
		if (getActivity() != null) {
			switch (response.getStatus()) {
				case 404:
					errorString = getString(R.string.error_not_found);
					break;
				case 500:
					errorString = getString(R.string.error_server_problems);
					break;
				default:
					errorString = getString(R.string.error_generic);
			}
		} else errorString = "Clicca per riprovare";
		errorView.setError(String.valueOf(response.getStatus()), errorString);
	}

	public void showError(RetrofitError error) {
		alreadyLoadedUi = false;
		progressBar.setVisibility(View.GONE);
		cardContent.setVisibility(View.GONE);
		errorView.setVisibility(View.VISIBLE);
		String errorString;
		if (getActivity() != null) {
			switch (error.getKind()) {

				case NETWORK:
					errorString = getString(R.string.error_connection_problem);
					break;
				case HTTP:
					showError(error.getResponse());
					return;
				default:
					errorString = getString(R.string.error_generic);
					break;
			}
		} else errorString = "Clicca per riprovare";
		errorView.setError("", errorString);
	}

	public void setUiLoading(boolean loading) {
		progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
		cardContent.setVisibility(loading ? View.GONE : View.VISIBLE);
		errorView.setVisibility(View.GONE);
	}

	public void persistElement(TimelineElement element) {
		element.setCompletelyFetched(true);
		final int positionForElement = getPositionForElement(element);
		if (positionForElement >= 0) {
			Logs.cache("Element persisted at position: " + positionForElement);
			App.timelineElements.set(positionForElement, element);
		}
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

	public abstract void reloadUi(TimelineElement element);

	public void setWindowToolbar(Domain domain) {
		if (mListener != null) mListener.setToolbarDomain(domain, page);
	}

	public abstract void scrollToTop();


	public void setPageListener(PageListener pageListener, int page) {
		this.page = page;
		this.pageListener = pageListener;
		if (page == 0 && toolbar != null) {
			toolbar.setNavigationIcon(null);
			toolbar.setNavigationOnClickListener(null);
		}
	}


	public interface PageListener {

		void onPageClicked(int position);
	}

	public interface TimelineElementFragmentCallback {

		TimelineElement getElement(String elementDescriptor, String elementDomain,
		                           String elementLanguage, int elementKind);

		void onElementClicked(String elementDescriptor, String elementDomain,
		                      String elementLanguage, int elementKind, int clickedFromPage);


		void onUpPressed();

		void setToolbarDomain(Domain domain, int page);
	}
}
