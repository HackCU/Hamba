package com.hamba.hambameet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.hamba.hambameet.utils.Constants;
import com.hamba.hambameet.utils.DialogUtils;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.QBSettings;
import com.quickblox.users.model.QBUser;

import java.util.List;

public class SplashActivity extends Activity {

    private Context context;
    private Resources resources;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context = this;
        resources = getResources();


        // Initialize QuickBlox application with credentials.
        //
        QBSettings.getInstance().fastConfigInit(String.valueOf(Constants.APP_ID), Constants.AUTH_KEY, Constants.AUTH_SECRET);

        // Create QuickBlox session
        //
        QBUser qbUser = new QBUser(Constants.USER_LOGIN, Constants.USER_PASSWORD);
        QBAuth.createSession(qbUser, new QBEntityCallbackImpl<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                //go to login page
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }

            @Override
            public void onError(List<String> errors) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

}