package dzieniu.minesweeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;

import dzieniu.minesweeper.R;

public class CustomGame extends AppCompatActivity {

    Button buttonCreateGame;
    TextView textViewHeight, textViewWidth, textViewMines, textViewDanger;
    SeekBar seekBarHeight, seekBarWidth, seekBarMines;

    private final int maxHeight = 50;
    private final int maxWidth = 50;

    private int height = 0;
    private int width = 0;
    private int mines = 0;
    private double danger = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_game);

        textViewHeight = findViewById(R.id.textViewHeight);
        textViewWidth = findViewById(R.id.textViewWidth);
        textViewMines = findViewById(R.id.textViewMines);
        textViewDanger = findViewById(R.id.textViewDanger);

        seekBarHeight = findViewById(R.id.seekBarHeight);
        seekBarWidth = findViewById(R.id.seekBarWidth);
        seekBarMines = findViewById(R.id.seekBarMines);

        seekBarHeight.setMax(maxHeight);
        seekBarWidth.setMax(maxWidth);
        seekBarMines.setMax(width * height);

        seekBarHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                height = i;
                textViewHeight.setText("Height: " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarMines.setMax(width * height);
            }
        });

        seekBarWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                width = i;
                textViewWidth.setText("Width: " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarMines.setMax(width * height);
            }
        });

        seekBarMines.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mines = i;
                textViewMines.setText("Mines: " + i);
                if (height>=1 && width>=1) {
                    danger = (double) mines/(height * width)*100;
                    textViewDanger.setText("Danger: " + new BigDecimal(danger).setScale(2, RoundingMode.HALF_UP) + "%");
                } else textViewDanger.setText("Danger: 0.00%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (danger > 20) Toast.makeText(getApplicationContext(), "Danger above 20% is not recommended", Toast.LENGTH_LONG).show();
            }
        });

        buttonCreateGame = findViewById(R.id.buttonCreateGame);
        buttonCreateGame.setOnClickListener(v -> {
            if (validSettings()) {
                startNewGame(height, width, mines);
                finish();
            }
        });
    }

    private boolean validSettings() {
        return  height>=1 && width>=1 && mines>=1;
    }

    public void startNewGame(int width, int height, int mines){
        Intent intent = new Intent(getApplicationContext(), GameBoard.class);
        intent.putExtra("width",width);
        intent.putExtra("height",height);
        intent.putExtra("mines",mines);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent(getApplicationContext(), MainMenu.class);
            startActivity(intent);
            finish();

        }
        return true;
    }
}
