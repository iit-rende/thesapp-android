package it.cnr.iit.thesapp.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import it.cnr.iit.thesapp.model.Word;

public class MockUtils {
    public static List<Word> createMockThesaurus() {
        IdHolder id = new IdHolder();
        List<Word> thesaurus = new ArrayList<>();

        for (char alphabet = 'A'; alphabet <= 'Z'; alphabet++) {
            thesaurus.add(createWord(thesaurus, null, String.valueOf(alphabet), id, 0));
        }

        Log.d("Thesaurus", "Mock thesaurus created with " + thesaurus.size() + " words");

        for (Word thesauru : thesaurus) {
            //  Log.d("Thesaurus", thesauru.getId() + " " + thesauru.getWord());
        }
        return thesaurus;
    }

    public static Word createWord(List<Word> thesaurus, Word parent, String a, IdHolder id, int depth) {
        Word word = new Word();
        word.setWord("Word " + a);
        word.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer vel urna mollis, finibus orci et, semper arcu. Cras dui justo, volutpat a rutrum sed, lacinia sollicitudin risus. Ut rutrum arcu et felis ullamcorper commodo. Praesent semper erat vel justo egestas interdum. Sed quis tellus interdum, sollicitudin sem sed, varius turpis. Vestibulum auctor ligula ut eros aliquam, eget mollis metus auctor. Morbi at erat a leo auctor volutpat. Aliquam erat volutpat. Ut odio justo, laoreet sodales tempus id, bibendum at dolor.");

        word.setId(id.nextId());

        //Related words
        //Specific words
        depth++;
        if (depth <= 2) {
            List<Word> specific = new ArrayList<>();
            for (char alphabet = 'A'; alphabet <= 'Z'; alphabet++) {
                specific.add(createWord(thesaurus, word, a + String.valueOf(alphabet), id, depth));
            }
            word.setMoreSpecific(specific);
            word.setRelated(specific);
        }

        if (parent != null) {
            //Synonims
            //Generic words
            List<Word> generic = new ArrayList<>();
            generic.add(parent);
            word.setMoreGeneric(generic);
            word.setSynonyms(generic);
        }
        thesaurus.add(word);
        return word;
    }

    public static class IdHolder {
        public long id;

        public long nextId() {
            id++;
            return id;
        }
    }
}
