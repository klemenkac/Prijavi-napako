package com.example.kac.prijavinapako;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeleteRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://klemenkac.000webhostapp.com/DeleteNapaka.php";
    private Map<String, String> params;

    public DeleteRequest(String id, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id", id);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
