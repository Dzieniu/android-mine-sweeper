package dzieniu.minesweeper.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import dzieniu.minesweeper.R;

public class HighscorePopup extends AppCompatActivity {

    EditText signYourself;
    Button buttonOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore_popup);

        signYourself = (EditText) findViewById(R.id.etSignYourself);
        buttonOk = (Button) findViewById(R.id.buttonOk);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!signYourself.getText().toString().matches("")) {

                    GameBoard.signature = signYourself.getText().toString();
                    setResult(4, null);
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
        getWindow().setLayout((int)(width*.8),(int)(height*.4));
    }
}
