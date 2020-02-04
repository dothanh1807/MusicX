package com.example.exercise2.view.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.exercise2.BaseUtils;
import com.example.exercise2.R;
import com.example.exercise2.listner.BaseControlListner;
import com.example.exercise2.listner.DetailsControlListener;
import com.example.exercise2.view.ViewUtils;
import com.example.exercise2.model.entitys.Song;
import com.example.exercise2.view.activity.HomeActivity;
import com.example.exercise2.view.customizeview.CustomizeAnimations;

public class FragmentBarControlBase extends BaseFragment {

    private TextView mTvNameSong;

    private TextView mTvnameAuthor;

    private ImageView mImgSong;

    private ImageView mImgStart;

    private ImageView mImgPause;

    private BroadcastReceiver mReceiver;

    private IntentFilter mFilter;

    private DetailsControlListener mActivityCallBack;

    public static FragmentBarControlBase newInstance(Song song, boolean statusSong) {
        FragmentBarControlBase fragmentBarControlBase = new FragmentBarControlBase();
        Bundle args = new Bundle();
        args.putParcelable(BaseUtils.KEY_SONGS, song);
        args.putBoolean(BaseUtils.KEY_STATUS_SONGS, statusSong);
        fragmentBarControlBase.setArguments(args);

        return fragmentBarControlBase;
    }

    @Override
    public void onAttach(Context context) {
        mActivityCallBack = (DetailsControlListener) context;
        mFilter = new IntentFilter(BaseUtils.ACTION_CREATE_SONG);
        mFilter.addAction(BaseUtils.ACTION_START_SONG);
        mFilter.addAction(BaseUtils.ACTION_PAUSE_SONG);
        mFilter.addAction(BaseUtils.ACTION_NEXT_SONG);
        mFilter.addAction(BaseUtils.ACTION_BACK_SONG);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                receiver(context, intent);
            }
        };

        context.registerReceiver(mReceiver, mFilter);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bar_control_song, null);

        mTvNameSong = view.findViewById(R.id.textViewNameSong);
        mTvnameAuthor = view.findViewById(R.id.textViewNameSinger);
        mImgSong = view.findViewById(R.id.imageArtist);
        mImgPause = view.findViewById(R.id.imagePause);
        mImgStart = view.findViewById(R.id.imageStart);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Song song = getArguments().getParcelable(BaseUtils.KEY_SONGS);
        boolean statusSong = getArguments().getBoolean(BaseUtils.KEY_STATUS_SONGS);
        createSong(song);
        if(statusSong){
            startSong();
        }else {
            pauseSong();
        }
        RotateAnimation rotate = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotate.setDuration(20000);
        rotate.setInterpolator(new LinearInterpolator());// quay đều
        rotate.setRepeatMode(Animation.RESTART);
        rotate.setRepeatCount(Animation.INFINITE);
        mImgSong.startAnimation(rotate);
    }

    @Override
    public void onDetach() {
        getContext().unregisterReceiver(mReceiver);
        super.onDetach();
    }

    @Override
    public void createSong(Song song) {
        mTvNameSong.setText(song.getmNameSong());
        mTvnameAuthor.setText(song.getmNameAuthor());
        if (song.getmImageSong() != null){
            mImgSong.setImageURI(Uri.parse(song.getmImageSong()));
            mImgSong.setColorFilter(null);
        }else {
            mImgSong.setImageResource(ViewUtils.IMAGE_DEFAULT);
            mImgSong.setColorFilter(getResources().getColor(R.color.hightLight));
        }
    }

    @Override
    public void startSong() {
        mImgStart.startAnimation(CustomizeAnimations.animHideButton());
        mImgStart.setVisibility(View.INVISIBLE);

        mImgPause.setVisibility(View.VISIBLE);
        mImgPause.startAnimation(CustomizeAnimations.animShowButton());
    }

    @Override
    public void pauseSong() {
        mImgPause.startAnimation(CustomizeAnimations.animHideButton());
        mImgPause.setVisibility(View.INVISIBLE);

        mImgStart.setVisibility(View.VISIBLE);
        mImgStart.startAnimation(CustomizeAnimations.animShowButton());
    }

    private void receiver(Context context, Intent intent) {

        if ( BaseUtils.ACTION_CREATE_SONG.equals(intent.getAction())) {

        }
        if ( BaseUtils.ACTION_START_SONG.equals(intent.getAction())) {
            startSong();
        }
        if ( BaseUtils.ACTION_PAUSE_SONG.equals(intent.getAction())) {
            pauseSong();
        }
        if ( BaseUtils.ACTION_NEXT_SONG.equals(intent.getAction())) {
            startSong();
            mActivityCallBack.nextSong();
        }
        if ( BaseUtils.ACTION_BACK_SONG.equals(intent.getAction())) {
            startSong();
            mActivityCallBack.backSong();
        }
    }

}
