package com.example.user.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;


public class MainActivity extends Activity {

    private ImageButton imgBtn1,imgBtn2,imgBtn3,imgBtn4,imgBtn5,imgBtn6;
    private ImageView imgView;
    private TextView tView;
    private final String str1 = " 兔子 , Rabbit\n";
    private final String str2= " 長頸鹿 , Giraffe\n";
    private final String str3 = " 大象 , Elephant\n";
    private final String str4= " 猴子 , Monkey\n";
    private final String str5 = " 河馬 , Hippo\n";
    private final String str6= " 獅子 , Lion\n";
    private int tColor1 = 0xFF008080;
    private int tColor2 = 0xFF984b4b;
    private int tColor3 = 0xFF949449;
    private int tColor4 = 0xFF9f4d95;
    private int tColor5 = 0xFF7373b9;
    private int tColor6 = 0xFFce0000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgBtn1 = (ImageButton)findViewById(R.id.imageButton_id1);
        imgBtn2 = (ImageButton)findViewById(R.id.imageButton_id2);
        imgBtn3 = (ImageButton)findViewById(R.id.imageButton_id3);
        imgBtn4 = (ImageButton)findViewById(R.id.imageButton_id4);
        imgBtn5 = (ImageButton)findViewById(R.id.imageButton_id5);
        imgBtn6 = (ImageButton)findViewById(R.id.imageButton_id6);

        imgBtn1.setOnClickListener(new imgBtnClick());
        imgBtn2.setOnClickListener(new imgBtnClick());
        imgBtn3.setOnClickListener(new imgBtnClick());
        imgBtn4.setOnClickListener(new imgBtnClick());
        imgBtn5.setOnClickListener(new imgBtnClick());
        imgBtn6.setOnClickListener(new imgBtnClick());

        imgView = (ImageView)findViewById(R.id.imageView_id);

        tView = (TextView)findViewById(R.id.textView_id);
        tView.setText("Animals");


    }

    private class imgBtnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.imageButton_id1:
                    imgView.setImageResource(R.drawable.icon_2_2);

                    tView.setText(str1);
                    tView.setTextColor(tColor1);


                    break;
                case R.id.imageButton_id2:
                    imgView.setImageResource(R.drawable.icon_3_2);
                    tView.setText(str2);
                    tView.setTextColor(tColor2);

                    break;
                case R.id.imageButton_id3:
                    imgView.setImageResource(R.drawable.icon_4_2);
                    tView.setText(str3);
                    tView.setTextColor(tColor3);

                    break;
                case R.id.imageButton_id4:
                    imgView.setImageResource(R.drawable.icon_8_2);
                    tView.setText(str4);
                    tView.setTextColor(tColor4);

                    break;
                case R.id.imageButton_id5:
                    imgView.setImageResource(R.drawable.icon_6_2);
                    tView.setText(str5);
                    tView.setTextColor(tColor5);

                    break;
                case R.id.imageButton_id6:
                    imgView.setImageResource(R.drawable.icon_7_2);
                    tView.setText(str6);
                    tView.setTextColor(tColor6);

                    break;
            }
        }
    }


}
