package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class Dice extends AppCompatActivity {

    final ArrayList<Integer> DiceID = new ArrayList<>(Arrays.asList(R.id.buttonD4,R.id.buttonD6,R.id.buttonD8,R.id.buttonD10,R.id.buttonD12,R.id.buttonD20,R.id.buttonD100,R.id.LanceDe,R.id.Reset));
    int D4,D6,D8,D10,D12,D20,D100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice);
        DiceButton();
    }

    public void Reset () {
        D4 = 0;
        D6 = 0;
        D8 = 0;
        D10 = 0;
        D12 = 0;
        D20 = 0;
        D100 = 0;
        Result.clear();
        NumberOfDice.clear();
    }

    public static Integer Random (int max) {
        return new Random().nextInt(max)+1;
    }

    ArrayList<Integer> NumberOfDice = new ArrayList<>();
    ArrayList<Integer> NumberDice = new ArrayList<>(Arrays.asList(4,6,8,10,12,20,100));
    ArrayList<Integer> Result = new ArrayList<>();
    public int Sum;

    public void DiceButton () {
        for (int i : DiceID) {
            findViewById(i).setOnClickListener(V -> Dice(i));
        }
    }

    @SuppressLint("SetTextI18n")
    public void Dice(int i) {
        if (i == DiceID.get(0)) {
            D4 += 1;
        }
        else if (i == DiceID.get(1)) {
            D6 += 1;
        }
        else if (i == DiceID.get(2)) {
            D8 += 1;
        }
        else if (i == DiceID.get(3)) {
            D10 += 1;
        }
        else if (i == DiceID.get(4)) {
            D12 += 1;
        }
        else if (i == DiceID.get(5)) {
            D20 += 1;
        }
        else if (i == DiceID.get(6)) {
            D100 += 1;
        }
        else if (i == DiceID.get(8)) {
            Reset();
        }
        else if (i == DiceID.get(7)) {
            NumberOfDice.add(D4);
            NumberOfDice.add(D6);
            NumberOfDice.add(D8);
            NumberOfDice.add(D10);
            NumberOfDice.add(D12);
            NumberOfDice.add(D20);
            NumberOfDice.add(D100);
            Sum = 0;
            for (int v = 0; v < NumberDice.size(); v++ ) {
                for (int j = 0; j < NumberOfDice.get(v); j++) {
                    Integer Resulted = Random(NumberDice.get(v));
                    Result.add(Resulted);
                    Sum += Resulted;
                }
            }
            StringBuilder TextCal;
            if (Result.size() > 1) {
                TextCal = new StringBuilder(Result.get(0).toString());
                for (int v = 1; v < Result.size(); v++) {
                    TextCal.append(" + ").append(Result.get(v).toString());
                }
                TextCal.append(" = ").append(Sum);
                ((TextView) findViewById(R.id.TextResultat)).setText(TextCal.toString());
            }
            else if (Result.size() == 0) {
                ((TextView) findViewById(R.id.TextResultat)).setText("Aucun dé selectionner");
            }
            else {
                ((TextView) findViewById(R.id.TextResultat)).setText(Integer.toString(Sum));
            }
            Reset();
        }
    }
}