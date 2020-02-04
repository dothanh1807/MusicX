package com.example.exercise2.view.activity;

import android.support.v7.app.AppCompatActivity;
import com.example.exercise2.listner.DetailsControlListener;
import com.example.exercise2.model.database.DataBaseHandler;
import com.example.exercise2.model.entitys.Song;
import com.example.exercise2.presenter.DataSongHandler;
import java.util.List;

public class BaseActivity extends AppCompatActivity implements DetailsControlListener, DataSongHandler, DataBaseHandler {

    @Override
    public void nextSong() {

    }

    @Override
    public void backSong() {

    }

    @Override
    public void playTo(int position) {

    }

    @Override
    public int getTimeCurrentSong() {
        return 0;
    }

    @Override
    public int getTimeTotalSong() {
        return 0;
    }

    @Override
    public void createSong(Song song) {

    }

    @Override
    public void startSong() {

    }

    @Override
    public void pauseSong() {

    }

    @Override
    public void getDataSong(List<Song> listSong) {

    }

    @Override
    public void synListSongsChanged(List<Song> listSong) {

    }

    @Override
    public long insert(Song song) {
        return 0;
    }

    @Override
    public int delete(Song song) {
        return 0;
    }

    @Override
    public int update(Song song) {
        return 0;
    }

    @Override
    public List<Song> getAllSong(String[] selectionArgs) {
        return null;
    }

    @Override
    public boolean saveListSong(List<Song> listSongs) {
        return false;
    }

    @Override
    public void closeDataBase() {

    }
}
