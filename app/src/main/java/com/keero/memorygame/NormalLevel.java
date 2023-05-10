package com.keero.memorygame;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.keero.memorygame.Adapter.FlipListener;
import com.keero.memorygame.Adapter.NormalModeAdapter;

import java.util.ArrayList;
import java.util.Random;

public class NormalLevel extends Fragment {
    public ArrayList<Integer> cards;
    //images
    public int[] CARDS = {
            R.drawable.card_1,
            R.drawable.card_2,
            R.drawable.card_3,
            R.drawable.card_4,
            R.drawable.card_5,
            R.drawable.card_6,

            R.drawable.card_1,
            R.drawable.card_2,
            R.drawable.card_3,
            R.drawable.card_4,
            R.drawable.card_5,
            R.drawable.card_6
    };
    // for the click handler
    private Boolean isClickable = true;
    private View firstChild, secondChild;

    private int firstChildPos, secondChildPos;


    private ValueAnimator flip;
    public long RemainingTime;
    public boolean isPaused, isCancelled;
    int count, bestScore;
    private SharedPreferences editablePreferences;
    private SharedPreferences.Editor editor;

    public NormalLevel() {
        // Required empty public constructor
    }

    public void shuffleCards(int[] cards, int num){
        Random random = new Random();

        for(int index = 0; index < num; index++){
            int randomNum = random.nextInt(num - index);

            int tempNum = cards[randomNum];
            cards[randomNum] = cards[index];
            cards[index] = tempNum;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_normal_level, container, false);

        RecyclerView normalLevelRecyclerView = view.findViewById(R.id.normalLevelView);

        //data
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(Constants.PREF_NAME, 0);
        bestScore = sharedPreferences.getInt(Constants.USER_HIGH_KEY, (int) (Constants.TIMER / Constants.TIMER_INTERVAL));

        ((TextView) view.findViewById(R.id.high_score_text)).setText((String.valueOf(bestScore)));

        RecyclerView.LayoutManager manager = new GridLayoutManager(requireContext(), 3, LinearLayoutManager.VERTICAL, false);
        normalLevelRecyclerView.setLayoutManager(manager);

        cards = new ArrayList<>();

        //shuffle the cards
        shuffleCards(CARDS, Constants.NO_OF_CARDS);
        shuffleCards(CARDS, Constants.NO_OF_CARDS);
        shuffleCards(CARDS, Constants.NO_OF_CARDS);

        //shuffle it thrice

        for(int card : CARDS){
            cards.add(card);
        }

        normalLevelRecyclerView.setAdapter(new NormalModeAdapter(cards));

        isPaused = false;
        isCancelled = false;

        new CountDownTimer(Constants.TIMER, Constants.TIMER_INTERVAL){
            @Override
            public void onTick(long millisUntilFinished){
                if(isPaused || isCancelled){
                    cancel();
                } else {
                    ((TextView) view.findViewById(R.id.timer_text)).setText(String.valueOf(millisUntilFinished / Constants.TIMER_INTERVAL));
                    RemainingTime = millisUntilFinished;

                    if(count == Constants.NO_OF_PAIRS){
                        long time = (Constants.TIMER - millisUntilFinished) / Constants.TIMER_INTERVAL;

                        if(time < bestScore){
                            editablePreferences = requireActivity().getSharedPreferences("HighScore", 0);
                            editor = editablePreferences.edit();

                            editor.putInt(Constants.USER_HIGH_KEY, (int) time).apply();

                            DialogueMaker("New HighScore!", "high score placeholder");

                        } else {
                            DialogueMaker("You won!", "win placeholder");
                        }

                        //win

                        cancel();
                        this.onFinish();
                    }

                }
            }

            @Override
            public void onFinish() {
                if(count < Constants.NO_OF_PAIRS) {

                    // lost
                    DialogueMaker("Game Over", "game over placeholder");

                }
            }

        }.start();

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    isPaused = true;
                    AlertDialog.Builder pause = new AlertDialog.Builder(getContext());
                    pause.setTitle("Game Paused");
                    pause.setCancelable(false);
                    pause.setMessage("Quit?");
                    pause.setPositiveButton("Resume", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isPaused = false;
                            new CountDownTimer(RemainingTime, Constants.TIMER_INTERVAL) {
                                @Override
                                public void onTick(long millisUntilFinished){
                                    if(isPaused || isCancelled) {
                                        cancel();
                                    } else {
                                        ((TextView) view.findViewById(R.id.timer_text)).setText(String.valueOf(millisUntilFinished / Constants.TIMER_INTERVAL));
                                        RemainingTime = millisUntilFinished;

                                        if(RemainingTime == 0){
                                            this.onFinish();
                                        }
                                    }
                                }
                                @Override
                                public void onFinish() {
                                    Toast.makeText(getActivity(), "no time", Toast.LENGTH_SHORT).show();
                                }

                            }.start();


                        }
                    });
                    pause.setNegativeButton("Quit", (dialog, which) -> {
                        isCancelled = true;
                        getParentFragmentManager().popBackStack();
                    });
                    pause.show();
                    return true;
                }
                return false;
            }
        });

        // buttons

        normalLevelRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

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
                    if(count == 5) count += 1;

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
        });

        return view;

    }

    private void Flip(final View view){

        if(view == null) return;

        FlipListener flipListener = new FlipListener(view.findViewById(R.id.cardBack), view.findViewById(R.id.cardFront));

        flip = ValueAnimator.ofFloat(0f, 1f);
        flip.addUpdateListener(flipListener);

        flip.setDuration(400);

//        flip.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//
//
//            }
//        });
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

    private void DialogueMaker(String title, String message){
        AlertDialog.Builder dialogue = new AlertDialog.Builder(getContext());
        dialogue.setTitle(title);
        dialogue.setCancelable(false);
        dialogue.setMessage(message);
        dialogue.setNegativeButton("Quit", (dialog, which) -> {
            isCancelled = true;
            getParentFragmentManager().popBackStack();
        });
        dialogue.show();
    }

}