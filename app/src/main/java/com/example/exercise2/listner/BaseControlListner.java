package com.example.exercise2.listner;

import com.example.exercise2.model.entitys.Song;

public interface BaseControlListner {

    void createSong(Song song);

    void startSong();

    void pauseSong();
}
