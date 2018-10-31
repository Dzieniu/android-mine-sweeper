package dzieniu.minesweeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.TextView;

import dzieniu.minesweeper.R;

public class DifficultyChoice extends AppCompatActivity {

    TextView tvDifficultyBeginner,tvDifficultyEasy,tvDifficultyIntermediate,tvDifficultyExpert,tvDifficultyCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty_choice);

        tvDifficultyBeginner = findViewById(R.id.tvDifficultyBeginner);
        tvDifficultyEasy = findViewById(R.id.tvDifficultyEasy);
        tvDifficultyIntermediate = findViewById(R.id.tvDifficultyIntermediate);
        tvDifficultyExpert = findViewById(R.id.tvDifficultyExpert);
        tvDifficultyCustom = findViewById(R.id.tvDifficultyCustom);

        tvDifficultyBeginner.setOnClickListener(v -> {
            startNewGame(9, 9, 10);
            finish();
        });

        tvDifficultyEasy.setOnClickListener(v -> {
            startNewGame(12, 12, 24);
            finish();
        });

        tvDifficultyIntermediate.setOnClickListener(v -> {
            startNewGame(16, 16, 40);
            finish();
        });

        tvDifficultyExpert.setOnClickListener(v -> {
            startNewGame(16, 30, 99);
            finish();
        });

        tvDifficultyCustom.setOnClickListener(v -> {
            Intent intent = new Intent(DifficultyChoice.this, CustomGame.class);
            DifficultyChoice.this.startActivity(intent);
            finish();
        });

    }

    public void startNewGame(int width, int height, int mines){
        Intent intent = new Intent(DifficultyChoice.this, GameBoard.class);
        intent.putExtra("isSave",false);
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
