package dzieniu.minesweeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import dzieniu.minesweeper.R;

public class CustomGame extends AppCompatActivity {

    Button buttonCreateGame;
    EditText etHeight,etWidth,etMines;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_difficulty);

        etHeight = (EditText) findViewById(R.id.etHeight);
        etWidth = (EditText) findViewById(R.id.etWidth);
        etMines = (EditText) findViewById(R.id.etMines);

        buttonCreateGame = (Button) findViewById(R.id.buttonCreateGame);
        buttonCreateGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hei = Integer.parseInt(etHeight.getText().toString());
                int wid = Integer.parseInt(etWidth.getText().toString());
                int min = Integer.parseInt(etMines.getText().toString());
                if(checkTheInput(hei,wid,min)) {
                    startNewGame(hei, wid, min, 180);
                    finish();
                }
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

    public boolean checkTheInput(int hei,int wid,int min){
        if(etHeight.getText().toString()!=""){
            if(etWidth.getText().toString()!=""){
                if(min<(hei*wid)){
                    return true;
                }
            }
        }
        return false;
    }

    public void startNewGame(int width, int height, int mines, int time){
        Intent intent = new Intent(CustomGame.this, GameBoard.class);
        intent.putExtra("width",width);
        intent.putExtra("height",height);
        intent.putExtra("mines",mines);
        intent.putExtra("time",time);
        CustomGame.this.startActivity(intent);
    }
}
