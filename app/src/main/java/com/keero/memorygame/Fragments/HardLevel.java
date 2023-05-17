package com.keero.memorygame.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.keero.memorygame.Adapter.ViewAdapter;
import com.keero.memorygame.Constants;
import com.keero.memorygame.R;
import com.keero.memorygame.Utils.ShuffleCards;
import com.keero.memorygame.Utils.TimeHandler;
import com.keero.memorygame.Utils.TouchListener;

import java.util.ArrayList;

public class HardLevel extends Fragment {

    public ArrayList<Integer> cards;
    TimeHandler timeHandler;
    public int[] CARDS = {
            R.drawable.card_1,
            R.drawable.card_2,
            R.drawable.card_3,
            R.drawable.card_4,
            R.drawable.card_5,
            R.drawable.card_6,
            R.drawable.card_7,
            R.drawable.card_8,
            R.drawable.card_9,
            R.drawable.card_10,

            R.drawable.card_1,
            R.drawable.card_2,
            R.drawable.card_3,
            R.drawable.card_4,
            R.drawable.card_5,
            R.drawable.card_6,
            R.drawable.card_7,
            R.drawable.card_8,
            R.drawable.card_9,
            R.drawable.card_10,
    };

    int count, bestScore;

    public HardLevel() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_hard_level, container, false);
        RecyclerView hardLevelRecyclerView = view.findViewById(R.id.hardLevelView);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(Constants.PREF_NAME, 0);
        bestScore = sharedPreferences.getInt(Constants.USER_HARD_HIGH_KEY, (int) (Constants.HARD_TIMER / Constants.TIMER_INTERVAL));

        ((TextView) view.findViewById(R.id.high_score_text)).setText((String.valueOf(bestScore)));

        RecyclerView.LayoutManager manager = new GridLayoutManager(requireContext(), 4, LinearLayoutManager.VERTICAL, false);
        hardLevelRecyclerView.setLayoutManager(manager);

        cards = new ArrayList<>();

        //shuffle the cards
        ShuffleCards shuffle = new ShuffleCards();

        shuffle.shuffleCards(CARDS, Constants.HARD_NO_OF_CARDS);
        shuffle.shuffleCards(CARDS, Constants.HARD_NO_OF_CARDS);
        shuffle.shuffleCards(CARDS, Constants.HARD_NO_OF_CARDS);

        //shuffle it thrice

        for(int card : CARDS){
            cards.add(card);
        }

        // adapter
        hardLevelRecyclerView.setAdapter(new ViewAdapter(cards, true));

        // region Time handler
        timeHandler = new TimeHandler(view, requireContext(), getParentFragmentManager(),
                sharedPreferences, count, bestScore, true);

        // endregion

        // region Click Listener
        TouchListener touchListener = new TouchListener(cards, timeHandler, true);
        hardLevelRecyclerView.addOnItemTouchListener(touchListener);

        // endregion

        return view;
    }

    @Override
    public void onPause(){
        super.onPause();

        timeHandler.setPause(true);
    }

    @Override
    public void onResume(){
        super.onResume();

        if(!timeHandler.isPaused()) return;

        timeHandler.resumeTimer();
    }

    @Override
    public void onDestroy() {
        timeHandler.stopTimer();
        super.onDestroy();
    }
}