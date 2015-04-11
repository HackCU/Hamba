package com.hamba.hambameet;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void onLoginClicked(View view){
        EditText username = (EditText) findViewById(R.id.editTextUsername);
        EditText password = (EditText) findViewById(R.id.editTextPassword);
        TextView incorrectPassword = (TextView) findViewById(R.id.textViewIncorrectPassword);
        String usernameString = username.getText().toString();
        String passwordString = password.getText().toString();
        incorrectPassword.setText("Incorrect Password");
        /*Vibrator incorrectPasswordVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        incorrectPasswordVibrator.vibrate(300);*/
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
    public void onRegisterClicked(View view){
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
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
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
