package idv.wujidadi.listviewex;

import android.app.*;
import android.bluetooth.*;
import android.content.*;
import android.content.pm.*;
import android.hardware.*;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.util.*;
import android.view.*;
import android.widget.*;

public class CarInSensorModeActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;
    private BluetoothAdapter btAdapter;
    private BTChatService mChatService;
    private Button ButtonLink;
    private Context context;
    private ImageView imageView_up, imageView_down, imageView_left, imageView_right;
    private int mode;
    private final int carSensorMode = 4;
    private MyAccListen listener;
    private SensorManager sensorManager;
    private static final String TAG = "Car",
                                GO_FORWARD = "f", GO_BACKWARD = "b", TURN_LEFT = "l", TURN_RIGHT = "r", CAR_STOP = "p",
                                Song_1 = "1",  Song_2 = "2", Song_3 = "3", Song_4 = "4", Song_OFF = "0";
    private String btName, btMacAddress, directionCmd, songCmd;
    private TextView btID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carinsensormode);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setTitle("Car Sensor Mode");
        context = this;

        btID = (TextView) findViewById(R.id.ciSmTv);

        Intent getIntent = getIntent();
        btName = getIntent.getStringExtra("btData");
        btID.setText(btName);
        mode = carSensorMode;

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        btAdapter.cancelDiscovery();

        mChatService = new BTChatService(context, mHandler);

        ButtonLink = (Button) findViewById(R.id.ciSmLb);
        ButtonLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btName != null) {
                    btMacAddress = btName.substring(btName.length() - 17);
                    BluetoothDevice device = btAdapter.getRemoteDevice(btMacAddress);
                    mChatService.connect(device);
                } else {
                    Toast.makeText(context, "No Paired BT device", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageView_up = (ImageView) findViewById(R.id.ciSmIvUp);
        imageView_down = (ImageView) findViewById(R.id.ciSmIvDn);
        imageView_left = (ImageView) findViewById(R.id.ciSmIvLf);
        imageView_right = (ImageView) findViewById(R.id.ciSmIvRt);

        imageView_up.setVisibility(View.INVISIBLE);  //讓imageView不要顯示
        imageView_down.setVisibility(View.INVISIBLE);
        imageView_left.setVisibility(View.INVISIBLE);
        imageView_right.setVisibility(View.INVISIBLE);

        int currentOrientation = this.getResources().getConfiguration().orientation;  //取得目前方向

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        listener = new MyAccListen();
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    // Sends a Command to remote BT device.
    private void sendCMD(String message) {
        // Check that we're actually connected before trying anything
        int mState = mChatService.getState();
        Log.d(TAG, "btstate in sendMessage =" + mState);

        if (mState != BTChatService.STATE_CONNECTED) {
            Log.d(TAG, "btstate =" + mState);
            // Toast.makeText(context, "Bluetooth device is not connected. ", Toast.LENGTH_SHORT).show();
            return;
        } else {
            // Check that there's actually something to send
            if (message.length() > 0) {
                // Get the message bytes and tell the BluetoothChatService to write
                byte[] send = message.getBytes();
                mChatService.BTWrite(send);
            }
        }
    }

    // The Handler that gets information back from the BluetoothChatService
    // There is no message queue leak problem
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    break;

                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    String mConnectedDevice = msg.getData().getString(Constants.DEVICE_NAME);
                    Toast.makeText(context, "Connected to "+ mConnectedDevice, Toast.LENGTH_SHORT).show();
                    break;

                case Constants.MESSAGE_TOAST:
                    Toast.makeText(context, msg.getData().getString(Constants.TOAST),Toast.LENGTH_SHORT).show();
                    break;

                case Constants.MESSAGE_ServerMode:
                    // Toast.makeText(context,"Enter Server accept state.",Toast.LENGTH_SHORT).show();   //display on TextView
                    break;

                case Constants.MESSAGE_ClientMode:
                    //  Toast.makeText(context,"Enter Client connect state.",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem item = menu.add(1, 1, Menu.NONE, "Exit");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        showDialog();
        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Exit");
        builder.setMessage("Sure to exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private class MyAccListen implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            StringBuilder sb = new StringBuilder();
            sb.append("sensor: " + event.sensor.getName() + "\n");
            sb.append("values: " + "\n");
            sb.append("X: " + event.values[0] + "\n");
            sb.append("Y: " + event.values[1] + "\n");
            sb.append("Z: " + event.values[2] + "\n");

            float X_value = event.values[0];
            float Y_value = event.values[1];
            float Z_value = event.values[2];

            if (X_value < -2.0) {
                imageView_up.setVisibility(View.INVISIBLE);
                imageView_down.setVisibility(View.INVISIBLE);
                imageView_left.setVisibility(View.INVISIBLE);
                imageView_right.setVisibility(View.VISIBLE);
                directionCmd = TURN_RIGHT;
                sendCMD(directionCmd);
            } else if (X_value > 2.0) {
                imageView_up.setVisibility(View.INVISIBLE);
                imageView_down.setVisibility(View.INVISIBLE);
                imageView_left.setVisibility(View.VISIBLE);
                imageView_right.setVisibility(View.INVISIBLE);
                directionCmd = TURN_LEFT;
                sendCMD(directionCmd);
            } else {
                if (Y_value < -2.0) {
                    imageView_up.setVisibility(View.VISIBLE);
                    imageView_down.setVisibility(View.INVISIBLE);
                    imageView_left.setVisibility(View.INVISIBLE);
                    imageView_right.setVisibility(View.INVISIBLE);
                    directionCmd = GO_FORWARD;
                    sendCMD(directionCmd);
                } else if (Y_value > 2.0) {
                    imageView_up.setVisibility(View.INVISIBLE);
                    imageView_down.setVisibility(View.VISIBLE);
                    imageView_left.setVisibility(View.INVISIBLE);
                    imageView_right.setVisibility(View.INVISIBLE);
                    directionCmd = GO_BACKWARD;
                    sendCMD(directionCmd);
                } else {
                    imageView_up.setVisibility(View.INVISIBLE);
                    imageView_down.setVisibility(View.INVISIBLE);
                    imageView_left.setVisibility(View.INVISIBLE);
                    imageView_right.setVisibility(View.INVISIBLE);
                    directionCmd = CAR_STOP;
                    sendCMD(directionCmd);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    @Override
    protected void onDestroy() {
        directionCmd = CAR_STOP;
        sendCMD(directionCmd);
        sensorManager.unregisterListener(listener);
        super.onDestroy();
    }
}
