package com.example.yvtc.bmi2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MainActivity extends Activity {

    private EditText userName,  userAge,bmiHeight,bmiWeigh;
    private ImageButton starButton;
    private Context context;
    private String temp, temp1;
    private int age;
    private int height, weight;
    private static final String TAG = "Main";
    private TextView bmiResult;
    private final int IntentRequestCode = 1; //收結果才需要 IntentRequestCode
    private final int resultErrorCode = 3;
    private final int resultOKCode = 2;


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
        bmiResult = (TextView)findViewById(R.id.textView_result);

        starButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String name = userName.getText().toString();
                    int age = Integer.parseInt(userAge.getText().toString());
                    int height = Integer.parseInt(bmiHeight.getText().toString());
                    int weight = Integer.parseInt(bmiWeigh.getText().toString());
                    Log.d(TAG,"name = "+name);
                    Log.d(TAG,"age = "+age);
                    Log.d(TAG,"height = "+height);
                    Log.d(TAG,"weight = "+weight);

                    Intent calIntent = new Intent(context, BMI2Activity.class);
                    Bundle dataBag = new Bundle();
                    dataBag.putString("name", name);
                    dataBag.putInt("age", age);
                    dataBag.putInt("height", height);
                    dataBag.putInt("weight", weight);
                    calIntent.putExtras(dataBag);
                    startActivityForResult(calIntent,IntentRequestCode); //final int IntentRequestCode = 1

                }catch(Exception e){
                    Toast.makeText(context,"Please input data",Toast.LENGTH_SHORT).show();

                }

                }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG,"requestCode = "+requestCode);
        Log.d(TAG,"resultCode = "+resultCode);

        if(requestCode == IntentRequestCode){
            switch (resultCode){
                case resultErrorCode:
                    Toast.makeText(context,"The Height is error.",Toast.LENGTH_SHORT).show();
                    String msg = data.getStringExtra("bmi_error");  //data 是上面參數的Intent data
                    bmiResult.setText(msg);
                    break;
                case resultOKCode: //上面參數有resultCode, 所以改成resultOKCode
                    String record = data.getStringExtra("bmi_record");
                    double value = data.getDoubleExtra("bmi_value", 0);

                    Log.d(TAG,"record = "+record);
                    Log.d(TAG,"value = "+value);

                    NumberFormat nf = new DecimalFormat("##.00");
                    bmiResult.setText("BMI value is : "+nf.format(value)+"\n");
                    bmiResult.append("Youre weight is : "+record);
                    break;

            }
        }

    }
}
