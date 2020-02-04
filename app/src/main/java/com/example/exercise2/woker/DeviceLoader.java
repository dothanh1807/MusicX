package com.example.exercise2.woker;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.exercise2.model.entitys.Song;

import java.util.ArrayList;
import java.util.List;


public class DeviceLoader {

    private Context mContext;

    public DeviceLoader(Context mContext) {
        this.mContext = mContext;
    }

    public List<Song> getListsSongFromDevice(){
        List<Song> listSong = new ArrayList<Song>();

        // List các song
        ArrayList<InfoSong> listInfoSong = new ArrayList<InfoSong>();
        Cursor cursor = mContext.getContentResolver().query( MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null);
        InfoSong infoSong = null;
        if (cursor.moveToFirst()) {
            do {
                infoSong = new InfoSong(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_ID)),
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE)),
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST)),
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION)),
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA)));
                listInfoSong.add(infoSong);

            } while ( cursor.moveToNext());
        }

        // List các image của album
        ArrayList<InfoImageAlbum> listImageAlbum = new ArrayList<InfoImageAlbum>();
        Cursor cursorImg = mContext.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null);
        InfoImageAlbum infoImageAlbum = null;
        if (cursorImg.moveToFirst()) {
            do {
                infoImageAlbum = new InfoImageAlbum(cursorImg.getString(cursorImg.getColumnIndex(MediaStore.Audio.Albums._ID)),
                        cursorImg.getString(cursorImg.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)));
                listImageAlbum.add(infoImageAlbum);

            } while (cursorImg.moveToNext());
        }

        /**
         *  So khớp list các image VỚI list các song bằng id để set cho chính xác, vì có những bài
         *  hát k có image cho nên size 2 List này sẽ k bằng nhau.
         */
        int sizeListImgAlbum = listImageAlbum.size();
        String pathImageSong = null;
        Song song = null;
        int a;
        for (InfoSong is : listInfoSong) {
            a = 0;
            while (a < sizeListImgAlbum) {
                if ( is.mAlbumID.equals(listImageAlbum.get(a).mImageAlbumID)) {
                    pathImageSong = listImageAlbum.get(a).mPathImageAlbum;
                    break;
                }
                a++;
            }

            song = new Song(is.mAlbumID,
                    is.mTittle,
                    is.mArtist,
                    is.mDuration,
                    is.mPath,
                    pathImageSong);

            listSong.add(song);

        }

        return listSong;
    }

    //////////////////////////////////////////////////////////////////////////

    private class InfoSong {
        private String mAlbumID, mTittle, mArtist, mDuration, mPath;

        private InfoSong(String albumID, String tittle, String artist, String duration, String path) {
            this.mAlbumID = albumID;
            this.mTittle = tittle;
            this.mArtist = artist;
            this.mDuration = duration;
            this.mPath = path;
        }
    }

    private class InfoImageAlbum {
        private String mImageAlbumID, mPathImageAlbum ;

        private InfoImageAlbum(String imageAlbumID, String pathImageAlbum) {
            this.mImageAlbumID = imageAlbumID;
            this.mPathImageAlbum = pathImageAlbum;
        }
    }
}
