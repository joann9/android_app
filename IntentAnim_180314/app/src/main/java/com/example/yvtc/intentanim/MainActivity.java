package com.example.yvtc.intentanim;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {

    private Context context;
    private ImageButton imageBtn1,imageBtn2,imageBtn3,imageBtn4,imageBtn5;
    private final static String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this; //忘了寫程式會當掉

        imageBtn1 = (ImageButton) findViewById(R.id.imageButton1);
        imageBtn2 = (ImageButton) findViewById(R.id.imageButton2);
        imageBtn3 = (ImageButton) findViewById(R.id.imageButton3);
        imageBtn4 = (ImageButton) findViewById(R.id.imageButton4);
        imageBtn5 = (ImageButton) findViewById(R.id.imageButton5);

        imageBtn1.setOnClickListener(new imageOnClick());
        imageBtn2.setOnClickListener(new imageOnClick());
        imageBtn3.setOnClickListener(new imageOnClick());
        imageBtn4.setOnClickListener(new imageOnClick());
        imageBtn5.setOnClickListener(new imageOnClick());
    }

    private class imageOnClick implements View.OnClickListener {
        private String name;
        private ActivityOptions option;

        @Override
        public void onClick(View view) {

            Intent intent = new Intent(context,DisplayActivity.class);

            switch (view.getId()){
                case R.id.imageButton1:
                    //Toast.makeText(context,"Button1",Toast.LENGTH_SHORT).show();
                    name = getResources().getString(R.string.taiwan);
                    Log.d(TAG, "name= "+name);
                    intent.putExtra("title",name);
                    intent.putExtra("imageID", R.drawable.banner1);
                    intent.putExtra("txtID", R.string.taiwan_info);
                    //startActivity(intent);

                    imageBtn1.setTransitionName(name);
                    option = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,imageBtn1,name);

                    break;

                case R.id.imageButton2:
                    //Toast.makeText(context,"Button2",Toast.LENGTH_SHORT).show();
                    name = getResources().getString(R.string.sanya);
                    Log.d(TAG, "name= "+name);
                    intent.putExtra("title",name);
                    intent.putExtra("imageID", R.drawable.banner2);
                    intent.putExtra("txtID", R.string.sanya_info);
                    //startActivity(intent);

                    imageBtn2.setTransitionName(name);
                    option = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,imageBtn2,name);


                    break;

                case R.id.imageButton3:
                    //Toast.makeText(context,"Button3",Toast.LENGTH_SHORT).show();
                    name = getResources().getString(R.string.chengde);
                    Log.d(TAG, "name= "+name);
                    intent.putExtra("title",name);
                    intent.putExtra("imageID", R.drawable.banner3);
                    intent.putExtra("txtID", R.string.chengde_info);
                    //startActivity(intent);

                    imageBtn3.setTransitionName(name);
                    option = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,imageBtn3,name);

                    break;

                case R.id.imageButton4:
                    //Toast.makeText(context,"Button4",Toast.LENGTH_SHORT).show();
                    name = getResources().getString(R.string.great_wall);
                    Log.d(TAG, "name= "+name);
                    intent.putExtra("title",name);
                    intent.putExtra("imageID", R.drawable.banner4);
                    intent.putExtra("txtID", R.string.great_wall_info);
                    //startActivity(intent);

                    imageBtn4.setTransitionName(name);
                    option = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,imageBtn4,name);

                    break;

                case R.id.imageButton5:
                    //Toast.makeText(context,"Button5",Toast.LENGTH_SHORT).show();
                    name = getResources().getString(R.string.chong_cing);
                    Log.d(TAG, "name= "+name);
                    intent.putExtra("title",name);
                    intent.putExtra("imageID", R.drawable.banner5);
                    intent.putExtra("txtID", R.string.chong_cing_info);
                    //startActivity(intent);

                    imageBtn5.setTransitionName(name);
                    option = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,imageBtn5,name);

                    break;

            }
            //startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle());
            startActivity(intent,option.toBundle());
        }
    }
}
