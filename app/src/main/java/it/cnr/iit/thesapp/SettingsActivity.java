package it.cnr.iit.thesapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import it.cnr.iit.thesapp.utils.PrefUtils;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
@SuppressLint("NewApi")
public class SettingsActivity extends AppCompatActivity {

	public static void show(Context context) {
		Intent i = new Intent(context, SettingsActivity.class);
		context.startActivity(i);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preferences);
		Toolbar toolbar = (Toolbar) findViewById(R.id.activity_toolbar);

		toolbar.setNavigationIcon(R.drawable.ic_navigation_arrow_back);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				NavUtils.navigateUpFromSameTask(SettingsActivity.this);
			}
		});
		toolbar.setTitle(getString(R.string.action_settings));
		setSupportActionBar(toolbar);
		getFragmentManager().beginTransaction().replace(R.id.main_content, new SettingsFragment())
							.commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static class SettingsFragment extends PreferenceFragment implements SharedPreferences
																					   .OnSharedPreferenceChangeListener {


		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences);
			getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(
					this);

			setLanguagePreferences();
		}

		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
			if (key.equals(PrefUtils.PREF_LANGUAGE) && getActivity() != null) {
				setLanguagePreferences();
				PrefUtils.destroyDomains(getActivity());
			}
		}

		private void setLanguagePreferences() {
			ListPreference languagePreference = (ListPreference) findPreference(
					PrefUtils.PREF_LANGUAGE);
			String currentLanguage = PrefUtils.loadLanguage(getActivity());
			final CharSequence[] entryCharSeq = {getString(R.string.italian), getString(
					R.string.english)};
			final CharSequence[] entryValsChar = {PrefUtils.IT, PrefUtils.EN};

			languagePreference.setEntries(entryCharSeq);
			languagePreference.setEntryValues(entryValsChar);

			switch (currentLanguage) {
				case PrefUtils.IT:
					languagePreference.setSummary(getString(R.string.italian));
					break;
				case PrefUtils.EN:
					languagePreference.setSummary(getString(R.string.english));
					break;
			}
		}
	}
}
