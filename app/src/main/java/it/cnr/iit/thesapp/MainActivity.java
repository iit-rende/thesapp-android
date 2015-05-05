package it.cnr.iit.thesapp;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import it.cnr.iit.thesapp.adapters.TermExplorerAdapter;
import it.cnr.iit.thesapp.adapters.TermSearchAdapter;
import it.cnr.iit.thesapp.fragments.TermFragment;
import it.cnr.iit.thesapp.model.Term;
import it.cnr.iit.thesapp.views.DelayedAutoCompleteTextView;


public class MainActivity extends AppCompatActivity implements TermFragment.WordFragmentCallbacks {

	private TermExplorerAdapter termExplorerAdapter;
	private TermSearchAdapter   termSearchAdapter;
	private ViewPager           pager;

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

		DelayedAutoCompleteTextView searchText = (DelayedAutoCompleteTextView) findViewById(
				R.id.search_text);
		searchText.setLoadingIndicator((android.widget.ProgressBar) findViewById(
				R.id.pb_loading_indicator));
		termSearchAdapter = new TermSearchAdapter(this, null);
		searchText.setAdapter(termSearchAdapter);
		searchText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
				final Term term = termSearchAdapter.getItem(pos);
				onWordSelected(term.getDescriptor(), term.getDomain(), term.getLanguage());
			}
		});
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
