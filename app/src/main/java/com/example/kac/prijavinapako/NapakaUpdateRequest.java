package com.example.kac.prijavinapako;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NapakaUpdateRequest extends StringRequest {
    private static final String REQUEST_URL = "https://klemenkac.000webhostapp.com/UpdateNapaka.php";
    private Map<String, String> params;

    public NapakaUpdateRequest(String id,String dom,String soba,String tip_napake, String opis, Response.Listener<String> listener) {
        super(Method.POST, REQUEST_URL, listener, null);

        Date cDate = new Date();
        String datum = new SimpleDateFormat("dd. MM. yyyy").format(cDate);

        params = new HashMap<>();
        params.put("id", id);
        params.put("dom", dom);
        params.put("soba", soba);
        params.put("tip_napake", tip_napake);
        params.put("opis", opis);
        params.put("datum", datum);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
