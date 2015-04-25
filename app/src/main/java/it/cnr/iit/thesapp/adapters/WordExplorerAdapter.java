package it.cnr.iit.thesapp.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.util.TypedValue;

import java.util.ArrayList;
import java.util.List;

import it.cnr.iit.thesapp.R;
import it.cnr.iit.thesapp.fragments.WordFragment;
import it.cnr.iit.thesapp.model.Word;

public class WordExplorerAdapter extends FragmentPagerAdapter {
    private final List<Word> words;
    private final float      pageWidth;
    private int count = -1;

    public WordExplorerAdapter(Context context, FragmentManager fm, List<Word> words) {
        super(fm);
        if (words != null)
            this.words = words;
        else this.words = new ArrayList<>();
        TypedValue outValue = new TypedValue();
        context.getResources().getValue(R.dimen.pager_page_width, outValue, true);
        pageWidth = outValue.getFloat();
    }

    @Override
    public float getPageWidth(int position) {
        return pageWidth;
    }

    public int addWord(Word word) {
        if (word != null) {
            Log.d("Thesaurus", "Adding " + word.getWord() + " to explorer");
            if (!isWordInList(word)) {
                words.add(word);
                count = -1;
                notifyDataSetChanged();
            }
            return getPositionForWord(word);
        }
        return -1;
    }

    private int getPositionForWord(Word targetWord) {
        int i = 0;
        for (Word word : words) {
            if (word.getId() == targetWord.getId()) return i;
            i++;
        }
        return -1;
    }

    private boolean isWordInList(Word word) {
        for (Word word1 : words) {
            if (word.getId() == word1.getId()) return true;
        }
        return false;
    }

    public void removeWords(long lastWordToShow) {
        //TODO
    }

    public Word getWord(long wordId) {
        for (Word word : words) {
            if (word.getId() == wordId) return word;
        }
        return null;
    }

    @Override
    public Fragment getItem(int i) {
        return WordFragment.newInstance(words.get(i).getId());
    }

    @Override
    public int getCount() {
        if (count == -1) {
            if (words != null) {
                count = words.size();
            } else {
                return 0;
            }
        }
        return count;
    }
}
