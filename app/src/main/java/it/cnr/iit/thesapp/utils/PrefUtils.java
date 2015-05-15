package it.cnr.iit.thesapp.utils;

import android.content.Context;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import it.cnr.iit.thesapp.R;
import it.cnr.iit.thesapp.model.Domain;

public class PrefUtils {
	public static final String PREF_LANGUAGE = "language";
	public static final String IT            = "it";
	public static final String EN            = "en";
	private static final String PREF_DOMAIN   = "domain";
	private static final String DOMAINS_CACHE = "domains";

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
				context.getString(R.string.default_language));
	}

	public static void saveDomains(Context context, List<Domain> domains) {
		String domainsJson = new Gson().toJson(domains);
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString(DOMAINS_CACHE,
				domainsJson).apply();
	}

	public static List<Domain> loadDomains(Context context) {
		String domainsJson = PreferenceManager.getDefaultSharedPreferences(context).getString(
				DOMAINS_CACHE, null);
		Type listType = new TypeToken<List<Domain>>() {
		}.getType();
		return new Gson().fromJson(domainsJson, listType);
	}

	public static void destroyDomains(Context context) {
		PreferenceManager.getDefaultSharedPreferences(context).edit().remove(DOMAINS_CACHE)
						 .commit();
	}
}
