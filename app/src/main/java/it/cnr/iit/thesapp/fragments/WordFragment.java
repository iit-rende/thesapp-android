package it.cnr.iit.thesapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.devspark.robototextview.widget.RobotoTextView;

import org.apmem.tools.layouts.FlowLayout;

import java.util.List;

import it.cnr.iit.thesapp.R;
import it.cnr.iit.thesapp.model.Word;

public class WordFragment extends Fragment {
	private static final String ARG_WORD_ID = "word_id";

	private long wordId;

	private WordFragmentCallbacks mListener;
	private RobotoTextView        wordTitle;
	private RobotoTextView        wordDescription;
	private FlowLayout            relatedContainer;
	private FlowLayout            synonymsContainer;
	private FlowLayout            moreGenericContainer;
	private FlowLayout            moreSpecificContainer;
	private ScrollView            scrollView;
	private CardView              cardView;

	public WordFragment() {
		// Required empty public constructor
	}

	public static WordFragment newInstance(long wordId) {
		WordFragment fragment = new WordFragment();
		Bundle args = new Bundle();
		args.putLong(ARG_WORD_ID, wordId);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			wordId = getArguments().getLong(ARG_WORD_ID);
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
		wordTitle = (RobotoTextView) view.findViewById(R.id.word_word);
		wordDescription = (RobotoTextView) view.findViewById(R.id.word_description);

		relatedContainer = (FlowLayout) view.findViewById(R.id.word_related);
		synonymsContainer = (FlowLayout) view.findViewById(R.id.word_synonyms);
		moreGenericContainer = (FlowLayout) view.findViewById(R.id.word_more_generic);
		moreSpecificContainer = (FlowLayout) view.findViewById(R.id.word_more_specific);

		scrollView = (ScrollView) view.findViewById(R.id.scrollView);
		cardView = (CardView) view.findViewById(R.id.cardView);
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
			final Word word = mListener.getWord(wordId);
			if (word != null) reloadUi(word);
		}
	}

	private void reloadUi(Word word) {
		wordTitle.setText(word.getWord());
		wordDescription.setText(word.getDescription());

		addWordsToContainer(relatedContainer, word.getRelated());
		addWordsToContainer(synonymsContainer, word.getSynonyms());
		addWordsToContainer(moreGenericContainer, word.getMoreGeneric());
		addWordsToContainer(moreSpecificContainer, word.getMoreSpecific());
	}

	private void addWordsToContainer(FlowLayout container, List<Word> words) {
		int padding = getResources().getDimensionPixelSize(R.dimen.padding_small);
		int backgroundColor = getResources().getColor(R.color.primary);
		int textColor = getResources().getColor(R.color.white);
		if (words != null) for (final Word word : words) {
			RobotoTextView tv = new RobotoTextView(getActivity());
			tv.setText(word.getWord());
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
					if (mListener != null) mListener.onWordClicked(word.getId());
				}
			});
			container.addView(tv);
		}
	}


	public void scrollToTop() {
		scrollView.pageScroll(View.FOCUS_UP);
	}

	public void elevate(boolean up) {
		float cardElevation = up ? getResources().getDimensionPixelSize(R.dimen.elevation_high) :
							  0f;
		cardView.setCardElevation(cardElevation);
	}

	public interface WordFragmentCallbacks {
		Word getWord(long wordId);

		void onWordClicked(long wordId);

		void onUpPressed();
	}
}
