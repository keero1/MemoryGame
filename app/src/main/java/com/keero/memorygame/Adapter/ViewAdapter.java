package com.keero.memorygame.Adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.keero.memorygame.R;
import com.keero.memorygame.Utils.FlipListener;

import java.util.ArrayList;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {
    private final ArrayList<Integer> frontCard_Array;
    private final boolean isHard;

    public ViewAdapter(ArrayList<Integer> front_array, boolean isHard) {
        this.frontCard_Array = front_array;
        this.isHard = isHard;
    }

    @NonNull
    @Override
    public ViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate( isHard ? R.layout.card_hard : R.layout.card,parent,false);
        view.setMinimumWidth(parent.getMeasuredWidth() / (isHard ? 4 : 3));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.frontCard.setImageResource(frontCard_Array.get(position));

        holder.itemView.setEnabled(false);

        new CountDownTimer(600, 1000) {
            public void onTick(long millisUntilFinished){
                // zxc
            }

            public void onFinish(){
                Flip(holder.itemView);

            }

        }.start();
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

    private void Flip(final View view){

        if(view == null) return;

        FlipListener flipListener = new FlipListener(view.findViewById(R.id.cardFront), view.findViewById(R.id.cardBack));

        ValueAnimator flip = ValueAnimator.ofFloat(0f, 1f);
        flip.addUpdateListener(flipListener);

        flip.setDuration(400);

        flip.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                view.setEnabled(true);
            }
        });
        flip.start();

    }


}
