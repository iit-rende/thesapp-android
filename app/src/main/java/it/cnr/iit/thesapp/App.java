package it.cnr.iit.thesapp;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import it.cnr.iit.thesapp.api.Api;
import it.cnr.iit.thesapp.model.Domain;
import it.cnr.iit.thesapp.model.DomainSearch;
import it.cnr.iit.thesapp.model.TimelineElement;
import it.cnr.iit.thesapp.utils.PrefUtils;

public class App extends Application {
	public static  List<TimelineElement> timelineElements;
	private static Api                   api;

	public static Api getApi() {
		if (api == null) api = new Api();
		return api;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		createTimeLine();
	}

	public void createTimeLine() {
		timelineElements = new ArrayList<>();
		DomainSearch domainSearch = new DomainSearch(Domain.getDefault(this),
				PrefUtils.loadLanguage(this));
		timelineElements.add(domainSearch);
	}
}
