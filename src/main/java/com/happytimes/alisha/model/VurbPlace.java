package com.happytimes.alisha.model;

/**
 * Created by alishaalam on 2/3/16.
 */
public class VurbPlace extends Card{

    public VurbPlace() {
        super();
    }

    public VurbPlace(String placeCategory) {
        super();
        this.setTitle("Test");
        this.setImageURL("http://upload.wikimedia.org/wikipedia/commons/thumb/1/17/DowneyMcdonalds.jpg/240px-DowneyMcdonalds.jpg");
        this.placeCategory = placeCategory;
    }

    private String placeCategory;

    public String getPlaceCategory() {
        return placeCategory;
    }

    public void setPlaceCategory(String placeCategory) {
        this.placeCategory = placeCategory;
    }



    @Override
    public String toString() {
        return "VurbPlace{" +
                ", title='" + this.getTitle() + '\'' +
                ", imageUrl=" + this.getImageURL() +
                ", placeCategory='" + placeCategory + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VurbPlace)) return false;
        if (!super.equals(o)) return false;

        VurbPlace vurbPlace = (VurbPlace) o;

        return getPlaceCategory().equals(vurbPlace.getPlaceCategory());

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getPlaceCategory().hashCode();
        return result;
    }
}
