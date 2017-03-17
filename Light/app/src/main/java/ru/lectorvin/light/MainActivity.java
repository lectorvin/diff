package ru.lectorvin.light;

import android.app.Activity;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity implements View.OnTouchListener{
    private EditText text;
    private ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (EditText)findViewById(R.id.color_text);
        layout = (ConstraintLayout)findViewById(R.id.Layout);
        String[] buttons = {"minus8", "minusF", "minusFF", "plus8", "plusF", "plusFF"};
        for(String button:buttons) {
            (findViewById(getResources().getIdentifier(button+"_button", "id", getPackageName()))).setOnTouchListener(this);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int color = Integer.parseInt(text.getText().toString(), 16);
        switch (v.getId()) {
            case R.id.minus8_button: if (color>=8) color=color-8; break;
            case R.id.plus8_button: if (color<=0xfffff8) color=color+8; break;
            case R.id.minusF_button: if (color>=0xf) color=color-0xF; break;
            case R.id.plusF_button: if (color<=0xfffff0) color=color+0xF; break;
            case R.id.minusFF_button: if (color>=0xff) color=color-0xFF; break;
            case R.id.plusFF_button: if (color<=0xffff00) color=color+0xFF; break;
        }
        text.setText(String.format("%06x", color));
        layout.setBackgroundColor(Color.parseColor("#"+String.format("%06x", color)));
        return false;
    }

    public void setBackgroundColor(View v) {
        int color = Color.parseColor("#"+text.getText().toString());
        layout.setBackgroundColor(color);
    }
}
