package it.cnr.iit.thesapp.api.gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;


public class MyInstanceIDService extends InstanceIDListenerService {

	public void onTokenRefresh() {
		// Fetch updated Instance ID token and notify our app's server of any changes (if
		// applicable).
		Intent intent = new Intent(this, GcmRegisterService.class);
		startService(intent);
	}
}

