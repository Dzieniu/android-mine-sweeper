package dzieniu.minesweeper.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dzieniu.minesweeper.Field;
import dzieniu.minesweeper.GameSaver;
import dzieniu.minesweeper.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

public class GameBoard extends AppCompatActivity{

    public static int mines,over;
    public static int saveCounter,isSave,width,height;
    public static TextView tvMinesLeft,tvTime,tvRisk;
    private  Button buttonReset,buttonSwitchMode,buttonDefuse;
    public static Field minefield[][];
    public static int mode,allFields,emptyFields,fieldsCounter,flagCounter,defuses;
    long startTime = 0;
    public static String signature;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            tvTime.setText(String.format("%d:%02d", minutes, seconds));
            gameTime = seconds;

            timerHandler.postDelayed(this, 500);
        }
    };

    private long gameTime = 0;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);

        isSave = getIntent().getIntExtra("isSave",0);

        if(isSave==0) {
            width = getIntent().getIntExtra("width", 0);
            height = getIntent().getIntExtra("height", 0);
            mines = getIntent().getIntExtra("mines", 0);
        }else if(isSave==1 || isSave==2){
            Map<String, Integer> save = GameSaver.readSave(getApplicationContext());
            height = save.get("height");
            width = save.get("width");
            mines = save.get("mines");
            saveCounter = save.get("counter");
        }

            tvMinesLeft = (TextView) findViewById(R.id.tvMinesLeft);
            tvTime = (TextView) findViewById(R.id.tvTime);
            tvRisk = (TextView) findViewById(R.id.tvRisk);
            buttonReset = (Button) findViewById(R.id.buttonReset);
            buttonSwitchMode = (Button) findViewById(R.id.buttonSwitchMode);
            buttonDefuse = (Button) findViewById(R.id.buttonDefuse);

            defuses = 3;
            fieldsCounter = 0;
            flagCounter = 0;
            allFields = height * width;
            emptyFields = (height * width) - mines;
            startTime = System.currentTimeMillis();
            timerHandler.postDelayed(timerRunnable, 0);
            over = 0;

            buttonDefuse.setText(defuses+"");
            buttonDefuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(defuses>0) {
                        defuses--;
                        buttonDefuse.setText(defuses+"");
                        randomDefuse();
                    }
                }
            });

            buttonReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    intent.putExtra("isSave",0);
                    intent.putExtra("width",width);
                    intent.putExtra("height",height);
                    intent.putExtra("mines",mines);
                    finish();
                    startActivity(intent);
                }
            });

            buttonSwitchMode.setText("Step");
            buttonSwitchMode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mode == 0) {
                        mode = 1;
                        buttonSwitchMode.setText("Flag");
                    } else if (mode == 1) {
                        mode = 0;
                        buttonSwitchMode.setText("Step");
                    }
                }
            });

            tvMinesLeft.setText(mines + "");

            setUpMinefield();
            setUpMines();
            GameSaver.saveSeed(height,width,mines,minefield,getApplicationContext());
            setUpFields();
            setUpAdjacent();
            if(isSave==2){
                setUpGameState();
            }
            GameSaver.saveGameState(height,width,mines,minefield,getApplicationContext());
            calculateRisk();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setUpMinefield(){

        LinearLayout layout = (LinearLayout) findViewById(R.id.gameBoard);
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
                field.getButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(GameBoard.mode==0) {
                            adjacent(field);
                        }else if(GameBoard.mode==1){
                            flag(field);
                        }
                        if(over==0) {
                            GameSaver.saveGameState(height,width,mines,minefield,getApplicationContext());
                        }
                    }
                });
                minefield[i+1][j+1] = field;

            }

            layout.addView(row);
        }
    }

    private void randomDefuse(){

        Random generator = new Random();
        int random1,random2;

        while(true){

            random1 = generator.nextInt(height) + 1;
            random2 = generator.nextInt(width) + 1;

            if (minefield[random1][random2].getClicked()==0) {
                if(minefield[random1][random2].getContent().matches("x")) {
                    flag(minefield[random1][random2]);
                }else adjacent(minefield[random1][random2]);
                GameSaver.saveGameState(height,width,mines,minefield,getApplicationContext());
                break;
            }
        }
    }

    private void setUpMines(){

        if(isSave==0) {
            int x=0;
            int random1, random2;
            Random random = new Random();
            for (; ; ) {

                random1 = random.nextInt(height) + 1;
                random2 = random.nextInt(width) + 1;

                if (minefield[random1][random2].getContent() != "x") {
                    minefield[random1][random2].setContent("x");
                    x++;
                }
                if (x == mines) break;
            }
        }else if(isSave==1){
            String save = getIntent().getStringExtra("save");
            for(int i=1;i<height+1;i++){
                for(int j=1;j<width+1;j++) {

                    if ((save.charAt(saveCounter)+"").matches("1")) {
                        minefield[i][j].setContent("x");
                    }
                    saveCounter++;
                }
            }
        }else if(isSave==2){
            String save = getIntent().getStringExtra("save");
            int counter = saveCounter;
            for(int i=1;i<height+1;i++){
                for(int j=1;j<width+1;j++) {

                    if ((save.charAt(counter)+"").matches("2") || (save.charAt(counter)+"").matches("3")) {
                        minefield[i][j].setContent("x");
                    }
                    counter++;
                }
            }
        }
    }

    private void setUpFields(){

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
        String save = getIntent().getStringExtra("save");
        int counter = saveCounter;
        for(int i=1;i<height+1;i++) {
            for (int j = 1; j < width + 1; j++) {

                if ((save.charAt(counter)+"").matches("1")) {
                    minefield[i][j].getButton().setBackgroundResource(R.drawable.border);
                    minefield[i][j].setClicked(1);
                    GameBoard.fieldsCounter++;
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
                }else if ((save.charAt(counter)+"").matches("3") || (save.charAt(counter)+"").matches("4")) {
                    minefield[i][j].setClicked(2);
                    minefield[i][j].getButton().setText("!");
                    minefield[i][j].getButton().setBackgroundResource(R.drawable.border3);
                    fieldsCounter++;
                    flagCounter++;
                    tvMinesLeft.setText(Integer.parseInt(tvMinesLeft.getText() + "") - 1 + "");
                }
                counter++;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

    private void calculateRisk(){
        if(!tvMinesLeft.getText().toString().matches("0")) {
            String risk = "";
            risk = "" + ((double) Integer.parseInt(tvMinesLeft.getText() + "") / (allFields - fieldsCounter)) * 100;
            risk = risk.substring(0, 4);
            tvRisk.setText("risk: "+risk+"%");
        }else{
            tvRisk.setText("risk: 0%");
        }
    }

    //
    //          FIELD METHODS!!!          FIELD METHODS!!!          FIELD METHODS!!!          FIELD METHODS!!!
    //

    private void adjacent(Field field) {

        if (field.getClicked() == 0) {

            field.getButton().setBackgroundResource(R.drawable.border);
            field.setClicked(1);
            GameBoard.fieldsCounter++;
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
                if(!tvMinesLeft.getText().toString().matches("0")) {
                    field.setClicked(2);
                    field.getButton().setText("!");
                    field.getButton().setBackgroundResource(R.drawable.border3);
                    fieldsCounter++;
                    flagCounter++;
                    tvMinesLeft.setText(Integer.parseInt(tvMinesLeft.getText() + "") - 1 + "");
                    calculateRisk();
                }
            } else if (field.getButton().getText() == "!") {
                field.setClicked(0);
                field.getButton().setText("");
                field.getButton().setBackgroundResource(R.drawable.border2);
                fieldsCounter--;
                flagCounter--;
                tvMinesLeft.setText(Integer.parseInt(GameBoard.tvMinesLeft.getText()+"")+1+"");
                calculateRisk();
            }
        }
    }

    private void gameOver(){
        for(int i=1;i<height+1;i++){
            for(int j=1;j<width+1;j++) {
                if(minefield[i][j].getClicked()==0){
                    minefield[i][j].getButton().setText(GameBoard.minefield[i][j].getContent());
                    minefield[i][j].getButton().setClickable(false);
                    minefield[i][j].setClicked(1);
                }
            }
        }
        deleteFile("minesweeperSavedGameState.txt");
        over = 1;
        Intent intent = new Intent(GameBoard.this,GameEndPopup.class);
        intent.putExtra("wynik","You lose ...");
        GameBoard.this.startActivityForResult(intent, 2);
    }

    private void checkForWin(){

        calculateRisk();
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
            deleteFile("minesweeperSavedGameState.txt");
            over = 1;

            uploadHighScore();
            Intent intent = new Intent(GameBoard.this,HighscorePopup.class);
            GameBoard.this.startActivityForResult(intent, 2);
        }
    }

    //
    //          END OF FIELD METHODS!!!          END OF FIELD METHODS!!!          END OF FIELD METHODS!!!
    //

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 2) {
            if(resultCode == 1) {

                GameBoard.this.finish();
            }else if(resultCode == 2){

                Intent intent = getIntent();
                intent.putExtra("isSave",0);
                intent.putExtra("width",width);
                intent.putExtra("height",height);
                intent.putExtra("mines",mines);
                finish();
                startActivity(intent);
            }else if(resultCode == 3){
                finish();
                Intent intent = new Intent(GameBoard.this, GameBoard.class);
                intent.putExtra("isSave",1);
                intent.putExtra("save", GameSaver.readFromFile("minesweeperSavedSeed.txt",getApplicationContext()));
                GameBoard.this.startActivity(intent);
            }else if(resultCode == 4){

                Intent intent = new Intent(getApplicationContext(), GameEndPopup.class);
                intent.putExtra("wynik", "You Win!!!");
                startActivityForResult(intent, 2);
            }else if(resultCode == 5){

                Intent intent = new Intent(GameBoard.this, LeaderBoard.class);
                GameBoard.this.startActivity(intent);
                GameBoard.this.finish();
            }
        }
    }

    public void uploadHighScore(){

        String difficulty = "";
        boolean notCustom = false;

        if(height==9 && width==9 && mines==10) {
            difficulty = "beginner";
        }else if(height==12 && width==12 && mines==24) {
            difficulty = "easy";
        }else if(height==16 && width==16 && mines==40) {
            difficulty = "intermediate";
        }else if(height==30 && width==16 && mines==99) {
            difficulty = "expert";
        } else notCustom = false;


        if (notCustom) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            databaseReference.child("highscores/" + difficulty + "/" + user.getDisplayName()).setValue(gameTime);
        }
    }
}
