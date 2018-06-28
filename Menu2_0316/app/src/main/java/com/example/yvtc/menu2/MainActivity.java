package com.example.yvtc.menu2;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

    private Context context;
    private TextView gameText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        gameText = (TextView) findViewById(R.id.);

    }
}
