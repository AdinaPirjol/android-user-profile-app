package com.app.md_hw.OpenWeatherMap;

import java.util.Date;

/**
 * Created by nnao9_000 on 1/13/2015.
 */
public class Weather {
    private String city;
    private String country;
    private float temperature;
    private Conditions condition;
    private Date date;
    private String icon;

    public static enum Conditions { CLEAR, CLOUDS, RAIN, SNOW };

    public String getCity(){
        return this.city;
    }

    public String getCountry() {
        return country;
    }

    public Date getDate() {
        return date;
    }

    public float getTemperature() {
        return temperature;
    }

    public Conditions getCondition() {
        return condition;
    }


    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public void setCondition(Conditions condition) {
        this.condition = condition;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
