package it.cnr.iit.thesapp.fragments;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.devspark.robototextview.widget.RobotoTextView;

import org.apmem.tools.layouts.FlowLayout;

import java.util.List;

import it.cnr.iit.thesapp.App;
import it.cnr.iit.thesapp.R;
import it.cnr.iit.thesapp.model.Term;
import it.cnr.iit.thesapp.utils.Logs;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TermFragment extends Fragment {
	private static final String ARG_WORD_DESCRIPTOR = "word_DESCRIPTOR";
	private static final String ARG_WORD_DOMAIN     = "word_DOMAIN";
	private static final String ARG_WORD_LANGUAGE   = "word_LANGUAGE";
	Property<CardView, Float> cardElevationProperty = new Property<CardView, Float>(Float.class,
			"cardElevation") {
		@Override
		public Float get(CardView object) {
			return object.getCardElevation();
		}

		@Override
		public void set(CardView object, Float value) {
			object.setCardElevation(value);
		}
	};
	private WordFragmentCallbacks mListener;
	private RobotoTextView termTitle;
	private RobotoTextView termDescription;
	private FlowLayout            relatedContainer;
	private FlowLayout            synonymsContainer;
	private FlowLayout     broaderContainer;
	private FlowLayout     narrowerContainer;
	private ScrollView            scrollView;
	private CardView              cardView;
	private PageListener          pageListener;
	private int                   page;
	private String         termDescriptor;
	private String         termDomain;
	private String         termLanguage;
	private ProgressBar    progressBar;
	private View           termContent;
	private RobotoTextView termSubtitle;

	public TermFragment() {
		// Required empty public constructor
	}

	public static TermFragment newInstance(Term term) {
		TermFragment fragment = new TermFragment();
		Bundle args = new Bundle();
		args.putString(ARG_WORD_DESCRIPTOR, term.getDescriptor());
		args.putString(ARG_WORD_DOMAIN, term.getDomain());
		args.putString(ARG_WORD_LANGUAGE, term.getLanguage());
		fragment.setArguments(args);
		return fragment;
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_term, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		Toolbar toolbar = (Toolbar) view.findViewById(R.id.card_toolbar);
		toolbar.setNavigationIcon(getActivity().getResources().getDrawable(
				R.drawable.ic_navigation_arrow_back));
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mListener != null) mListener.onUpPressed();
			}
		});
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		termContent = view.findViewById(R.id.term_content);

		termTitle = (RobotoTextView) view.findViewById(R.id.term_title);
		termSubtitle = (RobotoTextView) view.findViewById(R.id.term_subtitle);
		termDescription = (RobotoTextView) view.findViewById(R.id.term_description);

		relatedContainer = (FlowLayout) view.findViewById(R.id.term_related);
		synonymsContainer = (FlowLayout) view.findViewById(R.id.word_synonyms);
		broaderContainer = (FlowLayout) view.findViewById(R.id.term_broader);
		narrowerContainer = (FlowLayout) view.findViewById(R.id.term_narrower);

		scrollView = (ScrollView) view.findViewById(R.id.scrollView);
		scrollView.setVerticalScrollBarEnabled(false);
		scrollView.setHorizontalScrollBarEnabled(false);

		cardView = (CardView) view.findViewById(R.id.cardView);
		cardView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("CardView", "Card Clicked, position " + page);
				if (pageListener != null) pageListener.onPageClicked(page);
			}
		});
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (WordFragmentCallbacks) activity;
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
		loadTerm();
	}

	private void loadTerm() {
		if (mListener != null) {
			final Term term = mListener.getTerm(termDescriptor, termDomain, termLanguage);
			if (term != null && term.isCompletelyFetched()) {
				reloadUi(term);
			} else {
				fetchTerm();
			}
		}
	}

	private void fetchTerm() {
		setUiLoading(true);
		Logs.retrofit(
				"Fetching term: " + termDescriptor + " in " + termDomain + " (" + termLanguage +
				")");
		App.getApi().getService().term(termDescriptor, termDomain, termLanguage,
				new Callback<Term>() {
					@Override
					public void success(Term term, Response response) {
						if (response.getStatus() == 200 && term != null) {
							Logs.retrofit("Term fetched: " + term.getDescriptor());
							reloadUi(term);
							persistTerm(term);
						} else {
							Logs.retrofit("Error fetching term: " + response.getStatus() + " - " +
										  response.getReason());
						}
						setUiLoading(false);
					}

					@Override
					public void failure(RetrofitError error) {
						error.printStackTrace();
						setUiLoading(false);
					}
				});
	}

	private void setUiLoading(boolean loading) {
		progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
		termContent.setVisibility(loading ? View.GONE : View.VISIBLE);
	}

	private void persistTerm(Term term) {
		term.setCompletelyFetched(true);
		if (mListener != null) mListener.onTermFetched(term);
	}

	private void reloadUi(Term term) {
		Logs.retrofit("Loading UI for " + term);
		termTitle.setText(term.getDescriptor());
		if (term.getUsedFor() != null) {
			termSubtitle.setVisibility(View.VISIBLE);
			termSubtitle.setText(term.getUsedFor().getDescriptor());
		} else {
			termSubtitle.setVisibility(View.GONE);
		}
		termDescription.setText(term.getScopeNote());

		addWordsToContainer(relatedContainer, term.getRelatedTerms());
		addWordsToContainer(synonymsContainer, term.getCategories());
		addWordsToContainer(broaderContainer, term.getBroaderTerms());
		addWordsToContainer(narrowerContainer, term.getNarrowerTerms());
	}

	private void addWordsToContainer(FlowLayout container, List<Term> terms) {
		int padding = getResources().getDimensionPixelSize(R.dimen.padding_small);
		int backgroundColor = getResources().getColor(R.color.primary);
		int textColor = getResources().getColor(R.color.white);
		if (terms != null) for (final Term term : terms) {
			RobotoTextView tv = new RobotoTextView(getActivity());
			tv.setText(term.getDescriptor());
			tv.setTextColor(textColor);
			tv.setPadding(padding, padding, padding, padding);
			tv.setBackgroundColor(backgroundColor);

			final FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(
					FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
			params.setMargins(padding / 2, padding / 2, padding / 2, padding / 2);
			tv.setLayoutParams(params);

			tv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mListener != null) mListener.onTermClicked(term.getDescriptor(),
							term.getDomain(), term.getLanguage());
				}
			});
			container.addView(tv);
		}
	}

	public void scrollToTop() {
		scrollView.fullScroll(View.FOCUS_UP);
		//if (scrollView.getScrollY() > 0) scrollToTop();
	}

	public void elevate(boolean up) {
		float cardElevation = up ? getResources().getDimensionPixelSize(R.dimen.elevation_high) :
							  0f;
		ObjectAnimator anim = ObjectAnimator.ofFloat(cardView, cardElevationProperty,
				cardElevation);
		anim.start();
	}

	public void setPageListener(PageListener pageListener, int page) {
		this.page = page;
		this.pageListener = pageListener;
	}

	public void setPage(int page) {

	}

	public interface PageListener {
		void onPageClicked(int position);
	}

	public interface WordFragmentCallbacks {
		Term getTerm(String termDescriptor, String termDomain, String termLanguage);

		void onTermClicked(String termDescriptor, String termDomain, String termLanguage);

		void onTermFetched(Term term);

		void onUpPressed();
	}
}
