package it.cnr.iit.thesapp.utils;

import android.graphics.Color;

public class ColorUtils {

	public static int darkerColor(int color) {
		float[] hsv = new float[3];
		Color.colorToHSV(color, hsv);
		hsv[2] *= 0.8f; // value component
		return Color.HSVToColor(hsv);
	}
}
