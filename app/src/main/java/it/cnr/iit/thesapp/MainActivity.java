package it.cnr.iit.thesapp;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Property;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.wunderlist.slidinglayer.SlidingLayer;

import java.util.List;

import it.cnr.iit.thesapp.adapters.TimelineAdapter;
import it.cnr.iit.thesapp.fragments.TimelineElementFragment;
import it.cnr.iit.thesapp.model.Domain;
import it.cnr.iit.thesapp.model.DomainSearch;
import it.cnr.iit.thesapp.model.TimelineElement;
import it.cnr.iit.thesapp.utils.Logs;
import it.cnr.iit.thesapp.utils.PrefUtils;
import it.cnr.iit.thesapp.views.SearchBox;
import it.cnr.iit.thesapp.views.SearchPanel;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends AppCompatActivity implements TimelineElementFragment.TimelineElementFragmentCallback {


	private TimelineAdapter timelineAdapter;
	private ViewPager       pager;
	private ProgressDialog  dialog;
	private Toolbar         toolbar;
	private int             toolbarColor;
	private SearchPanel     searchPanel;
	final Property<MainActivity, Integer> uiColorProperty = new Property<MainActivity, Integer>(
			int.class, "color") {
		@Override
		public void set(MainActivity object, Integer value) {
			object.setToolbarColor(value);
		}

		@Override
		public Integer get(MainActivity object) {
			return object.getToolbarColor();
		}
	};
	private SlidingLayer slidingLayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		toolbar = (Toolbar) findViewById(R.id.activity_toolbar);
		setSupportActionBar(toolbar);
		createPager();

		searchPanel = (SearchPanel) findViewById(R.id.search_panel);
		searchPanel.setSearchListener(new SearchBox.SearchBoxListener() {
			@Override
			public void onTermSelected(String descriptor, String domain, String language) {
				slidingLayer.closeLayer(true);
				onElementClicked(descriptor, domain, language, TimelineElement.KIND_TERM, -1);
			}

			@Override
			public void onDomainSelected(Domain domain) {
				setToolbarDomain(domain, pager.getCurrentItem());
			}
		});

		slidingLayer = (SlidingLayer) findViewById(R.id.slidingLayer);
		slidingLayer.setOnInteractListener(new SlidingLayer.OnInteractListener() {
			@Override
			public void onOpen() {
				searchPanel.showKeyboard();
			}

			@Override
			public void onShowPreview() { }

			@Override
			public void onClose() {
				searchPanel.hideKeyboard();
			}

			@Override
			public void onOpened() { }

			@Override
			public void onPreviewShowed() { }

			@Override
			public void onClosed() { }
		});

		loadDomains();
		fetchDomains();

		PreferenceManager.getDefaultSharedPreferences(this)
		                 .registerOnSharedPreferenceChangeListener(
				                 new SharedPreferences.OnSharedPreferenceChangeListener() {
					                 @Override
					                 public void onSharedPreferenceChanged(
							                 SharedPreferences sharedPreferences, String key) {
						                 if (key.equals(PrefUtils.PREF_LANGUAGE)) {
							                 onLanguageChanged();
						                 }
					                 }
				                 });
	}


	private void onLanguageChanged() {
		Logs.thesaurus("Language changed");
		searchPanel.setLanguage(PrefUtils.loadLanguage(this));
	}


	private void createPager() {
		pager = null;
		timelineAdapter = null;
		pager = (ViewPager) findViewById(R.id.pager);
		timelineAdapter = new TimelineAdapter(this, getSupportFragmentManager(), pager);
		pager.setOffscreenPageLimit(3);
		pager.setAdapter(timelineAdapter);
		pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset,
			                           int positionOffsetPixels) {}

			@Override
			public void onPageSelected(int position) {
				setToolbarDomain(App.timelineElements.get(position).getDomain(), position);
			}

			@Override
			public void onPageScrollStateChanged(int state) {}
		});

		if (timelineAdapter.getCount() > 1) pager.setCurrentItem(1);
	}

	@Override
	public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
		super.onSaveInstanceState(outState, outPersistentState);
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
				SettingsActivity.show(this);
				return true;
			case R.id.action_open_search_panel:
				Logs.ui("Opening search panel");
				slidingLayer.openLayer(true);
				return true;
			case R.id.action_delete_history:
				Logs.ui("Deleting history");
				timelineAdapter.goHome();
			default:
				return false;
		}
	}

	@Override
	public void onBackPressed() {

		if (slidingLayer.isOpened()) slidingLayer.closeLayer(true);
		else super.onBackPressed();
	}

	private void loadDomains() {
		Logs.cache("Loading domains from cache");
		final List<Domain> domains = PrefUtils.loadDomains(this);
		if (domains != null) {
			searchPanel.setDomains(domains);
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
							searchPanel.setDomains(domainSearch.getDomains());
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
		if (TextUtils.isEmpty(termLanguage)) termLanguage = PrefUtils.loadLanguage(this);
		int pos = -1;
		switch (elementKind) {
			case TimelineElement.KIND_TERM:
				pos = timelineAdapter.addTerm(termDescriptor, termDomain, termLanguage,
						clickedFromPage);
				break;
			case TimelineElement.KIND_CATEGORY:
				pos = timelineAdapter.addCategory(termDescriptor, termDomain, termLanguage,
						clickedFromPage);
				break;
			case TimelineElement.KIND_DOMAIN_CONTAINER:
				pos = timelineAdapter.addDomainList(termLanguage);
				break;
			case TimelineElement.KIND_CATEGORY_LIST:
				pos = timelineAdapter.addCategoryList(termDomain, termLanguage, clickedFromPage);
				break;
		}
		Log.d("Pager", "Positon for word " + termDescriptor + ": " + pos);
		if (pos != -1) {
			pager.setCurrentItem(pos, true);
		}
	}

	@Override
	public TimelineElement getElement(String termDescriptor, String termDomain, String
			termLanguage, int elementKind) {
		return timelineAdapter.getTerm(termDescriptor, termDomain, termLanguage, elementKind);
	}

	@Override
	public void onElementClicked(String termDescriptor, String termDomain, String termLanguage,
	                             int elementKind, int clickedFromPage) {
		onElementSelected(termDescriptor, termDomain, termLanguage, elementKind, clickedFromPage);
	}

	@Override
	public void onElementFetched(TimelineElement term) {
		timelineAdapter.onElementFetched(term);
	}

	@Override
	public void onUpPressed() {
		if (pager.getCurrentItem() > 0) pager.setCurrentItem(pager.getCurrentItem() - 1, true);
	}

	@Override
	public void setToolbarDomain(Domain domain, int page) {
		if (domain != null && pager.getCurrentItem() == page) {

			ObjectAnimator anim = ObjectAnimator.ofInt(this, uiColorProperty, Color.parseColor(
					domain.getColor()));
			anim.setEvaluator(new ArgbEvaluator());
			anim.setDuration(250);
			anim.start();
			String domainName = " - " + domain.getDescriptor();
			if (domain.getDescriptor().equals(Domain.DEFAULT_DOMAIN_DESCRIPTOR)) domainName = "";
			toolbar.setTitle(getString(R.string.app_name) + domainName);
		}
	}

	public Integer getToolbarColor() {
		return toolbarColor;
	}

	public void setToolbarColor(Integer toolbarColor) {
		this.toolbarColor = toolbarColor;
		toolbar.setBackgroundColor(toolbarColor);
		searchPanel.setFakeToolbarColor(toolbarColor);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			//getWindow().setStatusBarColor(toolbarColor);
			getWindow().setStatusBarColor(Color.TRANSPARENT);
		}
	}
}
