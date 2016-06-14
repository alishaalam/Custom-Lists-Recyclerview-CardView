package com.happytimes.alisha.helper;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.happytimes.alisha.vurbalicious.RecyclerCardAdapter;

/**
 * Created by alishaalam on 2/4/16.
 */
public class CardTouchHelper extends ItemTouchHelper.SimpleCallback {

    RecyclerCardAdapter mRecyclerCardAdapter;

    public CardTouchHelper(RecyclerCardAdapter mRecyclerCardAdapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN,  ItemTouchHelper.RIGHT);
        this.mRecyclerCardAdapter = mRecyclerCardAdapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mRecyclerCardAdapter.swap(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mRecyclerCardAdapter.remove(viewHolder.getAdapterPosition());

    }
}
