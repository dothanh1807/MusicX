package com.example.exercise2.view.fragment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.exercise2.BaseUtils;
import com.example.exercise2.R;
import com.example.exercise2.adapter.SongAdapter;
import com.example.exercise2.listner.BaseControlListner;
import com.example.exercise2.model.entitys.Song;
import com.example.exercise2.presenter.TouchHelperCallBack;
import com.example.exercise2.view.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class FragmentListSong extends BaseFragment {

    private List<Song> mListSong;

    private RecyclerView mRecyclerView;

    private SongAdapter mSongAdapter;

    private BaseControlListner mActivityCallBack;

    private TouchHelperCallBack mTouchHelperCallBack;

    public static FragmentListSong newInstance(ArrayList<Song> listSong, Song songCurrent) {
        FragmentListSong fragmentAlbums = new FragmentListSong();
        Bundle args = new Bundle();
        args.putParcelableArrayList(BaseUtils.KEY_LIST_SONGS, listSong);
        args.putParcelable(BaseUtils.KEY_SONGS, songCurrent);
        fragmentAlbums.setArguments(args);

        return fragmentAlbums;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityCallBack = (BaseControlListner) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListSong = getArguments().getParcelableArrayList(BaseUtils.KEY_LIST_SONGS);
        Song song = getArguments().getParcelable(BaseUtils.KEY_SONGS);
        mSongAdapter = new SongAdapter(this, mListSong);
        if(song != null){
            mSongAdapter.setmNameSongCurrent(song.getmNameSong());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_song, null);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycleListSong);

        setupRecyclerView();

        return view;
    }

    @Override
    public void createSong(Song song) {
        super.createSong(song);
        mActivityCallBack.createSong(song);
    }

    public SongAdapter getmSongAdapter() {
        return mSongAdapter;
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mSongAdapter);
        mTouchHelperCallBack = new TouchHelperCallBack(getActivity(), mRecyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<TouchHelperCallBack.UnderlayButton> listUnderlayButtons) {
                TouchHelperCallBack.UnderlayButtonClickListener underlayButtonClickListener = new UnderlayButtonClickListener() {
                    @Override
                    public void onClickUnderlayButton(int position) {
                        ((SongAdapter)mRecyclerView.getAdapter()).onSwipe(position, 0);
                    }
                };

                // Khởi tạo buttons cho menu khi swiped, truyền vào 1 image cho button + sự kiện listener cho button đó
                TouchHelperCallBack.UnderlayButton underlayButton = new TouchHelperCallBack.UnderlayButton(ViewUtils.IMAGE_DELETE, underlayButtonClickListener, getActivity());

                listUnderlayButtons.add(underlayButton);
            }
        };
    }

}
