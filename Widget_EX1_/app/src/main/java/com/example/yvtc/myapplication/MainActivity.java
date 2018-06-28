package com.example.yvtc.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;

public class MainActivity extends Activity {

    private ImageButton imgBtn1,imgBtn2,imgBtn3,imgBtn4,imgBtn5,imgBtn6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgBtn1=(ImageButton) findViewById(R.id.imageButton_id1);
        imgBtn2=(ImageButton) findViewById(R.id.imageButton_id2);
        imgBtn3=(ImageButton) findViewById(R.id.imageButton_id3);
        imgBtn4=(ImageButton) findViewById(R.id.imageButton_id4);
        imgBtn5=(ImageButton) findViewById(R.id.imageButton_id5);
        imgBtn6=(ImageButton) findViewById(R.id.imageButton_id6);

    }
}
