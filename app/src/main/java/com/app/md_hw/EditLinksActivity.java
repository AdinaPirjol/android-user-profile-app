package com.app.md_hw;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.md_hw.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class EditLinksActivity extends ActionBarActivity {
    EditText link1;
    EditText link2;
    EditText link3;
    String link1txt;
    String link2txt;
    String link3txt;
    Button saveLinks;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_links);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //create 3 edittexts
        link1 = (EditText) findViewById(R.id.link1);
        link2 = (EditText) findViewById(R.id.link2);
        link3 = (EditText) findViewById(R.id.link3);

        //get the current user and retrieve the data accordingly
        ParseUser parse = ParseUser.getCurrentUser();
        String pasrseString = parse.getUsername();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("FavoriteLinks");
        query.whereEqualTo("username", pasrseString);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                if (e == null) {
                    String text = "";
                    Toast.makeText(getApplicationContext(), "Links retrieved successfully", Toast.LENGTH_LONG).show();
                    //get the data from the favorite_links string
                    for(ParseObject p : parseObjects) {
                        text = p.getString("favorite_links");
                    }
                    //trim the string to get rid of [ , ]
                    if(!text.equals("")) {
                        text = text.replaceAll("\\[", "").replaceAll("\\]","");
                        String[] array = text.split(", ");
                        //retrieve the first link if it exist
                        if(array.length>0) {
                            link1.setText(array[0]);
                        }
                        //retrieve the second link if it exist
                        if(array.length>0){
                            link2.setText(array[1]);
                        }
                        //retrieve the third link if it exist
                        if(array.length>0){
                            link3.setText(array[2]);
                        }
                    }
                }
            }});
        saveLinks = (Button)findViewById(R.id.mySaveBtn);

        saveLinks.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //get the current user when the button is pressed
                ParseUser user = ParseUser.getCurrentUser();
                String username = user.getUsername();

                ParseObject editLinks = new ParseObject("FavoriteLinks");
                //retrieve the newly saved data from the edittexts
                link1txt = link1.getText().toString();
                link2txt = link2.getText().toString();
                link3txt = link3.getText().toString();
                //store the data in an array and put them in the database
                String [] favoriteLinks ={link1txt , link2txt , link3txt};

                editLinks.put("username", username);
                editLinks.put("favorite_links", Arrays.toString(favoriteLinks));

                editLinks.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(getApplicationContext(), "Favorite links were saved", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "An error occurred! Interests were not saved!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                //send back to the homepage
                Intent intent = new Intent(
                        EditLinksActivity.this,
                        HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_links, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            Intent parentIntent1 = new Intent(this,HomeActivity.class);
            startActivity(parentIntent1);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
