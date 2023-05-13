package com.keero.memorygame.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.keero.memorygame.R;

import java.util.ArrayList;

//TODO : combine this with the normal mode adapter since im just changing literally 1 value here.
public class HardModeAdapter extends RecyclerView.Adapter<HardModeAdapter.ViewHolder> {
    private final ArrayList<Integer> frontCard_Array;

    public HardModeAdapter(ArrayList<Integer> front_array) {
        this.frontCard_Array = front_array;
    }

    @NonNull
    @Override
    public HardModeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_hard,parent,false);
        view.setMinimumWidth(parent.getMeasuredWidth() / 4);
        return new HardModeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HardModeAdapter.ViewHolder holder, int position) {
        holder.frontCard.setImageResource(frontCard_Array.get(position));
    }

    @Override
    public int getItemCount() {
        return frontCard_Array.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageView frontCard;
        public ViewHolder(View itemView) {
            super(itemView);
            frontCard = itemView.findViewById(R.id.cardFront);
        }
    }

}
