package it.cnr.iit.thesapp;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import it.cnr.iit.thesapp.api.Api;
import it.cnr.iit.thesapp.model.Term;

public class App extends Application {
	public static List<Term> terms = new ArrayList<>();
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
