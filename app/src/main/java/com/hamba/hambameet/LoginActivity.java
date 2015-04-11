package com.hamba.hambameet;

import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.hamba.hambameet.helper.DataHolder;
import com.hamba.hambameet.utils.DialogUtils;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import java.util.List;

public class LoginActivity extends ActionBarActivity {

    private EditText loginEditText;
    private EditText passwordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void onLoginClicked(View view){
        loginEditText = (EditText) findViewById(R.id.editTextUsername);
        passwordEditText = (EditText) findViewById(R.id.editTextPassword);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        switch (view.getId()) {
            case R.id.buttonLogin:

                // Sign in application with user
                //
                QBUser qbUser = new QBUser(loginEditText.getText().toString(), passwordEditText.getText().toString());
                QBUsers.signIn(qbUser, new QBEntityCallbackImpl<QBUser>() {
                    @Override
                    public void onSuccess(QBUser qbUser, Bundle bundle) {


                        setResult(RESULT_OK);

                        DataHolder.getDataHolder().setSignInQbUser(qbUser);
                        // password does not come, so if you want use it somewhere else, try something like this:
                        DataHolder.getDataHolder().setSignInUserPassword(passwordEditText.getText().toString());
                        finish();
                    }

                    @Override
                    public void onError(List<String> errors) {
                        DialogUtils.showLong(getApplicationContext(), errors.get(0));
                        TextView incorrectPassword = (TextView) findViewById(R.id.textViewIncorrectPassword);
                        incorrectPassword.setText("Incorrect Password");
                        Vibrator incorrectPasswordVibrator = (Vibrator) getSystemService(getApplicationContext().VIBRATOR_SERVICE);
                        incorrectPasswordVibrator.vibrate(300);
                    }
                });

                break;
        }
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
