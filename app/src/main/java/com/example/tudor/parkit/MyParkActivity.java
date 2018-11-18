package com.example.tudor.parkit;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class MyParkActivity extends Activity {
    Button setPark;
    EditText setCoord, setTime;

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
    }
}
