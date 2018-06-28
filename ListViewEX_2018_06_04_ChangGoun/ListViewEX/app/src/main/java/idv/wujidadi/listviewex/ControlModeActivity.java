package idv.wujidadi.listviewex;

import android.app.*;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.*;
import android.content.pm.ActivityInfo;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.widget.*;

public class ControlModeActivity extends AppCompatActivity {

    private boolean lamp1Flag = false, lamp2Flag = false, fan1Flag = false, fan2Flag = false;
    private BluetoothAdapter btAdapter;
    private BTChatService mChatService;
    private Button ButtonLink, playButton;
    private Context context;
    private Spinner spinner;
    private String remoteDeviceInfo, remoteMacAddress, songCmd, lampCmd, fanCmd;
    private static final String TAG = "Car",
                                Song_OFF = "0", Song_1 = "1", Song_2 = "2", Song_3 = "3", Song_4 = "4",
                                Lamp1_ON = "x", Lamp1_OFF = "y", Lamp2_ON = "c", Lamp2_OFF = "d",
                                Fan1_ON = "h", Fan1_OFF = "i", Fan2_ON = "j", Fan2_OFF = "k";
    private Switch lamp1Switch, lamp2Switch, fan1Switch, fan2Switch;
    private TextView BTText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controlmode);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setTitle("Control Mode");
        context = this;

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        btAdapter.cancelDiscovery();
        mChatService = new BTChatService(context, mHandler);

        Intent intent = getIntent();
        remoteDeviceInfo = intent.getStringExtra("btData");

        BTText = (TextView) findViewById(R.id.cMTv);
        BTText.setText(remoteDeviceInfo);

        ButtonLink = (Button) findViewById(R.id.cMLb);
        ButtonLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (remoteDeviceInfo != null) {
                    remoteMacAddress = remoteDeviceInfo.substring(remoteDeviceInfo.length() - 17);
                    BluetoothDevice device = btAdapter.getRemoteDevice(remoteMacAddress);
                    mChatService.connect(device);
                } else {
                    Toast.makeText(context, "No Paired BT device", Toast.LENGTH_SHORT).show();
                }
            }
        });

        spinner = (Spinner) findViewById(R.id.cM2Sn);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        songCmd = Song_OFF;
                        break;
                    case 1:
                        songCmd = Song_1;
                        break;
                    case 2:
                        songCmd = Song_2;
                        break;
                    case 3:
                        songCmd = Song_3;
                        break;
                    case 4:
                        songCmd = Song_4;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        playButton = (Button) findViewById(R.id.cM2Pl);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCMD(songCmd);
            }
        });

        lamp1Switch = (Switch) findViewById(R.id.cM3SwitchL1);
        lamp1Switch.setChecked(lamp1Flag);

        lamp2Switch = (Switch) findViewById(R.id.cM3SwitchL2);
        lamp2Switch.setChecked(lamp2Flag);

        fan1Switch = (Switch) findViewById(R.id.cM3SwitchM1);
        fan1Switch.setChecked(fan1Flag);

        fan2Switch = (Switch) findViewById(R.id.cM3SwitchM2);
        fan2Switch.setChecked(fan2Flag);

        lamp1Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                lamp1Flag = isChecked;
                if (isChecked) {
                    lampCmd = Lamp1_ON;
                } else {
                    lampCmd = Lamp1_OFF;
                }
                sendCMD(lampCmd);
            }
        });

        lamp2Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                lamp2Flag = isChecked;
                if (isChecked) {
                    lampCmd = Lamp2_ON;
                } else {
                    lampCmd = Lamp2_OFF;
                }
                sendCMD(lampCmd);
            }
        });

        fan1Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fan1Flag = isChecked;
                if (isChecked) {
                    fanCmd = Fan1_ON;
                } else {
                    fanCmd = Fan1_OFF;
                }
                sendCMD(fanCmd);
            }
        });

        fan2Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fan2Flag = isChecked;
                if (isChecked) {
                    fanCmd = Fan2_ON;
                } else {
                    fanCmd = Fan2_OFF;
                }
                sendCMD(fanCmd);
            }
        });
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
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mChatService != null) {
            mChatService.stop();
            mChatService = null;
        }
    }
}
