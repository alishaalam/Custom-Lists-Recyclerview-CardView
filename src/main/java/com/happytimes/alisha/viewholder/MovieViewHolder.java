package com.happytimes.alisha.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.happytimes.alisha.vurbalicious.R;

/**
 * Created by alishaalam on 2/3/16.
 */
public class MovieViewHolder extends RecyclerView.ViewHolder {

    protected TextView title;
    protected NetworkImageView imageURL;
    protected NetworkImageView movieExtraImageURL;

    public MovieViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        imageURL = (NetworkImageView) itemView.findViewById(R.id.thumbnail);
        movieExtraImageURL = (NetworkImageView) itemView.findViewById(R.id.movieExtraImg);
    }

    public TextView getTitle() {
        return title;
    }

    public NetworkImageView getImageURL() {
        return imageURL;
    }

    public NetworkImageView getMovieExtraImageURL() {
        return movieExtraImageURL;
    }

}
