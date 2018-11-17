package com.example.tudor.parkit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class LoginActivity extends AppCompatActivity {

    Button signInButton;
    EditText emailText, passwordText;
    TextView loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                String url ="https://murmuring-journey-83684.herokuapp.com/?fbclid=IwAR0Kn7uYngNDGB0aGgbS9p4N93mv_cwKSEceNIjVruyr6x2EPhSa29641cA";
                //String BASEURL ="https://murmuring-journey-83684.herokuapp.com/?fbclid=IwAR0Kn7uYngNDGB0aGgbS9p4N93mv_cwKSEceNIjVruyr6x2EPhSa29641cA";

// Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                loginText.setText("Response is: "+ response);
                                if(loginText.getText().toString().contains("works")){
                                    Intent screen2 = new Intent(LoginActivity.this, MapsActivity.class);
                                    startActivity(screen2);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loginText.setText("That didn't work!");
                    }
                });

// Add the request to the RequestQueue.
                queue.add(stringRequest);

            }
        });
    }
}
