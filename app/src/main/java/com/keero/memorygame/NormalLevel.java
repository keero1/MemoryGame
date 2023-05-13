package com.keero.memorygame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.keero.memorygame.Adapter.NormalModeAdapter;
import com.keero.memorygame.Utils.ShuffleCards;
import com.keero.memorygame.Utils.TimeHandler;
import com.keero.memorygame.Utils.TouchListener;

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
    int count, bestScore;

    public NormalLevel() {
        // Required empty public constructor
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
        ShuffleCards shuffle = new ShuffleCards();

        shuffle.shuffleCards(CARDS, Constants.NO_OF_CARDS);
        shuffle.shuffleCards(CARDS, Constants.NO_OF_CARDS);
        shuffle.shuffleCards(CARDS, Constants.NO_OF_CARDS);

        //shuffle it thrice

        for(int card : CARDS){
            cards.add(card);
        }

        // adapter
        normalLevelRecyclerView.setAdapter(new NormalModeAdapter(cards));

        // region Time handler
        TimeHandler timeHandler = new TimeHandler(view, requireContext(), getParentFragmentManager(),
                sharedPreferences, count, bestScore, false);

        // endregion

        // region Click Listener
        TouchListener touchListener = new TouchListener(cards, timeHandler);
        normalLevelRecyclerView.addOnItemTouchListener(touchListener);

        // endregion

        return view;

    }
}