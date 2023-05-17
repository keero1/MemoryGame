package com.keero.memorygame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

import com.keero.memorygame.Fragments.Start;

import javax.xml.transform.Result;

public class MainActivity extends AppCompatActivity {

    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layoutFragment, new Start());
        transaction.commit();

    }

    @Override
    public void onBackPressed(){

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.layoutFragment);

        counter++;

        if(currentFragment instanceof Start){
            AlertDialog.Builder exit = new AlertDialog.Builder(this);
            exit.setTitle("Confirm exit app");

            exit.setPositiveButton("Yes", (dialog, which) -> finish());
            exit.setNegativeButton("No", (dialog, which) -> {
                // zxc
            });

            exit.show();
        }
        else if (currentFragment instanceof Result){
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            super.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }
}