package com.hamba.hambameet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.parse.Parse;


import java.util.List;

public class SplashActivity extends Activity {

    private void startMapActivity() {
        finish();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(this, "8Os7PVIih2cRDfomi10U64mY07t0sypgNu8RUxWw", "8MWpDTnDLnaVhIjnzhtWSmjv5vIgr2HEc9AIBqN0");
        setContentView(R.layout.activity_splash);
        startMapActivity();
    }

    public void onError(List<String> errors) {
        AlertDialog.Builder exitDialog = new AlertDialog.Builder(SplashActivity.this);
        exitDialog.setTitle("Unable to connect");
        exitDialog.setMessage("Unable to connect to server \n Check internet connectivity and try again later");
        exitDialog.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            System.exit(0);
            }
        });
        exitDialog.create().show();
    }
        }

