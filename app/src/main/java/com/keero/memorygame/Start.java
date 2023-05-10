package com.keero.memorygame;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Start extends Fragment {
    public Start() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_start, container, false);

        rootView.findViewById(R.id.play_game).setOnClickListener(v -> fragmentTransaction(new NormalLevel()));

        rootView.findViewById(R.id.leaderboard).setOnClickListener(v -> fragmentTransaction(new Leaderboards()));
        
        rootView.findViewById(R.id.settings).setOnClickListener(v -> fragmentTransaction(new Settings()));

        return rootView;
    }

    private void fragmentTransaction(Fragment fragment){
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.layoutFragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}