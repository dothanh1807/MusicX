package com.example.exercise2.view.customizeview;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.example.exercise2.BaseUtils;
import com.example.exercise2.R;
import com.example.exercise2.view.activity.HomeActivity;
import com.example.exercise2.model.entitys.Song;
import com.example.exercise2.listner.SongReceiver;

public class CustomizeNotifications {

    private final String START = "START";

    private final String PAUSE = "PAUSE";

    private final String NEXT = "NEXT";

    private final String BACK = "BACK";

    private Context mContext;

    public CustomizeNotifications(Context mContext) {
        this.mContext = mContext;
    }

    public Notification notificationRun(Song song){
        Intent intent = new Intent(mContext, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntentPauseMusic = PendingIntent.getBroadcast(mContext,0,
                new Intent(mContext, SongReceiver.class).putExtra(BaseUtils.KEY_INTENT, BaseUtils.ACTION_PAUSE_SONG), 0);
        PendingIntent pendingIntentNextMusic = PendingIntent.getBroadcast(mContext,0,
                new Intent(mContext, SongReceiver.class).putExtra(BaseUtils.KEY_INTENT, BaseUtils.ACTION_NEXT_SONG), 0);
        PendingIntent pendingIntentBackMusic = PendingIntent.getBroadcast(mContext,0,
                new Intent(mContext, SongReceiver.class).putExtra(BaseUtils.KEY_INTENT, BaseUtils.ACTION_BACK_SONG), 0);

        Notification.Builder notificationBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder =  new Notification.Builder(mContext, BaseUtils.CHANNEL_ID)
                    .setAutoCancel(true)
                    .setContentIntent( pendingIntent)
                    .addAction( R.drawable.s_icon_back, BACK, pendingIntentBackMusic)
                    .addAction( R.drawable.s_icon_pause, PAUSE, pendingIntentPauseMusic)
                    .addAction( R.drawable.s_icon_next, NEXT, pendingIntentNextMusic)
                    .setSmallIcon(R.drawable.image_default)
                    .setSubText(mContext.getResources().getString(R.string.time) + " " +  BaseUtils.FORMAT_TIME_SONG.format(Integer.parseInt(song.getmTimeSong())))
                    .setContentTitle(song.getmNameSong())
                    .setColor( mContext.getResources().getColor(R.color.hightLight))
                    .setContentText(song.getmNameAuthor() )
                    .setChannelId(BaseUtils.CHANNEL_ID)
                    .setStyle( new Notification.MediaStyle());
        }else {
            notificationBuilder = new Notification.Builder(mContext)
                    .setAutoCancel(true)
                    .setContentIntent( pendingIntent)
                    .addAction( R.drawable.s_icon_back, BACK, pendingIntentBackMusic)
                    .addAction( R.drawable.s_icon_pause, PAUSE, pendingIntentPauseMusic)
                    .addAction( R.drawable.s_icon_next, NEXT, pendingIntentNextMusic)
                    .setSmallIcon(R.drawable.image_default)
                    .setSubText(mContext.getResources().getString(R.string.time) + " " +  BaseUtils.FORMAT_TIME_SONG.format(Integer.parseInt(song.getmTimeSong())))
                    .setContentTitle(song.getmNameSong())
                    .setColor( mContext.getResources().getColor(R.color.hightLight))
                    .setContentText(song.getmNameAuthor())
                    .setStyle( new Notification.MediaStyle());
        }
        Bitmap bitmapDefault = null;
        if(song.getmImageSong() != null){
            bitmapDefault = BitmapFactory.decodeFile(song.getmImageSong());
        }else {
            bitmapDefault = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.image_default);
        }
        notificationBuilder.setLargeIcon(bitmapDefault);

        Notification notification = notificationBuilder.build();

        return notification;
    }

    public Notification notificationPause(Song song){
        Intent intent = new Intent(mContext, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntentStartMusic = PendingIntent.getBroadcast(mContext,0,
                new Intent(mContext, SongReceiver.class).putExtra(BaseUtils.KEY_INTENT, BaseUtils.ACTION_START_SONG), 0);
        PendingIntent pendingIntentNextMusic = PendingIntent.getBroadcast(mContext,0,
                new Intent(mContext, SongReceiver.class).putExtra(BaseUtils.KEY_INTENT, BaseUtils.ACTION_NEXT_SONG), 0);
        PendingIntent pendingIntentBackMusic = PendingIntent.getBroadcast(mContext,0,
                new Intent(mContext, SongReceiver.class).putExtra(BaseUtils.KEY_INTENT, BaseUtils.ACTION_BACK_SONG), 0);

        Notification.Builder notificationBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder =  new Notification.Builder(mContext, BaseUtils.CHANNEL_ID)
                    .setAutoCancel(true)
                    .setContentIntent( pendingIntent)
                    .addAction( R.drawable.s_icon_back, BACK, pendingIntentBackMusic)
                    .addAction(R.drawable.s_icon_run, START, pendingIntentStartMusic)
                    .addAction( R.drawable.s_icon_next, NEXT, pendingIntentNextMusic)
                    .setSmallIcon(R.drawable.image_default)
                    .setSubText(mContext.getResources().getString(R.string.time) + " " + BaseUtils.FORMAT_TIME_SONG.format(Integer.parseInt(song.getmTimeSong())))
                    .setContentTitle(song.getmNameSong())
                    .setColor( mContext.getResources().getColor(R.color.hightLight))
                    .setContentText(song.getmNameAuthor() )
                    .setChannelId(BaseUtils.CHANNEL_ID)
                    .setStyle( new Notification.MediaStyle());
        }else {
            notificationBuilder = new Notification.Builder(mContext)
                    .setAutoCancel(true)
                    .setContentIntent( pendingIntent)
                    .addAction( R.drawable.s_icon_back, BACK, pendingIntentBackMusic)
                    .addAction( R.drawable.s_icon_run, START, pendingIntentStartMusic)
                    .addAction( R.drawable.s_icon_next, NEXT, pendingIntentNextMusic)
                    .setSmallIcon(R.drawable.image_default)
                    .setSubText(mContext.getResources().getString(R.string.time) + " " + BaseUtils.FORMAT_TIME_SONG.format(Integer.parseInt(song.getmTimeSong())))
                    .setContentTitle(song.getmNameSong())
                    .setColor( mContext.getResources().getColor(R.color.hightLight))
                    .setContentText(song.getmNameAuthor())
                    .setStyle( new Notification.MediaStyle());
        }
        Bitmap bitmapDefault = null;
        if(song.getmImageSong() != null){
            bitmapDefault = BitmapFactory.decodeFile(song.getmImageSong());
        }else {
            bitmapDefault = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.image_default);
        }
        notificationBuilder.setLargeIcon(bitmapDefault);

        Notification notification = notificationBuilder.build();

        return notification;
    }
}
