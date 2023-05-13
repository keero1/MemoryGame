package com.keero.memorygame.Utils;

import android.animation.ValueAnimator;
import android.view.View;

public class FlipListener implements ValueAnimator.AnimatorUpdateListener {
    private final View front;
    private final View back;
    private boolean isFlipped;

    public FlipListener(final View front, final View back) {
        this.front = front;
        this.back = back;
        this.back.setVisibility(View.GONE);
    }

    @Override
    public void onAnimationUpdate(final ValueAnimator animation) {
        final float value = animation.getAnimatedFraction();
        final float scaleValue = 0.625f + (1.5f * (value - 0.5f) * (value - 0.5f));

        if(value <= 0.5f){
            this.front.setRotationY(180 * value);
            this.front.setScaleX(scaleValue);
            this.front.setScaleY(scaleValue);
            if(isFlipped){
                setStateFlipped(false);
            }
        } else {
            this.back.setRotationY(-180 * (1f- value));
            this.back.setScaleX(scaleValue);
            this.back.setScaleY(scaleValue);
            if(!isFlipped){
                setStateFlipped(true);
            }
        }
    }

    private void setStateFlipped(boolean flipped) {
        isFlipped = flipped;
        this.front.setVisibility(flipped ? View.GONE : View.VISIBLE);
        this.back.setVisibility(flipped ? View.VISIBLE : View.GONE);
    }

    public boolean isFlipped(){
        return isFlipped;
    }

}