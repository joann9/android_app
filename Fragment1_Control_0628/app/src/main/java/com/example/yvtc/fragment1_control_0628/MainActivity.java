package com.example.yvtc.fragment1_control_0628;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {

    private TextView textDisplay;
    private static final String TAG = "Data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textDisplay = (TextView) findViewById(R.id.textView_ID);
        textDisplay.setText("Fragment Test");

        Log.d(TAG,"[A] onCreate");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"[A] onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"[A] onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"[A] onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"[A] onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"[A] onDestroy");
    }
}
