package com.keero.memorygame.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.keero.memorygame.R;
import com.keero.memorygame.Settings;

public class Credits extends Fragment {

    public Credits() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_credits, container, false);

        view.findViewById(R.id.credits_box).setOnLongClickListener(v -> {
            fragmentTransaction(new Settings());
            return false;
        });

        return view;
    }

    private void fragmentTransaction(Fragment fragment){
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.layoutFragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}