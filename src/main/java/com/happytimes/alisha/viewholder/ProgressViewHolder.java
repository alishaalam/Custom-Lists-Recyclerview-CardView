package com.happytimes.alisha.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.happytimes.alisha.vurbalicious.R;

/**
 * Created by alishaalam on 2/3/16.
 */
public class ProgressViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar progressBar;

    public ProgressViewHolder(View v) {
        super(v);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
    }
}