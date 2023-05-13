package com.keero.memorygame.Utils;

import java.util.Random;

public class ShuffleCards {

    public ShuffleCards(){
        //empty
    }
    public void shuffleCards(int[] cards, int num){
        Random random = new Random();

        for(int index = 0; index < num; index++){
            int randomNum = random.nextInt(num - index);

            int tempNum = cards[randomNum];
            cards[randomNum] = cards[index];
            cards[index] = tempNum;
        }
    }
}
