package com.example.kac.prijavinapako;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class NapakaRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://klemenkac.000webhostapp.com/Napaka.php";
    private Map<String, String> params;

    public NapakaRequest(String dom, int soba, String tip_napake, String opis, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("dom", dom);
        params.put("soba", soba + "");
        params.put("tip_napake", tip_napake);
        params.put("opis", opis);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
