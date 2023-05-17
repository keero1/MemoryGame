package com.keero.memorygame.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.keero.memorygame.R;

import java.util.ArrayList;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {
    private final ArrayList<Integer> frontCard_Array;
    private boolean isHard;
    public ViewAdapter(ArrayList<Integer> front_array, boolean isHard) {
        this.frontCard_Array = front_array;
        this.isHard = isHard;
    }

    @NonNull
    @Override
    public ViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card,parent,false);
        view.setMinimumWidth(parent.getMeasuredWidth() / (isHard ? 4 : 3));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
