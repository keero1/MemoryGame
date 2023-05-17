package com.keero.memorygame.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.keero.memorygame.R;

// no blind people gonna use this anyway so Im just gonna silence the warning :)))
@SuppressLint("ClickableViewAccessibility")
public class Start extends Fragment {

    ImageView play_button, credits_button;
    Dialog dialog;
    ImageView normal_button, hard_button;
    Animation scaleUp, scaleDown;
    public Start() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_start, container, false);

        play_button = rootView.findViewById(R.id.play_game);
        credits_button = rootView.findViewById(R.id.credits);

        dialog = new Dialog(requireContext());


        scaleUp = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_down);

        play_button.setOnTouchListener((v, event) -> {

            if(event.getAction() == MotionEvent.ACTION_UP)
                play_button.startAnimation(scaleUp);

            play_button.startAnimation(scaleDown);

            displayDialog();

            return true;
        });

        credits_button.setOnTouchListener((v, event) -> {

            credits_button.startAnimation(scaleDown);

            if(event.getAction() == MotionEvent.ACTION_UP) {
                credits_button.startAnimation(scaleUp);
                fragmentTransaction(new Credits());
            }

            return true;
        });


        return rootView;
    }

    //popup

    private void displayDialog(){
        dialog.setContentView(R.layout.difficulty_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        normal_button = dialog.findViewById(R.id.normal_mode);
        hard_button = dialog.findViewById(R.id.hard_mode);

        normal_button.setOnTouchListener((v, event) -> {
            normal_button.startAnimation(scaleDown);

            if(event.getAction() == MotionEvent.ACTION_UP) {
                normal_button.startAnimation(scaleUp);

                fragmentTransaction(new NormalLevel());

                dialog.dismiss();
            }

            return true;
        });

        hard_button.setOnTouchListener((v, event) -> {
            hard_button.startAnimation(scaleDown);

            if(event.getAction() == MotionEvent.ACTION_UP) {
                hard_button.startAnimation(scaleUp);

                fragmentTransaction(new HardLevel());

                dialog.dismiss();
            }

            return true;
        });
    }

    // fragment

    private void fragmentTransaction(Fragment fragment){
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.layoutFragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}