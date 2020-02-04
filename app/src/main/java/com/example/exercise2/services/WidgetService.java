package com.example.exercise2.services;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.exercise2.BaseUtils;
import com.example.exercise2.R;
import com.example.exercise2.model.database.DataSongProvider;
import com.example.exercise2.model.entitys.Song;
import com.example.exercise2.view.ViewUtils;

import java.util.List;

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetItemFactory(getApplicationContext(), intent);
    }

    class WidgetItemFactory implements RemoteViewsFactory{

        private Context mContext;

        private int mAppWidgetId;

        private List<Song> mListSongs;

        private String mNameCurrentSong;

        public WidgetItemFactory(Context context, Intent intent){
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            Log.i("XXX", "onCreate");
            DataSongProvider.SongDataBase songDataBase = new DataSongProvider.SongDataBase(mContext);
            mListSongs = songDataBase.getAllSong(null);
        }

        @Override
        public void onDataSetChanged() {
            Log.i("XXX", "onDataSetChanged");
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return mListSongs.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.item_song);
            views.setTextViewText(R.id.tvNumberSong, String.valueOf(position + 1));
            views.setTextViewText(R.id.tvNameSong, mListSongs.get(position).getmNameSong());
            views.setTextViewText(R.id.tvTimeSong, mListSongs.get(position).getmNameAuthor());
            if(mListSongs.get(position).getmImageSong() != null){
                Bitmap bitmap = BitmapFactory.decodeFile(mListSongs.get(position).getmImageSong());
                views.setImageViewBitmap(R.id.imgSong, bitmap);
            }else {
                views.setViewVisibility(R.id.imgSong, View.INVISIBLE);
                views.setViewVisibility(R.id.imgSongWidget, View.VISIBLE);
                views.setImageViewResource(R.id.imgSongWidget, ViewUtils.IMAGE_DEFAULT);
            }

            Intent fillIntent = new Intent();
            fillIntent.putExtra(BaseUtils.EXTRA_ITEM_POSITION, mListSongs.get(position));
            views.setOnClickFillInIntent(R.id.infoItem, fillIntent);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return Long.parseLong(mListSongs.get(position).getmIDSong());
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

}
