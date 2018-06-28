package com.example.yvtc.sensorlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private Context context;
    private Switch sensorSwitch;
    private ListView sensorListView;
    private SensorManager sensormanager;
    private List<Sensor> sensorList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        sensorSwitch = (Switch) findViewById(R.id.switch1);
        sensorListView = (ListView) findViewById(R.id.ListView);

        sensorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecded) {

                if(isChecded){  //switch ON 時
                    sensormanager = (SensorManager) getSystemService(Context.SENSOR_SERVICE); //取得sensorManager,控制sensor最主要的東西
                    sensorList = sensormanager.getSensorList(Sensor.TYPE_ALL);//列出手機上可用的sensor
                    List<String> sensorListName = new ArrayList<>();

                    for(Sensor sensor : sensorList){ //把所有sensorList的資料取出來
                        sensorListName.add(sensor.getType()+" - "+sensor.getName()+":"+sensor.getPower()+"mA\n"+sensor.getVendor());
                        //getType: sensorID,name:名稱, Power:耗電量, Vendor:IC廠商
                    }

                    adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,sensorListName);//畫面位置, 希望內容,
                    sensorListView.setAdapter(adapter);
                    setTitle("Sensor nmber : "+sensorListName.size()); //到目前為止, 執行會出現手機有多少sensor
                    //1 加速器, 5, 8
                    sensorListView.setOnItemClickListener(new itemOnClick());
                } else{  //switch OFF 時
                    adapter.clear(); //清除
                    adapter.notifyDataSetChanged();
                    setTitle("Sensor List");

                }

            }
        });


    }

    private class itemOnClick implements android.widget.AdapterView.OnItemClickListener {
        private Sensor sensorItem;
        private Sensor sensor;
        private Intent sensorintent;

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            sensorItem = sensorList.get(position); //sensor物件, 取得位置
            //取得sensor ID
            int sensorID = sensorItem.getType();


            //用switch方式把ID放進去
            switch(sensorID){
                case Sensor.TYPE_PROXIMITY:
                    sensor = sensormanager.getDefaultSensor(Sensor.TYPE_PROXIMITY); // NO.8
                    if(sensor == null){
                        Toast.makeText(context, "no Proximity sensor", Toast.LENGTH_LONG).show();
                    } else {
                        sensorintent = new Intent(context,ProximityActivity.class); //跳到下一個畫面
                        startActivity(sensorintent);
                    }
                    break;

                case Sensor.TYPE_LIGHT:
                    sensor = sensormanager.getDefaultSensor(Sensor.TYPE_LIGHT); // NO.5
                    if(sensor == null){
                        Toast.makeText(context, "no Proximity sensor", Toast.LENGTH_LONG).show();
                    } else {
                        sensorintent = new Intent(context,LightActivity.class); //跳到下一個畫面
                        startActivity(sensorintent);
                    }

                    break;

                case Sensor.TYPE_ACCELEROMETER:
                    sensor = sensormanager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); // NO.1
                    if(sensor == null){
                        Toast.makeText(context, "no ACC sensor", Toast.LENGTH_LONG).show();
                    } else {
                        sensorintent = new Intent(context,AccMeterActivity.class); //跳到下一個畫面
                        startActivity(sensorintent);
                    }

                    break;

                default:
                    Toast.makeText(context, "Programs do not support this sensor.", Toast.LENGTH_LONG).show();
                    break;
            }

        }
    }
}
