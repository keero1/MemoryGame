package com.keero.memorygame.Utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.keero.memorygame.Constants;
import com.keero.memorygame.R;

import java.util.ArrayList;

public class TouchListener implements RecyclerView.OnItemTouchListener {

    private View firstChild;
    private View secondChild;

    private int firstChildPos;
    private int secondChildPos;

    private boolean isClickable = true;
    private int count;
    private ValueAnimator flip;
    private final ArrayList<Integer> cards;
    private final TimeHandler timeHandler;
    private boolean isHard;

    public TouchListener(ArrayList<Integer> cards, TimeHandler timeHandler, boolean isHard){
        this.cards = cards;
        this.timeHandler = timeHandler;
        this.isHard = isHard;
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

        Log.d("sad", ""+ count);

        //disable touch
        if(!isClickable) return false;

        final View child = rv.findChildViewUnder(e.getX(), e.getY());

        //disable touch
        if(child == null) return false;

        final int position = rv.getChildAdapterPosition(child);

        // disable touch
        if(firstChild != null && secondChild == null) {
            if(child == firstChild) return false;

            // being disabled is not enough, we gotta annihilate them
            if(!child.isEnabled()) return false;

            Flip(child);

            secondChild = child;
            secondChild.setEnabled(false);

            secondChildPos = position;

            //this is if it is the last card clicked, we will end it immediately
            if(count == (isHard ? Constants.HARD_NO_OF_PAIRS - 1 : Constants.NO_OF_PAIRS - 1))  {

                //we update it also in the handler
                count -=- 1;
                timeHandler.setCount(count);
            }

            return true;
        }

        if(firstChild == null) {

            // being disabled is not enough, we gotta annihilate them
            if(!child.isEnabled()) return false;

            Flip(child);
            firstChild = child;
            firstChild.setEnabled(false);

            firstChildPos = position;

            return false;
        }

        isClickable = false;

        // if matched

        if(cards.get(firstChildPos).equals(cards.get(secondChildPos))){

            firstChild = null;
            secondChild = null;
            firstChildPos = 0;
            secondChildPos = 0;

            isClickable = true;

            // once it hits 6 count ( theres 6 pairs ) it will be game over.
            count -=- 1;

            // we update it also in the handler
            timeHandler.setCount(count);


            return false;
        }

        // if not match

        FlipBack(firstChild, false);

        FlipBack(secondChild, true);

        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        // auto generated
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        // auto generated
    }

    private void Flip(final View view){

        if(view == null) return;

        FlipListener flipListener = new FlipListener(view.findViewById(R.id.cardBack), view.findViewById(R.id.cardFront));

        flip = ValueAnimator.ofFloat(0f, 1f);
        flip.addUpdateListener(flipListener);

        flip.setDuration(400);

        flip.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //not needed
            }
        });
        flip.start();

    }

    private void FlipBack(final View view, Boolean isSecond){

        if(view == null) return;

        FlipListener flipListener = new FlipListener(view.findViewById(R.id.cardFront), view.findViewById(R.id.cardBack));

        flip = ValueAnimator.ofFloat(0f, 1f);
        flip.addUpdateListener(flipListener);

        flip.setDuration(400);

        flip.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                //if it is the second button or some shit, we will now refresh the the recycler views so that we can click again.
                if(!isSecond) return;

                firstChild.setEnabled(true);
                secondChild.setEnabled(true);

                firstChild = null;
                secondChild = null;
                firstChildPos = 0;
                secondChildPos = 0;

                isClickable = true;

            }
        });
        flip.start();

    }

}
