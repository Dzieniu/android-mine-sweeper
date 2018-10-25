package dzieniu.minesweeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import dzieniu.minesweeper.GameSaver;
import dzieniu.minesweeper.R;

public class MainMenu extends AppCompatActivity {

    Button buttonNewGame,buttonLeaderboard,buttonContinue;

    String SAVE_FILE = "minesweeperSavedGameState.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        buttonNewGame = (Button) findViewById(R.id.buttonNewGame);
        buttonLeaderboard = (Button) findViewById(R.id.buttonLeaderboard);
        buttonContinue = (Button) findViewById(R.id.buttonContinue);

        buttonContinue.setOnClickListener((event) -> {
            Intent intent = new Intent(MainMenu.this, GameBoard.class);
            intent.putExtra("isSave",2);
            intent.putExtra("save",GameSaver.readFromFile(SAVE_FILE,getApplicationContext()));
            MainMenu.this.startActivity(intent);
        });

        buttonNewGame.setOnClickListener((event) -> {
            Intent intent = new Intent(MainMenu.this, DifficultyChoice.class);
            MainMenu.this.startActivity(intent);
        });

        buttonLeaderboard.setOnClickListener((event) -> {
            Intent intent = new Intent(MainMenu.this, LeaderBoard.class);
            MainMenu.this.startActivity(intent);
        });

        String save = GameSaver.readFromFile(SAVE_FILE,getApplicationContext());
        if(!save.isEmpty()) {
            buttonContinue.setVisibility(View.VISIBLE);
            buttonContinue.setAllCaps(false);
        }
    }
}
