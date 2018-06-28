package com.example.user.listview_ex_2;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends Activity {

    private Context context;
    private Switch switch_m;
    private ListView listView_m;
    private final int carButton = 3;
    private final int carSensor = 4;
    private final int control = 5;
    private int flag =0;
    private String[] mode = {"Bluetooth 1","Bluetooth 2","Bluetooth 3","Bluetooth 4","Bluetooth 5"};
    private ArrayAdapter adapter;
    private List<String> dataList;
    private  int i;
    private final int IntentRequestCode = 1; //收結果才需要 IntentRequestCode
    private final int carModeCode = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        switch_m = (Switch) findViewById(R.id.switch_m);
        listView_m = (ListView) findViewById(R.id.listView_m);

        switch_m.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){   //switch ON 時
                    setTitle("Bluetooth ON");
                    dataList = new ArrayList<String>();
                    for (int i=0; i<5 ; i++){
                        dataList.add(mode[i]);
                    }

                    adapter = new ArrayAdapter(context,android.R.layout.simple_list_item_1,dataList);
                    //adapter = new ArrayAdapter(context,android.R.layout.simple_list_item_1,intDataList);
                    listView_m.setAdapter(adapter);
                    listView_m.setOnItemClickListener(new itemOnClick());

                } else{
                    flag = 0;

                    dataList.clear(); //清除
                    adapter.notifyDataSetChanged();
                    setTitle("Bluetooth OFF");
                }
            }
        });
    }

    private class itemOnClick implements android.widget.AdapterView.OnItemClickListener {
        private  String btItem;

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            btItem = dataList.get(position); //bluetooth物件, 取得位置

            if (flag == 1){

                Intent intent = new Intent(context, CarButtonMode.class);
                intent.putExtra("mode",carButton);
                intent.putExtra("title", "Car Button Mode");
                intent.putExtra("btItem",btItem);
                //startActivity(intent);
                startActivityForResult(intent,IntentRequestCode);
            } else if(flag == 2){

                Intent intent2 = new Intent(context, CarSensorMode.class);
                intent2.putExtra("mode",carSensor);
                intent2.putExtra("title", "Car Sensor Mode");
                intent2.putExtra("btItem",btItem);
                //startActivity(intent2);
                startActivityForResult(intent2,IntentRequestCode);

            } else if(flag == 3){

                Intent intent3 = new Intent(context, ControlMode.class);
                intent3.putExtra("mode", control);
                intent3.putExtra("title", "Control Mode");
                intent3.putExtra("btItem",btItem);
                //startActivity(intent3);
                startActivityForResult(intent3,IntentRequestCode);
            }

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //flag = 0;


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater m_inflater = getMenuInflater();
        m_inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                Toast.makeText(context, "You selected search.", Toast.LENGTH_SHORT).show();

                return true;

            case R.id.menu_discoverable:
                Toast.makeText(context, "You selected discoverable.", Toast.LENGTH_SHORT).show();

                return true;

            case R.id.menu_carButtonMode:
                flag = 1;
                setTitle("Car Button Mode");
                return true;

            case R.id.menu_carSensorMode:
                flag = 2;
                setTitle("Car Sensor Mode");
                return true;

            case R.id.menu_controlMode:
                flag = 3;
                setTitle("Control Mode");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }



}
