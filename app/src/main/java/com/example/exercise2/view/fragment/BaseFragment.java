package com.example.exercise2.view.fragment;

import android.support.v4.app.Fragment;

import com.example.exercise2.model.entitys.Song;
import com.example.exercise2.listner.DetailsControlListener;

public class BaseFragment extends Fragment implements DetailsControlListener {


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
    
}
