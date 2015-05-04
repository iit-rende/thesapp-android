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
	private RobotoTextView        wordTitle;
	private RobotoTextView        wordDescription;
	private FlowLayout            relatedContainer;
	private FlowLayout            synonymsContainer;
	private FlowLayout            moreGenericContainer;
	private FlowLayout            moreSpecificContainer;
	private ScrollView            scrollView;
	private CardView              cardView;
	private PageListener          pageListener;
	private int                   page;
	private String      termDescriptor;
	private String      termDomain;
	private String      termLanguage;
	private ProgressBar progressBar;
	private View        termContent;

	public TermFragment() {
		// Required empty public constructor
	}

	public static TermFragment newInstance(Term term) {
		TermFragment fragment = new TermFragment();
		Bundle args = new Bundle();
		args.putString(ARG_WORD_DESCRIPTOR, term.getDescriptor());
		args.putString(ARG_WORD_DOMAIN, term.getDescriptor());
		args.putString(ARG_WORD_LANGUAGE, term.getDescriptor());
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
		return inflater.inflate(R.layout.fragment_word, container, false);
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
		wordTitle = (RobotoTextView) view.findViewById(R.id.word_word);
		wordDescription = (RobotoTextView) view.findViewById(R.id.word_description);

		relatedContainer = (FlowLayout) view.findViewById(R.id.word_related);
		synonymsContainer = (FlowLayout) view.findViewById(R.id.word_synonyms);
		moreGenericContainer = (FlowLayout) view.findViewById(R.id.word_more_generic);
		moreSpecificContainer = (FlowLayout) view.findViewById(R.id.word_more_specific);

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
		loadWord();
	}

	private void loadWord() {
		if (mListener != null) {
			final Term word = mListener.getTerm(termDescriptor, termDomain, termLanguage);
			if (word != null) {
				reloadUi(word);
			} else {
				fetchTerm();
			}
		}
	}

	private void fetchTerm() {
		setUiLoading(true);
		App.getApi().getService().term(termDescriptor, termDomain, termLanguage,
				new Callback<Term>() {
					@Override
					public void success(Term term, Response response) {
						if (response.getStatus() == 200 && term != null) {
							reloadUi(term);
							persistTerm(term);
						} else {
							Log.d("Retrofit",
									"Error fetching term: " + response.getStatus() + " - " +
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
		//TODO
	}

	private void reloadUi(Term word) {
		wordTitle.setText(word.getDescriptor());
		wordDescription.setText(word.getScopeNote());

		addWordsToContainer(relatedContainer, word.getRelatedTerms());
		addWordsToContainer(synonymsContainer, word.getCategories());
		addWordsToContainer(moreGenericContainer, word.getBroaderTerms());
		addWordsToContainer(moreSpecificContainer, word.getNarrowerTerms());
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
					if (mListener != null) mListener.onWordClicked(term.getDescriptor(),
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

		void onWordClicked(String termDescriptor, String termDomain, String termLanguage);

		void onUpPressed();
	}
}
