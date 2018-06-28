package com.example.user.listview_ex_2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CarSensorMode extends Activity {

    private Context context;
    private TextView textView,textView2;
    private ImageView imageView1,imageView2,imageView3,imageView4;
    private SensorManager sensorManager;
    private MyAccListen listener;
    private Button btnLink;
    private final int carSensor = 4;
    private AlertDialog.Builder builder;
    private final int modeCode = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_sensor_mode);

        context = this;
        textView = (TextView) findViewById(R.id.textViewID3);
        textView2= (TextView) findViewById(R.id.textViewID4);
        textView.setText("");
        imageView1 = (ImageView)findViewById(R.id.imageView1);
        imageView2 = (ImageView)findViewById(R.id.imageView2);
        imageView3 = (ImageView)findViewById(R.id.imageView3);
        imageView4 = (ImageView)findViewById(R.id.imageView4);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        int carMode = intent.getIntExtra("mode",carSensor);
        String btItem = intent.getStringExtra("btItem");

        setTitle(title);
        textView.setText(btItem+" \n");
        setResult(carSensor, intent);

        btnLink = (Button)findViewById(R.id.btnLink);
        btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Link Button",Toast.LENGTH_SHORT).show();
            }
        });

        //把每一個圖關掉
        imageView1.setVisibility(View.INVISIBLE);
        imageView2.setVisibility(View.INVISIBLE);
        imageView3.setVisibility(View.INVISIBLE);
        imageView4.setVisibility(View.INVISIBLE);

        //畫面固定在直的方向
        int currentOrientation = this.getResources().getConfiguration().orientation; //取得目前畫面是直還是橫
        Log.d("ACC","current = "+currentOrientation);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // =1 //強制畫面設定為垂直

        //把sensorManager叫進來
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        //回傳一個sensor物件
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); //NO.1
        listener = new MyAccListen(); //Inner class
        sensorManager.registerListener(listener, sensor,sensorManager.SENSOR_DELAY_UI);


    }
    private class MyAccListen implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            StringBuilder sb = new StringBuilder();
            sb.append("sensor : "+sensorEvent.sensor.getName()+"\n");
            sb.append("values : \n");
            sb.append("X :"+sensorEvent.values[0]+"\n");
            sb.append("Y :"+sensorEvent.values[1]+"\n");
            sb.append("Z :"+sensorEvent.values[2]+"\n");
            textView2.setText(sb.toString());

            float X_value = sensorEvent.values[0];
            float Y_value = sensorEvent.values[1];
            float Z_value = sensorEvent.values[2];

            if(X_value < -2.0){
                imageView1.setVisibility(View.INVISIBLE);
                imageView2.setVisibility(View.INVISIBLE);
                imageView3.setVisibility(View.INVISIBLE);
                imageView4.setVisibility(View.VISIBLE);
            } else if(X_value > 2.0){
                imageView1.setVisibility(View.INVISIBLE);
                imageView2.setVisibility(View.INVISIBLE);
                imageView3.setVisibility(View.VISIBLE);
                imageView4.setVisibility(View.INVISIBLE);
            } else {
                if(Z_value > 5){
                    imageView1.setVisibility(View.VISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    imageView3.setVisibility(View.INVISIBLE);
                    imageView4.setVisibility(View.INVISIBLE);
                } else if(Z_value < 0){
                    imageView1.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.VISIBLE);
                    imageView3.setVisibility(View.INVISIBLE);
                    imageView4.setVisibility(View.INVISIBLE);
                } else {
                    imageView1.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    imageView3.setVisibility(View.INVISIBLE);
                    imageView4.setVisibility(View.INVISIBLE);
                }
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }


    @Override  //Menu設定開始
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mode_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_exit) {
            showDialog1();
        }
        return super.onOptionsItemSelected(item);
    }
    private void showDialog1() {
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Exit");
        builder.setMessage("Sure to Exit?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                /*//int flag = 0;
                Intent flagIntent = new Intent();
                //flagIntent.putExtra("flag", flag);
                setResult(modeCode, flagIntent);*/

                dialogInterface.dismiss();
                CarSensorMode.this.finish();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }  //Menu設定結束



    @Override
    protected void onDestroy() {
        sensorManager.unregisterListener(listener);
        super.onDestroy();
    }
}
