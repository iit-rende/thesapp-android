package it.cnr.iit.thesapp.api.gcm;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import it.cnr.iit.thesapp.R;
import it.cnr.iit.thesapp.utils.Logs;


public class GcmRegisterService extends IntentService {

	public GcmRegisterService() {super(null);}

	/**
	 * Creates an IntentService.  Invoked by your subclass's constructor.
	 *
	 * @param name Used to name the worker thread, important only for debugging.
	 */
	public GcmRegisterService(String name) {
		super(name);
	}

	public static void startRegistration(Context context) {
		Logs.gcm("Creating GCM register service");
		Intent i = new Intent(context, GcmRegisterService.class);
		context.startService(i);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Logs.gcm("Starting GCM register service");
		try {
			InstanceID instanceID = InstanceID.getInstance(this);
			String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
					GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
			Logs.gcm("GCM Registration Token: " + token);

			// Subscribe to topic channels
			subscribeTopics(token);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void subscribeTopics(String token) {
		Intent i = new Intent(this, GcmTopicRegisterService.class);
		i.putExtra(GcmTopicRegisterService.TOKEN, token);
		startService(i);
	}
}
