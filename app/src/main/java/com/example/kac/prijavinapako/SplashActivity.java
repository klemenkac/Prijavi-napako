package com.example.kac.prijavinapako;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Klemen on 20. 12. 2017.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationMy app;
        app = (ApplicationMy) getApplication();
        SharedPreferences sharedpreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        String ime=sharedpreferences.getString("name",null);
        String prvizagon=sharedpreferences.getString("prvizagon",null);

        if(prvizagon==null || prvizagon.equals("")){
            Intent intent = new Intent(getApplicationContext(), ActivityIntro.class);
            startActivity(intent);
            finish();
        }
        else if(ime!=null && !ime.equals("")){
            app.getData();
        }else{
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        }






}