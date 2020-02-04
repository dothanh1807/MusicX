package com.example.exercise2.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.exercise2.BaseUtils;
import com.example.exercise2.R;
import com.example.exercise2.listner.DetailsControlListener;
import com.example.exercise2.model.entitys.Song;
import com.example.exercise2.view.ViewUtils;
import com.example.exercise2.view.customizeview.CustomizeAnimations;
import com.example.exercise2.view.customizeview.RipplePulseAnimation;

public class FragmentBarControlDetails extends BaseFragment implements SeekBar.OnSeekBarChangeListener{

    private TextView mTvTimeStart;

    private TextView mTvTimeEnd;

    private ImageView mImgStart;

    private ImageView mImgPause;

    private ImageView mImgNext;

    private ImageView mImgBack;

    private SeekBar mSeekBar;

    private DetailsControlListener mActivityCallback;

    private RipplePulseAnimation mRipplePulseButton;

    private RipplePulseAnimation mRipplePulseFragment;

    public static FragmentBarControlDetails newInstance(Song song, boolean statusSong, int timeCurrent) {
        FragmentBarControlDetails fragmentBarControlDetails = new FragmentBarControlDetails();
        Bundle args = new Bundle();
        args.putParcelable(BaseUtils.KEY_SONGS, song);
        args.putBoolean(BaseUtils.KEY_STATUS_SONGS, statusSong);
        args.putInt(BaseUtils.KEY_TIME_CURRENT_SONG, timeCurrent);
        fragmentBarControlDetails.setArguments(args);

        return fragmentBarControlDetails;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityCallback = (DetailsControlListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bar_control_song_details, null);

        mTvTimeStart = view.findViewById(R.id.timeMusicStart);
        mTvTimeEnd = view.findViewById(R.id.timeMusicEnd);
        mImgStart = view.findViewById(R.id.imgStart);
        mImgPause = view.findViewById(R.id.imgPause);
        mImgNext = view.findViewById(R.id.imgNext);
        mImgBack = view.findViewById(R.id.imgBack);
        mSeekBar = view.findViewById(R.id.seekbarMusic);
        mRipplePulseButton = view.findViewById(R.id.ripplepulse_button);
        mRipplePulseFragment = view.findViewById(R.id.ripplepulse_fragment);

        mSeekBar.setOnSeekBarChangeListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Song song = getArguments().getParcelable(BaseUtils.KEY_SONGS);
        boolean statusSong = getArguments().getBoolean(BaseUtils.KEY_STATUS_SONGS);
        int timeCurrent = getArguments().getInt(BaseUtils.KEY_TIME_CURRENT_SONG);

        mTvTimeStart.setText(BaseUtils.FORMAT_TIME_SONG.format(timeCurrent));
        mTvTimeEnd.setText(BaseUtils.FORMAT_TIME_SONG.format(Integer.parseInt(song.getmTimeSong())));
        mSeekBar.setMax(Integer.parseInt(song.getmTimeSong()));
        mSeekBar.setProgress(timeCurrent);
        if(statusSong){
            startSong();
            mActivityCallback.startSong();
        }else {
            pauseSong();
        }
    }

    @Override
    public void createSong(Song song) {
        super.createSong(song);
        mTvTimeStart.setText(ViewUtils.TIME_START_DEFAULT);
        mTvTimeEnd.setText(BaseUtils.FORMAT_TIME_SONG.format(Integer.parseInt(song.getmTimeSong())));
        mSeekBar.setMax(Integer.parseInt(song.getmTimeSong()));
        startSong();
    }

    @Override
    public void startSong() {
        CustomizeAnimations.startRippleAnimationBtn(mRipplePulseFragment, mRipplePulseButton);

        mImgStart.startAnimation(CustomizeAnimations.animHideButton());
        mImgStart.setVisibility(View.INVISIBLE);

        mImgPause.setVisibility(View.VISIBLE);
        mImgPause.startAnimation(CustomizeAnimations.animShowButton());

    }

    @Override
    public void pauseSong() {
        CustomizeAnimations.pauseRippleAnimationBtn(mRipplePulseFragment, mRipplePulseButton);

        mImgPause.startAnimation(CustomizeAnimations.animHideButton());
        mImgPause.setVisibility(View.INVISIBLE);

        mImgStart.setVisibility(View.VISIBLE);
        mImgStart.startAnimation(CustomizeAnimations.animShowButton());
    }

    @Override
    public void nextSong() {
        super.nextSong();
        mActivityCallback.nextSong();
    }

    @Override
    public void backSong() {
        super.backSong();
        mActivityCallback.backSong();
    }

    @Override
    public void playTo(int position) {
        super.playTo(position);
        mSeekBar.setProgress(position);
        mTvTimeStart.setText(BaseUtils.FORMAT_TIME_SONG.format(position));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mActivityCallback.playTo(seekBar.getProgress());
    }

}
