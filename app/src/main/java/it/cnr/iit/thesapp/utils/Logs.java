package it.cnr.iit.thesapp.utils;

import android.util.Log;

import it.cnr.iit.thesapp.BuildConfig;

public class Logs {
	private static final boolean DEBUG = BuildConfig.DEBUG;

	private static final boolean DOCUMENT   = true && DEBUG;
	private static final boolean NAV_DRAWER = true && DEBUG;
	private static final boolean UI         = true && DEBUG;
	private static final boolean RETROFIT   = true && DEBUG;
	private static final boolean IMAGE      = true && DEBUG;
	private static final boolean CACHE      = true && DEBUG;

	private static final String  T_DOCUMENT   = "DOCUMENT";
	private static final String  T_NAV_DRAWER = "NAV_DRAWER";
	private static final String  T_UI         = "UI_CALL";
	private static final String  T_RETROFIT   = "RETROFIT";
	private static final String  T_IMAGE      = "IMAGE";
	private static final String  T_CACHE      = "CACHE";
	private static final boolean THESAURUS    = true && DEBUG;
	private static final String  T_THESAURUS  = "THESAURUS";


	public static void document(String message) {
		if (DOCUMENT) {
			Log.d(T_DOCUMENT, message);
		}
	}


	public static void navDraw(String message) {
		if (NAV_DRAWER) {
			Log.d(T_NAV_DRAWER, message);
		}
	}


	public static void ui(String message) {
		if (UI) {
			Log.d(T_UI, message);
		}
	}

	public static void retrofit(String message) {
		if (RETROFIT) {
			Log.d(T_RETROFIT, message);
		}
	}

	public static void image(String message) {
		if (IMAGE) {
			Log.d(T_IMAGE, message);
		}
	}

	public static void cache(String message) {
		if (CACHE) {
			Log.d(T_CACHE, message);
		}
	}

	public static void thesaurus(String s) {
		if (THESAURUS) {
			Log.d(T_THESAURUS, s);
		}
	}
}
