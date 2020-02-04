package com.example.exercise2.presenter;

import android.content.Context;

import com.example.exercise2.model.database.DataBaseHandler;
import com.example.exercise2.model.database.DataSongProvider;
import com.example.exercise2.model.entitys.Song;
import com.example.exercise2.woker.DeviceLoader;

import java.util.List;

public class SongPresenter implements DataBaseHandler {

    private DataSongHandler mActivityCallback;

    private DataSongProvider.SongDataBase mSongDataBase;

    private List<Song> mListSong;

    public SongPresenter(DataSongHandler dataSongHandler) {
        mActivityCallback = dataSongHandler;
        mSongDataBase = new DataSongProvider.SongDataBase((Context) dataSongHandler);
    }

    public void getDataSong() {
        mListSong = getAllSong(null);

        if(mListSong.size() == 0){
            mListSong = new DeviceLoader((Context) mActivityCallback).getListsSongFromDevice();
        }

        mActivityCallback.getDataSong(mListSong);
    }

    @Override
    public long insert(Song song) {

        return mSongDataBase.insert(song);
    }

    @Override
    public int delete(Song song) {

        return mSongDataBase.delete(song);
    }

    @Override
    public int update(Song song) {

        return mSongDataBase.update(song);
    }

    @Override
    public List<Song> getAllSong(String[] selectionArgs) {

        return mSongDataBase.getAllSong(null);
    }

    @Override
    public boolean saveListSong(List<Song> listSongs) {
        return false;
    }

    @Override
    public void closeDataBase() {
        mSongDataBase.closeDataBase();
    }

}
