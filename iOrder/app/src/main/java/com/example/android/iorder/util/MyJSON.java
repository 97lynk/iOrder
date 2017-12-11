package com.example.android.iorder.util;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;


// lớp có nhiệm vụ tạo ra các request đến server
// TODO Request Server
public class MyJSON {
    // chứa request parameters
    private static Map<String, String> params;

    private static Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    };

    public static void post(Context context, String url,
                            Response.Listener<String> listener, Map<String, String> prms) {
        request(context, Request.Method.POST, url, listener, prms);
    }

    public static void get(Context context, String url,
                           Response.Listener<String> listener, Map<String, String> prms) {
        request(context, Request.Method.GET, url, listener, prms);
    }

    public static void request(Context context, int method, String url,
                               Response.Listener<String> listener, Map<String, String> prms) {
        params = prms;
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(
                method, url,
                listener,
                errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
