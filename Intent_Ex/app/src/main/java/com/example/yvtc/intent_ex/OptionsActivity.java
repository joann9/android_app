package com.example.yvtc.intent_ex;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class OptionsActivity extends Activity {

    private Button cancelBtn, okBtn;
    private RadioGroup radioGroup;
    private CheckBox checkSport, checkReading, checkPainting, checkMovie ;
    private Context context;
    //private boolean maleFlag , femaleFlag;
    //private boolean sportFlag, readingFlag, paintingFlag, movieFlag;
    private String sc1, sc2, sc3, sc4, sr, sc;
    private RadioButton radioMale, radioFemale;
    private final int resultOKCode = 2;

    Intent intent = getIntent();
    //Bundle data = intent.getExtras();
    //String name = data.getString("name");
    //int age = data.getInt("age");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        context = this;
        setTitle("Options"); //改畫面左上角的名稱.

        cancelBtn = (Button) findViewById(R.id.btn_cancel);
        okBtn = (Button) findViewById(R.id.btn_ok);
        cancelBtn.setOnClickListener(new BtnOnClick());
        okBtn.setOnClickListener(new BtnOnClick());

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup_id);
        radioMale = (RadioButton) findViewById(R.id.radioButton_male);
        radioFemale = (RadioButton) findViewById(R.id.radioButton_female);
        checkSport = (CheckBox) findViewById(R.id.checkBox_sport);
        checkReading = (CheckBox) findViewById(R.id.checkBox_reading);
        checkPainting = (CheckBox) findViewById(R.id.checkBox_painting);
        checkMovie = (CheckBox) findViewById(R.id.checkBox_movie);
        checkSport.setOnCheckedChangeListener(new onClickCheckBox());
        checkReading.setOnCheckedChangeListener(new onClickCheckBox());
        checkPainting.setOnCheckedChangeListener(new onClickCheckBox());
        checkMovie.setOnCheckedChangeListener(new onClickCheckBox());

        radioGroup.setOnCheckedChangeListener(new CheckRadioChange());


    }

    private class CheckRadioChange implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch(checkedId){
                case R.id.radioButton_male:
                    //maleFlag = true;
                    //femaleFlag = false;
                    if(radioMale.isChecked()){
                        sr = radioMale.getText().toString();
                    }
                    break;

                case R.id.radioButton_female:
                    //maleFlag = false;
                    //femaleFlag = true;
                    if(radioFemale.isChecked()){
                        sr = radioFemale.getText().toString();
                    }
                    break;
            }
        }
    }

    private class onClickCheckBox implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            //int n=0;

            if (checkSport.isChecked()){
                //n = n++;
               // sportFlag = true;
                sc1 = checkSport.getText().toString();
            } else {
                //sportFlag = false;
                sc1 = "";
            }
            if (checkReading.isChecked()){
                //n = n++;
                //readingFlag = true;
                sc2 = checkReading.getText().toString();
            } else {
                //readingFlag = false;
                sc2 = "";
            }
            if (checkPainting.isChecked()){
                //n = n++;
                //paintingFlag = true;
                sc3 = checkPainting.getText().toString();
            } else {
                //paintingFlag = false;
                sc3 = "";
            }
            if (checkMovie.isChecked()){
                //n = n++;
                //movieFlag = true;
                sc4 = checkMovie.getText().toString();
            } else {
                //movieFlag = false;
                sc4 = "";
            }


        }


    }

    private class BtnOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_cancel:

                    checkSport.setChecked(false);
                    checkReading.setChecked(false);
                    checkPainting.setChecked(false);
                    checkMovie.setChecked(false);
                    sc1=sc2=sc3=sc4="";
                    radioMale.setChecked(false);
                    radioFemale.setChecked(false);
                    sr = "";
                    break;

                case R.id.btn_ok:

                    if(radioMale.isChecked() || radioFemale.isChecked()){
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("sc1", sc1);
                        resultIntent.putExtra("sc2", sc2);
                        resultIntent.putExtra("sc3", sc3);
                        resultIntent.putExtra("sc4", sc4);
                        resultIntent.putExtra("sr", sr);
                        resultIntent.putExtra("sc", sc);

                        setResult(resultOKCode, resultIntent);
                        finish();
                    } else {
                        Toast.makeText(context, "請選擇性別", Toast.LENGTH_SHORT).show();
                    }



                    break;
            }
        }
    }
}
