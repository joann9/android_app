package com.example.yvtc.intentapp;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.net.URI;

public class MainActivity extends Activity {

    private ImageButton phoneButton, emailButton, httpButton, searchButton;
    private EditText intentData;
    private Context context;
    private String action;
    private Uri uri;
    private Intent intent;
    private final static String TAG = "Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        phoneButton = (ImageButton)findViewById(R.id.imageButton1);
        emailButton = (ImageButton)findViewById(R.id.imageButton2);
        httpButton = (ImageButton)findViewById(R.id.imageButton3);
        searchButton = (ImageButton)findViewById(R.id.imageButton4);

        intentData = (EditText)findViewById(R.id.editText);

        phoneButton.setOnClickListener(new imageButtonClick());
        emailButton.setOnClickListener(new imageButtonClick());
        httpButton.setOnClickListener(new imageButtonClick());
        searchButton.setOnClickListener(new imageButtonClick());
    }

    private class imageButtonClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.imageButton1:
                    //Toast.makeText(context, "imageButton1 = Phone Button",Toast.LENGTH_SHORT).show();
                    action = Intent.ACTION_DIAL; //播電話, action 是 String.
                    String phoneNumber = intentData.getText().toString();
                    if(phoneNumber == null || phoneNumber .equals("")){
                        Toast.makeText(context,"Please input phone number.",Toast.LENGTH_SHORT).show();
                    } else{
                        uri = Uri.parse("tel:"+phoneNumber); //uri 是 URI 類別
                        Log.d(TAG,"uri="+uri);

                        intent = new Intent(action,uri);
                        startActivity(intent);
                    }

                    break;
                case R.id.imageButton2:
                    //Toast.makeText(context,"imageButton2 = Email Button",Toast.LENGTH_SHORT).show();
                    action = Intent.ACTION_SENDTO;
                    String emailAddr = intentData.getText().toString();
                    if(emailAddr == null || emailAddr.equals("")){
                        Toast.makeText(context,"Please input email address.",Toast.LENGTH_SHORT).show();
                    } else {
                        uri = Uri.parse("mailto: "+emailAddr);
                        intent = new Intent(action,uri);
                        startActivity(intent);
                    }

                    break;
                case R.id.imageButton3:
                    //Toast.makeText(context,"imageButton3 = HTTP Button",Toast.LENGTH_SHORT).show();
                    action = Intent.ACTION_VIEW;
                    String webAddr = intentData.getText().toString();
                    if(webAddr == null || webAddr.equals("")){
                        Toast.makeText(context,"Please input web site address.",Toast.LENGTH_SHORT).show();
                    } else {
                        uri = Uri.parse("http:"+webAddr);
                        intent = new Intent(action,uri);
                        startActivity(intent);
                    }

                    break;
                case R.id.imageButton4:
                    //Toast.makeText(context,"imageButton4 = Search Button",Toast.LENGTH_SHORT).show();
                    action = Intent.ACTION_WEB_SEARCH;
                    String webSearch = intentData.getText().toString();
                    if(webSearch == null || webSearch.equals("")){
                        Toast.makeText(context,"Please input search data.",Toast.LENGTH_SHORT).show();
                    } else{
                        intent = new Intent(action);
                        intent.putExtra(SearchManager.QUERY, webSearch);
                        startActivity(intent);
                    }

                    break;
            }

        }
    }
}
