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

public class LightActivity extends Activity {

    private Context context;
    private TextView textView;
    private ImageView imageView;
    private SensorManager sensorManager;
    private LightListen listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        setTitle("light Sensor");
        context = this;
        textView = (TextView) findViewById(R.id.textViewID2);
        textView.setText("");
        imageView = (ImageView) findViewById(R.id.imageViewID2);

        //把sensorManager叫進來
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        //回傳一個sensor物件
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT); //NO.5
        listener = new LightListen(); //Inner class
        sensorManager.registerListener(listener, sensor,sensorManager.SENSOR_DELAY_FASTEST);

    }

    private class LightListen implements SensorEventListener{
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            StringBuilder sb = new StringBuilder();
            sb.append("Sensor: " + sensorEvent.sensor.getName()+"\n");
            float lux = sensorEvent.values[0];
            sb.append("value : "+lux+" Lux\n");
            textView.setText(sb.toString());

            if(lux <=20){
                imageView.setImageResource(R.drawable.dark_light);
            } else {
                imageView.setImageResource(R.drawable.imageslight);
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
