package com.example.yvtc.intentanim;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayActivity extends Activity {

    private ImageView image1;
    private TextView textDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        image1 = (ImageView) findViewById(R.id.imageView_id);
        textDisplay = (TextView) findViewById(R.id.textView_id);

        Intent intent = getIntent();
        String name = intent.getStringExtra("title");
        setTitle(name);

        image1.setTransitionName(name);

        int imageID = intent.getIntExtra("imageID", R.drawable.banner1);
        int txtID = intent.getIntExtra("txtID", 0);
        image1.setImageResource(imageID);
        String txt = getResources().getString(txtID);
        textDisplay.setText(txt);
    }
}
