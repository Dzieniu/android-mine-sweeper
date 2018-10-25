package dzieniu.minesweeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import dzieniu.minesweeper.R;

public class DifficultyChoice extends AppCompatActivity {

    TextView tvDifficultyBeginner,tvDifficultyEasy,tvDifficultyIntermediate,tvDifficultyExpert,tvDifficultyCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_difficulty);
        DifficultyChoice.this.setTitle("Choose difficulty");

        tvDifficultyBeginner = (TextView) findViewById(R.id.tvDifficultyBeginner);
        tvDifficultyEasy = (TextView) findViewById(R.id.tvDifficultyEasy);
        tvDifficultyIntermediate = (TextView) findViewById(R.id.tvDifficultyIntermediate);
        tvDifficultyExpert = (TextView) findViewById(R.id.tvDifficultyExpert);
        tvDifficultyCustom = (TextView) findViewById(R.id.tvDifficultyCustom);

        tvDifficultyBeginner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGame(0,9, 9, 10, 60);
                finish();
            }
        });

        tvDifficultyEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGame(0,12, 12, 24, 120);
                finish();
            }
        });

        tvDifficultyIntermediate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGame(0,16, 16, 40, 140);
                finish();
            }
        });

        tvDifficultyExpert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGame(0,16, 30, 99, 180);
                finish();
            }
        });

        tvDifficultyCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DifficultyChoice.this, CustomGame.class);
                DifficultyChoice.this.startActivity(intent);
                finish();
            }
        });

    }

    private void makeThisPopUp(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.60));
    }

    public void startNewGame(int isSave,int width, int height, int mines, int time){
        Intent intent = new Intent(DifficultyChoice.this, GameBoard.class);
        intent.putExtra("isSave",0);
        intent.putExtra("width",width);
        intent.putExtra("height",height);
        intent.putExtra("mines",mines);
        intent.putExtra("time",time);
        DifficultyChoice.this.startActivity(intent);
    }
}
