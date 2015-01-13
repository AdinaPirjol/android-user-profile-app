package com.app.md_hw;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class ParseApplication extends Application {
 
    @Override
    public void onCreate() {
        super.onCreate();
 
        // Add your initialization code here
        Parse.initialize(this, "pizEYCj8zYu8dcQ9AU7DxfRqGjx6Lt9Ie2aHV8Rn", "5e9eoQeZiL8yKntAe9tRmRsp3Xe7l9fLShGwoz7E");
 
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
 
        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);
 
        ParseACL.setDefaultACL(defaultACL, true);
    }
 
}