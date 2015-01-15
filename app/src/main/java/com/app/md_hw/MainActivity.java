package com.app.md_hw;

/**
 * Created by Lau on 13.01.2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;

public class MainActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Determine whether the current user is an anonymous user
        if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            // If user is anonymous, send the user to LoginSignupActivity.class
            Intent intent = new Intent(MainActivity.this,
                    LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            // If current user is NOT anonymous user
            // Get current user data from Parse.com
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                // Send logged in users to HomeActivity
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                // Send user to LoginSignupActivity
                Intent intent = new Intent(MainActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }
}

