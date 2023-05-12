package com.keero.memorygame;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

public class Start extends Fragment {

    ImageView play_button, credits_button;
    RelativeLayout layout;
    Animation scaleUp, scaleDown;
    public Start() {
        // Required empty public constructor
    }

    // no blind people gonna use this anyway so Im just gonna silence the warning :)))
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_start, container, false);

        play_button = rootView.findViewById(R.id.play_game);
        credits_button = rootView.findViewById(R.id.credits);

        layout = rootView.findViewById(R.id.relative);


        scaleUp = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_down);


        play_button.setOnTouchListener((v, event) -> {

            if(event.getAction() == MotionEvent.ACTION_UP) {
                play_button.startAnimation(scaleUp);
                fragmentTransaction(new NormalLevel());
            } else {
                play_button.startAnimation(scaleDown);

            }
            return true;
        });

        credits_button.setOnTouchListener((v, event) -> {

            if(event.getAction() == MotionEvent.ACTION_UP) {
                credits_button.startAnimation(scaleUp);
                fragmentTransaction(new Credits());
            } else {
                credits_button.startAnimation(scaleDown);
            }

            return true;
        });


        return rootView;
    }

    // fragment

    private void fragmentTransaction(Fragment fragment){
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.layoutFragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}