package com.example.exercise2.view.customizeview;

import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

public class CustomizeAnimations {

    public RotateAnimation baseRotateAnimation(float fromDegrees, float toDegrees, int duration, Interpolator interpolator){
        RotateAnimation rotate = new RotateAnimation(fromDegrees, toDegrees,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotate.setDuration(duration);
        rotate.setInterpolator(interpolator);

        return rotate;
    }

    public static AnimationSet animShowButton(){
        AnimationSet animSetShow = new AnimationSet(false);
        Animation rotateAnimShow = new RotateAnimation(-180, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateAnimShow.setDuration(500);
        animSetShow.addAnimation(rotateAnimShow);
        Animation alphaShow = new AlphaAnimation(0.0f, 1.0f);
        alphaShow.setDuration(1000);
        animSetShow.addAnimation(alphaShow);

        return animSetShow;
    }

    public static AnimationSet animHideButton(){
        AnimationSet animSetHide = new AnimationSet(false);
        Animation rotateAnimHide = new RotateAnimation(0, 180,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateAnimHide.setDuration(500);
        animSetHide.addAnimation(rotateAnimHide);
        Animation alphaHide = new AlphaAnimation(1.0f, 0.0f);
        alphaHide.setDuration(500);
        animSetHide.addAnimation(alphaHide);

        return animSetHide;
    }

    public static void startRippleAnimationForHome(final RipplePulseAnimation ripplePulseLayout){
        ripplePulseLayout.startRippleAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ripplePulseLayout.stopRippleAnimation();
            }
        }, 2000);
    }

    public static void startRippleAnimationBtn(final RipplePulseAnimation ripplePulseFragment, final RipplePulseAnimation ripplePulseButton){
        ripplePulseFragment.stopRippleAnimation();
        ripplePulseButton.startRippleAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ripplePulseButton.stopRippleAnimation();
            }
        },300);
    }

    public static void pauseRippleAnimationBtn(final RipplePulseAnimation ripplePulseFragment, final RipplePulseAnimation ripplePulseButton){
        ripplePulseButton.startRippleAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ripplePulseButton.stopRippleAnimation();
            }
        },300);
        ripplePulseFragment.startRippleAnimation();
    }

}
