package com.app.md_hw;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.md_hw.OpenWeatherMap.OpenWeatherMapClient;
import com.app.md_hw.OpenWeatherMap.Weather;

import java.io.IOException;


public class HomeActivity extends ActionBarActivity {
    private TextView location;
    private TextView date;
    private TextView temp;
    private ImageView imgView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        String city = "Bucharest,RO";

        location = (TextView) findViewById(R.id.textLocation);
        date = (TextView) findViewById(R.id.textDate);
        temp = (TextView) findViewById(R.id.textTemperature);
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


    private class WeatherAsyncTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = null;
            try {
                data = (new OpenWeatherMapClient()).getWeatherMessage(params[0]);
                weather = (new OpenWeatherMapClient()).parseWeatherMessage(data);
            }catch(Exception e){
                e.printStackTrace();
            }

            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather){
            super.onPostExecute(weather);
            switch(weather.getCondition()){
                case CLEAR:
                    imgView.setImageResource(R.drawable.clear);
                    break;
                default:
                    break;
            }

            location.setText(weather.getCity() + ", " + weather.getCountry());
            temp.setText(weather.getTemperature()+"Farhr");
            date.setText(weather.getDate().toString());
        }
    }
}
