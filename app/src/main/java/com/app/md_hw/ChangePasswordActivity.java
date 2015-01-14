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

    private EditText editCurrent;
    private EditText editNew;
    private EditText editRetype;
    private Button buttonSaveNewPassword;
    String currentP, newP, retypeP;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        editCurrent = (EditText) findViewById(R.id.editCurrent);
        editNew = (EditText) findViewById(R.id.editNew);
        editRetype = (EditText) findViewById(R.id.editRetype);

        buttonSaveNewPassword = (Button) findViewById(R.id.buttonSaveNewPassword);

        // Logout Button Click Listener
        buttonSaveNewPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                currentP = editCurrent.getText().toString().trim();
                newP = editNew.getText().toString().trim();
                retypeP = editRetype.getText().toString().trim();

                ParseUser currentUser = ParseUser.getCurrentUser();
                String currentPassword = (String) currentUser.get("password");

                if(currentP == "" || newP == "" || retypeP == ""){
                    Toast.makeText(getApplicationContext(), "You didn't complete all fields", Toast.LENGTH_LONG).show();
                }else{
                    if(currentPassword.compareTo(currentP) != 0){
                        Toast.makeText(getApplicationContext(), "Current password is wrong! Try again", Toast.LENGTH_LONG).show();
                    }else{
                        if (newP.compareTo(retypeP) != 0) {
                            Toast.makeText(getApplicationContext(), "New password and retype new password do not match!", Toast.LENGTH_LONG).show();
                        } else {
                            if (newP.compareTo(currentP) == 0) {
                                Toast.makeText(getApplicationContext(), "New password and current password are the same! Password was not changed", Toast.LENGTH_LONG).show();
                            }
                            currentUser.setPassword(newP);
                            currentUser.saveInBackground();
                            Intent intent = new Intent(
                                    ChangePasswordActivity.this,
                                    HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }
        });
    }
}
