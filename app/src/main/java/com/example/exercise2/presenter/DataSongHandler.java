package com.example.exercise2.presenter;

import com.example.exercise2.model.entitys.Song;

import java.util.List;

public interface DataSongHandler {

    void getDataSong(List<Song> listSong);

    void synListSongsChanged(List<Song> listSong);

}
