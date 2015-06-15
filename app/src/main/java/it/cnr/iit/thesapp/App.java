package it.cnr.iit.thesapp;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import it.cnr.iit.thesapp.api.Api;
import it.cnr.iit.thesapp.api.gcm.GcmRegisterService;
import it.cnr.iit.thesapp.model.CategoryList;
import it.cnr.iit.thesapp.model.Domain;
import it.cnr.iit.thesapp.model.DomainSearch;
import it.cnr.iit.thesapp.model.TimelineElement;
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
		Crashlytics crashlytics = new Crashlytics.Builder().disabled(BuildConfig.DEBUG).build();
		final Fabric fabric = new Fabric.Builder(this).kits(crashlytics).debuggable(false).build();
		Fabric.with(fabric);
		createTimeLine();
		GcmRegisterService.startRegistration(this);
	}

	public void createTimeLine() {
		timelineElements = new ArrayList<>();
		DomainSearch domainSearch = new DomainSearch(Domain.getDefault(this),
				PrefUtils.loadLanguage(this));
		timelineElements.add(domainSearch);

		if (!TextUtils.isEmpty(PrefUtils.loadDomain(this))) {
			CategoryList categoryList = new CategoryList(PrefUtils.loadDomain(this),
					PrefUtils.loadLanguage(this));
			timelineElements.add(categoryList);
		}
	}
}
