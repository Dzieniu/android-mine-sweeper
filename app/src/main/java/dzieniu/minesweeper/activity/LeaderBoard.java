package dzieniu.minesweeper.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dzieniu.minesweeper.R;

public class LeaderBoard extends AppCompatActivity {

    String difficulty = "beginner";
    TextView tvBeginner,tvEasy,tvIntermediate,tvExpert;
    LinearLayout highScoreList;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    ValueEventListener databaseListener = new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            Iterable<DataSnapshot> children = dataSnapshot.getChildren();
            if(children.iterator().hasNext()) {
                for(DataSnapshot snapshot : children) {
                    addUserScore(snapshot.getKey() + " - " + snapshot.getValue());
                }
            } else addUserScore("No highscores found");
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        highScoreList = findViewById(R.id.highScoreList);

        tvBeginner = findViewById(R.id.tvBeginner);
        tvEasy = findViewById(R.id.tvEasy);
        tvIntermediate = findViewById(R.id.tvIntermediate);
        tvExpert = findViewById(R.id.tvExpert);

        tvBeginner.setOnClickListener(v -> {
            if(highScoreList.getChildCount()>0) {
                highScoreList.removeAllViews();
            }
            difficulty = "beginner";
            tvBeginner.setBackgroundResource(R.drawable.border);
            tvEasy.setBackgroundResource(R.drawable.border2);
            tvIntermediate.setBackgroundResource(R.drawable.border2);
            tvExpert.setBackgroundResource(R.drawable.border2);

            loadHighscores();
        });
        tvEasy.setOnClickListener(v -> {
            if(highScoreList.getChildCount()>0) {
                highScoreList.removeAllViews();
            }
            difficulty = "easy";
            tvBeginner.setBackgroundResource(R.drawable.border2);
            tvEasy.setBackgroundResource(R.drawable.border);
            tvIntermediate.setBackgroundResource(R.drawable.border2);
            tvExpert.setBackgroundResource(R.drawable.border2);

            loadHighscores();
        });
        tvIntermediate.setOnClickListener(v -> {
            if(highScoreList.getChildCount()>0) {
                highScoreList.removeAllViews();
            }
            difficulty = "intermediate";
            tvBeginner.setBackgroundResource(R.drawable.border2);
            tvEasy.setBackgroundResource(R.drawable.border2);
            tvIntermediate.setBackgroundResource(R.drawable.border);
            tvExpert.setBackgroundResource(R.drawable.border2);

            loadHighscores();
        });
        tvExpert.setOnClickListener(v -> {
            if(highScoreList.getChildCount()>0) {
                highScoreList.removeAllViews();
            }
            difficulty = "expert";
            tvBeginner.setBackgroundResource(R.drawable.border2);
            tvEasy.setBackgroundResource(R.drawable.border2);
            tvIntermediate.setBackgroundResource(R.drawable.border2);
            tvExpert.setBackgroundResource(R.drawable.border);

            loadHighscores();
        });

        loadHighscores();

//        databaseReference.child("highscores/beginner/Dawid Dzien").setValue("123");
//        databaseReference.child("highscores/beginner/Inny ziomek").setValue("156");
//        databaseReference.child("highscores/beginner/Inna laska").setValue("178");
//        databaseReference.child("highscores/intermediate/Dawid Dzien").setValue("111");
    }

    private void addUserScore(String highscore) {
        TextView highScore = new TextView(LeaderBoard.this);
        highScore.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        highScore.setText(highscore);
        highScore.setGravity(Gravity.CENTER);
        highScore.setPadding(0,10,0,10);
        highScore.setTextSize(18);
        TextView line = new TextView(LeaderBoard.this);
        line.setHeight(1);
        line.setBackgroundColor(Color.parseColor("#ff000000"));
        highScoreList.addView(highScore);
        highScoreList.addView(line);
    }

    private void loadHighscores(){
        databaseReference.child("highscores").child(difficulty).addListenerForSingleValueEvent(databaseListener);
    }
}
