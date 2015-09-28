package it.cnr.iit.thesapp;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import it.cnr.iit.thesapp.api.Api;
import it.cnr.iit.thesapp.api.gcm.GcmRegisterService;
import it.cnr.iit.thesapp.model.CategoryList;
import it.cnr.iit.thesapp.model.Domain;
import it.cnr.iit.thesapp.model.DomainSearch;
import it.cnr.iit.thesapp.model.TimelineElement;
import it.cnr.iit.thesapp.utils.Logs;
import it.cnr.iit.thesapp.utils.PrefUtils;

public class App extends Application {

	public static  List<TimelineElement> timelineElements;
	private static Api                   api;

	public static App getApp(Context context) {
		return ((App) context.getApplicationContext());
	}

	public static Api getApi() {
		if (api == null) api = new Api();
		return api;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Crashlytics crashlyticsKit = new Crashlytics.Builder().core(
				new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build();
		Fabric.with(this, crashlyticsKit);

		createTimeLine();
		GcmRegisterService.startRegistration(this);
	}

	public void createTimeLine() {
		if (timelineElements != null) timelineElements.clear();
		timelineElements = new ArrayList<>();
		final String language = PrefUtils.loadLanguage(this);
		Logs.thesaurus("Language used to build timeline: " + language);
		DomainSearch domainSearch = new DomainSearch(Domain.getDefault(this), language);
		timelineElements.add(domainSearch);

		if (!TextUtils.isEmpty(PrefUtils.loadDomain(this))) {
			CategoryList categoryList = new CategoryList(PrefUtils.loadDomain(this), language);
			timelineElements.add(categoryList);
		}
	}
}
