package com.maulida.kinanti.peminjamanalat.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Send extends StringRequest {
    private static final String Url = "http://172.16.202.209/pinjambarang/update.php";
    private final String code;
    private final Response.Listener<String> listener;

    public Send(String code, Response.Listener<String> listener) {
        super(Request.Method.POST, Url, listener, null);
        this.code = code;
        this.listener = listener;
    }

    @Override
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("id_pinjam", code);
        return params;
    }

    @Override
    protected void deliverResponse(String response) {
        super.deliverResponse(response);
        Log.d("UpdateActivity", "Server Response: " + response);
        listener.onResponse(response);
    }
}
