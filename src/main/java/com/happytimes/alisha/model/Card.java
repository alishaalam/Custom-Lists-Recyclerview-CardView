package com.happytimes.alisha.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Created by alishaalam on 2/3/16.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = VurbPlace.class, name = "place"),
        @JsonSubTypes.Type(value = VurbMovie.class, name = "movie"),
        @JsonSubTypes.Type(value = VurbMusic.class, name = "music"),
         })
public abstract class Card {

    public String title;
    public String imageURL;

    public Card(String title, String imageURL) {
        this.title = title;
        this.imageURL = imageURL;
    }

    public Card() {
        this.title = "title";
        this.imageURL = "imageURL";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "Card{" +
                ", title='" + title + '\'' +
                ", imageURL=" + imageURL +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;

        Card card = (Card) o;

        if (!getTitle().equals(card.getTitle())) return false;
        return getImageURL().equals(card.getImageURL());

    }

    @Override
    public int hashCode() {
        int result = getTitle().hashCode();
        result = 31 * result + getImageURL().hashCode();
        return result;
    }
}
