package com.example.kac.prijavinapako;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        final EditText e_naslov = (EditText) findViewById(R.id.usernameUA);
        final EditText sifra = (EditText) findViewById(R.id.sifra);
        final EditText geslo1 = (EditText) findViewById(R.id.geslo1);
        final EditText geslo2 = (EditText) findViewById(R.id.geslo2);
        final Button bRegister = (Button) findViewById(R.id.bRegister);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String e_naslovtx = e_naslov.getText().toString();
                final int sifratx = Integer.parseInt(sifra.getText().toString());
                final String geslo1tx = geslo1.getText().toString();
                final String geslo2tx = geslo2.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(geslo1tx.equals(geslo2tx)){
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                Toast.makeText(RegisterActivity.this, "Registracija uspela!" , Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                RegisterActivity.this.startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("Register Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        }else{
                            Toast.makeText(RegisterActivity.this, "Gesli se ne ujemata!" , Toast.LENGTH_SHORT).show();

                        }
                    }
                };

                RegisterRequest registerRequest = new RegisterRequest(e_naslovtx, sifratx, geslo1tx, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }
        });
    }
}