package com.app.md_hw;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Enable Local Datastore
        // for when there is no available internet connection
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "pizEYCj8zYu8dcQ9AU7DxfRqGjx6Lt9Ie2aHV8Rn", "5e9eoQeZiL8yKntAe9tRmRsp3Xe7l9fLShGwoz7E");

        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
    }


    private void login(View view){
        if(isNetworkConnected()) {
            EditText editUsername = (EditText) findViewById(R.id.editUsername);
            EditText editPassword = (EditText) findViewById(R.id.editPassword);

            String username = editUsername.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this.getApplicationContext(), "Username and/or password are null!", Toast.LENGTH_SHORT);
            } else {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("UserCredentials");

                ParseObject credentials = new ParseObject("UserCredentials");
                credentials.add("username", username);
                credentials.add("password", password);

//                query.findInBackground();
            }
        } else {
            Toast.makeText(this.getApplicationContext(), "Username and/or password are null!", Toast.LENGTH_SHORT);
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidMainfofest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
