package it.cnr.iit.thesapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ProgressBar;

import it.cnr.iit.thesapp.R;
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
	public TermFragmentCallbacks mListener;
	public PageListener          pageListener;
	public int                   page;
	public String                termDescriptor;
	public String                termDomain;
	public String                termLanguage;
	public ProgressBar           progressBar;
	public View                  cardContent;
	public ErrorView             errorView;

	public TimelineElementFragment() {
		// Required empty public constructor
	}

	public static TimelineElementFragment newInstance(TimelineElement element) {
		Bundle args = new Bundle();
		args.putString(ARG_WORD_DESCRIPTOR, element.getDescriptor());
		args.putString(ARG_WORD_DOMAIN, element.getDomainDescriptor());
		args.putString(ARG_WORD_LANGUAGE, element.getLanguage());
		args.putInt(ARG_WORD_ELEMENT_KIND, element.getElementKind());

		if (element.getElementKind() == TimelineElement.KIND_TERM) {
			Logs.ui("Creating TermFragment");
			TermFragment termFragment = new TermFragment();
			termFragment.setArguments(args);
			return termFragment;
		}
		if (element.getElementKind() == TimelineElement.KIND_CATEGORY) {
			Logs.ui("Creating CategoryFragment");
			CategoryFragment categoryFragment = new CategoryFragment();
			categoryFragment.setArguments(args);
			return categoryFragment;
		}
		return null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			termDescriptor = getArguments().getString(ARG_WORD_DESCRIPTOR);
			termDomain = getArguments().getString(ARG_WORD_DOMAIN);
			termLanguage = getArguments().getString(ARG_WORD_LANGUAGE);
		}
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (TermFragmentCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					activity.toString() + " must implement WordFragmentCallbacks");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	@Override
	public void onResume() {
		super.onResume();
		loadElement();
	}

	public abstract void loadElement();

	public abstract void fetchElement();

	public void showError(Response response) {
		progressBar.setVisibility(View.GONE);
		cardContent.setVisibility(View.GONE);
		errorView.setVisibility(View.VISIBLE);
		String errorString;
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
		errorView.setError(String.valueOf(response.getStatus()), errorString);
	}

	public void showError(RetrofitError error) {
		progressBar.setVisibility(View.GONE);
		cardContent.setVisibility(View.GONE);
		errorView.setVisibility(View.VISIBLE);
		String errorString;
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

		errorView.setError("", errorString);
	}

	public void setUiLoading(boolean loading) {
		progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
		cardContent.setVisibility(loading ? View.GONE : View.VISIBLE);
		errorView.setVisibility(View.GONE);
	}

	public void persistTerm(TimelineElement term) {
		term.setCompletelyFetched(true);
		if (mListener != null) mListener.onTermFetched(term);
	}

	public abstract void reloadUi(TimelineElement element);


	public abstract void scrollToTop();


	public void setPageListener(PageListener pageListener, int page) {
		this.page = page;
		this.pageListener = pageListener;
	}


	public interface PageListener {
		void onPageClicked(int position);
	}

	public interface TermFragmentCallbacks {
		TimelineElement getTerm(String elementDescriptor, String elementDomain,
								String elementLanguage);

		void onTermClicked(String elementDescriptor, String elementDomain, String elementLanguage,
						   int elementKind, int clickedFromPage);

		void onTermFetched(TimelineElement term);

		void onUpPressed();
	}
}
