package com.example.yvtc.sensorlist;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ProximityActivity extends Activity {

    private Context context;
    private TextView textView;
    private ImageView imageView;
    private SensorManager sensorManager;
    private Sensor sensor;
    private ProximityListen listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity);

        context = this;

        textView = (TextView)findViewById(R.id.textViewID1);
        textView.setText("");
        imageView = (ImageView)findViewById(R.id.imageViewID1);
        //sensor資料再呼叫一次
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //把距離的sensor物件取出
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        //內建一個Inner Class, 監聽程式
        listener = new ProximityListen();
        //註冊監聽
        sensorManager.registerListener(listener, sensor,sensorManager.SENSOR_DELAY_FASTEST);

    }
    //要實作
    private class ProximityListen implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) { //準確改變
            StringBuilder sb = new StringBuilder();
            sb.append("Sensor : "+sensorEvent.sensor.getName()+"\n"); //放資料進去  把even資料取出來

            //設一個參數去盛接進來
            float value = sensorEvent.values[0];
            sb.append("Proximity value : "+value+"cm\n\n");
            //textView.setText(sb.toString());

            //判斷數值
            if(value <1){
                textView.setText(sb.toString()+"Too close!!!");
                imageView.setImageResource(R.drawable.p2); //給哭臉
            } else {
                textView.setText(sb.toString());
                imageView.setImageResource(R.drawable.p1);//給笑臉
            }
        }

        @Override  //通常是空的, 不做
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }


    @Override//監聽關掉, 在onDestroy 階段
    protected void onDestroy() {
        sensorManager.unregisterListener(listener);
        super.onDestroy();
    }
}
