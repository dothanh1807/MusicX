package com.example.exercise2.woker;

import android.app.Activity;
import android.os.AsyncTask;

import com.example.exercise2.model.database.DataSongProvider;
import com.example.exercise2.model.entitys.Song;

import java.util.List;

public class DataBaseSynchronizer extends AsyncTask<Void, Void, List<Song>> {

    private Activity mContext;

    private DataSongProvider.SongDataBase mSongDataBase;

    public DataBaseSynchronizer(Activity mContext) {
        this.mContext = mContext;
    }

    @Override
    protected List<Song> doInBackground(Void... values) {
        mSongDataBase = new DataSongProvider.SongDataBase(mContext);
        List<Song> listDataBase = mSongDataBase.getAllSong(null);
        List<Song> listScan = new DeviceLoader(mContext).getListsSongFromDevice();

        /**
         * Thêm vào database nếu bài hát chưa có
         */
        for(Song songScan : listScan){
            boolean isExist = false;
            for(Song songDA : listDataBase){
                if(songScan.getmNameSong().equals(songDA.getmNameSong())){
                    isExist = true;
                    break;
                }
            }
            if( ! isExist ){
                mSongDataBase.insert(songScan);
            }
        }

        /**
         * Xóa khỏi database nếu bài hát trong database k tồn tại trong listSongs vừa scan
         */
        for(Song songDA : listDataBase){
            boolean isExist = false;
            for(Song songScan : listScan){
                if(songDA.getmNameSong().equals(songScan.getmNameSong())){
                    isExist = true;
                    break;
                }
            }
            if( ! isExist ){
                mSongDataBase.delete(songDA);
            }
        }

        mSongDataBase.closeDataBase();

        return listScan;
    }

    @Override
    protected void onPostExecute(List<Song> songs) {

    }
}
