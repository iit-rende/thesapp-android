package it.cnr.iit.thesapp.api.gcm;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.GsonBuilder;

import it.cnr.iit.thesapp.model.GcmData;
import it.cnr.iit.thesapp.utils.Logs;
import it.cnr.iit.thesapp.utils.NotificationUtils;


public class GcmReceiverService extends GcmListenerService {

	@Override
	public void onMessageReceived(String from, Bundle data) {
		String domain = data.getString("notification");
		Logs.gcm("From: " + from);
		Logs.gcm("Notification: " + domain);
		GcmData gcmData = new GsonBuilder().create().fromJson(domain, GcmData.class);
		NotificationUtils.showNotificationFromGcm(this.getApplicationContext(), gcmData);
	}
}
