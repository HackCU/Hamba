package com.hamba.hambameet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.hamba.hambameet.utils.DialogUtils;
import com.parse.LogInCallback;
import com.parse.ParseUser;

import java.util.List;


public class LoginActivity extends ActionBarActivity {
    private EditText loginEditText;
    private EditText passwordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onRegisterClicked(View view){
        finish();
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
    }
    private void startMapActivity() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }
    public void onLoginClicked(View view) {
        loginEditText = (EditText) findViewById(R.id.editTextUsername);
        passwordEditText = (EditText) findViewById(R.id.editTextPassword);
        final TextView incorrectPassword = (TextView) findViewById(R.id.textViewIncorrectPassword);

        ParseUser.logInInBackground(loginEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
            public void done(ParseUser user, com.parse.ParseException e) {
                if (user != null) {
                    //start sinch service
                    //start next activity
                    incorrectPassword.setText("");
                    Toast.makeText(getApplicationContext(),"Success!", Toast.LENGTH_LONG).show();
                    startMapActivity();
                    finish();
                } else {
                    incorrectPassword.setText("Incorrect Password");
                    DialogUtils.showLong(getApplicationContext(), "Do the needful");
                }

            }
        });

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
}
