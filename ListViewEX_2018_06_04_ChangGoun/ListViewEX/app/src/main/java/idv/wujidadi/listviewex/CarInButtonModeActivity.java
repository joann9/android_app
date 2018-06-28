package idv.wujidadi.listviewex;

import android.app.*;
import android.bluetooth.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.util.*;
import android.view.*;
import android.widget.*;

public class CarInButtonModeActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;
    private BluetoothAdapter btAdapter;
    private BTChatService mChatService;
    private Button ButtonLink;
    private Context context;
    private ImageButton buttonTop, buttonDown, buttonLeft, buttonRight, buttonStop;
    private int mode;
    private final int carButtonMode = 3;
    private static final String TAG = "Car",
                                GO_FORWARD = "f", GO_BACKWARD = "b", TURN_LEFT = "l", TURN_RIGHT = "r", CAR_STOP = "p",
                                Song_1 = "1",  Song_2 = "2", Song_3 = "3", Song_4 = "4", Song_OFF = "0";
    private String btName, btMacAddress, directionCmd, songCmd;
    private TextView btID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carinbuttonmode);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setTitle("Car Button Mode");
        context = this;

        btID = (TextView) findViewById(R.id.ciBmTv);

        Intent getIntent = getIntent();
        btName = getIntent.getStringExtra("btData");
        btID.setText(btName);
        mode = carButtonMode;

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        btAdapter.cancelDiscovery();

        mChatService = new BTChatService(context, mHandler);

        ButtonLink = (Button) findViewById(R.id.ciBmLb);
        ButtonLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btName != null) {
                    btMacAddress = btName.substring(btName.length() - 17);
                    BluetoothDevice device = btAdapter.getRemoteDevice(btMacAddress);
                    mChatService.connect(device);
                } else {
                    Toast.makeText(context, "No paired Bluetooth device!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonTop = (ImageButton) findViewById(R.id.ciBmIbUp);
        buttonDown = (ImageButton) findViewById(R.id.ciBmIbDn);
        buttonLeft = (ImageButton) findViewById(R.id.ciBmIbLf);
        buttonRight = (ImageButton) findViewById(R.id.ciBmIbRt);
        buttonStop = (ImageButton) findViewById(R.id.ciBmIbSt);

        buttonTop.setOnClickListener(new ButtonClick());
        buttonDown.setOnClickListener(new ButtonClick());
        buttonLeft.setOnClickListener(new ButtonClick());
        buttonRight.setOnClickListener(new ButtonClick());
        buttonStop.setOnClickListener(new ButtonClick());
    }

    private class ButtonClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ciBmIbUp:
                    Toast.makeText(context, "Car go forward.", Toast.LENGTH_SHORT).show();
                    directionCmd = GO_FORWARD;
                    sendCMD(directionCmd);
                    break;

                case R.id.ciBmIbDn:
                    Toast.makeText(context, "Car go backward.", Toast.LENGTH_SHORT).show();
                    directionCmd = GO_BACKWARD;
                    sendCMD(directionCmd);
                    break;

                case R.id.ciBmIbLf:
                    Toast.makeText(context, "Car go backward.", Toast.LENGTH_SHORT).show();
                    directionCmd = TURN_LEFT;
                    sendCMD(directionCmd);
                    break;

                case R.id.ciBmIbRt:
                    Toast.makeText(context, "Car go backward.", Toast.LENGTH_SHORT).show();
                    directionCmd = TURN_RIGHT;
                    sendCMD(directionCmd);
                    break;

                case R.id.ciBmIbSt:
                    Toast.makeText(context, "Car go backward.", Toast.LENGTH_SHORT).show();
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//
//        MenuItem item = menu.add(1, 1, Menu.NONE, "Exit");
//        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        showDialog();
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void showDialog() {
//        builder = new AlertDialog.Builder(context);
//        builder.setTitle("Exit");
//        builder.setMessage("Sure to exit?");
//        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                finish();
//            }
//        });
//        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.create().show();
//    }

    @Override
    protected void onDestroy() {
        directionCmd = CAR_STOP;
        sendCMD(directionCmd);
        super.onDestroy();
    }
}
