package com.example.yvtc.bmi1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class BMIActivity extends Activity {

    private static final String TAG = "BMI";
    private TextView BMIResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        BMIResult = (TextView)findViewById(R.id.textView_resultID);

        Intent bmiIntent = getIntent();
        String name = bmiIntent.getStringExtra("name");
        int age = bmiIntent.getIntExtra("age",25);
        BMIResult.setText("\nName : "+name+"\nAge : "+Integer.toString(age)+"\n");

        double height = bmiIntent.getIntExtra("height",1)/100.0;
        Log.d(TAG,"height "+height);
        double weight = bmiIntent.getIntExtra("weight",1);
        Log.d(TAG,"weight "+weight);

        double BMIValue = weight/(height*height);
        Log.d(TAG,"BMIValue = "+BMIValue);

        NumberFormat nf = new DecimalFormat("##.00");
        String bmiString = nf.format(BMIValue);
        BMIResult.append("Your BMI value is : "+bmiString+"\n");
        String textResult = getBMIMessage(BMIValue);
        BMIResult.append("Your weight is :"+textResult+"\n");

    }

    private String getBMIMessage(double bmiValue) {
        String message;
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
