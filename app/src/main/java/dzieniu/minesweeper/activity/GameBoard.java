package dzieniu.minesweeper.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import dzieniu.minesweeper.Difficulty;
import dzieniu.minesweeper.Field;
import dzieniu.minesweeper.GameSaver;
import dzieniu.minesweeper.R;
import dzieniu.minesweeper.activity.leaderboard.LeaderBoard;
import dzieniu.minesweeper.activity.leaderboard.UserDto;

public class GameBoard extends AppCompatActivity{

    private boolean isSave = false;
    private int width, height, mines, saveCounter;
    private Field minefield[][];

    private int totalFields,emptyFields, minesLeft;
    private int mode = 0;
    private int fieldsCounter = 0;
    private int flagCounter = 0;
    private long gameTime = 0;
    private int defuses = 3;
    private boolean isOver = false;

    private String gameState;

    private TextView tvMinesLeft, tvTime, tvDanger, tvDefusesLeft;
    private Button buttonReset, buttonSwitchMode, buttonDefuse;

    private static final String GAME_SAVE_FILE = "minesweeperSavedGameState.txt";

    private Timer timer = new Timer();

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);

        tvMinesLeft = findViewById(R.id.tvMinesLeft);
        tvTime = findViewById(R.id.tvTime);
        tvDanger = findViewById(R.id.tvDanger);
        tvDefusesLeft = findViewById(R.id.tvDefusesLeft);
        buttonReset = findViewById(R.id.buttonReset);
        buttonSwitchMode = findViewById(R.id.buttonSwitchMode);
        buttonDefuse = findViewById(R.id.buttonDefuse);

        isSave = getIntent().getBooleanExtra("isSave",false);

        if(isSave) {

            gameState = GameSaver.readFromFile(GAME_SAVE_FILE, getApplicationContext());
            Map<String, Integer> save = GameSaver.readSave(getApplicationContext());
            height = save.get("height");
            width = save.get("width");
            mines = save.get("mines");
            gameTime = save.get("time");
            defuses = save.get("defuses");
            minesLeft = save.get("minesLeft");
            saveCounter = save.get("counter");

        }else {

            width = getIntent().getIntExtra("width", 0);
            height = getIntent().getIntExtra("height", 0);
            mines = getIntent().getIntExtra("mines", 0);
            minesLeft = mines;

        }

        totalFields = height * width;
        emptyFields = (height * width) - mines;

        // core >>>
        generateMinefield();

        if(isSave) loadMines();
        else generateMines();

        setUpFieldsContent();
        setUpAdjacent();

        if(isSave){
            setUpGameState();
        }
        // <<<

        calculateDanger();

        tvMinesLeft.setText(minesLeft + "");
        tvDefusesLeft.setText("Defuses: " + defuses);
        if (mode == 0) {
            buttonSwitchMode.setText("Step");
        } else buttonSwitchMode.setText("Flag");

        buttonDefuse.setOnClickListener(v -> {
            if(defuses>0) {
                defuses--;
                tvDefusesLeft.setText("Defuses: " + defuses);
                defuse();
            }
        });

        buttonReset.setOnClickListener(v -> {
            Intent intent = getIntent();
            intent.putExtra("isSave",0);
            intent.putExtra("width",width);
            intent.putExtra("height",height);
            intent.putExtra("mines",mines);
            finish();
            startActivity(intent);
        });

        buttonSwitchMode.setOnClickListener(v -> {
            if (mode == 0) {
                mode = 1;
                buttonSwitchMode.setText("Flag");
            } else if (mode == 1) {
                mode = 0;
                buttonSwitchMode.setText("Step");
            }
        });
    }

    // Generates minefield structure
    private void generateMinefield(){

        LinearLayout layout = findViewById(R.id.gameBoard);
        layout.setOrientation(LinearLayout.VERTICAL);

        minefield = new Field[height+2][width+2];
        for(int i=0;i<height+2;i++){
            for(int j=0;j<width+2;j++) {
                minefield[i][j]=null;
            }
        }

        for (int i = 0; i < height; i++) {
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < width; j++){
                Button button = new Button(this);
                button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                button.setHeight(15);
                button.setWidth(15);
                button.setTextSize(24);
                button.setTypeface(Typeface.DEFAULT_BOLD);
                button.setBackground(ContextCompat.getDrawable(GameBoard.this,R.drawable.border2));
                button.setId(j + 1 + (i * 4));
                row.addView(button);
                final Field field = new Field(button,"");
                field.getButton().setOnClickListener(v -> {
                    if(mode==0) {
                        adjacent(field);
                    }else if(mode==1){
                        flag(field);
                    }
                    if(!isOver) {
                        saveGame();
                    }
                });
                minefield[i+1][j+1] = field;

            }

            layout.addView(row);
        }
    }

    // Randomly generates mines from the initial mine pool
    private void generateMines(){

            int x=0;
            int random1, random2;
            Random random = new Random();
            while(true) {

                random1 = random.nextInt(height) + 1;
                random2 = random.nextInt(width) + 1;

                if (minefield[random1][random2].getContent() != "x") {
                    minefield[random1][random2].setContent("x");
                    x++;
                }
                if (x == mines) break;
            }
    }

    // Loads mines from save file into the game board
    private void loadMines() {

        int counter = saveCounter;
        for(int i=1;i<height+1;i++){
            for(int j=1;j<width+1;j++) {

                if ((gameState.charAt(counter)+"").matches("2") || (gameState.charAt(counter)+"").matches("3")) {
                    minefield[i][j].setContent("x");
                }
                counter++;
            }
        }
    }

    // Counts number of adjacent mines for every field
    private void setUpFieldsContent(){

        for(int i=1;i<height+1;i++){
            for(int j=1;j<width+1;j++){
                if(minefield[i][j].getContent()=="x"){
                    continue;
                }else {
                    int counter = 0;
                    if(minefield[i-1][j-1]!=null && minefield[i-1][j-1].getContent()=="x"){
                        counter++;
                    }
                    if(minefield[i-1][j]!=null && minefield[i-1][j].getContent()=="x"){
                        counter++;
                    }
                    if(minefield[i-1][j+1]!=null && minefield[i-1][j+1].getContent()=="x"){
                        counter++;
                    }
                    if(minefield[i][j-1]!=null && minefield[i][j-1].getContent()=="x"){
                        counter++;
                    }
                    if(minefield[i][j+1]!=null && minefield[i][j+1].getContent()=="x"){
                        counter++;
                    }
                    if(minefield[i+1][j-1]!=null && minefield[i+1][j-1].getContent()=="x"){
                        counter++;
                    }
                    if(minefield[i+1][j]!=null && minefield[i+1][j].getContent()=="x"){
                        counter++;
                    }
                    if(minefield[i+1][j+1]!=null && minefield[i+1][j+1].getContent()=="x"){
                        counter++;
                    }
                    minefield[i][j].setContent(counter+"");
                }
            }
        }
    }

    // Sets up field adjacency
    private void setUpAdjacent(){
        for(int i=1;i<height+1;i++){
            for(int j=1;j<width+1;j++) {
                minefield[i][j].setNW(minefield[i-1][j-1]);
                minefield[i][j].setN(minefield[i-1][j]);
                minefield[i][j].setNE(minefield[i-1][j+1]);
                minefield[i][j].setW(minefield[i][j-1]);
                minefield[i][j].setE(minefield[i][j+1]);
                minefield[i][j].setSW(minefield[i+1][j-1]);
                minefield[i][j].setS(minefield[i+1][j]);
                minefield[i][j].setSE(minefield[i+1][j+1]);
            }
        }
    }

    public void setUpGameState(){

        int counter = saveCounter;
        for(int i=1;i<height+1;i++) {
            for (int j = 1; j < width + 1; j++) {

                if ((gameState.charAt(counter)+"").matches("1")) {
                    minefield[i][j].getButton().setBackgroundResource(R.drawable.border);
                    minefield[i][j].setClicked(1);
                    fieldsCounter++;
                    if(!minefield[i][j].getContent().contains("0")){
                        minefield[i][j].getButton().setText(minefield[i][j].getContent());
                        if(minefield[i][j].getContent().matches("1")) minefield[i][j].getButton().setTextColor(Color.parseColor("#FF2A701E"));
                        if(minefield[i][j].getContent().matches("2")) minefield[i][j].getButton().setTextColor(Color.parseColor("#3f51b5"));
                        if(minefield[i][j].getContent().matches("3")) minefield[i][j].getButton().setTextColor(Color.parseColor("#FF5B30A4"));
                        if(minefield[i][j].getContent().matches("4")) minefield[i][j].getButton().setTextColor(Color.parseColor("#ffff4444"));
                        if(minefield[i][j].getContent().matches("5")) minefield[i][j].getButton().setTextColor(Color.parseColor("#FFFAEB13"));
                        if(minefield[i][j].getContent().matches("6")) minefield[i][j].getButton().setTextColor(Color.parseColor("#FFE0BD32"));
                        if(minefield[i][j].getContent().matches("7")) minefield[i][j].getButton().setTextColor(Color.parseColor("#aaa"));
                        if(minefield[i][j].getContent().matches("8")) minefield[i][j].getButton().setTextColor(Color.parseColor("#FF000000"));
                    }
                    minefield[i][j].getButton().setClickable(false);
                }else if ((gameState.charAt(counter)+"").matches("3") || (gameState.charAt(counter)+"").matches("4")) {
                    minefield[i][j].setClicked(2);
                    minefield[i][j].getButton().setText("!");
                    minefield[i][j].getButton().setBackgroundResource(R.drawable.border3);
                    fieldsCounter++;
                    flagCounter++;
                    minesLeft--;
                    tvMinesLeft.setText(minesLeft + "");
                }
                counter++;
            }
        }
    }

    private void calculateDanger(){
        if(minesLeft!=0) {
            double danger = ((double) minesLeft / (totalFields - fieldsCounter)) * 100;
            tvDanger.setText("Danger: " + new BigDecimal(danger).setScale(2, RoundingMode.HALF_UP) + "%");
        }else{
            tvDanger.setText("Danger: 0%");
        }
    }

    private void adjacent(Field field) {

        if (field.getClicked() == 0) {

            field.getButton().setBackgroundResource(R.drawable.border);
            field.setClicked(1);
            fieldsCounter++;
            if(!field.getContent().contains("0")){
                field.getButton().setText(field.getContent());
                if(field.getContent().matches("1")) field.getButton().setTextColor(Color.parseColor("#FF2A701E"));
                if(field.getContent().matches("2")) field.getButton().setTextColor(Color.parseColor("#3f51b5"));
                if(field.getContent().matches("3")) field.getButton().setTextColor(Color.parseColor("#FF5B30A4"));
                if(field.getContent().matches("4")) field.getButton().setTextColor(Color.parseColor("#ffff4444"));
                if(field.getContent().matches("5")) field.getButton().setTextColor(Color.parseColor("#FFFAEB13"));
                if(field.getContent().matches("6")) field.getButton().setTextColor(Color.parseColor("#FFE0BD32"));
                if(field.getContent().matches("7")) field.getButton().setTextColor(Color.parseColor("#aaa"));
                if(field.getContent().matches("8")) field.getButton().setTextColor(Color.parseColor("#FF000000"));
            }
            field.getButton().setClickable(false);
            if(field.getContent() == "x"){
                gameOver();
            } else {
                if(field.getContent().contains("0")) {
                    if (field.getNW() != null) adjacent(field.getNW());
                    if (field.getN() != null) adjacent(field.getN());
                    if (field.getNE() != null) adjacent(field.getNE());
                    if (field.getW() != null) adjacent(field.getW());
                    if (field.getE() != null) adjacent(field.getE());
                    if (field.getSW() != null) adjacent(field.getSW());
                    if (field.getS() != null) adjacent(field.getS());
                    if (field.getSE() != null) adjacent(field.getSE());
                }
                checkForWin();
            }
        }
    }

    private void flag(Field field){
        if(field.getClicked() != 1) {
            if (field.getButton().getText() != "!") {
                if(minesLeft!=0) {
                    field.setClicked(2);
                    field.getButton().setText("!");
                    field.getButton().setBackgroundResource(R.drawable.border3);
                    fieldsCounter++;
                    flagCounter++;
                    minesLeft--;
                    tvMinesLeft.setText(minesLeft + "");
                    calculateDanger();
                }
            } else if (field.getButton().getText() == "!") {
                field.setClicked(0);
                field.getButton().setText("");
                field.getButton().setBackgroundResource(R.drawable.border2);
                fieldsCounter--;
                flagCounter--;
                minesLeft++;
                tvMinesLeft.setText(minesLeft + "");
                calculateDanger();
            }
        }
    }

    private void gameOver(){
        for(int i=1;i<height+1;i++){
            for(int j=1;j<width+1;j++) {
                if(minefield[i][j].getClicked()==0){
                    minefield[i][j].getButton().setText(minefield[i][j].getContent());
                    minefield[i][j].getButton().setClickable(false);
                    minefield[i][j].setClicked(1);
                }
            }
        }
        deleteFile(GAME_SAVE_FILE);
        isOver = true;
        Intent intent = new Intent(getApplicationContext(),GameEndPopup.class);
        intent.putExtra("gameResult","You lose ...");
        GameBoard.this.startActivityForResult(intent, 2);
    }

    private void checkForWin(){

        calculateDanger();
        if((fieldsCounter-flagCounter)==emptyFields){
            for(int i=1;i<height+1;i++){
                for(int j=1;j<width+1;j++) {
                    if(minefield[i][j].getClicked()==0){
                        minefield[i][j].getButton().setText(minefield[i][j].getContent());
                        minefield[i][j].getButton().setClickable(false);
                        minefield[i][j].setClicked(1);
                    }
                }
            }
            deleteFile(GAME_SAVE_FILE);
            if (getParent()!=null) getParent().recreate();
            isOver = true;

            Intent intent = new Intent(getApplicationContext(), GameEndPopup.class);
            intent.putExtra("gameResult", "You Win!!!");
            startActivityForResult(intent, 2);

            uploadHighScore();
        }
    }

    private void defuse(){

        Random generator = new Random();
        int random1,random2;

        while(true){

            random1 = generator.nextInt(height) + 1;
            random2 = generator.nextInt(width) + 1;

            if (minefield[random1][random2].getClicked()==0) {
                if(minefield[random1][random2].getContent().matches("x")) {
                    flag(minefield[random1][random2]);
                }else adjacent(minefield[random1][random2]);
                saveGame();
                break;
            }
        }
    }

    public void uploadHighScore(){

        Difficulty difficulty = Difficulty.difficultyOf(width, height, mines);

        if (difficulty != Difficulty.CUSTOM) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            databaseReference.child("highscores").child(difficulty.name().toLowerCase()).child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue()!=null) {
                        UserDto userDto = dataSnapshot.getValue(UserDto.class);
                        if (userDto.getTime() > gameTime) {
                            userDto.setTime(gameTime);
                            databaseReference.child("highscores").child(difficulty.name().toLowerCase()).child(user.getUid()).setValue(userDto);

                            Intent intent = new Intent(GameBoard.this,HighScorePopup.class);
                            startActivity(intent);
                        }
                    } else {
                        UserDto userDto = new UserDto(user.getDisplayName(), gameTime);
                        databaseReference.child("highscores").child(difficulty.name().toLowerCase()).child(user.getUid()).setValue(userDto);

                        Intent intent = new Intent(GameBoard.this,HighScorePopup.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
        if (isOver) {
            deleteFile(GAME_SAVE_FILE);
        } else {
            saveGame();
        }
    }

    @Override
    public void onPostResume() {
        super.onPostResume();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!isOver) {
                    tvTime.setText(gameTime + " s");
                    gameTime++;
                }
            }
        }, 1000, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 2) {
            switch (resultCode) {
                case 1:
                    playAgain();
                    break;
                case 3:
                    exitToHighScores();
                    break;
                case 4:
                    exitToMenu();
                    break;
            }
        }
    }

    private void exitToMenu() {
        deleteFile(GAME_SAVE_FILE);
        Intent intent = new Intent(getApplicationContext(), MainMenu.class);
        startActivity(intent);
        finish();
    }

    private void playAgain() {
        Intent intent = getIntent();
        intent.putExtra("isSave",0);
        intent.putExtra("width",width);
        intent.putExtra("height",height);
        intent.putExtra("mines",mines);
        startActivity(intent);
        finish();
    }

    private void exitToHighScores() {
        Intent intent = new Intent(getApplicationContext(), LeaderBoard.class);
        startActivity(intent);
        finish();
    }

    private void saveGame() {
        GameSaver.saveGameState(height,width,mines,gameTime,defuses, minesLeft, minefield,getApplicationContext());
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
