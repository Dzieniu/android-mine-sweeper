package dzieniu.minesweeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import dzieniu.minesweeper.GameSaver;
import dzieniu.minesweeper.R;

public class MainMenu extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private GoogleSignInClient mGoogleSignInClient;

    private Button buttonNewGame, buttonLeaderBoard,buttonContinue,buttonExit;

    private String SAVE_FILE = "minesweeperSavedGameState.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        buttonNewGame = findViewById(R.id.buttonNewGame);
        buttonLeaderBoard = findViewById(R.id.buttonLeaderboard);
        buttonContinue = findViewById(R.id.buttonContinue);
        buttonExit = findViewById(R.id.buttonExit);

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

        buttonLeaderBoard.setOnClickListener((event) -> {
            Intent intent = new Intent(MainMenu.this, LeaderBoard.class);
            MainMenu.this.startActivity(intent);
        });

        buttonExit.setOnClickListener(v ->{
            auth.signOut();
            mGoogleSignInClient.revokeAccess()
                    .addOnCompleteListener(this, task -> {
                        Intent intent = new Intent(getApplicationContext(),Login.class);
                        startActivity(intent);
                        finish();
                    });
        });

        String save = GameSaver.readFromFile(SAVE_FILE,getApplicationContext());
        if(!save.isEmpty()) {
            buttonContinue.setVisibility(View.VISIBLE);
            buttonContinue.setAllCaps(false);
        }
    }
}
