package com.example.exercise2.view.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.example.exercise2.BaseUtils;
import com.example.exercise2.R;
import com.example.exercise2.adapter.SectionsFragmentAdapter;
import com.example.exercise2.model.entitys.Song;
import com.example.exercise2.services.SongService;
import com.example.exercise2.view.ViewUtils;
import com.example.exercise2.view.customizeview.PageTransformer;
import com.example.exercise2.view.fragment.FragmentDetailSong;
import com.example.exercise2.view.fragment.FragmentListSong;
import com.example.exercise2.view.fragment.FragmentLyrics;
import java.util.ArrayList;
import java.util.List;

public class SongDetailsActivity extends BaseActivity implements View.OnClickListener {

    public static final int OFF_PAGE_LIMIT = 2;

    private ViewPager mPager;

    private TabLayout mTabLayout;

    private List<Song> mListSong;

    private SectionsFragmentAdapter mSectionsFragmentAdapter;

    private FragmentListSong mFragmentListSong;

    private FragmentDetailSong mFragmentDetailSong;

    private FragmentLyrics mFragmentLyrics;

    private Intent mIntentService;

    private SongService mSongService;

    private ServiceConnection mServiceConnection;

    private boolean mIsBound;

    private boolean mStatusSong;

    private Song mCurrentSong;

    private int mSongPosition;

    private int mTimeCurrent;

    private Handler mHandlerSeekbar;

    private Runnable mRunnableSeekbar;

    @Override public void setTheme(int resid) {
        super.setTheme(resid);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_presenter);

        mPager = findViewById(R.id.viewPager);
        mTabLayout = findViewById(R.id.tabLayout);

        initViewPager(getIntent());
        mIntentService = new Intent(SongDetailsActivity.this, SongService.class);
        mIntentService.putParcelableArrayListExtra(BaseUtils.KEY_INTENT, (ArrayList<? extends Parcelable>) mListSong);
        mHandlerSeekbar = new Handler();
        mRunnableSeekbar = new Runnable() {
            @Override
            public void run() {
                if(mFragmentDetailSong != null){
                    mFragmentDetailSong.playTo(mSongService.getTimeCurrentSong());
                    mHandlerSeekbar.postDelayed(this, ViewUtils.TIME_HANDLER_SEEKBAR);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                SongService.SongBinder songBinder = (SongService.SongBinder) service;
                mSongService = songBinder.getServices();
                mIsBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mIsBound = false;
            }
        };
        bindService(mIntentService, mServiceConnection, Context.BIND_NOT_FOREGROUND);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        mTimeCurrent = mSongService.getTimeCurrentSong();

        intent.putExtra(BaseUtils.KEY_SONGS, mCurrentSong);
        intent.putExtra(BaseUtils.KEY_STATUS_SONGS, mStatusSong);
        intent.putExtra(BaseUtils.KEY_TIME_CURRENT_SONG, mTimeCurrent);

        setResult(BaseUtils.RESULT_CODE, intent);
        finish();
        overridePendingTransition( R.anim.slide_stay , R.anim.slide_out_up);

        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mSongService != null && mCurrentSong != null){
            SharedPreferences sharedPreferences = getSharedPreferences(BaseUtils.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(BaseUtils.KEY_SONGS, mCurrentSong.getmNameSong());
            editor.putBoolean(BaseUtils.KEY_STATUS_SONGS, mStatusSong);
            if(mSongService.getTimeCurrentSong() > mTimeCurrent){
                mTimeCurrent = mSongService.getTimeCurrentSong();
            }
            editor.putInt(BaseUtils.KEY_TIME_CURRENT_SONG, mTimeCurrent);
            editor.commit();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mIsBound | mServiceConnection != null) {
            unbindService(mServiceConnection);
            mIsBound = false;
        }
        mHandlerSeekbar.removeCallbacks(mRunnableSeekbar);
    }

    //////////////////////////////////// Method ////////////////////////////////////////////

