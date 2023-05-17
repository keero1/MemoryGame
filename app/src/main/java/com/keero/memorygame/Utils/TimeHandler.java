package com.keero.memorygame.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.keero.memorygame.Constants;
import com.keero.memorygame.Fragments.HardLevel;
import com.keero.memorygame.Fragments.NormalLevel;
import com.keero.memorygame.R;

public class TimeHandler {
    //timer
    private CountDownTimer mainTimer, pauseTimer;
    private boolean isPaused;
    private boolean isCancelled;
    private long RemainingTime;

    // views
    private final View view;
    private final Context context;

    //fragment manager
    private final FragmentManager fragmentManager;

    //preferences
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private final int bestScore;
    private int count;

    // for difficulty

    private final boolean isHard;

    public TimeHandler(View view, Context context,FragmentManager fragmentManager,
                       SharedPreferences pref, int count, int bestScore, boolean isHard){

        this.view = view;
        this.context = context;

        this.fragmentManager = fragmentManager;
        this.pref = pref;

        this.count = count;
        this.bestScore = bestScore;

        this.isHard = isHard;

        startCountDown();
        setOnKeyListener();

    }


    // im lazy rn so its ternary operator to change the value

    private void startCountDown() {

        mainTimer = new CountDownTimer(isHard ? Constants.HARD_TIMER : Constants.TIMER, Constants.TIMER_INTERVAL){
            @Override
            public void onTick(long millisUntilFinished){
                if(isPaused || isCancelled){
                    cancel();
                } else {
                    ((TextView) view.findViewById(R.id.timer_text)).setText(String.valueOf(millisUntilFinished / Constants.TIMER_INTERVAL));
                    RemainingTime = millisUntilFinished;

                    if(count == (isHard ? Constants.HARD_NO_OF_PAIRS : Constants.NO_OF_PAIRS)){
                        long time = ((isHard ? Constants.HARD_TIMER : Constants.TIMER) - millisUntilFinished) / Constants.TIMER_INTERVAL;

                        if(time < bestScore){
                            pref = context.getSharedPreferences("HighScore", 0);
                            editor = pref.edit();

                            editor.putInt(isHard ? Constants.USER_HARD_HIGH_KEY : Constants.USER_HIGH_KEY, (int) time).apply();

                            DialogueMaker("New HighScore", "Congratulations for setting a new high score");

                        } else {
                            DialogueMaker("You won", "You've matched all available pairs!");
                        }

                        //win

                        cancel();
                        this.onFinish();
                    }

                }
            }

            @Override
            public void onFinish() {
                if(count < (isHard ? Constants.HARD_NO_OF_PAIRS : Constants.NO_OF_PAIRS)) {

                    // lost
                    DialogueMaker("Game Over", "You ran out of time! Better luck next time.");

                }
            }

        }.start();

    }

    private void setOnKeyListener() {

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                Fragment currentFragment = fragmentManager.findFragmentById(R.id.layoutFragment);

                if(currentFragment instanceof HardLevel || currentFragment instanceof NormalLevel) {

                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                        isPaused = true;
                        AlertDialog.Builder pause = new AlertDialog.Builder(context);
                        pause.setTitle("Game Paused");
                        pause.setCancelable(false);
                        pause.setMessage("Quit?");
                        pause.setPositiveButton("Resume", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                isPaused = false;
                                pauseTimer = new CountDownTimer(RemainingTime, Constants.TIMER_INTERVAL) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        if (isPaused || isCancelled) {
                                            cancel();
                                        } else {
                                            ((TextView) view.findViewById(R.id.timer_text)).setText(String.valueOf(millisUntilFinished / Constants.TIMER_INTERVAL));
                                            RemainingTime = millisUntilFinished;

                                            if (count == (isHard ? Constants.HARD_NO_OF_PAIRS : Constants.NO_OF_PAIRS)) {
                                                long time = ((isHard ? Constants.HARD_TIMER : Constants.TIMER) - millisUntilFinished) / Constants.TIMER_INTERVAL;

                                                if (time < bestScore) {
                                                    pref = context.getSharedPreferences("HighScore", 0);
                                                    editor = pref.edit();

                                                    editor.putInt(isHard ? Constants.USER_HARD_HIGH_KEY : Constants.USER_HIGH_KEY, (int) time).apply();

                                                    DialogueMaker("New HighScore", "Congratulations for setting a new high score");

                                                } else {
                                                    DialogueMaker("You won", "You've matched all available pairs!");
                                                }

                                                //win

                                                cancel();
                                                this.onFinish();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFinish() {
                                        if (count < (isHard ? Constants.HARD_NO_OF_PAIRS : Constants.NO_OF_PAIRS)) {
                                            // lost
                                            DialogueMaker("Game Over", "You ran out of time! Better luck next time.");

                                        }
                                    }

                                }.start();


                            }
                        });
                        pause.setNegativeButton("Quit", (dialog, which) -> {
                            isCancelled = true;

                            fragmentManager.popBackStack();
                        });
                        pause.show();
                        return true;
                    }
                }
                return false;
            }
        });

    }

    //countdown timer does not stop when I refresh the app (re-running and reapplying code)
    public void stopTimer(){
        if(pauseTimer != null) pauseTimer.cancel();
        mainTimer.cancel();
    }

    public void resumeTimer(){
        isPaused = false;
        pauseTimer = new CountDownTimer(RemainingTime, Constants.TIMER_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished){
                if(isPaused || isCancelled) {
                    cancel();
                } else {
                    ((TextView) view.findViewById(R.id.timer_text)).setText(String.valueOf(millisUntilFinished / Constants.TIMER_INTERVAL));
                    RemainingTime = millisUntilFinished;

                    if(count == (isHard ? Constants.HARD_NO_OF_PAIRS : Constants.NO_OF_PAIRS)){
                        long time = ((isHard ? Constants.HARD_TIMER : Constants.TIMER) - millisUntilFinished) / Constants.TIMER_INTERVAL;

                        if(time < bestScore){
                            pref = context.getSharedPreferences("HighScore", 0);
                            editor = pref.edit();

                            editor.putInt(isHard ? Constants.USER_HARD_HIGH_KEY : Constants.USER_HIGH_KEY, (int) time).apply();

                            DialogueMaker("New HighScore", "Congratulations for setting a new high score");

                        } else {
                            DialogueMaker("You won", "You've matched all available pairs!");
                        }

                        //win

                        cancel();
                        this.onFinish();
                    }
                }
            }
            @Override
            public void onFinish() {
                if(count < (isHard ? Constants.HARD_NO_OF_PAIRS : Constants.NO_OF_PAIRS)) {
                    // lost
                    DialogueMaker("Game Over", "You ran out of time! Better luck next time.");

                }
            }

        }.start();
    }

    public void setPause(boolean isPaused){
        this.isPaused = isPaused;
    }

    public boolean isPaused(){
        return isPaused;
    }

    public void setCount(int count){
        this.count = count;
    }

    //dialogue

    private void DialogueMaker(String title, String message){
        AlertDialog.Builder dialogue = new AlertDialog.Builder(context);
        dialogue.setTitle(title);
        dialogue.setCancelable(false);
        dialogue.setMessage(message);

        dialogue.setPositiveButton("Retry", (dialog, which) -> {

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.layoutFragment, isHard ? new HardLevel() : new NormalLevel());
            transaction.commit();

        });

        dialogue.setNegativeButton("Quit", (dialog, which) -> {
            isCancelled = true;
            fragmentManager.popBackStack();
        });
        dialogue.show();
    }

}
