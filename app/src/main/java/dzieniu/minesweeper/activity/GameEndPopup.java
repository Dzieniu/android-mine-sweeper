package dzieniu.minesweeper.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import dzieniu.minesweeper.R;

public class GameEndPopup extends AppCompatActivity {

    private Button buttonPlayAgain,buttonRestart,buttonHighscores,buttonEndGame;
    private TextView tvGameResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_end_popup);

        buttonPlayAgain = findViewById(R.id.buttonPlayAgain);
        buttonRestart = findViewById(R.id.buttonRestart);
        buttonHighscores = findViewById(R.id.buttonHighscores);
        buttonEndGame = findViewById(R.id.buttonEndGame);

        tvGameResult = findViewById(R.id.tvGameResult);
        tvGameResult.setText(getIntent().getStringExtra("wynik"));

        buttonPlayAgain.setOnClickListener(v -> {
            setResult(2, null);
            finish();
        });

        buttonRestart.setVisibility(View.GONE);
        buttonRestart.setOnClickListener(v -> {
            setResult(3, null);
            finish();
        });

        buttonHighscores.setOnClickListener(v -> {
            setResult(5, null);
            finish();
        });

        buttonEndGame.setOnClickListener(v -> {
            setResult(1, null);
            finish();
        });

        makeThisPopUp();
    }

    private void makeThisPopUp(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.8));
    }
}
