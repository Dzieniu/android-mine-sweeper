package dzieniu.minesweeper.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import dzieniu.minesweeper.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class LeaderBoard extends AppCompatActivity {

    String difficulty;
    ArrayList<String> array;
    TextView tvBeginner,tvEasy,tvIntermediate,tvExpert;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);

        layout = (LinearLayout) findViewById(R.id.linearLayoutHighscores);

        tvBeginner = (TextView) findViewById(R.id.tvBeginner);
        tvEasy = (TextView) findViewById(R.id.tvEasy);
        tvIntermediate = (TextView) findViewById(R.id.tvIntermediate);
        tvExpert = (TextView) findViewById(R.id.tvExpert);

        tvBeginner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layout.getChildCount()>0) {
                    layout.removeAllViews();
                }
                difficulty = "beginner";
                tvBeginner.setBackgroundResource(R.drawable.border);
                tvEasy.setBackgroundResource(R.drawable.border2);
                tvIntermediate.setBackgroundResource(R.drawable.border2);
                tvExpert.setBackgroundResource(R.drawable.border2);
                tvBeginner.setPadding(0,15,0,15);
                tvEasy.setPadding(0,15,0,15);
                tvIntermediate.setPadding(0,15,0,15);
                tvExpert.setPadding(0,15,0,15);
//                loadHighscores();
            }
        });
        tvEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layout.getChildCount()>0) {
                    layout.removeAllViews();
                }
                difficulty = "easy";
                tvBeginner.setBackgroundResource(R.drawable.border2);
                tvEasy.setBackgroundResource(R.drawable.border);
                tvIntermediate.setBackgroundResource(R.drawable.border2);
                tvExpert.setBackgroundResource(R.drawable.border2);
                tvBeginner.setPadding(0,15,0,15);
                tvEasy.setPadding(0,15,0,15);
                tvIntermediate.setPadding(0,15,0,15);
                tvExpert.setPadding(0,15,0,15);
//                loadHighscores();
            }
        });
        tvIntermediate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layout.getChildCount()>0) {
                    layout.removeAllViews();
                }
                difficulty = "intermediate";
                tvBeginner.setBackgroundResource(R.drawable.border2);
                tvEasy.setBackgroundResource(R.drawable.border2);
                tvIntermediate.setBackgroundResource(R.drawable.border);
                tvExpert.setBackgroundResource(R.drawable.border2);
                tvBeginner.setPadding(0,15,0,15);
                tvEasy.setPadding(0,15,0,15);
                tvIntermediate.setPadding(0,15,0,15);
                tvExpert.setPadding(0,15,0,15);
//                loadHighscores();
            }
        });
        tvExpert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layout.getChildCount()>0) {
                    layout.removeAllViews();
                }
                difficulty = "expert";
                tvBeginner.setBackgroundResource(R.drawable.border2);
                tvEasy.setBackgroundResource(R.drawable.border2);
                tvIntermediate.setBackgroundResource(R.drawable.border2);
                tvExpert.setBackgroundResource(R.drawable.border);
                tvBeginner.setPadding(0,15,0,15);
                tvEasy.setPadding(0,15,0,15);
                tvIntermediate.setPadding(0,15,0,15);
                tvExpert.setPadding(0,15,0,15);
//                loadHighscores();
            }
        });

        difficulty = "beginner";
        tvBeginner.setPadding(0,15,0,15);
        tvEasy.setPadding(0,15,0,15);
        tvIntermediate.setPadding(0,15,0,15);
        tvExpert.setPadding(0,15,0,15);
//        loadHighscores();
    }

//    private void loadHighscores(){
//
//        String url ="";
//        if(difficulty.matches("beginner")){
//            url = "http://dd96.000webhostapp.com/receiveHighscoresBeginner.php";
//        }else if(difficulty.matches("easy")){
//            url = "http://dd96.000webhostapp.com/receiveHighscoresEasy.php";
//        }else if(difficulty.matches("intermediate")){
//            url = "http://dd96.000webhostapp.com/receiveHighscoresIntermediate.php";
//        }else if(difficulty.matches("expert")){
//            url = "http://dd96.000webhostapp.com/receiveHighscoresExpert.php";
//        }
//
//        Response.Listener<String> responseListener = new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//
//                    array = jsonStringToArray(response);
//
//                    int size = array.size();
//
//                    for (int i = 0; i < size; i=i+3) {
//
//                        TextView highscore = new TextView(LeaderBoard.this);
//                        highscore.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//                        highscore.setText(array.get(i)+" "+array.get(i+1)+" "+array.get(i+2));
//                        highscore.setGravity(Gravity.CENTER);
//                        highscore.setPadding(0,10,0,10);
//                        highscore.setTextSize(18);
//                        TextView line = new TextView(LeaderBoard.this);
//                        line.setHeight(1);
//                        line.setBackgroundColor(Color.parseColor("#ff000000"));
//                        layout.addView(highscore);
//                        layout.addView(line);
//                    }
//                }
//                catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        ReceiveHighscore receiveHighscore = new ReceiveHighscore(url, responseListener);
//        RequestQueue queue = Volley.newRequestQueue(LeaderBoard.this);
//        receiveHighscore.setRetryPolicy(new DefaultRetryPolicy(
//                1000,
//                50,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        queue.add(receiveHighscore);
//    }


    ArrayList<String> jsonStringToArray(String jsonString) throws JSONException {

        ArrayList<String> stringArray = new ArrayList<String>();

        JSONArray jsonArray = new JSONArray(jsonString);

        for (int i = 0; i < jsonArray.length(); i++) {
            String string;
            string = jsonArray.getString(i);
            int length = string.length();
            string = string.substring(2,length-2);
            stringArray.add(string);
        }

        return stringArray;
    }
}
