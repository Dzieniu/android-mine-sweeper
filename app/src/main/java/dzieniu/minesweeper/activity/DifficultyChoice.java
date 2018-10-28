package dzieniu.minesweeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import dzieniu.minesweeper.R;

public class DifficultyChoice extends AppCompatActivity {

    TextView tvDifficultyBeginner,tvDifficultyEasy,tvDifficultyIntermediate,tvDifficultyExpert,tvDifficultyCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty_choice);
        DifficultyChoice.this.setTitle("Choose difficulty");

        tvDifficultyBeginner = findViewById(R.id.tvDifficultyBeginner);
        tvDifficultyEasy = findViewById(R.id.tvDifficultyEasy);
        tvDifficultyIntermediate = findViewById(R.id.tvDifficultyIntermediate);
        tvDifficultyExpert = findViewById(R.id.tvDifficultyExpert);
        tvDifficultyCustom = findViewById(R.id.tvDifficultyCustom);

        tvDifficultyBeginner.setOnClickListener(v -> {
            startNewGame(0,9, 9, 10, 60);
            finish();
        });

        tvDifficultyEasy.setOnClickListener(v -> {
            startNewGame(0,12, 12, 24, 120);
            finish();
        });

        tvDifficultyIntermediate.setOnClickListener(v -> {
            startNewGame(0,16, 16, 40, 140);
            finish();
        });

        tvDifficultyExpert.setOnClickListener(v -> {
            startNewGame(0,16, 30, 99, 180);
            finish();
        });

        tvDifficultyCustom.setOnClickListener(v -> {
            Intent intent = new Intent(DifficultyChoice.this, CustomGame.class);
            DifficultyChoice.this.startActivity(intent);
            finish();
        });

    }

    public void startNewGame(int isSave,int width, int height, int mines, int time){
        Intent intent = new Intent(DifficultyChoice.this, GameBoard.class);
        intent.putExtra("isSave",0);
        intent.putExtra("width",width);
        intent.putExtra("height",height);
        intent.putExtra("mines",mines);
        intent.putExtra("time",time);
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
