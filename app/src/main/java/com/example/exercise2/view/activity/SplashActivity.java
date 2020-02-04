package com.example.exercise2.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.exercise2.BaseUtils;
import com.example.exercise2.R;
import com.example.exercise2.view.customizeview.Circle;
import com.example.exercise2.view.customizeview.CircleAngleAnimation;
import com.example.exercise2.view.customizeview.RipplePulseAnimation;

public class SplashActivity extends AppCompatActivity {

    private final int ANGLE = 360;

    private final int TIME_OUT = 2000;

    private RipplePulseAnimation mRipplePulse;

    private ImageView mImageView;

    private TextView mMyName;

    private TextView mAppName;

    private Circle mCircle;

    private Handler mHandler;

    @Override public void setTheme(int resid) {
        super.setTheme(resid);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mImageView = findViewById(R.id.imgSplash);
        mMyName = findViewById(R.id.myName);
        mAppName = findViewById(R.id.appName);
        mCircle = findViewById(R.id.circle);
        mRipplePulse = findViewById(R.id.ripplepulse_splash);

        mHandler = new Handler();

        startAnimations();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRipplePulse.stopRippleAnimation();
                permissionCheck();
            }
        }, TIME_OUT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case BaseUtils.REQUEST_PERMISSION_READ_EXTERNAL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startHomeActivity();
                } else {
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
                return ;
            }
        }
    }

    private void startAnimations(){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRipplePulse.startRippleAnimation();
            }
        }, 1000);

        CircleAngleAnimation animation = new CircleAngleAnimation(mCircle, ANGLE);
        animation.setDuration(1000);
        mCircle.startAnimation(animation);

        AnimationSet animationSet = new AnimationSet(false);
        Animation alphaShow = new AlphaAnimation(0.0f, 0.9f);
        alphaShow.setDuration(1000);
        animationSet.addAnimation(alphaShow);
        ScaleAnimation scaleAnimation =  new ScaleAnimation(0.3f, 0.9f, 0.3f, 0.9f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1700);
//        animationSet.addAnimation(scaleAnimation);

        mImageView.startAnimation(animationSet);
        mMyName.startAnimation(animationSet);
        mAppName.startAnimation(animationSet);
    }

    private void permissionCheck(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, BaseUtils.REQUEST_PERMISSION_READ_EXTERNAL);
            } else {
                startHomeActivity();
            }
        } else {
            startHomeActivity();
        }
    }

    private void startHomeActivity(){
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}
