package com.example.exercise2.listner;

import com.example.exercise2.model.entitys.Song;
import java.util.List;

public interface DetailsControlListener extends BaseControlListner{

    void nextSong();

    void backSong();

    void playTo(int position);

    int getTimeCurrentSong();

    int getTimeTotalSong();

    void synListSongsChanged(List<Song> listSong);

}
