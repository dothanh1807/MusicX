package com.example.exercise2.view.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.example.exercise2.BaseUtils;
import com.example.exercise2.R;
import com.example.exercise2.view.ViewUtils;
import com.example.exercise2.model.entitys.Song;
import com.example.exercise2.view.customizeview.CircleImageVIew;
import com.example.exercise2.view.customizeview.CustomizeAnimations;

public class FragmentDetailSong extends BaseFragment implements View.OnClickListener {

    private final int TIME_ROTEATE = 30000;

    private CircleImageVIew mImgSongDetail;

    private TextView mTvNameSongDetails;

    private TextView mTvNameAuthorDetails;

    private FragmentBarControlDetails mFragmentBarControlDetails;

    private Uri mUri;

    private boolean mSwapImage;

    public static FragmentDetailSong newInstance(Song song, boolean statusSong, int timeCurrent) {
        FragmentDetailSong fragmentDetailSong = new FragmentDetailSong();
        Bundle args = new Bundle();
        args.putParcelable(BaseUtils.KEY_SONGS, song);
        args.putBoolean(BaseUtils.KEY_STATUS_SONGS, statusSong);
        args.putInt(BaseUtils.KEY_TIME_CURRENT_SONG, timeCurrent);
        fragmentDetailSong.setArguments(args);

        return fragmentDetailSong;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_song, null);

        mImgSongDetail = view.findViewById(R.id.imgSongDetail);
        mTvNameSongDetails = view.findViewById(R.id.tvNameSongDetails);
        mTvNameAuthorDetails = view.findViewById(R.id.tvNameArtistDetails);

        mImgSongDetail.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Song song = getArguments().getParcelable(BaseUtils.KEY_SONGS);
        boolean statusSong = getArguments().getBoolean(BaseUtils.KEY_STATUS_SONGS);
        int timeCurrent = getArguments().getInt(BaseUtils.KEY_TIME_CURRENT_SONG);

        initImgSong(song);
        RotateAnimation rotate = new CustomizeAnimations().baseRotateAnimation(0, 360,
                TIME_ROTEATE, new LinearInterpolator());// LinearInterpolator quay đều
        rotate.setRepeatMode(Animation.RESTART);
        rotate.setRepeatCount(Animation.INFINITE);
        mImgSongDetail.startAnimation(rotate);

        mFragmentBarControlDetails = FragmentBarControlDetails.newInstance(song, statusSong, timeCurrent);
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.spaceBarControlDetail, mFragmentBarControlDetails).commit();
    }

    @Override
    public void createSong(Song song) {
        super.createSong(song);
        initImgSong(song);
        startSong();
    }

    @Override
    public void startSong() {
        super.startSong();
        mFragmentBarControlDetails.startSong();
    }

    @Override
    public void pauseSong() {
        super.pauseSong();
        mFragmentBarControlDetails.pauseSong();
    }

    @Override
    public void playTo(int position) {
        super.playTo(position);
        mFragmentBarControlDetails.playTo(position);
    }

    private boolean initImgSong(Song song){
        if (song.getmImageSong() != null){
            mUri = Uri.parse(song.getmImageSong());
            initImageSong();
        }else {
            initImageDefault();
        }
        mTvNameSongDetails.setText(song.getmNameSong());
        mTvNameAuthorDetails.setText(song.getmNameAuthor());

        return true;
    }

    @Override
    public void onClick(View v) {
        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(mImgSongDetail, "scaleX", 1f, 0f);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(mImgSongDetail, "scaleX", 0f, 1f);
        oa1.setDuration(ViewUtils.TIME_ANIMATION_FLIP_IMG);
        oa2.setDuration(ViewUtils.TIME_ANIMATION_FLIP_IMG);
        oa1.setInterpolator(new DecelerateInterpolator());
        oa2.setInterpolator(new AccelerateDecelerateInterpolator());
        oa1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if( ! mSwapImage){
                    initImageSong();
                }else {
                    initImageDefault();
                }
                oa2.start();
            }
        });
        oa1.start();
    }

    private void initImageSong(){
        mImgSongDetail.setImageURI(mUri);
        mImgSongDetail.setColorFilter(null);
        mSwapImage = true;
    }

    private void initImageDefault(){
        mImgSongDetail.setImageResource(ViewUtils.IMAGE_DEFAULT_LARGE);
        mImgSongDetail.setColorFilter(getResources().getColor(R.color.hightLight));
        mSwapImage = false;
    }

}
