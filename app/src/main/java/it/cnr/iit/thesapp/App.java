package it.cnr.iit.thesapp;

import android.app.Application;

import it.cnr.iit.thesapp.api.Api;

public class App extends Application {
	private static Api api;

	public static Api getApi() {
		if (api == null) api = new Api();
		return api;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}
}
