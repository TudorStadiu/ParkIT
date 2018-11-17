package com.example.tudor.parkit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button signInButton;
    EditText emailText, passwordText;
    TextView loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
/*
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.saved_high_score_key), newHighScore);
        editor.commit();
        */

       emailText = findViewById(R.id.email_txt);
       passwordText = findViewById(R.id.password_txt);
       signInButton = findViewById(R.id.signin_btn);
       loginText = findViewById(R.id.login_txt);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("EMAIL:::::",emailText.getText().toString());
                Log.d("PASSWORD:::::",passwordText.getText().toString());

                RequestQueue queue = Volley.newRequestQueue(getBaseContext());
                String BASEURL ="https://murmuring-journey-83684.herokuapp.com";
                String PROFILE ="/profile";


                JsonObjectRequest objectRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        BASEURL+PROFILE,
                        null,

                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String firstName = response.getString("firstName");
                                    String lastName = response.getString("lastName");
                                    Log.d("DEBUG", firstName);
                                    Log.d("DEBUG", lastName);

                                    Intent screen3 = new Intent(LoginActivity.this, MapsActivity.class);
                                    startActivity(screen3);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loginText.setText("That didn't work!");
                        Log.d("Eroare::::",error.toString());
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> params =  new HashMap<>();
                        String token = emailText.getText().toString()+":"+passwordText.getText().toString();
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
