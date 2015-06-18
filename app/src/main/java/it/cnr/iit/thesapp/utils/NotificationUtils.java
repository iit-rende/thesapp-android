package it.cnr.iit.thesapp.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import it.cnr.iit.thesapp.MainActivity;
import it.cnr.iit.thesapp.R;
import it.cnr.iit.thesapp.model.GcmData;

public class NotificationUtils {

	public static final  String OPEN_WITH_DOMAIN = "Open_with_domain";
	private static final int    NOTIFICATION_ID  = 12345;

	public static void showNotificationFromGcm(Context context, @NonNull GcmData gcmData) {
		String language = PrefUtils.loadLanguage(context);
		GcmData.Localization localization = null;
		for (GcmData.Localization loc : gcmData.getLocalizations()) {
			if (loc.getLanguage().equalsIgnoreCase(language)) localization = loc;
		}
		if (localization == null && gcmData.getLocalizations() != null &&
		    gcmData.getLocalizations().get(0) != null)
			localization = gcmData.getLocalizations().get(0);
		final NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		builder.setContentTitle(gcmData.getDomain());
		if (localization != null) builder.setContentText(localization.getText());

		Intent resultIntent = new Intent(context, MainActivity.class);
		resultIntent.putExtra(OPEN_WITH_DOMAIN, gcmData.getDomain());
		PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		builder.setContentIntent(resultPendingIntent);
		builder.setSmallIcon(R.mipmap.ic_launcher);
		builder.setAutoCancel(true);
		builder.setDefaults(NotificationCompat.DEFAULT_ALL);

		if (localization != null) {
			final NotificationCompat.BigTextStyle bigTextStyle =
					new NotificationCompat.BigTextStyle().bigText(localization.getText())
					                                     .setBigContentTitle(gcmData.getDomain())
					                                     .setSummaryText(localization.getText());
			builder.setStyle(bigTextStyle);
		}

		NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(
				Context.NOTIFICATION_SERVICE);
		mNotifyMgr.notify(NOTIFICATION_ID, builder.build());
	}
}
