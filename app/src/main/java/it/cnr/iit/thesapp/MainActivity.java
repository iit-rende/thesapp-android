package it.cnr.iit.thesapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import it.cnr.iit.thesapp.adapters.DomainSpinnerAdapter;
import it.cnr.iit.thesapp.adapters.TermExplorerAdapter;
import it.cnr.iit.thesapp.fragments.TimelineElementFragment;
import it.cnr.iit.thesapp.model.Domain;
import it.cnr.iit.thesapp.model.DomainSearch;
import it.cnr.iit.thesapp.model.TimelineElement;
import it.cnr.iit.thesapp.utils.Logs;
import it.cnr.iit.thesapp.utils.PrefUtils;
import it.cnr.iit.thesapp.views.SearchBox;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends AppCompatActivity implements TimelineElementFragment
																	   .TermFragmentCallbacks {
	private TermExplorerAdapter termExplorerAdapter;
	private ViewPager           pager;
	private SearchBox      searchBox;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setSupportActionBar((Toolbar) findViewById(R.id.activity_toolbar));
		pager = (ViewPager) findViewById(R.id.pager);
		termExplorerAdapter = new TermExplorerAdapter(this, getSupportFragmentManager(), null,
				pager);
		pager.setOffscreenPageLimit(3);
		pager.setAdapter(termExplorerAdapter);

		searchBox = (SearchBox) findViewById(R.id.search_container);
		searchBox.setSearchBoxListener(new SearchBox.SearchBoxListener() {
			@Override
			public void onTermSelected(String descriptor, String domain, String language) {
				onElementSelected(descriptor, domain, language, TimelineElement.KIND_TERM, -1);
			}
		});
		DomainSpinnerAdapter domainSpinnerAdapter = new DomainSpinnerAdapter(MainActivity.this,
				null);
		searchBox.setDomainSpinnerAdapter(domainSpinnerAdapter);

		loadDomains();
		fetchDomains();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_settings:
				return true;
			default:
				return false;
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
		super.onSaveInstanceState(outState, outPersistentState);
	}


	private void loadDomains() {
		Logs.cache("Loading domains from cache");
		final List<Domain> domains = PrefUtils.loadDomains(this);
		if (domains != null) {
			searchBox.setDomains(domains);
		} else {
			showLoadingDialog(true);
		}
	}

	private void showLoadingDialog(boolean loading) {
		if (dialog == null) {
			dialog = new ProgressDialog(this);
			dialog.setIndeterminate(true);
			dialog.setMessage(getString(R.string.loading_domains));
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
		}
		if (loading) {
			dialog.show();
		} else {
			if (dialog != null) dialog.cancel();
		}
	}

	private void fetchDomains() {
		App.getApi().getService().domains(PrefUtils.loadLanguage(this),
				new Callback<DomainSearch>() {
					@Override
					public void success(DomainSearch domainSearch, Response response) {
						if (response.getStatus() == 200) {
							Logs.retrofit("Domain fetched: " + domainSearch.getDomains().size());
							searchBox.setDomains(domainSearch.getDomains());
							PrefUtils.saveDomains(MainActivity.this, domainSearch.getDomains());
						} else {
							Logs.retrofit("Domain fetch failed: " + response.getStatus() + " - " +
										  response.getReason());
						}
						showLoadingDialog(false);
					}

					@Override
					public void failure(RetrofitError error) {
						error.printStackTrace();
						showLoadingDialog(false);
					}
				});
	}

	public void onElementSelected(String termDescriptor, String termDomain, String termLanguage,
								  int elementKind, int clickedFromPage) {
		int pos = -1;
		if (elementKind == TimelineElement.KIND_TERM) {
			pos = termExplorerAdapter.addTerm(termDescriptor, termDomain, termLanguage,
					clickedFromPage);
		}
		if (elementKind == TimelineElement.KIND_CATEGORY) {
			pos = termExplorerAdapter.addCategory(termDescriptor, termDomain, termLanguage,
					clickedFromPage);
		}
		Log.d("Pager", "Positon for word " + termDescriptor + ": " + pos);
		if (pos != -1) {
			pager.setCurrentItem(pos, true);
		}
	}

	@Override
	public TimelineElement getElement(String termDescriptor, String termDomain, String
			termLanguage,
									  int elementKind) {
		return termExplorerAdapter.getTerm(termDescriptor, termDomain, termLanguage, elementKind);
	}

	@Override
	public void onElementClicked(String termDescriptor, String termDomain, String termLanguage,
								 int elementKind, int clickedFromPage) {
		onElementSelected(termDescriptor, termDomain, termLanguage, elementKind, clickedFromPage);
	}

	@Override
	public void onElementFetched(TimelineElement term) {
		termExplorerAdapter.onTermFetched(term);
	}

	@Override
	public void onUpPressed() {
		if (pager.getCurrentItem() > 0) pager.setCurrentItem(pager.getCurrentItem() - 1, true);
	}
}
