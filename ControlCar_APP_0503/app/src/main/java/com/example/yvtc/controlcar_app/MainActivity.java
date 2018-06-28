package com.example.yvtc.controlcar_app;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.Set;

import android.Manifest;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
//import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private int mode;
    private final int ButtonMode = 1;
    private final int SensorMode = 2;
    private final int ControlMode = 3;
    private Switch btSwitch;
    private ListView btListView;
    private BluetoothAdapter btAdapter;
    private static final int REQUEST_ENABLE_BT = 2;
    private Set<BluetoothDevice> allBTDevices;
    private ArrayList<String> btDeviceList;
    private ArrayAdapter<String> adapter;
    private static final int Permission_REQUEST_CODE = 100;  //隨便給數值
    private boolean receiverFlag = false;
    private String itemData;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        context = this;
        mode = ButtonMode;

        btSwitch = (Switch) findViewById(R.id.btSwitch);
        btListView = (ListView) findViewById(R.id.listViewID);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        //確認有無被打開
        if(btAdapter == null){
            finish();
        }else if (!btAdapter.isEnabled()) { //如果沒有被打開, 就開啟
            Intent btIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(btIntent, REQUEST_ENABLE_BT);
        }

        btSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){  //switch 打開
                    allBTDevices = btAdapter.getBondedDevices(); //所有曾經被註過的資料, 配對過的bluetooth
                    btDeviceList = new ArrayList<String>();
                    if(allBTDevices.size() > 0){ //一筆一筆找出來
                        for(BluetoothDevice device : allBTDevices){
                            btDeviceList.add("Paired:   "+ device.getName()+"\n"+device.getAddress());
                            adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, btDeviceList);
                            btListView.setAdapter(adapter);
                        }
                    }
                } else { //switch 關掉, 資料要清掉
                    if(adapter != null){
                        adapter.clear();
                        adapter.notifyDataSetChanged();
                    }
                }

            }
        });
        btListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                itemData = parent.getItemAtPosition(position).toString();  //bluetooth的資料, 傳到下一個Activity去
                Toast.makeText(context, itemData, Toast.LENGTH_SHORT).show();
                if(mode == ButtonMode){
                    intent = new Intent(context, CarActivity.class);
                    intent.putExtra("layout", R.layout.activity_car_button);
                    intent.putExtra("btData", itemData);
                    startActivity(intent);
                } else if(mode == SensorMode){
                    intent = new Intent(context, CarActivity.class);
                    intent.putExtra("layout", R.layout.activity_car_sensor);
                    intent.putExtra("btData", itemData);
                    startActivity(intent);
                } else if(mode == ControlMode){
                    intent = new Intent(context, ControlActivity.class);
                    intent.putExtra("btData", itemData);
                    startActivity(intent);
                }

            }
        });


    } //end of onCreate()

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){

            case REQUEST_ENABLE_BT:

                if(requestCode == RESULT_CANCELED){
                    Toast.makeText(context, "Deny BT enable", Toast.LENGTH_SHORT).show();
                    finish();
                } else if(requestCode == RESULT_OK){
                    Toast.makeText(context,"Turn on BT", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch((item.getItemId())){

            case R.id.bt_search:
                int permission = ActivityCompat.checkSelfPermission(context, "Manifest.permission.ACCESS_COARSE_LOCATION");
                if(permission != PERMISSION_GRANTED)    //check 這個value , 如果不同意, 希望它同意
                {  //權限被關掉, 打開
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION}, Permission_REQUEST_CODE);
                }
                btAdapter.startDiscovery();

                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND); //註冊廣播接收器
                receiverFlag = true;

                registerReceiver(mReceiver, filter);
                Toast.makeText(context, "Start to scan BT device.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.bt_discovery: //被別人發現
                btAdapter.cancelDiscovery();
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 100); //100秒
                startActivity(intent);
                Toast.makeText(context, "Start to be found", Toast.LENGTH_SHORT).show();

                break;

            case R.id.car_buttonMode:
                mode = ButtonMode;
                break;

            case R.id.car_sensorMode:
                mode = SensorMode;
                break;

            case R.id.control_mode:
                mode = ControlMode;
                break;
        }



        return super.onOptionsItemSelected(item);
    }
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(adapter==null){
                    Toast.makeText(context, "Please switch on", Toast.LENGTH_SHORT).show();
                }else{
                    adapter.add("Found:    "+device.getName()+"\n"+device.getAddress());
                }

            }
        }
    };

    @Override //承接上面的 request code(case R.id.bt_search:)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case Permission_REQUEST_CODE:
                if(grantResults[0] == PERMISSION_GRANTED){

                }else{

                }
                return;  //一定要用return, 用 break 會當掉, 也許是 android的 bug


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        btAdapter.cancelDiscovery();
        if(mReceiver != null){
            if(receiverFlag){
                unregisterReceiver(mReceiver);
                receiverFlag=false;

            }
        }
    }
}
