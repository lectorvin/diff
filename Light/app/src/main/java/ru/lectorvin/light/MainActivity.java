package ru.lectorvin.light;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity implements View.OnClickListener{
    private int[] rgb;
    private TextView textView;
    private RelativeLayout layout;
    private ArrayList<SeekBar> seekBars;

    protected int getId(String id) {
        return getResources().getIdentifier(id, "id", getPackageName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rgb = new int[] {0x30, 0xB0, 0xB0};
        textView = (TextView)findViewById(R.id.color_text);
        layout = (RelativeLayout)findViewById(R.id.Layout);
        seekBars = new ArrayList<>();
        String[] colors = {"red", "green", "blue"};
        for(String color : colors) {
            (findViewById(getId("minus_"+color+"_button"))).setOnTouchListener(new RepeatListener(300, 80, this));
            (findViewById(getId("plus_"+color+"_button"))).setOnTouchListener(new RepeatListener(300, 80, this));
            SeekBar bar = (SeekBar)findViewById(getId("seek_"+color));
            seekBars.add(bar);
            bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                  @Override
                  public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                      changeSeekBarColor(seekBar, progress);
                      textView.setText(String.format(Locale.getDefault(), "#%02X%02X%02X", rgb[0], rgb[1], rgb[2]));
                      layout.setBackgroundColor(Color.rgb(rgb[0], rgb[1], rgb[2]));
                  }

                  @Override
                  public void onStartTrackingTouch(SeekBar seekBar) {}

                  @Override
                  public void onStopTrackingTouch(SeekBar seekBar) {}
            });
        }
        initializeColor();
    }

    public void changeSeekBarColor(SeekBar seekBar, int progress) {
        String color = "#FFFFFF";
        switch (seekBar.getId()) {
            case R.id.seek_red:
                color = String.format(Locale.getDefault(), "#%02x0000", progress);
                rgb[0] = progress;
                break;
            case R.id.seek_green:
                color = String.format(Locale.getDefault(), "#00%02x00", progress);
                rgb[1] = progress;
                break;
            case R.id.seek_blue:
                color = String.format(Locale.getDefault(), "#0000%02x", progress);
                rgb[2] = progress;
                break;
        }
        seekBar.setBackgroundColor(Color.parseColor(color));
    }

    public void initializeColor() {
        for(int i=0; i<3; i++) {
            SeekBar bar = seekBars.get(i);
            bar.setProgress(rgb[i]);
            changeSeekBarColor(bar, rgb[i]);
        }
    }

    @Override
    public void onClick(View v) {
        int id = 0;
        switch (v.getId()) {
            case R.id.minus_red_button: if (rgb[0]>0) rgb[0]--; id = 0; break;
            case R.id.plus_red_button: if (rgb[0]<0xFF) rgb[0]++; id = 0; break;
            case R.id.minus_green_button: if (rgb[1]>0) rgb[1]--; id = 1; break;
            case R.id.plus_green_button: if (rgb[1]<0xFF) rgb[1]++; id = 1; break;
            case R.id.minus_blue_button: if (rgb[2]>0) rgb[2]--; id = 2; break;
            case R.id.plus_blue_button: if (rgb[2]<0xFF) rgb[2]++; id = 2; break;
        }
        seekBars.get(id).setProgress(rgb[id]);
        String textColor = String.format(Locale.getDefault(), "#%02X%02X%02X", rgb[0], rgb[1], rgb[2]);
        textView.setText(textColor);
        layout.setBackgroundColor(Color.parseColor(textColor));
    }
}


class RepeatListener implements View.OnTouchListener {
    private Handler handler = new Handler();
    private View downView;
    private int initialInterval, normalInterval;
    private final View.OnClickListener clickListener;

    RepeatListener(int initialInterval, int normalInterval, View.OnClickListener onClickListener) {
        this.normalInterval = normalInterval;
        this.initialInterval = initialInterval;
        this.clickListener = onClickListener;
    }

    private Runnable runnableHandler = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, normalInterval);
            clickListener.onClick(downView);
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handler.removeCallbacks(runnableHandler);
                handler.postDelayed(runnableHandler, initialInterval);
                downView = v;
                clickListener.onClick(v);
                return true;
            case MotionEvent.ACTION_UP:
                handler.removeCallbacks(runnableHandler);
                downView.setPressed(false);
                return true;
            case MotionEvent.ACTION_CANCEL:
                handler.removeCallbacks(runnableHandler);
                downView.setPressed(false);
                return false;
            default:
                return false;
        }
    }
}
