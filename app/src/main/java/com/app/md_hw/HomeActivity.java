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
import com.parse.ParseUser;

import org.json.JSONException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;


public class HomeActivity extends ActionBarActivity {
    private TextView location;
    private TextView date;
    private TextView temp;
    private TextView description;
    private TextView welcome;
    private ImageView imgView;
    private Button changePassword;
    private Button interests;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ParseUser currentUser = ParseUser.getCurrentUser();

        String struser = currentUser.getUsername().toString();
        welcome = (TextView) findViewById(R.id.textWelcome);
        welcome.setText("Welcome, " + struser + "!");

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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
                Toast.makeText(getApplicationContext(), "The weather api could not be reached", Toast.LENGTH_LONG);
            }catch(IOException e){
                Toast.makeText(getApplicationContext(), "Could not read weather response from server", Toast.LENGTH_LONG);
            }catch(JSONException e){
                Toast.makeText(getApplicationContext(), "Could not read weather response", Toast.LENGTH_LONG);
            }

            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather){
            super.onPostExecute(weather);
//            switch(weather.getCondition()){
//                case CLEAR:
//                    imgView.setImageResource(R.drawable.clear);
//                    break;
//                default:
//                    break;
//            }

            imgView.setImageResource(R.drawable.weather);

            location.setText(weather.getCity() + ", " + weather.getCountry());
            description.setText(weather.getDescription());
            temp.setText(weather.getTemperature() + "°C");
            date.setText(weather.getDate().toString());
        }
    }
}
