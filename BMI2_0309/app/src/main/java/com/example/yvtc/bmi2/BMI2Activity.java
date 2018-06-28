package com.example.yvtc.bmi2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class BMI2Activity extends Activity {
    private TextView BMIResult;
    private Context context;
    private static final String TAG = "BMI";
    private final String errorMsg="Your height is error.\n It can not be less than 30cm.\n";
    private double BMIValue;
    private final int resultErrorCode = 3;
    private final int resultCode = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi2);

        context = this;
        setTitle("BMI2 Result"); //改畫面左上角的名稱.

        BMIResult = (TextView) findViewById(R.id.textView_resultID);

        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        String name = data.getString("name");
        int age = data.getInt("age");
        Log.d(TAG,"name = "+name);
        Log.d(TAG,"age = "+age);
        BMIResult.setText("\nName: "+name+"\nAge: "+age+"\n");
        double height = data.getInt("height")/100.0; //輸入公分, 取出要公尺, 所以除100
        Log.d(TAG,"height="+height);

        if(height < 0.3){
            Toast.makeText(context,"The height can not be less than 30cm.",Toast.LENGTH_LONG).show();
            Intent errorIntent = new Intent();
            errorIntent.putExtra("bmi_error",errorMsg);
            setResult(resultErrorCode , errorIntent);
            Log.d(TAG,"resultErrorCode = 3");
            finish(); //返回, 把這個螢幕關掉
        }
        double weight = data.getInt("weight");
        Log.d(TAG,"weight"+weight);

        BMIValue = weight/(height*height);
        Log.d(TAG,"BMIValue"+BMIValue);
        NumberFormat nf = new DecimalFormat("##.000");
        //DecimalFormat 是 NumberFormat 的子類別
        BMIResult.append("Your BMI value is :"+ nf.format(BMIValue)+"\n");

        String textResult = getBMIMessage(BMIValue);
        BMIResult.append("Your weight is : "+textResult);

        //傳回 error Intent
        Intent resultIntent = new Intent();
        resultIntent.putExtra("bmi_record",textResult);
        resultIntent.putExtra("bmi_value",BMIValue);
        setResult(resultCode, resultIntent);
    }

    private String getBMIMessage(double bmiValue) {
        String message;
        //bmi_normal, bmi_high, bmi_overhigh,bmi_error, 定義在 /app/res/values/strings.xml

        if(bmiValue > 0 && bmiValue < 20){
            message = "偏低";
        }else if(bmiValue >=20 && bmiValue <26){
            message = getResources().getString(R.string.bmi_normal);
        }else if(bmiValue >=26 && bmiValue <30){
            message = getResources().getString(R.string.bmi_high);
        }else if(bmiValue >=30 && bmiValue <50){
            message = getResources().getString(R.string.bmi_overhigh);
        }else{
            message = getResources().getString(R.string.bmi_error);
        }
        return message;

    }

}
