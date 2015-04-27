package it.cnr.iit.thesapp;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.devspark.robototextview.widget.RobotoAutoCompleteTextView;

import it.cnr.iit.thesapp.adapters.WordExplorerAdapter;
import it.cnr.iit.thesapp.adapters.WordSearchAdapter;
import it.cnr.iit.thesapp.fragments.WordFragment;
import it.cnr.iit.thesapp.model.Word;


public class MainActivity extends AppCompatActivity implements WordFragment.WordFragmentCallbacks {

	private WordExplorerAdapter mAdapter;
	private WordSearchAdapter   wordSearchAdapter;
	private ViewPager           pager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setSupportActionBar((android.support.v7.widget.Toolbar) findViewById(
				R.id.activity_toolbar));
		pager = (ViewPager) findViewById(R.id.pager);
		mAdapter = new WordExplorerAdapter(this, getSupportFragmentManager(), null, pager);
		pager.setOffscreenPageLimit(5);
		pager.setAdapter(mAdapter);

		RobotoAutoCompleteTextView searchText = (RobotoAutoCompleteTextView) findViewById(
				R.id.search_text);
		wordSearchAdapter = new WordSearchAdapter(this, App.getThesaurus());
		searchText.setAdapter(wordSearchAdapter);
		searchText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
				onWordSelected(id);
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

	public void onWordSelected(long wordId) {
		int pos = mAdapter.addWord(App.getWord(wordId));
		Log.d("Pager", "Positon for word " + wordId + ": " + pos);
		if (pos != -1) {
			pager.setCurrentItem(pos, true);
		}
	}

	@Override
	public Word getWord(long wordId) {
		return mAdapter.getWord(wordId);
	}

	@Override
	public void onWordClicked(long wordId) {
		onWordSelected(wordId);
	}

	@Override
	public void onUpPressed() {
		if (pager.getCurrentItem() > 0) pager.setCurrentItem(pager.getCurrentItem() - 1, true);
	}
}
