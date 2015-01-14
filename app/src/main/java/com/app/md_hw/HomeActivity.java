package com.app.md_hw;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.md_hw.OpenWeatherMap.OpenWeatherMapClient;
import com.app.md_hw.OpenWeatherMap.Weather;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.List;


public class HomeActivity extends ActionBarActivity {
    private TextView location;
    private TextView date;
    private TextView temp;
    private TextView description;
    private TextView welcome;
    private ImageView imgView;
    private Button changePassword;
    private Button interests;
    private TextView textInterests;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ParseUser currentUser = ParseUser.getCurrentUser();

        String username = currentUser.getUsername().toString();
        welcome = (TextView) findViewById(R.id.textWelcome);
        welcome.setText("Welcome, " + username + "!");

        changePassword = (Button) findViewById(R.id.changePassword);

        changePassword.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                Intent intent = new Intent(
                        HomeActivity.this,
                        ChangePasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        interests = (Button) findViewById(R.id.interests);

        interests.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                Intent intent = new Intent(
                        HomeActivity.this,
                        InterestsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        String city = "Bucharest,RO";

        location = (TextView) findViewById(R.id.textLocation);
        date = (TextView) findViewById(R.id.textDate);
        temp = (TextView) findViewById(R.id.textTemperature);
        description = (TextView) findViewById(R.id.textDescription);
        imgView = (ImageView) findViewById(R.id.imageView);

        WeatherAsyncTask task = new WeatherAsyncTask();
        task.execute(new String[]{city});

        
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Interests");
        query.whereEqualTo("username", username);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                if (e == null) {
                    String text = "";
                    textInterests = (TextView) findViewById(R.id.textInterestsFound);
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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    // <String, Void, Weather> = <Params, Progress, Result>
    private class WeatherAsyncTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = null;
            OpenWeatherMapClient client = new OpenWeatherMapClient();
            try {
                data = client.getWeatherMessage(params[0]);
                weather = client.parseWeatherMessage(data);
            }catch(UnknownHostException e){
                Toast.makeText(getApplicationContext(), "The weather api could not be reached", Toast.LENGTH_LONG).show();
            }catch(IOException e){
                Toast.makeText(getApplicationContext(), "Could not read weather response from server", Toast.LENGTH_LONG).show();
            }catch(JSONException e){
                Toast.makeText(getApplicationContext(), "Could not read weather response", Toast.LENGTH_LONG).show();
            }

            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather){
            super.onPostExecute(weather);

            imgView.setImageResource(R.drawable.weather);

            location.setText(weather.getCity() + ", " + weather.getCountry());
            description.setText(weather.getDescription());
            temp.setText(weather.getTemperature() + "°C");
            date.setText(weather.getDate().toString());
        }
    }
}
