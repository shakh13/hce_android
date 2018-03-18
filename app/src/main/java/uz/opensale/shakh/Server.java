/**
 * Created by shakh on 11.01.18.
 */
package uz.opensale.shakh;


import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Map;

public class Server {

    private String server = "http://192.168.1.130/hce/backend/web/?r=api/";
    private Context context;
    public String response;

    public Server(Context context, String url, Map<String, String> params){
        this.context = context;
        request(url, params);
    }

    public Server(Context context, String server){
        this.context = context;
        this.server = server;
    }

    public interface ServerListener{
        public void OnResponse(String data);
        public void OnError(String error);
    }

    private ServerListener listener;

    public void setListener(ServerListener listener) {
        this.listener = listener;
    }

    public void request(String url, final Map<String, String> params, int method){
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(method, server+url, new Response.Listener<String>() {
                @Override
                public void onResponse(String resp) {
                    if (listener != null)
                        listener.OnResponse(resp);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (listener != null)
                        listener.OnError(error.toString());
                }
            }
        ){
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void request(String url, Map<String, String> params){
        request(url, params, 1);
    }

    public void requestGET(String url, Map<String, String> params){
        request(url, params, 0);
    }
}
