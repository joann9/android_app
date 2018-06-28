package idv.wujidadi.listviewex;

import android.bluetooth.*;
import android.content.*;
import android.os.*;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import android.widget.*;

import java.util.*;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    private ArrayList<String> btList, btDeviceList;
    private BluetoothAdapter btAdapter;
    private boolean carButtonModeFlag = false, carSensorModeFlag = false, controlModeFlag = false, receiverFlag = false;
    private Context context;
    private int mode;
    private final int menuID_1 = 1, menuID_2 = 2, menuID_3 = 3, menuID_4 = 4, menuID_5 = 5,
                      search = 1, discoverable = 2, carButtonMode = 3, carSensorMode = 4, controlMode = 5, speechMode = 6, rockerMode = 7,
                      resultCode = 2,
                      Permission_REQUEST_CODE = 100;
    private Intent carButtonModeIntent, carSensorModeIntent, controlModeIntent, intent;
    private ListView btItem;
    private Set<BluetoothDevice> allBTDevices;
    private static final int REQUEST_ENABLE_BT = 2;
    private String itemData;
    private String[] bluetooth;
    private Switch btSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        mode = carButtonMode;

        btSwitch = (Switch) findViewById(R.id.btSwitch);
        btItem = (ListView) findViewById(R.id.btItem);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            finish();
        } else if (!btAdapter.isEnabled()) {
            Intent btIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(btIntent, REQUEST_ENABLE_BT);
        }

        //bluetooth = getResources().getStringArray(R.array.bluetooth);

        //btItem.setVisibility(View.INVISIBLE);

        btSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btItem.setVisibility(View.VISIBLE);
                    allBTDevices = btAdapter.getBondedDevices();    // 取得配對過的藍牙裝置
                    btDeviceList = new ArrayList<String>();
                    if (allBTDevices.size() > 0) {
                        for (BluetoothDevice device : allBTDevices) {
                            btDeviceList.add(/*"Paired:   " + */device.getName() + "\n" + device.getAddress());
                        }
                        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, btDeviceList);
                        btItem.setAdapter(adapter);
                    }
                } else {
                    btItem.setVisibility(View.INVISIBLE);
                    if (adapter != null) {
                        adapter.clear();
                        adapter.notifyDataSetChanged(); // 刷新藍牙列表
                    }
                }
            }
        });

