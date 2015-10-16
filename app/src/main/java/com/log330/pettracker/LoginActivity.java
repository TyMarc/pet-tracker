package com.log330.pettracker;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.log330.pettracker.listener.FetchListener;
import com.log330.pettracker.model.GPSPoint;
import com.log330.pettracker.network.Server;

import java.util.ArrayList;

public class LoginActivity extends Activity implements View.OnClickListener, FetchListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.connect).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.connect) {
            if(((EditText) findViewById(R.id.username)).getText().toString().equals("demo")
                    && ((EditText) findViewById(R.id.password)).getText().toString().equals("demo")) {
                MainActivity.show(this);
                finish();
            } else {
                Snackbar.make(findViewById(android.R.id.content), R.string.error_login, Snackbar.LENGTH_SHORT).show();
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
