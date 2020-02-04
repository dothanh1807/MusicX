package com.example.exercise2.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.exercise2.BaseUtils;
import com.example.exercise2.R;
import com.example.exercise2.view.ViewUtils;
import com.example.exercise2.model.entitys.Song;
import com.example.exercise2.listner.BaseControlListner;
import com.example.exercise2.listner.ClickItemListener;
import com.example.exercise2.listner.TouchItemListener;
import com.example.exercise2.view.activity.BaseActivity;
import com.example.exercise2.view.fragment.FragmentListSong;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> implements Filterable, ClickItemListener, TouchItemListener {

    private Context mContext;

    private List<Song> mListSong;

    private String mSearchString = "";

    private List<Song> mListSongSearch;

    private LayoutInflater mLayoutInflater;

    private int mColor;

    private String mNameSongCurrent;

    private BaseControlListner mFragmentCallBack;

    private BaseActivity mActivityCallback;

    public SongAdapter(BaseControlListner baseControlListner, List<Song> listSong) {
        mFragmentCallBack = baseControlListner;
        mListSong = listSong;
        mListSongSearch = listSong;
        mColor = Color.WHITE;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(mContext == null){
            mContext = viewGroup.getContext();
            mActivityCallback = (BaseActivity) mContext;
            mLayoutInflater = LayoutInflater.from(mContext);
        }
        View songView = mLayoutInflater.inflate(R.layout.item_song, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(songView, mColor);
        viewHolder.setmClickItemListener(this);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        Song song = mListSong.get(i);
        viewHolder.mTvNumberSong.setText(String.valueOf(i + 1));
        viewHolder.mTvNameSong.setText(ViewUtils.highlightText(mSearchString, song.getmNameSong()));
        viewHolder.mTvTimeSong.setText(BaseUtils.FORMAT_TIME_SONG.format(Integer.parseInt(song.getmTimeSong())));
        if(song.getmImageSong() != null){
            viewHolder.mImgSong.setImageURI(Uri.parse(song.getmImageSong()));
            viewHolder.mImgSong.setColorFilter(null);
        }else {
            viewHolder.mImgSong.setImageResource(ViewUtils.IMAGE_DEFAULT);
            viewHolder.mImgSong.setColorFilter(mColor);
        }

        // Cập nhật lại màu item vừa click
        if(mListSong.get(i).getmNameSong().equals( mNameSongCurrent)){
            viewHolder.mTvNumberSong.setTextColor(mContext.getResources().getColor(R.color.hightLight));
            viewHolder.mTvNameSong.setTextColor(mContext.getResources().getColor(R.color.hightLight));
            viewHolder.mTvTimeSong.setTextColor(mContext.getResources().getColor(R.color.hightLight));
            if(song.getmImageSong() == null){
                viewHolder.mImgSong.setColorFilter(mContext.getResources().getColor(R.color.hightLight));
            }
        }else {
            viewHolder.mTvNumberSong.setTextColor(mColor);
            viewHolder.mTvNameSong.setTextColor(mColor);
            viewHolder.mTvTimeSong.setTextColor(mColor);
        }
    }

    @Override
    public int getItemCount() {
        return mListSong.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                mSearchString = charSequence.toString();
                FilterResults results = new FilterResults();
                List<Song> listFilter = new ArrayList<Song>();

                if ( charSequence == null || charSequence.length() == 0) {
                    results.values = mListSongSearch;
                } else {
                    charSequence = charSequence.toString().toLowerCase();
                    String nameSong = null;
                    for (Song song : mListSongSearch) {
                        nameSong = song.getmNameSong();
                        if ( nameSong.toLowerCase().contains( charSequence.toString())) {
                            listFilter.add( song);
                        }
                    }
                    results.values = listFilter;
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results) {
                mListSong = (List<Song>) results.values;
                notifyDataSetChanged();
            }
        };

        return filter;
    }

    @Override
    public void onClick(View view, int positon) {
        mFragmentCallBack.createSong(mListSong.get(positon));
        mNameSongCurrent = mListSong.get(positon).getmNameSong();
        notifyDataSetChanged();
    }

    @Override
    public void onLongClick(View view, int positon) {
        view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.longclick_animations));
    }

    @Override
    public void onMove(int oldPosition, int newPosition) {
        if (oldPosition < newPosition) {
            for (int i = oldPosition; i < newPosition; i++) {
                Collections.swap(mListSong, i, i + 1);
            }
        } else {
            for (int i = oldPosition; i > newPosition; i--) {
                Collections.swap(mListSong, i, i - 1);
            }
        }
        notifyItemMoved(oldPosition, newPosition);
    }

    @Override
    public void onSwipe(int position, int direction) {
        mListSong.remove(position);
        notifyItemRemoved(position);
        getNotifiDataSetChanged();
        mActivityCallback.delete(mListSong.get(position));
    }

    @Override
    public void onMoveCompleted() {
        getNotifiDataSetChanged();
    }

    ///////////////////////////////// Method //////////////////////////////////////////

    private void getNotifiDataSetChanged(){
        /**
         * Để cập nhật lại số thứ tự bài hát, nhưng phải postDelay để giữ đc animation
         * của method notifyItemRemoved(position) ở trên.
         */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        }, ViewUtils.TIME_POST_DELAY);
        mActivityCallback.synListSongsChanged(mListSong);

    }

    public void setmNameSongCurrent(String mNameSongCurrent) {
        this.mNameSongCurrent = mNameSongCurrent;
    }

    public void setmListSong(List<Song> mListSong) {
        this.mListSong = mListSong;
    }

    public String getmNameSongCurrent() {
        return mNameSongCurrent;
    }

    public void setmColor(int mColor) {
        this.mColor = mColor;
    }

    /////////////////////////////// ViewHolder //////////////////////////////////////

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView mTvNumberSong;

        private TextView mTvNameSong;

        private TextView mTvTimeSong;

        private ImageView mImgSong;

        private RelativeLayout mItem;

        public ClickItemListener mClickItemListener;

        private ViewHolder(@NonNull View itemView, int color) {
            super(itemView);
            mTvNumberSong = (TextView) itemView.findViewById(R.id.tvNumberSong);
            mTvNameSong = (TextView) itemView.findViewById(R.id.tvNameSong);
            mTvTimeSong = (TextView) itemView.findViewById(R.id.tvTimeSong);
            mImgSong = (ImageView) itemView.findViewById(R.id.imgSong);
            mItem = itemView.findViewById(R.id.infoItem);

            mTvNumberSong.setTextColor(color);
            mTvNameSong.setTextColor(color);
            mTvTimeSong.setTextColor(color);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickItemListener.onClick(v, getAdapterPosition());
        }

        public void setmClickItemListener(ClickItemListener mClickItemListener) {
            this.mClickItemListener = mClickItemListener;
        }

        @Override
        public boolean onLongClick(View v) {
            mClickItemListener.onLongClick(v, getAdapterPosition());
            return true;
        }
    }

}
