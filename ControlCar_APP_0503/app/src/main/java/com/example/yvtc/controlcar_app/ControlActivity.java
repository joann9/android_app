package com.example.yvtc.controlcar_app;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class ControlActivity extends AppCompatActivity {

    private Context context;
    private TextView BTText;
    private String btData;
    private Button ButtonLink;
    private Spinner spinner;
    private static final String Song_OFF = "0";  //0604
    private static final String Song_1 = "1";
    private static final String Song_2 = "2";
    private static final String Song_3 = "3";
    private static final String Song_4 = "4";
    private static final String Lamp1_ON = "x";
    private static final String Lamp1_OFF = "y";
    private static final String Lamp2_ON = "c";
    private static final String Lamp2_OFF = "d";
    private static final String Fan1_ON = "h";
    private static final String Fan1_OFF = "i";
    private static final String Fan2_ON = "j";
    private static final String Fan2_OFF = "k";
    private BluetoothAdapter btAdapter;
    private BTChatService mChatService;
    private String remoteDeviceInfo, remoteMacAddress, songCmd; //0604
    private Button playButton; //0604
    private Switch lamp1Switch, lamp2Switch, fan1Switch, fan2Switch;
    private static boolean lamp1Flag=false, lamp2Flag=false, fan1Flag=false, fan2Flag=false;
    private String lampCmd;
    private String TAG = "controlMode";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        context = this;
        setTitle("Control mode");

        btAdapter = BluetoothAdapter.getDefaultAdapter();//0604
        btAdapter.cancelDiscovery();//0604
        mChatService = new BTChatService(context,mHandler);//0604


        Intent intent = getIntent();
        remoteDeviceInfo = intent.getStringExtra("btData");
        BTText = (TextView) findViewById(R.id.Text_Control);
        BTText.setText(remoteDeviceInfo);

        ButtonLink = (Button) findViewById(R.id.Link_Btn_Control);
        ButtonLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context,"Link with BT",Toast.LENGTH_SHORT).show();
                    if(remoteDeviceInfo != null){  //0604
                        remoteMacAddress = remoteDeviceInfo.substring(remoteDeviceInfo.length()-17);
                        BluetoothDevice device = btAdapter.getRemoteDevice(remoteMacAddress);
                        mChatService.connect(device);
                    } else {
                        Toast.makeText(context,"There is no paired BT device.",Toast.LENGTH_SHORT).show();
                    }
            }
            });

        spinner = (Spinner) findViewById(R.id.spinnerID);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
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

        playButton = (Button) findViewById(R.id.button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCMD(songCmd);
            }
        });

        lamp1Switch = (Switch) findViewById(R.id.lampSwitch1);
        lamp1Switch.setChecked(lamp1Flag);

        lamp2Switch = (Switch) findViewById(R.id.lampSwitch2);
        lamp2Switch.setChecked(lamp2Flag);

        fan1Switch = (Switch) findViewById(R.id.fanSwitch1);
        fan1Switch.setChecked(lamp1Flag);

        fan2Switch = (Switch) findViewById(R.id.fanSwitch2);
        fan2Switch.setChecked(lamp1Flag);

        lamp1Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                lamp1Flag = isChecked;
                if(isChecked){
                    lampCmd =Lamp1_ON;
                } else {
                    lampCmd =Lamp1_OFF;
                }
                sendCMD(lampCmd);
            }
        });
        lamp2Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                lamp2Flag = isChecked;
                if(isChecked){
                    lampCmd =Lamp2_ON;
                } else {
                    lampCmd =Lamp2_OFF;
                }
                sendCMD(lampCmd);
            }
        });
        fan1Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                lamp1Flag = isChecked;
                if(isChecked){
                    lampCmd =Fan1_ON;
                } else {
                    lampCmd =Fan1_OFF;
                }
                sendCMD(lampCmd);
            }
        });
        fan2Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                lamp2Flag = isChecked;
                if(isChecked){
                    lampCmd =Fan2_ON;
                } else {
                    lampCmd =Fan2_OFF;
                }
                sendCMD(lampCmd);
            }
        });

    } //end of onCreate()

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.control_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.control_exit:
                finish();
                break;

            case R.id.music_mode:

                break;
        }

        return super.onOptionsItemSelected(item);
    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mChatService != null){
            mChatService.stop();
            mChatService = null;
        }
    }
}
