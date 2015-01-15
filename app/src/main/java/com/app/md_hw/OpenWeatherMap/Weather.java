package com.app.md_hw.OpenWeatherMap;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;

/**
 * Created by nnao9_000 on 1/13/2015.
 */


// class that retains information received from the weather api json
public class Weather {
    private String city;
    private String country;
    private String description;
    private double temperature;
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

    public double getTemperature() {
        return temperature;
    }

    public Conditions getCondition() {
        return condition;
    }

    public String getDescription() {
        return description;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setTemperature(double temperature) {
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

    public void setDescription(String description) {
        this.description = description;
    }

    /* Method to test input json functionality */
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append("Location: " + this.getCity() + ", " + this.getCountry());
        s.append("Temperature: " + this.getTemperature());
//        s.append("Condition: " + this.getCondition().toString());
//        s.append("Date: " + this.getDate().toString());
        return s.toString();
    }
}
