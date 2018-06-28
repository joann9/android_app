package com.example.yvtc.controlcar_app;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CarActivity extends AppCompatActivity {

    private static final String TAG = "Car";
    private String remoteDeviceInfo;
    private Context context;
    private BluetoothAdapter btAdapter;
    private BTChatService mChatService;
    private final int ButtonMode = 1;
    private final int SensorMode = 2;
    private final int ControlMode = 3;
    private TextView BTText;
    private int mode;
    private Button ButtonLink;
    private String remoteMacAddress;
    private ImageButton buttonTop,buttonDown,buttonLeft,buttonRight,buttonStop;
    private static final String GO_FORWARD = "f";
    private static final String GO_BACKWARD = "b";
    private static final String TRUN_LEFT = "l";
    private static final String TRUN_RIGHT = "r";
    private static final String CAR_STOP = "p";
    private static final String Song_1 = "1";
    private static final String Song_2 = "2";
    private static final String Song_3 = "3";
    private static final String Song_4 = "4";
    private static final String Song_OFF = "0";
    private String directionCmd, songCmd;
    private int linkFlag;
    private ImageView imageTop,imageDown,imageLeft,imageRight;
    private ImageButton imageStop;
    private SensorManager sensor_manager;
    private MyACCListerner listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //設定手機為直立式
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        //取出layout資料, 畫出來
        int layoutID = intent.getIntExtra("layout", R.layout.activity_car_button);
        //bluetooth資料取出來
        remoteDeviceInfo = intent.getStringExtra("btData");
        setContentView(layoutID);

        context = this;
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        btAdapter.cancelDiscovery();

        mChatService = new BTChatService(context, mHandler);

        if(layoutID == R.layout.activity_car_button){

            setTitle("Car in Button mode");
            mode = ButtonMode;
            BTText = (TextView) findViewById(R.id.Text_Button);
            BTText.setText(remoteDeviceInfo);
            ButtonLink = (Button) findViewById(R.id.Link_Btn);
            ButtonLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(remoteDeviceInfo != null){
                        remoteMacAddress = remoteDeviceInfo.substring(remoteDeviceInfo.length()-17);
                        BluetoothDevice device = btAdapter.getRemoteDevice(remoteMacAddress);
                        //取得資料放到mChatSevice裡
                        mChatService.connect(device);

                    } else {
                        Toast.makeText(context, "No Paired BT device", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            buttonTop = (ImageButton) findViewById(R.id.imageButtonTop);
            buttonDown = (ImageButton) findViewById(R.id.imageButtonDown);
            buttonLeft = (ImageButton) findViewById(R.id.imageButtonLeft);
            buttonRight = (ImageButton) findViewById(R.id.imageButtonRight);
            buttonStop = (ImageButton) findViewById(R.id.imageButtonStop);
            //監聽button
            buttonTop.setOnClickListener(new ButtonClick());
            buttonDown.setOnClickListener(new ButtonClick());
            buttonLeft.setOnClickListener(new ButtonClick());
            buttonRight.setOnClickListener(new ButtonClick());
            buttonStop.setOnClickListener(new ButtonClick());
        } else {
            //sensor mode
            //0604
            setTitle("Car in sensor mode");
            mode = SensorMode;
            linkFlag = 0;
            BTText = (TextView) findViewById(R.id.Text_Sensor);
            BTText.setText(remoteDeviceInfo);
            ButtonLink = (Button) findViewById(R.id.Link_Btn_Sensor);
            ButtonLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(remoteDeviceInfo != null){
                        //取remote端mac address
                        remoteMacAddress = remoteDeviceInfo.substring(remoteDeviceInfo.length()-17);
                        BluetoothDevice device = btAdapter.getRemoteDevice(remoteMacAddress);
                        linkFlag = 1;
                        mChatService.connect(device); //連線 BTChatService.java

                    } else {
                        Toast.makeText(context, "There is no paried BT device",Toast.LENGTH_SHORT).show();
                    }

                }
            });

            imageTop = (ImageView) findViewById(R.id.imageViewTop);
            imageDown = (ImageView) findViewById(R.id.imageViewDown);
            imageLeft = (ImageView) findViewById(R.id.imageViewLeft);
            imageRight = (ImageView) findViewById(R.id.imageViewRight);
            imageStop = (ImageButton) findViewById(R.id.imageButtonStop); //這是Button

            //先隱藏
            imageTop.setVisibility(View.INVISIBLE);
            imageDown.setVisibility(View.INVISIBLE);
            imageLeft.setVisibility(View.INVISIBLE);
            imageStop.setVisibility(View.INVISIBLE);

            imageStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    directionCmd=CAR_STOP;
                    sendCMD(directionCmd);
                }
            });

            sensor_manager = (SensorManager) getSystemService(SENSOR_SERVICE);
            Sensor sensor = sensor_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            listener = new MyACCListerner();
            sensor_manager.registerListener(listener,sensor,SensorManager.SENSOR_DELAY_UI); //實作sensor的一些方法,在最下方MyACCListerner
        }  //0604

    }

    private class ButtonClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.imageButtonTop:
                    Toast.makeText(context, "car forward", Toast.LENGTH_SHORT).show();
                    directionCmd = GO_FORWARD;
                    sendCMD(directionCmd);
                    break;
                case R.id.imageButtonDown:
                    Toast.makeText(context, "car back", Toast.LENGTH_SHORT).show();
                    directionCmd = GO_BACKWARD;
                    sendCMD(directionCmd);
                    break;
                case R.id.imageButtonLeft:
                    Toast.makeText(context, "car left", Toast.LENGTH_SHORT).show();
                    directionCmd = TRUN_LEFT;
                    sendCMD(directionCmd);
                    break;
                case R.id.imageButtonRight:
                    Toast.makeText(context, "car right", Toast.LENGTH_SHORT).show();
                    directionCmd = TRUN_RIGHT;
                    sendCMD(directionCmd);
                    break;
                case R.id.imageButtonStop:
                    Toast.makeText(context, "car stop", Toast.LENGTH_SHORT).show();
                    directionCmd = CAR_STOP;
                    sendCMD(directionCmd);
                    break;


            }
        }
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
    //There is no message queue leak problem
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

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.car_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.car_exit:
                finish();
                break;

            case R.id.song1:
                songCmd=Song_1;
                sendCMD(songCmd);
                break;

            case R.id.song2:
                songCmd=Song_2;
                sendCMD(songCmd);
                break;

            case R.id.song3:
                songCmd=Song_3;
                sendCMD(songCmd);
                break;

            case R.id.song4:
                songCmd=Song_4;
                sendCMD(songCmd);
                break;



        }
        return super.onOptionsItemSelected(item);
    }

    private class MyACCListerner implements SensorEventListener{  //0604

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) { //只用到這個

            if(linkFlag==1){
                //讀x,y,z的值,送command到 M0
                float X_value = sensorEvent.values[0];
                float Y_value = sensorEvent.values[1];
                float Z_value = sensorEvent.values[2];

                if(X_value < -3.0){ //trun right
                    directionCmd = TRUN_RIGHT;
                    sendCMD(directionCmd);
                    imageTop.setVisibility(View.INVISIBLE);
                    imageDown.setVisibility(View.INVISIBLE);
                    imageLeft.setVisibility(View.INVISIBLE);
                    imageRight.setVisibility(View.VISIBLE);

                } else if(X_value > -3.0){//trun left
                    directionCmd = TRUN_LEFT;
                    sendCMD(directionCmd);
                    imageTop.setVisibility(View.INVISIBLE);
                    imageDown.setVisibility(View.INVISIBLE);
                    imageLeft.setVisibility(View.VISIBLE);
                    imageRight.setVisibility(View.INVISIBLE);
                } else {//trun left
                    if(Z_value > 7){  //forward
                        directionCmd = GO_FORWARD;
                        sendCMD(directionCmd);
                        imageTop.setVisibility(View.VISIBLE);
                        imageDown.setVisibility(View.INVISIBLE);
                        imageLeft.setVisibility(View.INVISIBLE);
                        imageRight.setVisibility(View.INVISIBLE);
                    } else if(Z_value < 3){ //back
                        directionCmd = GO_BACKWARD;
                        sendCMD(directionCmd);
                        imageTop.setVisibility(View.INVISIBLE);
                        imageDown.setVisibility(View.VISIBLE);
                        imageLeft.setVisibility(View.INVISIBLE);
                        imageRight.setVisibility(View.INVISIBLE);
                    } else { //stop
                        directionCmd = CAR_STOP;
                        sendCMD(directionCmd);
                        imageTop.setVisibility(View.INVISIBLE);
                        imageDown.setVisibility(View.INVISIBLE);
                        imageLeft.setVisibility(View.INVISIBLE);
                        imageRight.setVisibility(View.INVISIBLE);
                    }
                }  //end of X_value

                } //end of linkFlag
        }  //end of onSensorChanged

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {   //0604

        }
    }

    @Override //0604
    protected void onStop() {
        super.onStop();
        if(mode==SensorMode){
            sensor_manager.unregisterListener(listener);
            linkFlag=0;

        }
    }

    @Override //0604
    protected void onDestroy() {
        super.onDestroy();
        if(mChatService != null){
            //一個一個把執行緒清除掉
            mChatService.stop();
            mChatService = null;

        }
    }
}
