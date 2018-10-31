package dzieniu.minesweeper.activity.leaderboard;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import dzieniu.minesweeper.R;

public class TabIntermediate extends Fragment {

    private static final String TAB_DIFFICULTY = "intermediate";

    private LinearLayout linearLayout;

    private NestedScrollView scrollView;

    private DatabaseReference databaseReference;

    private int loadLimit = 24;
    private final int loadIncrease = 24;

    ValueEventListener databaseListener = new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if(getContext() != null) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                if (children.iterator().hasNext()) {
                    linearLayout.removeAllViews();
                    for (DataSnapshot snapshot : children) {
                        UserDto userDto = snapshot.getValue(UserDto.class);
                        addUserScore(userDto.getDisplayName() + " - " + userDto.getTime());
                    }
                } else {
                    TextView result = new TextView(getContext());
                    result.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    result.setText("No highscores found");
                    result.setGravity(Gravity.CENTER);
                    result.setPadding(0, 40, 0, 10);
                    result.setTextSize(22);
                    linearLayout.addView(result);
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_intermediate, container, false);
        linearLayout = view.findViewById(R.id.intermediateList);
        scrollView = view.findViewById(R.id.intermediateScrollView);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (!scrollView.canScrollVertically(1)) {
                    loadLimit += loadIncrease;
                    databaseReference.child("highscores").child(TAB_DIFFICULTY).orderByValue().limitToFirst(loadLimit).addValueEventListener(databaseListener);
                }
            }
        });
        databaseReference.child("highscores").child(TAB_DIFFICULTY).orderByValue().limitToFirst(loadLimit).addValueEventListener(databaseListener);
        return view;
    }

    private void addUserScore(String highscore) {
        TextView highScore = new TextView(getContext());
        highScore.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        highScore.setText(highscore);
        highScore.setGravity(Gravity.CENTER);
        highScore.setPadding(0,10,0,10);
        highScore.setTextSize(18);
        TextView line = new TextView(getContext());
        line.setHeight(1);
        line.setBackgroundColor(Color.parseColor("#ff000000"));
        linearLayout.addView(highScore);
        linearLayout.addView(line);
    }
}
