package com.example.exercise2.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.example.exercise2.BaseUtils;
import com.example.exercise2.listner.DetailsControlListener;
import com.example.exercise2.model.entitys.Song;
import com.example.exercise2.view.customizeview.CustomizeNotifications;
import java.io.IOException;
import java.util.List;

public class SongService extends Service implements DetailsControlListener, MediaPlayer.OnCompletionListener {

    private List<Song> mListSong;

    private IBinder mIBinder;

    private MediaPlayer mMediaPlayer;

    private NotificationManager mNotificationManager;

    private BroadcastReceiver mBroadcastReceiver;

    private CustomizeNotifications mCustomizeNotifications;

    private Song mSongCurrent;

    private int mSongPosition;

    public class SongBinder extends Binder {

        public SongService getServices(){

            return SongService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("XXX", "onCreate SongService");
        mIBinder = new SongBinder();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(this);
        mCustomizeNotifications = new CustomizeNotifications(this);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        IntentFilter filter = new IntentFilter();
        filter.addAction(BaseUtils.ACTION_PAUSE_SONG);
        filter.addAction(BaseUtils.ACTION_START_SONG);
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch ( intent.getAction()){
                    case BaseUtils.ACTION_PAUSE_SONG: {
                        Log.i("XXX", "SongService onReceive ACTION_PAUSE_SONG");
                        pauseSong();
                        break;
                    }
                    case BaseUtils.ACTION_START_SONG:{
                        Log.i("XXX", "SongService onReceive ACTION_START_SONG");
                        startSong();
                        break;
                    }
                }
            }
        };
        registerReceiver(mBroadcastReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("XXX", "onStartCommand SongService");
        if(intent != null){
            mListSong = intent.getParcelableArrayListExtra(BaseUtils.KEY_INTENT);
            Log.i("XXX", "mListSong: " + mListSong.size());
            Song song = intent.getParcelableExtra(BaseUtils.EXTRA_ITEM_POSITION);
            if(song != null){
                createSong(song);
            }
        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("XXX", "onBind SongService");
        return mIBinder;
    }

    @Override public boolean onUnbind(Intent intent) {
        Log.i("XXX", "onUnbind SongService");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    /////////////////////////////// Method //////////////////////////////////////////////

    private Notification mNotificationRun;

    private Notification mNotificationPause;

    @Override
    public void createSong(Song song) {
        mSongCurrent = song;
        synSongPosition();
        if( mMediaPlayer != null){
            mMediaPlayer.reset();
        }
        try {
            mMediaPlayer.setDataSource(mSongCurrent.getmPathSong());
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();

        mNotificationRun = mCustomizeNotifications.notificationRun(mSongCurrent);
        startForeground(BaseUtils.ID_NOTICATION, mNotificationRun);
    }

    @Override
    public void startSong() {
        if(mMediaPlayer != null && mSongCurrent != null){
            mMediaPlayer.start();
            mNotificationRun = mCustomizeNotifications.notificationRun(mSongCurrent);
            mNotificationManager.notify( BaseUtils.ID_NOTICATION, mNotificationRun);
            startForeground(BaseUtils.ID_NOTICATION, mNotificationRun);
        }
    }

    @Override
    public void pauseSong() {
        if(mMediaPlayer != null && mSongCurrent != null){
            mMediaPlayer.pause();
            if(mNotificationPause == null){
                mNotificationPause = mCustomizeNotifications.notificationPause(mSongCurrent);
            }
            mNotificationManager.notify( BaseUtils.ID_NOTICATION, mNotificationPause);
            stopForeground(false);
        }
    }

    @Override
    public void nextSong() {

    }

    @Override
    public void backSong() {

    }

    @Override
    public void playTo(int position) {
        mMediaPlayer.seekTo(position);
    }

    @Override
    public int getTimeCurrentSong() {
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public int getTimeTotalSong() {
        return mMediaPlayer.getDuration();
    }

    public Song getmSongCurrent() {
        return mSongCurrent;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void synListSongsChanged(List<Song> listSong){
        mListSong = listSong;
        synSongPosition();
    }

    private void synSongPosition(){
        if(mSongCurrent != null){
            int sizeList = mListSong.size();
            for(int i = 0; i < sizeList; i++){
                if(mSongCurrent.getmNameSong().equals(mListSong.get(i).getmNameSong())){
                    mSongPosition = i;
                }
            }
        }
    }
}
