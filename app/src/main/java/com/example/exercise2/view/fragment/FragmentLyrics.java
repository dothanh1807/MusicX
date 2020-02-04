package com.example.exercise2.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.exercise2.BaseUtils;
import com.example.exercise2.R;
import com.example.exercise2.model.entitys.Song;

public class FragmentLyrics extends BaseFragment {

    private TextView mTvNameSongLyrics;

    private TextView mTvNameAuthorLyrics;

    public static FragmentLyrics newInstance(Song song) {
        FragmentLyrics fragmentLyrics = new FragmentLyrics();
        Bundle args = new Bundle();
        args.putParcelable(BaseUtils.KEY_SONGS, song);
        fragmentLyrics.setArguments(args);

        return fragmentLyrics;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lyrics, null);

        mTvNameSongLyrics = view.findViewById(R.id.tvNameSongLyrics);
        mTvNameAuthorLyrics = view.findViewById(R.id.tvNameArtistLyrics);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Song song = getArguments().getParcelable(BaseUtils.KEY_SONGS);
        setInfoSong(song);
    }

    @Override
    public void createSong(Song song) {
        super.createSong(song);
        setInfoSong(song);
    }

    private boolean setInfoSong(Song song){
        mTvNameSongLyrics.setText(song.getmNameSong());
        mTvNameAuthorLyrics.setText(song.getmNameAuthor());

        return true;
    }
}
