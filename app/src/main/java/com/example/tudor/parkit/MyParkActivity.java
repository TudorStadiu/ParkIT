package com.example.tudor.parkit;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MyParkActivity extends Activity {
    Button setPark;
    EditText setCoord, setTime;
    String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_park);

        setPark = findViewById(R.id.set_btn);
        setCoord = findViewById(R.id.set_coord);
        setTime = findViewById(R.id.set_time);

        SharedPreferences prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        username = prefs.getString("first_name", null);
        password = prefs.getString("last_name", null);

        setPark.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String longitude;
                String latitude;
                String time;

                String[] aux = setCoord.getText().toString().split(":");
                latitude = aux[0];
                longitude = aux[1];
                Log.d("asd", aux[0]);
                Log.d("asd", aux[1]);


                RequestQueue queue = Volley.newRequestQueue(getBaseContext());
                String BASEURL ="https://murmuring-journey-83684.herokuapp.com";
                String PINPOINS ="/register/pinpoint";

                Random rand = new Random();

                HashMap<String, String> jsonHashMap = new HashMap<>();
                jsonHashMap.put("latitude", latitude);
                jsonHashMap.put("longitude", longitude);
                jsonHashMap.put("title", "Demo" + rand.nextInt(100));


                JsonObjectRequest objectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        BASEURL+PINPOINS,
                        new JSONObject(jsonHashMap),

                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("asd", response.toString());
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Eroare::::",error.toString());
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> params =  new HashMap<>();
                        String token = username+":"+password;
                        String finalToken = new String(Base64.encode(token.getBytes(),Base64.DEFAULT));
                        Log.d("finalToken", finalToken);
                        params.put("Authorization","Basic "+ finalToken);
                        //..add other headers
                        return params;
                    }
                };

                queue.add(objectRequest);


            }
        });
    }
}
