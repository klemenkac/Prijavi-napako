package com.example.kac.prijavinapako;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NapakaKoncanoRequest extends StringRequest {
    private static final String REQUEST_URL = "https://klemenkac.000webhostapp.com/UpdateKoncano.php";
    private Map<String, String> params;

    public NapakaKoncanoRequest(String id, Response.Listener<String> listener) {
        super(Method.POST, REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("id", id);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
