package com.happytimes.alisha.model;

/**
 * Created by alishaalam on 2/3/16.
 */
public class VurbMusic extends Card {

    private String musicVideoURL;

    public String getMusicVideoURL() {
        return musicVideoURL;
    }

    public void setMusicVideoURL(String musicVideoURL) {
        this.musicVideoURL = musicVideoURL;
    }

    @Override
    public String toString() {
        return "VurbMusic{" +
                ", title='" + this.getTitle() + '\'' +
                ", imageUrl=" + this.getImageURL() +
                ", musicVideoURL=" + musicVideoURL +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VurbMusic)) return false;
        if (!super.equals(o)) return false;

        VurbMusic vurbMusic = (VurbMusic) o;

        return getMusicVideoURL().equals(vurbMusic.getMusicVideoURL());

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getMusicVideoURL().hashCode();
        return result;
    }
}
