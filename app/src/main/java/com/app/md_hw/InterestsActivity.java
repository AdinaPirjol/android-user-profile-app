package com.app.md_hw;

import android.content.Intent;
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
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class InterestsActivity extends ActionBarActivity {
    private CheckBox movies1,movies2,movies3;
    private CheckBox music1,music2,music3;
    private CheckBox prog1,prog2,prog3,prog4;
    private Button buttonSaveInterests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);

        initializeButtons();

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
                        onSave();
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "An error occured! Interests were not saved!", Toast.LENGTH_LONG).show();
                    }

                    Intent intent = new Intent(
                            InterestsActivity.this,
                            HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        );
    }

    public void initializeButtons(){
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
        JSONObject interests = new JSONObject();

        StringBuffer input = new StringBuffer();

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

        JSONObject MUSIC = json.getJSONObject("music");
        JSONObject MOVIES = json.getJSONObject("movies");
        JSONObject PROG_LANG = json.getJSONObject("prog-lang");

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
        ParseUser currentUser = ParseUser.getCurrentUser();
        String user = currentUser.getUsername();

        JSONObject jsonObject = new JSONObject();

        boolean choice = false;

        if(music1.isChecked() || music2.isChecked() || music3.isChecked()){
            choice = true;

            JSONObject music = new JSONObject();

            if(music1.isChecked()){
                music.put("titanic", true);
            }
            if(music2.isChecked()){
                music.put("hunger-games", true);
            }
            if(music3.isChecked()){
                music.put("casablanca", true);
            }
            jsonObject.put("music", music);
        }

        if(movies1.isChecked() || movies2.isChecked() || movies3.isChecked()){
            choice = true;

            JSONObject movies = new JSONObject();

            if(movies1.isChecked()){
                movies.put("hard-rock", true);
            }
            if(movies2.isChecked()){
                movies.put("pop", true);
            }
            if(movies3.isChecked()){
                movies.put("dance", true);
            }
            jsonObject.put("movies", movies);
        }

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
        getMenuInflater().inflate(R.menu.menu_interests, menu);
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
