package com.app.md_hw;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;

/**
 * Created by Lau on 13.01.2015.
 */
public class ChangePasswordActivity extends Activity {

    private EditText ChangePassword;
    private Button SaveNewPassword;
    String changePasswordText;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        ChangePassword = (EditText) findViewById(R.id.ChangePassword);


        SaveNewPassword = (Button) findViewById(R.id.saveNewPassword);

        // Logout Button Click Listener
        SaveNewPassword.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                changePasswordText = ChangePassword.getText().toString();
                ParseUser currentUser = ParseUser.getCurrentUser();
                currentUser.setPassword(changePasswordText);
                currentUser.saveInBackground();
                Intent intent = new Intent(
                        ChangePasswordActivity.this,
                        HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }
}
