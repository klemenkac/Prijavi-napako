package com.example.kac.prijavinapako;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "https://klemenkac.000webhostapp.com/Login.php";
    private Map<String, String> params;

    public LoginRequest(String e_naslov, String geslo, Response.Listener<String> listener) {
        super(Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("e_naslov", e_naslov);
        params.put("geslo", geslo);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
