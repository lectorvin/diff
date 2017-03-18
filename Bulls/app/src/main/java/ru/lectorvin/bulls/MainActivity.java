package ru.lectorvin.bulls;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private long score;
    private int[] user_number;
    private int winStrike, attempt_count;
    private HashMap<String, TextView> text;
    private ArrayList<Integer> guess_number;
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        score = mPreferences.getLong(getString(R.string.score), 0);
        winStrike = mPreferences.getInt(getString(R.string.strike), 0);
        String[] names = new String[] {"score", "strike", "attempt", "bull", "cow"};
        text = new HashMap<>();
        for (String name: names)
            text.put(name, ((TextView) getView(name)));
        user_number = new int[4];
        guess_number = new ArrayList<>();

        for(int i=0; i<4; i++) {
            user_number[i] = 0;
            text.put("num"+String.valueOf(i), (TextView)(getView("num"+String.valueOf(i))));
            final int myNumber = i;
            (getView("plus"+String.valueOf(i))).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (user_number[myNumber] < 9)
                        user_number[myNumber]++;
                    else
                        user_number[myNumber] = 0;
                    text.get("num"+myNumber).setText(String.valueOf(user_number[myNumber]));
                }
            });
            (getView("minus"+String.valueOf(i))).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (user_number[myNumber] > 0)
                        user_number[myNumber]--;
                    else
                        user_number[myNumber] = 9;
                    text.get("num"+myNumber).setText(String.valueOf(user_number[myNumber]));
                }
            });
        }
        newGame("Have fun!", false);
    }

    public View getView(String s) {
        return findViewById(getResources().getIdentifier(s, "id", getPackageName()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putLong(getString(R.string.score), score);
        editor.putInt(getString(R.string.strike), winStrike);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        score = mPreferences.getLong(getString(R.string.score), 0);
        winStrike = mPreferences.getInt(getString(R.string.strike), 0);
        text.get("score").setText(String.valueOf(score));
        text.get("strike").setText(String.valueOf(winStrike));
    }

    public void checkButtonAction (View v) {
        HashSet<Integer> set = new HashSet<>();
        for(int i : user_number)
            if(!set.add(i)) {
                (Toast.makeText(getApplicationContext(), getString(R.string.different),
                        Toast.LENGTH_SHORT)).show();
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
        text.get("cow").setText(String.valueOf(cow));
        text.get("bull").setText(String.valueOf(bull));
        text.get("attempt").setText(String.valueOf(--attempt_count));
        if (bull == 4) {
            winStrike++;
            changeScore();
            text.get("score").setText(String.valueOf(score));
            text.get("strike").setText(String.valueOf(winStrike));
            newGame(getString(R.string.winner), true);
        }
        else
            if (attempt_count == 0) {
                winStrike = 0;
                newGame(getString(R.string.loser), true);
            }
    }

    public void changeScore() {
        if (winStrike >= 5)
            score += 50;
        else
            score += winStrike*10;
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
        (Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT)).show();
        generateRandomNumber();
        attempt_count = 20;
        text.get("attempt").setText(getString(R.string.start_attempt));
        text.get("cow").setText("0");
        text.get("bull").setText("0");
        for(int i=0; i<4; i++) {
            user_number[i] = 0;
            text.get("num"+String.valueOf(i)).setText("0");
        }
    }
}
