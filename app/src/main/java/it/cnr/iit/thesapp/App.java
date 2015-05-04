package it.cnr.iit.thesapp;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import it.cnr.iit.thesapp.api.Api;
import it.cnr.iit.thesapp.model.Word;
import it.cnr.iit.thesapp.utils.MockUtils;

public class App extends Application {
	private static List<Word> thesaurus;
	private static Api        api;

	public static List<Word> getThesaurus() {
		return thesaurus;
	}

	public static Word getWord(long wordId) {
		for (Word word : thesaurus) {
			if (word.getId() == wordId) return word;
		}
		Log.d("Thesaurus", "Word NOT found: " + wordId);
		return null;
	}

	public static List<Word> getFilteredThesaurus(CharSequence filter) {
		List<Word> filtered = new ArrayList<>();
		if (filter != null) for (Word word : thesaurus) {
			if (word.getWord().contains(filter)) filtered.add(word);
		}
		return filtered;
	}

	public static Api getApi() {
		if (api == null) api = new Api();
		return api;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		thesaurus = MockUtils.createMockThesaurus();
	}
}
