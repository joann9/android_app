package com.example.yvtc.bmi1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


public class MainActivity extends Activity {

    private EditText userName,  userAge,bmiHeight,bmiWeigh;
    private ImageButton starButton;
    private Context context;
    private String temp, temp1;
    private int age;
    private int height, weight;
    private static final String TAG = "BMI";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        userName = (EditText)findViewById(R.id.editText_nameID);
        userAge = (EditText)findViewById(R.id.editText_ageID);
        bmiHeight = (EditText)findViewById(R.id.editText_heightID);
        bmiWeigh = (EditText)findViewById(R.id.editText_weightID);
        starButton = (ImageButton)findViewById(R.id.imageButton);

        starButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = userName.getText().toString();
                //用Logcat檢查
                Log.d(TAG,"name= "+name);
                //判斷長度及是否為空
                if(userName.length()==0){
                    Toast.makeText(context,"Please input name.",Toast.LENGTH_SHORT).show();
                }
                if(userAge.length()==0){
                    Toast.makeText(context,"Please input age.",Toast.LENGTH_SHORT).show();
                    age = 1;//若沒輸入數值, 空白執行會當機.
                }else{
                    temp = userAge.getText().toString();
                    Log.d(TAG,"age= "+temp);
                    age = Integer.parseInt(temp); //transfer String to integer轉字串為整數
                }
                if(bmiHeight.length()==0 || bmiWeigh.length()==0){
                    Toast.makeText(context,"Please input your height and weight.",Toast.LENGTH_SHORT).show();
                }else{
                    temp = bmiHeight.getText().toString();
                    Log.d(TAG,"height ="+temp);
                    temp1 = bmiWeigh.getText().toString();
                    Log.d(TAG,"weight ="+temp1);

                    height = Integer.parseInt(temp);
                    weight = Integer.parseInt(temp1);

                    Intent calIntent = new Intent(context,BMIActivity.class);
                    calIntent.putExtra("name",name);
                    calIntent.putExtra("age",age);
                    calIntent.putExtra("height",height);
                    calIntent.putExtra("weight",weight);
                    startActivity(calIntent);
                }
            }
        });
    }
}
