package it.cnr.iit.thesapp;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import it.cnr.iit.thesapp.adapters.DomainSpinnerAdapter;
import it.cnr.iit.thesapp.adapters.TermExplorerAdapter;
import it.cnr.iit.thesapp.fragments.TermFragment;
import it.cnr.iit.thesapp.model.DomainSearch;
import it.cnr.iit.thesapp.model.Term;
import it.cnr.iit.thesapp.utils.Logs;
import it.cnr.iit.thesapp.utils.PrefUtils;
import it.cnr.iit.thesapp.views.SearchBox;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends AppCompatActivity implements TermFragment.WordFragmentCallbacks {

	private TermExplorerAdapter termExplorerAdapter;
	private ViewPager           pager;
	private SearchBox searchBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setSupportActionBar((android.support.v7.widget.Toolbar) findViewById(
				R.id.activity_toolbar));
		pager = (ViewPager) findViewById(R.id.pager);
		termExplorerAdapter = new TermExplorerAdapter(this, getSupportFragmentManager(), null,
				pager);
		pager.setOffscreenPageLimit(3);
		pager.setAdapter(termExplorerAdapter);

		searchBox = (SearchBox) findViewById(R.id.search_container);
		searchBox.setSearchBoxListener(new SearchBox.SearchBoxListener() {
			@Override
			public void onTermSelected(String descriptor, String domain, String language) {
				onWordSelected(descriptor, domain, language);
			}
		});

		loadDomains();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}


	private void loadDomains() {
		App.getApi().getService().domains(PrefUtils.loadLanguage(this),
				new Callback<DomainSearch>() {
					@Override
					public void success(DomainSearch domainSearch, Response response) {
						if (response.getStatus() == 200) {
							Logs.retrofit("Domain fetched: " + domainSearch.getDomains().size());
							DomainSpinnerAdapter domainSpinnerAdapter = new DomainSpinnerAdapter(
									MainActivity.this, domainSearch.getDomains());
							searchBox.setDomainSpinnerAdapter(domainSpinnerAdapter);
						} else {
							Logs.retrofit("Domain fetch failed: " + response.getStatus() + " - " +
										  response.getReason());
						}
					}

					@Override
					public void failure(RetrofitError error) {
						error.printStackTrace();
					}
				});
	}

	public void onWordSelected(String termDescriptor, String termDomain, String termLanguage) {
		int pos = termExplorerAdapter.addTerm(termDescriptor, termDomain, termLanguage);
		Log.d("Pager", "Positon for word " + termDescriptor + ": " + pos);
		if (pos != -1) {
			pager.setCurrentItem(pos, true);
		}
	}

	@Override
	public Term getTerm(String termDescriptor, String termDomain, String termLanguage) {
		return termExplorerAdapter.getTerm(termDescriptor, termDomain, termLanguage);
	}

	@Override
	public void onTermClicked(String termDescriptor, String termDomain, String termLanguage) {
		onWordSelected(termDescriptor, termDomain, termLanguage);
	}

	@Override
	public void onTermFetched(Term term) {
		termExplorerAdapter.onTermFetched(term);
	}

	@Override
	public void onUpPressed() {
		if (pager.getCurrentItem() > 0) pager.setCurrentItem(pager.getCurrentItem() - 1, true);
	}
}
