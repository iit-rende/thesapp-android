package it.cnr.iit.thesapp.model;

import java.util.List;

public class Word {
    private long       id;
    private String     word;
    private String     description;
    private List<Word> synonyms;
    private List<Word> related;
    private List<Word> moreGeneric;
    private List<Word> moreSpecific;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Word> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<Word> synonyms) {
        this.synonyms = synonyms;
    }

    public List<Word> getRelated() {
        return related;
    }

    public void setRelated(List<Word> related) {
        this.related = related;
    }

    public List<Word> getMoreGeneric() {
        return moreGeneric;
    }

    public void setMoreGeneric(List<Word> moreGeneric) {
        this.moreGeneric = moreGeneric;
    }

    public List<Word> getMoreSpecific() {
        return moreSpecific;
    }

    public void setMoreSpecific(List<Word> moreSpecific) {
        this.moreSpecific = moreSpecific;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
