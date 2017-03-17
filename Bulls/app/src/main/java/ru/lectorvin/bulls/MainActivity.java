package ru.lectorvin.bulls;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private int[] user_number;
    private int attempt_count;
    private ArrayList<Integer> guess_number;
    private ArrayList<TextView> numView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_number = new int[4];
        numView = new ArrayList<>();
        guess_number = new ArrayList<>();

        for(int i=0; i<4; i++) {
            user_number[i] = 0;
            numView.add((TextView)findViewById(getResources().getIdentifier(
                    "num" + Integer.toString(i), "id", getPackageName())));
            final int myNumber = i;
            (findViewById(getResources().getIdentifier("plus" + Integer.toString(i), "id",
                    getPackageName()))).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (user_number[myNumber] < 9)
                        user_number[myNumber]++;
                    else
                        user_number[myNumber] = 0;
                    numView.get(myNumber).setText(String.valueOf(user_number[myNumber]));
                }
            });
            (findViewById(getResources().getIdentifier("minus" + Integer.toString(i), "id",
                    getPackageName()))).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (user_number[myNumber] > 0)
                        user_number[myNumber]--;
                    else
                        user_number[myNumber] = 9;
                    numView.get(myNumber).setText(String.valueOf(user_number[myNumber]));
                }
            });
        }
        newGame("Have fun!", false);
    }

    public void checkButtonAction (View v) {
        HashSet<Integer> set = new HashSet<>();
        for(int i : user_number)
            if(!set.add(i)) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        getString(R.string.different), Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
        int bull=0, cow=0;
        for(int i=0; i<4; i++) {
            if (user_number[i] == guess_number.get(i))
                bull++;
            else
                if (guess_number.contains(user_number[i]))
                    cow++;
        }
        ((TextView)findViewById(R.id.cow)).setText(String.valueOf(cow));
        ((TextView)findViewById(R.id.bull)).setText(String.valueOf(bull));
        ((TextView)findViewById(R.id.attempt)).setText(String.valueOf(--attempt_count));
        if (bull == 4)
            newGame(getString(R.string.winner), true);
        if ((attempt_count == 0) && (bull != 4))
            newGame(getString(R.string.loser), true);
    }

    public void generateRandomNumber() {
        final Random random = new Random();
        int number = random.nextInt(10);
        guess_number.clear();
        for (int i=0; i<4; i++) {
            while (guess_number.contains(number))
                number = random.nextInt(10);
            guess_number.add(number);
        }
    }

    public void newGame(String s, boolean showAnswer) {
        if (showAnswer)
            s += "\tAnswer: " + guess_number.toString();
        Toast toast = Toast.makeText(getApplicationContext(), s,
                Toast.LENGTH_LONG);
        toast.show();
        generateRandomNumber();
        attempt_count = 20;
        ((TextView)findViewById(R.id.attempt)).setText(getString(R.string.start_attempt));
        ((TextView)findViewById(R.id.cow)).setText("0");
        ((TextView)findViewById(R.id.bull)).setText("0");
        for(int i=0; i<4; i++) {
            user_number[i] = 0;
            numView.get(i).setText("0");
        }
    }
}
