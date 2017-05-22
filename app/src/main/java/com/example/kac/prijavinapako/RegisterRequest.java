package com.example.kac.prijavinapako;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://klemenkac.000webhostapp.com/Register.php";
    private Map<String, String> params;

    public RegisterRequest(String e_naslov, int sifra, String geslo, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("e_naslov", e_naslov);
        params.put("sifra", sifra + "");
        params.put("geslo", geslo);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
