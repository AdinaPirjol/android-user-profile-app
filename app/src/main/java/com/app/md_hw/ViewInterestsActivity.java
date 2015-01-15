package com.app.md_hw;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class ViewInterestsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_interests);

        ParseUser user = ParseUser.getCurrentUser();
        String username = user.getUsername();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Interests");
        query.whereEqualTo("username", username);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                if (e == null) {
                    String text = "";
                    TextView textInterests = (TextView) findViewById(R.id.textViewInterests);
                    Toast.makeText(getApplicationContext(), "Interests retrieved successfully", Toast.LENGTH_LONG).show();
                    for(ParseObject p : parseObjects) {
                        text = (String) p.get("interests");
                        textInterests.setText(text);
                    }
                    if(text.equals("")){
                        textInterests.setText("No saved interests found.");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Could not retrieve interests", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Create onClick user action for intent of going to the ChangeInterestsActivity
        Button buttonChangeInterests = (Button) findViewById(R.id.buttonChangeInterests);
        buttonChangeInterests.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
