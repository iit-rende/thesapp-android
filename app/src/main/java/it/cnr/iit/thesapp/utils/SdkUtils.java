package it.cnr.iit.thesapp.utils;

import android.os.Build;

public class SdkUtils {

	public static boolean isPreSDK18() {
		return Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2;
	}

	public static boolean isPreSDK16() {
		return Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN;
	}

	public static boolean isPreLollipop() {
		return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
	}
}
