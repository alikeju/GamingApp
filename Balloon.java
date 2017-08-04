package com.example.pauline.gameapp;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.example.pauline.gameapp.utils.PixelHelper;

/**
 * Created by Pauline on 1/6/2017.
 */

public class Balloon extends ImageView implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {

    private ValueAnimator mAnimator;
    private BalloonListener mListener;
    private boolean mPopped;
    //defaults to false

    public Balloon(Context context) {
        super(context);
    }

    public Balloon(Context context, int color, int rawHeight) {
        super(context);

        mListener = (BalloonListener) context;

        this.setImageResource(R.drawable.balloon);
        this.setColorFilter(color);

        int rawWidth = rawHeight /2;

        int dpHeight = PixelHelper.pixelsToDp(rawHeight, context);
        int dpWidth = PixelHelper.pixelsToDp(rawWidth, context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(dpWidth, dpHeight);
        setLayoutParams(params);
    }

    public void releaseBalloon(int screenHeight, int duration){
        mAnimator = new ValueAnimator();
        mAnimator.setDuration(duration);
        mAnimator.setFloatValues(screenHeight, 0f);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setTarget(this);
        mAnimator.addListener(this);
        mAnimator.addUpdateListener(this);
        mAnimator.start();
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
    //this means that you have finished the animation and the baloons y position is 0.
        if(!mPopped){
            mListener.popBalloon(this, false);
            //means this baloon popped because it got to the top of the screen
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        setY((float) valueAnimator.getAnimatedValue());

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!mPopped && event.getAction() == MotionEvent.ACTION_DOWN){
            mListener.popBalloon(this, true);
            mPopped = true;
            // setting it to true so it can not be popped again
            mAnimator.cancel();
        }
        return super.onTouchEvent(event);
    }

    public void setPopped(boolean popped) {
        mPopped = popped;
        if(popped){
            mAnimator.cancel();
            //by cancelling the balloons you are getting rid of unintended side effects
        }
    }

    public interface BalloonListener{
        void popBalloon(Balloon balloon, boolean userTouch);
    }
}
