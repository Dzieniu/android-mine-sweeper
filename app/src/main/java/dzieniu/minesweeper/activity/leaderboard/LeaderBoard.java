package dzieniu.minesweeper.activity.leaderboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dzieniu.minesweeper.R;
import dzieniu.minesweeper.activity.MainMenu;

public class LeaderBoard extends AppCompatActivity {

    private ViewPager viewPager;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        viewPager = findViewById(R.id.container);
        setUpViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setUpViewPager(ViewPager viewPager) {

        SectionsPageAdapter sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        TabBeginner tabBeginner = new TabBeginner();
        tabBeginner.setDatabaseReference(databaseReference);
        sectionsPageAdapter.addFragment(tabBeginner, "BEGINNER");

        TabEasy tabEasy = new TabEasy();
        tabEasy.setDatabaseReference(databaseReference);
        sectionsPageAdapter.addFragment(tabEasy, "EASY");

        TabIntermediate tabIntermediate = new TabIntermediate();
        tabIntermediate.setDatabaseReference(databaseReference);
        sectionsPageAdapter.addFragment(tabIntermediate, "MEDIUM");

        TabExpert tabExpert = new TabExpert();
        tabExpert.setDatabaseReference(databaseReference);
        sectionsPageAdapter.addFragment(tabExpert, "EXPERT");

        viewPager.setAdapter(sectionsPageAdapter);
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
