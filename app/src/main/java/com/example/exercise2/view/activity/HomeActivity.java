package com.example.exercise2.view.activity;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.example.exercise2.BaseUtils;
import com.example.exercise2.R;
import com.example.exercise2.adapter.SectionsFragmentAdapter;
import com.example.exercise2.listner.DetailsControlListener;
import com.example.exercise2.model.entitys.Song;
import com.example.exercise2.presenter.SongPresenter;
import com.example.exercise2.services.SongService;
import com.example.exercise2.view.customizeview.CustomizeAnimations;
import com.example.exercise2.view.customizeview.RipplePulseAnimation;
import com.example.exercise2.view.fragment.FragmentBarControlBase;
import com.example.exercise2.view.fragment.FragmentListSong;
import com.example.exercise2.view.fragment.FragmentSettings;
import com.example.exercise2.woker.DataBaseSynchronizer;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;

    private MenuItem mMenuItemSearch;

    private SearchView mSearchView;

    private NavigationView mNavigationView;

    private DrawerLayout mDrawer;

    private ViewPager mPager;

    private TabLayout mTabLayout;

    private SectionsFragmentAdapter mSectionsFragmentAdapter;

    private FragmentListSong mFragmentListSong;

    private FrameLayout mSpaceFrame;

    private boolean isBarVisible;

    private FragmentBarControlBase mFragmentBarControlBase;

    private List<Song> mListAllSong;

    private Intent mIntentService;

    private DetailsControlListener mSongService;

    private ServiceConnection mServiceConnection;

    private boolean mIsBound;

    private SharedPreferences mSharedPreferences;

    private SharedPreferences.Editor mEditor;

    private boolean mStatusSong;

    private Song mCurrentSong;

    private int mSongPosition;

    private int mTimeCurrent;

    private boolean mIsRestore;

    private SongPresenter mSongPresenter;

    private RipplePulseAnimation mRipplePulseFragment;

    @Override public void setTheme(int resid) {
        super.setTheme(resid);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mToolbar = findViewById(R.id.toolbar);
        mNavigationView = findViewById(R.id.navigation_albums);
        mDrawer = findViewById(R.id.drawer_layout);
        mPager = findViewById(R.id.viewPager);
        mTabLayout = findViewById(R.id.tabLayout);
        mSpaceFrame = findViewById(R.id.spaceBarControl);
        mRipplePulseFragment = findViewById(R.id.ripplepulse_fragment);

        instantiatePager();
        instantiateService();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    protected void onStart() {
        CustomizeAnimations.startRippleAnimationForHome(mRipplePulseFragment);
        bindService(mIntentService, mServiceConnection, Context.BIND_NOT_FOREGROUND);

        super.onStart();
    }

    @Override
    protected void onResume() {
        /**
         * Chạy asyntask để đồng bộ dữ liệu
         */
        new DataBaseSynchronizer(this).execute();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_music, menu);
        mMenuItemSearch = menu.findItem(R.id.menuSearchMusic);
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.menuSearchMusic).getActionView();
        mSearchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mFragmentListSong.getmSongAdapter().getFilter().filter(s);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSearchMusic:{
                if(mPager.getCurrentItem() != BaseUtils.TAB_DEFAULT_HOME){
                    mPager.setCurrentItem(BaseUtils.TAB_DEFAULT_HOME);
                    break;
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == BaseUtils.RESULT_CODE && data != null){
            mStatusSong = data.getBooleanExtra(BaseUtils.KEY_STATUS_SONGS, false);
            mTimeCurrent = data.getIntExtra(BaseUtils.KEY_TIME_CURRENT_SONG, 0);
            mCurrentSong = data.getParcelableExtra(BaseUtils.KEY_SONGS);

            instantiateBarControl(mCurrentSong);
            updateSongPosition();
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mSongService != null && mCurrentSong != null){
            mEditor.putString(BaseUtils.KEY_SONGS, mCurrentSong.getmNameSong());
            mEditor.putBoolean(BaseUtils.KEY_STATUS_SONGS, mStatusSong);
            if(mSongService.getTimeCurrentSong() > mTimeCurrent){
                mTimeCurrent = mSongService.getTimeCurrentSong();
            }
            mEditor.putInt(BaseUtils.KEY_TIME_CURRENT_SONG, mTimeCurrent);
            mEditor.commit();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if ( mIsBound | mServiceConnection != null) {
            this.unbindService(mServiceConnection);
            mIsBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSongPresenter.closeDataBase();
    }

    //////////////////////////////////////// Method //////////////////////////////////////////////////

    @Override
    public void createSong(Song song) {
        super.createSong(song);
        mStatusSong = true;
        mCurrentSong = song;
        mSongService.createSong(mCurrentSong);
        instantiateBarControl(mCurrentSong);
        updateSongPosition();
    }

    @Override
    public void nextSong() {
        super.nextSong();
        ++mSongPosition;
        createSong(mListAllSong.get(mSongPosition));
        mFragmentListSong.getmSongAdapter().setmNameSongCurrent(mCurrentSong.getmNameSong());
        mFragmentListSong.getmSongAdapter().notifyDataSetChanged();
    }

    @Override
    public void backSong() {
        super.backSong();
        --mSongPosition;
        createSong(mListAllSong.get(mSongPosition));
        mFragmentListSong.getmSongAdapter().setmNameSongCurrent(mCurrentSong.getmNameSong());
        mFragmentListSong.getmSongAdapter().notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imagePause:{
                mStatusSong = false;
                mSongService.pauseSong();
                mFragmentBarControlBase.pauseSong();
                break;
            }
            case R.id.imageStart:{
                if( mIsRestore ){
                    mSongService.createSong(mCurrentSong);
                    mSongService.playTo(mTimeCurrent);
                    mIsRestore = false;
                }
                mStatusSong = true;
                mSongService.startSong();
                mFragmentBarControlBase.startSong();

                break;
            }
            case R.id.fragmentBarControlBase:{
                mRipplePulseFragment.stopRippleAnimation();
                Intent intent = new Intent(this, SongDetailsActivity.class);
                if(mSongService.getTimeCurrentSong() > mTimeCurrent){
                    mTimeCurrent = mSongService.getTimeCurrentSong();
                }

                intent.putParcelableArrayListExtra(BaseUtils.KEY_LIST_SONGS, (ArrayList<? extends Parcelable>) mListAllSong);
                intent.putExtra(BaseUtils.KEY_SONGS, mCurrentSong);
                intent.putExtra(BaseUtils.KEY_STATUS_SONGS, mStatusSong);
                intent.putExtra(BaseUtils.KEY_TIME_CURRENT_SONG, mTimeCurrent);

                startActivityForResult(intent, BaseUtils.REQUEST_CODE);
                overridePendingTransition( R.anim.slide_in_up, R.anim.slide_stay );
                break;
            }
        }
    }

    @Override
    public void getDataSong(List<Song> listSong) {
        super.getDataSong(listSong);
        mListAllSong = listSong;
    }

    @Override
    public void synListSongsChanged(List<Song> listSong) {
        super.synListSongsChanged(listSong);
        mListAllSong = listSong;
        mSongService.synListSongsChanged(listSong);
        updateSongPosition();
    }

    @Override
    public int delete(Song song) {

        return mSongPresenter.delete(song);
    }

    private boolean instantiatePager(){
        mSectionsFragmentAdapter = new SectionsFragmentAdapter(getSupportFragmentManager());
        mSharedPreferences = getSharedPreferences(BaseUtils.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mSongPresenter = new SongPresenter(this);// Lấy dữ liệu từ Model rồi callback về getDataSong() để gán dữ liệu cho mListAllSong
        mSongPresenter.getDataSong();

        String nameSong = mSharedPreferences.getString(BaseUtils.KEY_SONGS, null);
        mStatusSong = mSharedPreferences.getBoolean(BaseUtils.KEY_STATUS_SONGS, false);
        mTimeCurrent = mSharedPreferences.getInt(BaseUtils.KEY_TIME_CURRENT_SONG, 0);

        if(nameSong != null ){
            for(Song song : mListAllSong){
                if(nameSong.equals(song.getmNameSong())){
                    mCurrentSong = song;
                    instantiateBarControl(mCurrentSong);
                    mIsRestore = true;
                    break;
                }
            }
        }

        mFragmentListSong = FragmentListSong.newInstance((ArrayList<Song>) mListAllSong, mCurrentSong);
        mSectionsFragmentAdapter.addFragment(mFragmentListSong);

        FragmentSettings fragmentSettings = new FragmentSettings();
        mSectionsFragmentAdapter.addFragment(fragmentSettings);

        mPager.setAdapter(mSectionsFragmentAdapter);
        mTabLayout.setupWithViewPager(mPager);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Đang ở chế độ search(bàn phím hiện) thì khi chuyển tab sẽ collapse view seach.
                if( mMenuItemSearch.isActionViewExpanded()) {
                    mMenuItemSearch.collapseActionView();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        Resources resources = getResources();
        mTabLayout.getTabAt(0).setText(resources.getString(R.string.tab_song));
        mTabLayout.getTabAt(1).setText(resources.getString(R.string.tab_setting));
        mTabLayout.setTabTextColors(resources.getColor(R.color.colorToolBar), resources.getColor(R.color.hightLight));
        mPager.setCurrentItem(BaseUtils.TAB_DEFAULT_HOME);

        return true;
    }

    private void instantiateService(){
        mIntentService = new Intent(HomeActivity.this, SongService.class);
        mIntentService.putParcelableArrayListExtra(BaseUtils.KEY_INTENT, (ArrayList<? extends Parcelable>) mListAllSong);

        startService(mIntentService);

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
    }

    private void instantiateBarControl(Song song){
        if( ! isBarVisible){
            mFragmentBarControlBase = FragmentBarControlBase.newInstance(song, mStatusSong);
            getSupportFragmentManager().beginTransaction().add(R.id.spaceBarControl, mFragmentBarControlBase).commit();
            mSpaceFrame.setVisibility(View.VISIBLE);
            isBarVisible = true;
        }else {
            mFragmentBarControlBase.createSong(song);
            if(mStatusSong){
                mFragmentBarControlBase.startSong();
            }else {
                mFragmentBarControlBase.pauseSong();
            }
        }
    }

    private void updateSongPosition(){
        int sizeList = mListAllSong.size();
        for(int i = 0; i < sizeList; i++){
            if(mCurrentSong.getmNameSong().equals(mListAllSong.get(i).getmNameSong())){
                mSongPosition = i;
            }
        }
    }

}
