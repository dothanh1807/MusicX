package com.example.exercise2.view.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.exercise2.BaseUtils;
import com.example.exercise2.R;
import com.example.exercise2.model.entitys.Song;
import com.example.exercise2.services.SongService;
import com.example.exercise2.services.WidgetService;
import com.example.exercise2.view.activity.HomeActivity;

public class MusicAppWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int appWidgetId : appWidgetIds){
            Log.i("XXX", "onUpdate " + appWidgetId);

            Intent imageIntent = new Intent(context, HomeActivity.class);
            PendingIntent imagePendingIntent = PendingIntent.getActivity(context, 0, imageIntent, 0);

            Intent serviceIntent = new Intent(context, WidgetService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

            Intent clickListViewIntent = new Intent(context, MusicAppWidgetProvider.class);
            clickListViewIntent.setAction(BaseUtils.ACTION_CREATE_SONG);
            PendingIntent clickListViewPendingIntent = PendingIntent.getBroadcast(context, 0, clickListViewIntent, 0);

            Intent clickStartIntent = new Intent(context, MusicAppWidgetProvider.class);
            clickStartIntent.setAction(BaseUtils.ACTION_START_SONG);
            PendingIntent clickStartPendingIntent = PendingIntent.getBroadcast(context, 0, clickStartIntent, 0);

            Intent clickPauseIntent = new Intent(context, MusicAppWidgetProvider.class);
            clickPauseIntent.setAction(BaseUtils.ACTION_PAUSE_SONG);
            PendingIntent clickPausePendingIntent = PendingIntent.getBroadcast(context, 0, clickPauseIntent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.music_widget);
            views.setOnClickPendingIntent(R.id.imgSongWidget, imagePendingIntent);
            views.setRemoteAdapter(R.id.listViewWidget, serviceIntent);
            views.setPendingIntentTemplate(R.id.listViewWidget, clickListViewPendingIntent);
            views.setOnClickPendingIntent(R.id.imgStartWidget, clickStartPendingIntent);
            views.setOnClickPendingIntent(R.id.imgPauseWidget, clickPausePendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.listViewWidget);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.music_widget);

        resizeWidget(newOptions, views);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.music_widget);
        switch ( intent.getAction()){
            case BaseUtils.ACTION_CREATE_SONG:{
                Log.i("XXX", "MusicAppWidgetProvider onReceive ACTION_CREATE_SONG");
                Song song = intent.getParcelableExtra(BaseUtils.EXTRA_ITEM_POSITION);
                Toast.makeText(context, song.getmNameSong() + "", Toast.LENGTH_SHORT).show();

                Bitmap bitmap = BitmapFactory.decodeFile(song.getmImageSong());
                views.setImageViewBitmap(R.id.imgSongWidget, bitmap);

                context.sendBroadcast(new Intent(BaseUtils.ACTION_PAUSE_SONG));

                int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                appWidgetManager.updateAppWidget(appWidgetId, views);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.listViewWidget);
                break;
            }
            case BaseUtils.ACTION_START_SONG:{
                Log.i("XXX", "MusicAppWidgetProvider onReceive ACTION_START_SONG");
                break;
            }
            case BaseUtils.ACTION_PAUSE_SONG: {
                Log.i("XXX", "MusicAppWidgetProvider onReceive ACTION_PAUSE_SONG");
                break;
            }
        }
    }

    private void resizeWidget(Bundle appWidgetOptions, RemoteViews views){
        int minWidth = appWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        int maxWidth = appWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
        int minHeight = appWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
        int maxHeight = appWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);

        if(maxHeight > 100){
            views.setViewVisibility(R.id.listViewWidget, View.VISIBLE);
        }else {
            views.setViewVisibility(R.id.listViewWidget, View.GONE);
        }
        if(maxWidth > 400){
            views.setViewVisibility(R.id.image_nonmix, View.VISIBLE);
            views.setViewVisibility(R.id.image_mix, View.VISIBLE);
            views.setViewVisibility(R.id.image_nonloop, View.VISIBLE);
            views.setViewVisibility(R.id.image_loop, View.VISIBLE);
        }else {
            views.setViewVisibility(R.id.image_nonmix, View.GONE);
            views.setViewVisibility(R.id.image_mix, View.GONE);
            views.setViewVisibility(R.id.image_nonloop, View.GONE);
            views.setViewVisibility(R.id.image_loop, View.GONE);
        }
    }

}