    @Override
    public void createSong(Song song) {
        super.createSong(song);
        mStatusSong = true;
        mCurrentSong = song;
        mFragmentDetailSong.createSong(mCurrentSong);
        mFragmentLyrics.createSong(mCurrentSong);
        mSongService.createSong(mCurrentSong);
        mHandlerSeekbar.post(mRunnableSeekbar);
        updateSongPosition();
    }

    @Override
    public void startSong() {
        super.startSong();
        mHandlerSeekbar.post(mRunnableSeekbar);
    }

    @Override
    public void playTo(int position) {
        super.playTo(position);
        mSongService.playTo(position);
    }

    @Override
    public void synListSongsChanged(List<Song> listSong) {
        super.synListSongsChanged(listSong);
        mListSong = listSong;
        mSongService.synListSongsChanged(listSong);
        updateSongPosition();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgStart:{
                if(mSongService.getmSongCurrent() == null){
                    mSongService.createSong(mCurrentSong);
                    mSongService.playTo(mTimeCurrent);
                }
                mStatusSong = true;
                mSongService.startSong();
                mFragmentDetailSong.startSong();
                mHandlerSeekbar.post(mRunnableSeekbar);
                break;
            }
            case R.id.imgPause:{
                mStatusSong = false;
                mSongService.pauseSong();
                mFragmentDetailSong.pauseSong();
                mHandlerSeekbar.removeCallbacks(mRunnableSeekbar);
                break;
            }
            case R.id.imgNext:{
                mHandlerSeekbar.removeCallbacks(mRunnableSeekbar);
                ++mSongPosition;
                createSong(mListSong.get(mSongPosition));
                mFragmentListSong.getmSongAdapter().setmNameSongCurrent(mCurrentSong.getmNameSong());
                mFragmentListSong.getmSongAdapter().notifyDataSetChanged();
                break;
            }
            case R.id.imgBack:{
                mHandlerSeekbar.removeCallbacks(mRunnableSeekbar);
                --mSongPosition;
                createSong(mListSong.get(mSongPosition));
                mFragmentListSong.getmSongAdapter().setmNameSongCurrent(mCurrentSong.getmNameSong());
                mFragmentListSong.getmSongAdapter().notifyDataSetChanged();
                break;
            }
        }
    }

    private boolean initViewPager(Intent intent){
        mSectionsFragmentAdapter = new SectionsFragmentAdapter(getSupportFragmentManager());

        if(intent != null){
            mListSong = intent.getParcelableArrayListExtra(BaseUtils.KEY_LIST_SONGS);
            mCurrentSong = intent.getParcelableExtra(BaseUtils.KEY_SONGS);
            mStatusSong = intent.getBooleanExtra(BaseUtils.KEY_STATUS_SONGS, false);
            mTimeCurrent = intent.getIntExtra(BaseUtils.KEY_TIME_CURRENT_SONG, 0);
            updateSongPosition();
        }

        mFragmentListSong = FragmentListSong.newInstance((ArrayList<Song>) mListSong, mCurrentSong);
        mSectionsFragmentAdapter.addFragment(mFragmentListSong);

        mFragmentDetailSong = FragmentDetailSong.newInstance(mCurrentSong, mStatusSong, mTimeCurrent);
        mSectionsFragmentAdapter.addFragment(mFragmentDetailSong);

        mFragmentLyrics = FragmentLyrics.newInstance(mCurrentSong);
        mSectionsFragmentAdapter.addFragment(mFragmentLyrics);

        mPager.setAdapter(mSectionsFragmentAdapter);
        mTabLayout.setupWithViewPager(mPager);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        mPager.setPageTransformer(true, new PageTransformer());
        mPager.setOffscreenPageLimit(OFF_PAGE_LIMIT);
        mPager.setCurrentItem(BaseUtils.TAB_DEFAULT);

        return true;
    }

    private void updateSongPosition(){
        int sizeList = mListSong.size();
        for(int i = 0; i < sizeList; i++){
            if(mCurrentSong.getmNameSong().equals(mListSong.get(i).getmNameSong())){
                mSongPosition = i;
            }
        }
    }

}
