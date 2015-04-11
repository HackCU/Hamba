package com.hamba.hambameet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hamba.hambameet.utils.DialogUtils;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class RegisterActivity extends ActionBarActivity {
    EditText usernameField;
    EditText passwordField;
    EditText emailField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        usernameField = (EditText) findViewById(R.id.editTextUsername2);
        passwordField = (EditText) findViewById(R.id.editTextPassword2);
        emailField = (EditText) findViewById(R.id.editTextEmail);
    }
    public void onRegister2Clicked(View view){

            String username = usernameField.getText().toString();
            String password = passwordField.getText().toString();
            String email = emailField.getText().toString();
            ParseUser user = new ParseUser();
            user.setEmail(email);
            user.setUsername(username);
            user.setPassword(password);
            user.signUpInBackground(new SignUpCallback() {
                public void done(com.parse.ParseException e) {
                    if (e == null) {
                        //start sinch service
                        //start next activity
                        finish();
                        Toast.makeText(getApplicationContext(),"Registration complete", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    } else {
                        DialogUtils.showLong(getApplicationContext(), e.toString());
                    }
                }
            });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
