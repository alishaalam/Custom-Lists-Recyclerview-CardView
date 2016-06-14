package com.happytimes.alisha.vurbalicious;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.happytimes.alisha.helper.AppController;
import com.happytimes.alisha.model.Card;
import com.happytimes.alisha.model.VurbMovie;
import com.happytimes.alisha.model.VurbMusic;
import com.happytimes.alisha.model.VurbPlace;
import com.happytimes.alisha.viewholder.MovieViewHolder;
import com.happytimes.alisha.viewholder.MusicViewHolder;
import com.happytimes.alisha.viewholder.PlaceViewHolder;
import com.happytimes.alisha.viewholder.ProgressViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by alishaalam on 2/3/16.
 */
public class RecyclerCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = RecyclerCardAdapter.class.getSimpleName();
    private List<Card> cardsList = new ArrayList<>();
    ImageLoader mImageLoader = AppController.getInstance().getImageLoader();
    Context context;

    //Constants to help with ViewType
    public static final int VIEW_PROG = 0; //Represents end of the list
    public static final int PLACE = 1, MOVIE = 2, MUSIC = 3; //Represent object types in the list

    // The minimum amount of items to have below your current scroll position before loading more.
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public RecyclerCardAdapter() { super(); }

    //This contructor is needed when adapter is initialised for normal view without scrolling
    public RecyclerCardAdapter(List<Card> cardsList) {
        this.cardsList = cardsList;
    }

    public RecyclerCardAdapter(List<Card> list, RecyclerView recyclerView) {
        cardsList = list;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    /**
     * Returns the view type of the item at position for view recycling**/
    @Override
    public int getItemViewType(int position) {
        if(cardsList.get(position) == null) {
            return VIEW_PROG;
        } else {
            if (cardsList.get(position) instanceof VurbPlace) {
                return PLACE;
            } else if (cardsList.get(position) instanceof VurbMovie) {
                return MOVIE;
            } else if (cardsList.get(position) instanceof VurbMusic) {
                return MUSIC;
            }
        }
        return -1;
    }

    /**
     * Creates different RecyclerView.ViewHolder objects based on the item view type.
     * **/
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            switch (viewType) {
                case VIEW_PROG:
                    View v0 = inflater.inflate(R.layout.progressbar_item, parent, false);
                    viewHolder = new ProgressViewHolder(v0);
                    break;
                case PLACE:
                    View v1 = inflater.inflate(R.layout.place_card_layout, parent, false);
                    viewHolder = new PlaceViewHolder(v1);
                    break;
                case MOVIE:
                    View v2 = inflater.inflate(R.layout.movie_card_layout, parent, false);
                    viewHolder = new MovieViewHolder(v2);
                    break;
                case MUSIC:
                    View v3 = inflater.inflate(R.layout.music_card_layout, parent, false);
                    viewHolder = new MusicViewHolder(v3);
                    break;
                default:
                    View v = inflater.inflate(R.layout.default_card_layout, parent, false);
                    viewHolder = new PlaceViewHolder(v);
                    break;
            }
        //}
        return viewHolder;
    }

    /**
     * This method is used to configure the ViewHolder with different types data that needs to be displayed. */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case VIEW_PROG:
                ((ProgressViewHolder) viewHolder).progressBar.setIndeterminate(true);
                break;
            case PLACE:
                PlaceViewHolder placeVH = (PlaceViewHolder) viewHolder;
                configurePlaceViewHolder(placeVH, position);
                break;
            case MOVIE:
                MovieViewHolder movieVH = (MovieViewHolder) viewHolder;
                configureMovieViewHolder(movieVH, position);
                break;
            case MUSIC:
                MusicViewHolder musicVH = (MusicViewHolder) viewHolder;
                configureMusicViewHolder(musicVH, position);
                break;
        }

    }



    @Override
    public int getItemCount() {
        return cardsList.size();
    }


    private void configurePlaceViewHolder(PlaceViewHolder holder, int position) {
        VurbPlace vurbPlace = (VurbPlace) cardsList.get(position);
        if (mImageLoader == null)
            mImageLoader = AppController.getInstance().getImageLoader();

        if (vurbPlace != null) {
            holder.getTitle().setText(vurbPlace.getTitle());
            holder.getImageURL().setImageUrl(vurbPlace.getImageURL(), mImageLoader);
            holder.getPlaceCategory().setText("Category: " + vurbPlace.getPlaceCategory());
        }
    }

    private void configureMovieViewHolder(MovieViewHolder holder, int position) {
        VurbMovie vurbMovie = (VurbMovie) cardsList.get(position);
        if (mImageLoader == null)
            mImageLoader = AppController.getInstance().getImageLoader();


        if (vurbMovie != null) {
            holder.getTitle().setText(vurbMovie.getTitle());
            holder.getImageURL().setImageUrl(vurbMovie.getImageURL(), mImageLoader);
            holder.getMovieExtraImageURL().setImageUrl(vurbMovie.getMovieExtraImageURL(), mImageLoader);
        }
    }

    private void configureMusicViewHolder(MusicViewHolder holder, int position) {
        final VurbMusic vurbMusic = (VurbMusic) cardsList.get(position);
        if (mImageLoader == null)
            mImageLoader = AppController.getInstance().getImageLoader();


        if (vurbMusic != null) {
            holder.getTitle().setText(vurbMusic.getTitle());
            holder.getImageURL().setImageUrl(vurbMusic.getImageURL(), mImageLoader);
            holder.getMusicExtraImageURL().setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent playMusic = new Intent(Intent.ACTION_VIEW);
                    playMusic.setData(Uri.parse(vurbMusic.getMusicVideoURL()));
                    context.startActivity(playMusic);
                }
            });

        }
    }

    /**Called when card is swiped right to dismiss*/
    public void remove(int position) {
        cardsList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Called when cards are dragged to reorder*/
    public void swap(int orgPosition, int targetPosition){
        Collections.swap(cardsList, orgPosition, targetPosition);
        notifyItemMoved(orgPosition, targetPosition);
    }

    /**Methods for infinite scrolling*/

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
