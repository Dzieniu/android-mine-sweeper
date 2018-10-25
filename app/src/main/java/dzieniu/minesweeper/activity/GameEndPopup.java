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

        buttonPlayAgain = (Button) findViewById(R.id.buttonPlayAgain);
        buttonRestart = (Button) findViewById(R.id.buttonRestart);
        buttonHighscores = (Button) findViewById(R.id.buttonHighscores);
        buttonEndGame = (Button) findViewById(R.id.buttonEndGame);

        tvGameResult = (TextView) findViewById(R.id.tvGameResult);
        tvGameResult.setText(getIntent().getStringExtra("wynik"));

        buttonPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(2, null);
                finish();
            }
        });

        buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(3, null);
                finish();
            }
        });

        buttonHighscores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(5, null);
                finish();
            }
        });

        buttonEndGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1, null);
                finish();
            }
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
