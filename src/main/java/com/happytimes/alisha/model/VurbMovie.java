package com.happytimes.alisha.model;

/**
 * Created by alishaalam on 2/3/16.
 */
public class VurbMovie extends Card{

    public String movieExtraImageURL;

    public String getMovieExtraImageURL() {
        return movieExtraImageURL;
    }

    public void setMovieExtraImageURL(String movieExtraImageURL) {
        this.movieExtraImageURL = movieExtraImageURL;
    }

    @Override
    public String toString() {
        return "VurbMovie{" +
                ", title='" + this.getTitle() + '\'' +
                ", imageUrl=" + this.getImageURL() +
                ", movieExtraImageURL='" + movieExtraImageURL + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VurbMovie)) return false;
        if (!super.equals(o)) return false;

        VurbMovie vurbMovie = (VurbMovie) o;

        return getMovieExtraImageURL().equals(vurbMovie.getMovieExtraImageURL());

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getMovieExtraImageURL().hashCode();
        return result;
    }
}
