package it.cnr.iit.thesapp.api.gcm;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

import it.cnr.iit.thesapp.utils.Logs;


public class GcmReceiverService extends GcmListenerService {

	@Override
	public void onMessageReceived(String from, Bundle data) {
		String message = data.getString("message");
		Logs.gcm("From: " + from);
		Logs.gcm("Message: " + message);
	}
}
