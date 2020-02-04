package com.example.exercise2.model.database;

import com.example.exercise2.model.entitys.Song;

import java.util.List;

public interface DataBaseHandler {

    long insert(Song song);

    int delete(Song song);

    int update(Song song);

    List<Song> getAllSong(String[] selectionArgs);

    boolean saveListSong(List<Song> listSongs);

    void closeDataBase();

}
