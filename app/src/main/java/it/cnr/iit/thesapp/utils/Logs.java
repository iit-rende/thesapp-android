package it.cnr.iit.thesapp.utils;

import android.util.Log;

import it.cnr.iit.thesapp.BuildConfig;

public class Logs {
	private static final boolean DEBUG      = BuildConfig.DEBUG;
	private static final boolean RETROFIT   = true && DEBUG;
	private static final String  T_RETROFIT = "Retrofit";

	public static void retrofit(String message) {
		if (RETROFIT) {
			Log.d(T_RETROFIT, message);
		}
	}
}
