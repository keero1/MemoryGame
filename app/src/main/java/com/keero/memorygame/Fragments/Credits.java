package com.keero.memorygame.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keero.memorygame.R;

public class Credits extends Fragment {

    public Credits() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//        view.findViewById(R.id.credits_box).setOnLongClickListener(v -> {
//            fragmentTransaction(new Settings());
//            return false;
//        });

        return inflater.inflate(R.layout.fragment_credits, container, false);
    }

//    private void fragmentTransaction(Fragment fragment){
//        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
//        transaction.replace(R.id.layoutFragment, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }
}