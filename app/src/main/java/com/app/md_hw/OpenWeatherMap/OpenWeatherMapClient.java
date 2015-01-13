package com.app.md_hw.OpenWeatherMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import com.app.md_hw.OpenWeatherMap.Weather;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nnao9_000 on 1/13/2015.
 */
public class OpenWeatherMapClient {
    private static String URL = "http://api.openweathermap.org/data/2.5/weather?q=";

    public String getWeatherMessage(String location) throws IOException {
        HttpURLConnection conn = null;
        InputStream input = null;
        try {
            conn = (HttpURLConnection) (new URL(URL + location)).openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();

            input = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            StringBuilder data = new StringBuilder();
            String line = br.readLine();
            while((line = br.readLine()) != null){
                data.append(line);
            }
            input.close();
            br.close();
            conn.disconnect();

            return data.toString();

        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch(IOException io){
            io.printStackTrace();
        } finally {
            input.close();
            conn.disconnect();
        }

        return null;
    }

    public Weather parseWeatherMessage(String data) throws JSONException {
        Weather weather = new Weather();

        JSONObject json = new JSONObject(data);

        JSONObject COORD = json.getJSONObject("coord");
        JSONObject SYS = json.getJSONObject("sys");
        JSONObject WEATHER = json.getJSONObject("weather");
        JSONObject MAIN = json.getJSONObject("main");

        weather.setCity(json.getString("name"));
        weather.setCountry(SYS.getString("country"));

        weather.setTemperature((float) MAIN.getDouble("temp"));

        Weather.Conditions condition = null;

        switch(WEATHER.getString("main")){
            case "Clear":
                condition = Weather.Conditions.CLEAR;
                break;
            case "Clouds":
                condition = Weather.Conditions.CLOUDS;
                break;
            case "Rain":
                condition = Weather.Conditions.RAIN;
                break;
            case "Snow":
                condition = Weather.Conditions.SNOW;
                break;
        }

        weather.setCondition(condition);
        weather.setIcon(WEATHER.getString("icon"));

        Date date = new Date((long)Double.parseDouble(json.getString("dt")));
        weather.setDate(date);

        return weather;
    }
}