//        btList = new ArrayList<String>();
//        for (String bt : bluetooth) {
//            btList.add(bt);
//        }
//
//        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, btList);
//        btItem.setAdapter(adapter);
//        btItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String msg = parent.getItemAtPosition(position).toString();
//                //Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
//
//                if (carButtonModeFlag) {
//                    carButtonModeIntent = new Intent(context, CarInButtonModeActivity.class);
//                    carButtonModeIntent.putExtra("Item", msg);
//                    startActivity(carButtonModeIntent);
//                } else if (carSensorModeFlag) {
//                    carSensorModeIntent = new Intent(context, CarInSensorModeActivity.class);
//                    carSensorModeIntent.putExtra("Item", msg);
//                    startActivity(carSensorModeIntent);
//                } else if (controlModeFlag) {
//                    controlModeIntent = new Intent(context, ControlModeActivity.class);
//                    controlModeIntent.putExtra("Item", msg);
//                    startActivity(controlModeIntent);
//                } else {
//                    Toast.makeText(context, "Please select mode first!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        btItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemData = parent.getItemAtPosition(position).toString();
                Toast.makeText(context, itemData, Toast.LENGTH_SHORT).show();

                if (mode == carButtonMode) {
                    intent = new Intent(context, CarInButtonModeActivity.class);
                    intent.putExtra("layout", R.layout.activity_carinbuttonmode);
                    intent.putExtra("btData", itemData);
                    startActivity(intent);
                } else if (mode == carSensorMode) {
                    intent = new Intent(context, CarInSensorModeActivity.class);
                    intent.putExtra("layout", R.layout.activity_carinsensormode);
                    intent.putExtra("btData", itemData);
                    startActivity(intent);
                } else if (mode == controlMode) {
                    intent = new Intent(context, ControlModeActivity.class);
                    intent.putExtra("layout", R.layout.activity_controlmode);
                    intent.putExtra("btData", itemData);
                    startActivity(intent);

                } else if (mode == speechMode){
                    intent = new Intent(context, SpeechModeActivity.class);
                    intent.putExtra("layout", R.layout.activity_speechmode);
                    intent.putExtra("btData", itemData);
                    startActivity(intent);

                } else if (mode == rockerMode){
                    intent = new Intent(context, RockerModeActivity.class);
                    intent.putExtra("layout", R.layout.activity_rockermode);
                    intent.putExtra("btData", itemData);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (requestCode == RESULT_CANCELED) {
                    Toast.makeText(context, "Deny Bluetooth enable", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (resultCode == RESULT_OK) {
                    Toast.makeText(context, "Turn on Bluetooth", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionmenu, menu);

//        menu.add(menuID_3, carButtonMode, Menu.NONE, "Car ButtonMode");
//        menu.add(menuID_4, carSensorMode, Menu.NONE, "Car SensorMode");
//        menu.add(menuID_5, controlMode, Menu.NONE, "Control Mode");

        return true;
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(BluetoothDevice.ACTION_FOUND)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (adapter == null) {
                Toast.makeText(context, "Please switch on", Toast.LENGTH_SHORT).show();
            } else {
                adapter.add(/*"Found:    " + */device.getName() + "\n" + device.getAddress());
            }
        }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_btSearch:
                int permission = ActivityCompat.checkSelfPermission(context, "Manifest.permission.ACCESS_COARSE_LOCATION");
                if (permission != PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{"Manifest.permission.ACCESS_COARSE_LOCATION", "Manifest.permission.ACCESS_FINE_LOCATION"}, Permission_REQUEST_CODE);
                }

                btAdapter.startDiscovery();

                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                receiverFlag = true;
                registerReceiver(mReceiver, filter);

                if (adapter != null) {
                    Toast.makeText(context, "Scanning Bluetooth devices...", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.menu_btDiscoverable:
                btAdapter.cancelDiscovery();
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 60);
                startActivity(intent);
                Toast.makeText(context, "Bluetooth is now discoverable.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_carButtonMode:
                Toast.makeText(context, "Starting Car Button Mode...", Toast.LENGTH_SHORT).show();
                mode = carButtonMode;
                break;

            case R.id.menu_carSensorMode:
                Toast.makeText(context, "Starting Car Sensor Mode...", Toast.LENGTH_SHORT).show();
                mode = carSensorMode;
                break;

            case R.id.menu_controlMode:
                Toast.makeText(context, "Starting Control Mode...", Toast.LENGTH_SHORT).show();
                mode = controlMode;
                break;

            case R.id.menu_speech_mode:
                Toast.makeText(context, "Starting Car Speech Mode...", Toast.LENGTH_SHORT).show();
                mode = speechMode;
                break;

            case R.id.menu_rocker_mode:
                Toast.makeText(context, "Starting Car Rocker Mode...", Toast.LENGTH_SHORT).show();
                mode = rockerMode;
                break;

            default:
                Toast.makeText(context, "Unassigned...", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        //Log.d("MenuItemID", "MenuItemID = " + item.getItemId());
//        switch (item.getItemId()) {
//            case R.id.menu_btSearch:
//                Toast.makeText(context, "Searching Bluetooth signal...",Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.menu_btDiscoverable:
//                Toast.makeText(context, "Bluetooth is now discoverable.",Toast.LENGTH_SHORT).show();
//                break;
//            case carButtonMode:
//                Toast.makeText(context, "Starting Car ButtonMode...",Toast.LENGTH_SHORT).show();
//                carButtonModeFlag = true;
//                carSensorModeFlag = false;
//                controlModeFlag = false;
//                break;
//            case carSensorMode:
//                Toast.makeText(context, "Starting Car SensorMode...",Toast.LENGTH_SHORT).show();
//                carButtonModeFlag = false;
//                carSensorModeFlag = true;
//                controlModeFlag = false;
//                break;
//            case controlMode:
//                Toast.makeText(context, "Starting Control Mode...",Toast.LENGTH_SHORT).show();
//                carButtonModeFlag = false;
//                carSensorModeFlag = false;
//                controlModeFlag = true;
//                break;
//            default:
//                Toast.makeText(context, "Unassigned...",Toast.LENGTH_SHORT).show();
//                break;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Permission_REQUEST_CODE:
                if (grantResults[0] == PERMISSION_GRANTED) {

                } else {

                }
                return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        btAdapter.cancelDiscovery();
//        if (mReceiver != null) {
//            if (receiverFlag) {
//                unregisterReceiver(mReceiver);
//                receiverFlag = false;
//            }
//        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);  //關閉應用程式
    }
}
