package com.example.tudor.parkit;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends Activity {
    Button registerButton;
    EditText txtUsername, txtPassword, txtConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        registerButton = findViewById(R.id.register_btn);
        txtUsername = findViewById(R.id.reg_username);
        txtPassword = findViewById(R.id.reg_password);
        txtConfirm = findViewById(R.id.reg_confirm);
    }
}
