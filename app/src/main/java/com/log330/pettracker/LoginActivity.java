package com.log330.pettracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.log330.pettracker.listener.FetchListener;
import com.log330.pettracker.model.GPSPoint;
import com.log330.pettracker.network.Server;
import com.log330.pettracker.utils.PreferencesController;

import java.util.ArrayList;

public class LoginActivity extends Activity implements View.OnClickListener, FetchListener {

    public static void show(Context context) {
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(PreferencesController.getBooleanPreference(this, PreferencesController.IS_ALREADY_LOGGED_IN)) {
            MainActivity.show(this);
            finish();
        }

        findViewById(R.id.connect).setOnClickListener(this);
        findViewById(R.id.register).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.connect) {
            if(((EditText) findViewById(R.id.username)).getText().toString().equals("demo")
                    && ((EditText) findViewById(R.id.password)).getText().toString().equals("demo")) {
                PreferencesController.setPreference(this, PreferencesController.IS_ALREADY_LOGGED_IN, true);
                MainActivity.show(this);
                finish();
            } else {
                Snackbar.make(findViewById(android.R.id.content), R.string.error_login, Snackbar.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.register && findViewById(R.id.connect).getVisibility() == View.VISIBLE) {
            ((EditText) findViewById(R.id.username)).clearComposingText();
            ((EditText) findViewById(R.id.password)).clearComposingText();
            findViewById(R.id.connect).setVisibility(View.GONE);
            ((LinearLayout.LayoutParams) findViewById(R.id.register).getLayoutParams()).rightMargin = 0;
        } else if (v.getId() == R.id.register && findViewById(R.id.connect).getVisibility() == View.GONE) {
            ((EditText) findViewById(R.id.username)).setError(null);
            ((EditText) findViewById(R.id.password)).setError(null);
            if(((EditText) findViewById(R.id.username)).getText().toString().equals("demo")) {
                ((EditText) findViewById(R.id.username)).setError(getString(R.string.username_used));

                if(((EditText) findViewById(R.id.password)).getText().toString().length() <= 3){
                    ((EditText) findViewById(R.id.password)).setError(getString(R.string.minimum_characters));
                }
            } else if(((EditText) findViewById(R.id.username)).getText().toString().length() > 3
                    && ((EditText) findViewById(R.id.password)).getText().toString().length() > 3 ) {
                PreferencesController.setPreference(this, PreferencesController.IS_ALREADY_LOGGED_IN, true);
                MainActivity.show(this);
                finish();
            } else {
                if(((EditText) findViewById(R.id.username)).getText().toString().length() <= 3){
                    ((EditText) findViewById(R.id.username)).setError(getString(R.string.minimum_characters));
                }

                if(((EditText) findViewById(R.id.password)).getText().toString().length() <= 3){
                    ((EditText) findViewById(R.id.password)).setError(getString(R.string.minimum_characters));
                }
            }
        }
    }

    @Override
    public void onFetchSuccessful(ArrayList<GPSPoint> points) {

    }

    @Override
    public void onFetchError() {

    }
}
