package com.example.yvtc.intentapp;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class GetIntentActivity extends Activity {

    private TextView textDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_intent);

        textDisplay = (TextView)findViewById(R.id.textView2_id);
        Intent intent = getIntent();
        String data = intent.getStringExtra(SearchManager.QUERY);
        textDisplay.setText("Intent.ACTION_WEB_SEARCH = \n"+data);

    }
}
