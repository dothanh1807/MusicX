package com.example.exercise2.listner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.exercise2.BaseUtils;

public class SongReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("XXX", "SongReceiver");
        if ( BaseUtils.ACTION_START_SONG.equals(intent.getStringExtra(BaseUtils.KEY_INTENT))) {
            Log.i("XXX", "SongReceiver ACTION_START_SONG");
            context.sendBroadcast(new Intent(BaseUtils.ACTION_START_SONG));
        }
        if ( BaseUtils.ACTION_PAUSE_SONG.equals(intent.getStringExtra(BaseUtils.KEY_INTENT))) {
            Log.i("XXX", "SongReceiver ACTION_PAUSE_SONG");
            context.sendBroadcast(new Intent(BaseUtils.ACTION_PAUSE_SONG));
        }
        if ( BaseUtils.ACTION_NEXT_SONG.equals(intent.getAction())) {
            context.sendBroadcast(new Intent(BaseUtils.ACTION_NEXT_SONG));
        }
        if ( BaseUtils.ACTION_BACK_SONG.equals(intent.getAction())) {
            context.sendBroadcast(new Intent(BaseUtils.ACTION_BACK_SONG));
        }
    }

}
