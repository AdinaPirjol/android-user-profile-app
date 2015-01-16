package com.app.md_hw;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class ChangeInterestsActivity extends ActionBarActivity {
    private CheckBox movies1,movies2,movies3;
    private CheckBox music1,music2,music3;
    private CheckBox prog1,prog2,prog3,prog4;
    private Button buttonSaveInterests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the layout from activity_interests
        setContentView(R.layout.activity_interests);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeButtons();
        //retrieve the data if available
        try {
            String interests = readInterests();
            parseInterestsJSON(interests);
        }catch(IOException e){
            Toast.makeText(getApplicationContext(), "Cannot read the interests file", Toast.LENGTH_LONG).show();
        }catch(JSONException e){
            Toast.makeText(getApplicationContext(), "Cannot parse interests file data", Toast.LENGTH_LONG).show();
        }

        buttonSaveInterests.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        //call the save method
                        onSave();
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "An error occured! Interests were not saved!", Toast.LENGTH_LONG).show();
                    }
                    //send to the viewInterestActivity class
                    Intent intent = new Intent(
                            ChangeInterestsActivity.this,
                            ViewInterestsActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        );
    }

    public void initializeButtons(){
        //initialize all the fields
        movies1 = (CheckBox) findViewById(R.id.checkBoxM1);
        movies2 = (CheckBox) findViewById(R.id.checkBoxM2);
        movies3 = (CheckBox) findViewById(R.id.checkBoxM3);

        music1 = (CheckBox) findViewById(R.id.checkBoxMusic1);
        music2 = (CheckBox) findViewById(R.id.checkBoxMusic2);
        music3 = (CheckBox) findViewById(R.id.checkBoxMusic3);

        prog1 = (CheckBox) findViewById(R.id.checkBoxP1);
        prog2 = (CheckBox) findViewById(R.id.checkBoxP2);
        prog3 = (CheckBox) findViewById(R.id.checkBoxP3);
        prog4 = (CheckBox) findViewById(R.id.checkBoxP4);

        buttonSaveInterests = (Button) findViewById(R.id.buttonInterests);
    }

    public String readInterests() throws IOException {
        //create a new JSONObject
        JSONObject interests = new JSONObject();

        StringBuffer input = new StringBuffer();
        //get the interests from the .txt
        InputStream is = getAssets().open("interests.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

        String line;
        while((line = br.readLine()) != null){
            input.append(line);
        }

        return input.toString();
    }

    public void parseInterestsJSON(String input) throws JSONException {
        JSONObject json = new JSONObject(input);
        //create 3 jsonobjects with specific classes
        JSONObject MUSIC = json.getJSONObject("music");
        JSONObject MOVIES = json.getJSONObject("movies");
        JSONObject PROG_LANG = json.getJSONObject("prog-lang");
        //set id's for buttons to be retrieved easier
        music1.setText(MUSIC.getString("1"));
        music2.setText(MUSIC.getString("2"));
        music3.setText(MUSIC.getString("3"));

        movies1.setText(MOVIES.getString("1"));
        movies2.setText(MOVIES.getString("2"));
        movies3.setText(MOVIES.getString("3"));

        prog1.setText(PROG_LANG.getString("1"));
        prog2.setText(PROG_LANG.getString("2"));
        prog3.setText(PROG_LANG.getString("3"));
        prog4.setText(PROG_LANG.getString("4"));
    }

    public void onSave() throws JSONException {
        //get the current user
        ParseUser currentUser = ParseUser.getCurrentUser();
        String user = currentUser.getUsername();

        JSONObject jsonObject = new JSONObject();

        boolean choice = false;
        //check if either of the options are checked and put them in the music JSON
        if(music1.isChecked() || music2.isChecked() || music3.isChecked()){
            choice = true;

            JSONObject music = new JSONObject();

            if(music1.isChecked()){
                music.put("hard-rock", true);
            }
            if(music2.isChecked()){
                music.put("pop", true);
            }
            if(music3.isChecked()){
                music.put("dance", true);
            }
            jsonObject.put("music", music);
        }
        //check if either of the options are checked and put them in the movies JSON
        if(movies1.isChecked() || movies2.isChecked() || movies3.isChecked()){
            choice = true;

            JSONObject movies = new JSONObject();

            if(movies1.isChecked()){
                movies.put("titanic", true);
            }
            if(movies2.isChecked()){
                movies.put("hunger-games", true);
            }
            if(movies3.isChecked()){
                movies.put("casablanca", true);
            }
            jsonObject.put("movies", movies);
        }
        //check if either of the options are checked and put them in the prog_lang JSON
        if(prog1.isChecked() || prog2.isChecked() || prog3.isChecked() || prog4.isChecked()){
            choice = true;
            JSONObject prog_lang = new JSONObject();

            if(prog1.isChecked()){
                prog_lang.put("what-is-that", true);
            }

            if(prog2.isChecked()){
                prog_lang.put("c", true);
            }

            if(prog3.isChecked()){
                prog_lang.put("web-dev", true);
            }

            if(prog4.isChecked()){
                prog_lang.put("java", true);
            }

            jsonObject.put("prog-lang", prog_lang);
        }

        if(choice) {
            //for the current user, store his/her new interests
            ParseObject obj = new ParseObject("Interests");
            obj.put("username", user);
            obj.put("interests", jsonObject.toString());
            obj.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(getApplicationContext(), "Interests were saved", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "An error occured! Interests were not saved!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_interests, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            Intent parentIntent1 = new Intent(this,ViewInterestsActivity.class);
            startActivity(parentIntent1);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
