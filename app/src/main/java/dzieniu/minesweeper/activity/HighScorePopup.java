package dzieniu.minesweeper.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.Button;

import dzieniu.minesweeper.R;

public class HighScorePopup extends AppCompatActivity {

    Button buttonOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore_popup);

        buttonOk = findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(v -> {
                setResult(4, null);
                finish();
        });
        makeThisPopUp();
    }

    private void makeThisPopUp(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.3));
    }
}
