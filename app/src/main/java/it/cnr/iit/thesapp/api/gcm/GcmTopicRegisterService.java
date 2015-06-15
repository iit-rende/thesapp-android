package it.cnr.iit.thesapp.api.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;

import com.google.android.gms.gcm.GcmPubSub;

import java.io.IOException;

import it.cnr.iit.thesapp.utils.Logs;

public class GcmTopicRegisterService extends IntentService {

	public static final  String       TOKEN         = "token";
	private static final CharSequence DEFAULT_TOPIC = "DEFAULT_TOPIC";

	public GcmTopicRegisterService() {super(null);}

	/**
	 * Creates an IntentService.  Invoked by your subclass's constructor.
	 *
	 * @param name Used to name the worker thread, important only for debugging.
	 */
	public GcmTopicRegisterService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String token = intent.getStringExtra(TOKEN);
		if (TextUtils.isEmpty(token)) {
			Logs.gcm("Empty token.");
			return;
		}
		if (!TextUtils.isEmpty(DEFAULT_TOPIC)) {

			Logs.gcm("subscribing to topic: " + DEFAULT_TOPIC + " with token " + token);
			try {
				GcmPubSub.getInstance(this).subscribe(token, "/topics/" + DEFAULT_TOPIC, null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
