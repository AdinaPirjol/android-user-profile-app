package com.app.md_hw;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.md_hw.InterestsFragments.MoviesFragment;
import com.app.md_hw.InterestsFragments.MusicFragment;
import com.app.md_hw.InterestsFragments.ProgrammingLanguagesFragment;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class ViewInterestsActivity extends ActionBarActivity
        implements MusicFragment.OnFragmentInteractionListener,
        MoviesFragment.OnFragmentInteractionListener,
        ProgrammingLanguagesFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //create a new FragmentManager object
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //create fragments for music and movies
        MusicFragment music = new MusicFragment();
        MoviesFragment movies = new MoviesFragment();

        fragmentTransaction.add(R.id.fragment_music, music);
        fragmentTransaction.add(R.id.fragment_movies, movies);

        fragmentTransaction.commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //set the current layout to the activity_view_interests.xml
        setContentView(R.layout.activity_view_interests);
        //retrieve the current user
        ParseUser user = ParseUser.getCurrentUser();
        String username = user.getUsername();
        //create a new query by the username to retrieve his current interests
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Interests");
        query.whereEqualTo("username", username);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                if (e == null) {
                    String text = "";
                    //parse the interests into a string
                    TextView interestsLow = (TextView) findViewById(R.id.textInterestsLow);
                    for (ParseObject p : parseObjects) {
                        text = (String) p.get("interests");
                    }
                    //if there are no interests prompt the user
                    if(text.isEmpty()){
                        interestsLow.setText("No saved interests found.");
                    //else display them
                    }else{
                        interestsLow.setText("Interests: " + text);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Could not retrieve interests", Toast.LENGTH_LONG).show();
                }
            }
        });

        changeInterestsListener();
    }

    public void changeInterestsListener(){
        // Create onClick user action for intent of going to the ChangeInterestsActivity
        Button buttonChangeInterests = (Button) findViewById(R.id.buttonChangeInterests);
        buttonChangeInterests.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //sent to the ChangeInterestsActivityClass
                Intent intent = new Intent(
                        ViewInterestsActivity.this,
                        ChangeInterestsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_interests, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

                Intent parentIntent1 = new Intent(this,HomeActivity.class);
                startActivity(parentIntent1);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}