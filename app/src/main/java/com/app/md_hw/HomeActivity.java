package com.app.md_hw;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
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

import java.io.IOException;
import java.net.UnknownHostException;
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

        String username = currentUser.getUsername();
        welcome = (TextView) findViewById(R.id.textWelcome);
        welcome.setText("Welcome, " + username + "!");

        // Create intent for user redirection to the ChangePasswordActivity
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

        interests = (Button) findViewById(R.id.buttonInterests);

        interests.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                Intent intent = new Intent(
                        HomeActivity.this,
                        ViewInterestsActivity.class);
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
        task.execute(city);
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
            OpenWeatherMapClient client = new OpenWeatherMapClient();
            try {
                String data = client.getWeatherMessage(params[0]);
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
