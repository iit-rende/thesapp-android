package it.cnr.iit.thesapp.utils;

import android.content.Context;
import android.preference.PreferenceManager;

public class PrefUtils {
	private static final String PREF_DOMAIN   = "domain";
	private static final String PREF_LANGUAGE = "language";

	public static void saveDomain(Context context, String domain) {
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString(PREF_DOMAIN,
				domain)
						 .apply();
	}

	public static String loadDomain(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_DOMAIN, null);
	}

	public static void saveLanguage(Context context, String language) {
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString(PREF_LANGUAGE,
				language).apply();
	}

	public static String loadLanguage(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_LANGUAGE,
				"it");
	}
}
