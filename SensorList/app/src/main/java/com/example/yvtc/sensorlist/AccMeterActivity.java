package com.example.yvtc.sensorlist;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AccMeterActivity extends Activity {

    private Context context;
    private TextView textView;
    private ImageView imageView1,imageView2,imageView3,imageView4;
    private SensorManager sensorManager;
    private MyAccListen listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_meter);

        setTitle("AccMeter Sensor");
        context = this;

        textView = (TextView) findViewById(R.id.textViewID3);
        textView.setText("");
        imageView1 = (ImageView)findViewById(R.id.imageView1);
        imageView2 = (ImageView)findViewById(R.id.imageView2);
        imageView3 = (ImageView)findViewById(R.id.imageView3);
        imageView4 = (ImageView)findViewById(R.id.imageView4);

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

    private class MyAccListen implements SensorEventListener{
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            StringBuilder sb = new StringBuilder();
            sb.append("sensor : "+sensorEvent.sensor.getName()+"\n");
            sb.append("values : \n");
            sb.append("X :"+sensorEvent.values[0]+"\n");
            sb.append("Y :"+sensorEvent.values[1]+"\n");
            sb.append("Z :"+sensorEvent.values[2]+"\n");
            textView.setText(sb.toString());

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
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

    @Override
    protected void onDestroy() {
        sensorManager.unregisterListener(listener);
        super.onDestroy();
    }
}
