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

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashActivity.this, NiStreznika.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    Intent intent = new Intent(SplashActivity.this, NiStreznika.class);
                    startActivity(intent);
                    e.printStackTrace();
                }
            }
        };

        DelovanjeRequest delovanjeRequestRequest = new DelovanjeRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(SplashActivity.this);
        queue.add(delovanjeRequestRequest);



    }

}