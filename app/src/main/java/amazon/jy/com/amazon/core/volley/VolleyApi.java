package amazon.jy.com.amazon.core.volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class VolleyApi {

    private static final String URL = "http://devg.bjpygh.com/Amazon/";

    public static void GET(Context context,String api,  Response.Listener listener){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,URL + api,
                listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error is : ", error.toString());
            }
        });
        queue.add(stringRequest);
    }

    public static void POST(Context context,String api, Response.Listener listener){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URL + api,
                listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error is : ", error.toString());
            }
        });
        queue.add(stringRequest);
    }
}
