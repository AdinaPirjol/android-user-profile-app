package com.app.md_hw;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.regex.Pattern;


public class HomeActivity extends ActionBarActivity {
    private TextView location;
    private TextView date;
    private TextView temp;
    private TextView description;
    private TextView welcome;
    private ImageView imgView;
    private Button changePassword;
    private Button interests;
    Button mybtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from activity_home.xml
        setContentView(R.layout.activity_home);

        //get the current user from the database
        ParseUser currentUser = ParseUser.getCurrentUser();

        String username = currentUser.getUsername();
        //display welcome message for current user
        welcome = (TextView) findViewById(R.id.textWelcome);
        welcome.setText("Welcome, " + username + "!");

        TextView mTextSample1 = (TextView) findViewById(R.id.text1);
        TextView mTextSample2 = (TextView) findViewById(R.id.text2);
        TextView mTextSample3 = (TextView) findViewById(R.id.text3);

        //query for the favorite links from the FavoriteLinks table
        ParseQuery<ParseObject> query = ParseQuery.getQuery("FavoriteLinks");
        query.whereEqualTo("username", username);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                if (e == null) {
                    String text = "";
                    //tell the user the links were retrieved successfully
                    Toast.makeText(getApplicationContext(), "Links retrieved successfully", Toast.LENGTH_LONG).show();
                    for(ParseObject p : parseObjects) {
                        text = p.getString("favorite_links");
                    }
                    //trim the array string
                    if(!text.equals("")){
                        text = text.replaceAll("\\[", "").replaceAll("\\]","");
                        String[] array = text.split(", ");
                    //if favorite link1 was edited make a link out of it
                        if(array.length>0)
                        {
                            TextView mTextSample1 = (TextView) findViewById(R.id.text1);
                            mTextSample1.setText(array[0]);
                            Pattern pattern1 = Pattern.compile(array[0]);
                            Linkify.addLinks(mTextSample1, pattern1, "http://");
                        }
                        //if favorite link2 was edited make a link out of it
                        if(array.length>1)
                        {
                            TextView mTextSample2 = (TextView) findViewById(R.id.text2);
                            mTextSample2.setText(array[1]);
                            Pattern pattern2 = Pattern.compile(array[1]);
                            Linkify.addLinks(mTextSample2, pattern2, "http://");
                        }
                        //if favorite link3 was edited make a link out of it
                        if(array.length>2)
                        {
                            TextView mTextSample3 = (TextView) findViewById(R.id.text3);
                            mTextSample3.setText(array[2]);
                            Pattern pattern3 = Pattern.compile(array[2]);
                            Linkify.addLinks(mTextSample3, pattern3, "http://");
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Could not retrieve interests", Toast.LENGTH_LONG).show();
                }
            }
        });

        //on the press of edit links button send to the EditLinksActivity
        mybtn = (Button)findViewById(R.id.myBtn);
        mybtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                Intent intent = new Intent(
                        HomeActivity.this,
                        EditLinksActivity.class);
                startActivity(intent);
                finish();
            }
        });


        // Create intent for user redirection to the ChangePasswordActivity
        changePassword = (Button) findViewById(R.id.changePassword);

        //on the press of edit links button send to the ChangePasswordActivity
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

        //on the press of edit links button send to the ViewInterestsActivity
        interests.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                Intent intent = new Intent(
                        HomeActivity.this,
                        ViewInterestsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //set current city to Bucharest
        String city = "Bucharest,RO";
        //create a layout from the desired input
        location = (TextView) findViewById(R.id.textLocation);
        date = (TextView) findViewById(R.id.textDate);
        temp = (TextView) findViewById(R.id.textTemperature);
        description = (TextView) findViewById(R.id.textDescription);
        imgView = (ImageView) findViewById(R.id.imageView);
        //create a retrieve weather task and execute it asyncronous
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
            //create new Weather object
            Weather weather = new Weather();
            //open the openweathermapclient
            OpenWeatherMapClient client = new OpenWeatherMapClient();
            try {
                //if all went accordingly, get the weater
                String data = client.getWeatherMessage(params[0]);
                weather = client.parseWeatherMessage(data);
            //else catch an exception
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
            //retrieve the weather after the execution has begun
            super.onPostExecute(weather);

            imgView.setImageResource(R.drawable.weather);
            //make the result readable
            location.setText(weather.getCity() + ", " + weather.getCountry());
            description.setText(weather.getDescription());
            temp.setText(weather.getTemperature() + "°C");
            date.setText(weather.getDate().toString());
        }
    }
}
