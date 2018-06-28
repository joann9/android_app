package idv.wujidadi.listviewex;

import android.bluetooth.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.*;
import android.view.*;
import android.widget.*;

import java.util.Locale;

import static idv.wujidadi.listviewex.RockerView.DirectionMode.DIRECTION_4_ROTATE_45;

public class RockerModeActivity extends AppCompatActivity {

    private BluetoothAdapter btAdapter;
    private BTChatService mChatService;
    private Button ButtonLink;
    private Context context;
    private final int RockerMode = 7;
    private int mode;
    private RockerView mRockerView;
    private static final String TAG = "Car",
                                GO_FORWARD = "f", GO_BACKWARD = "b", TURN_LEFT = "l", TURN_RIGHT = "r", CAR_STOP = "p",
                                Song_1 = "1", Song_2 = "2", Song_3 = "3", Song_4 = "4", Song_OFF = "0";
    private String btName, btMacAddress, directionCmd, songCmd;
    private TextToSpeech tts;
    private TextView btText, mTvAngle, mTvLevel, mTvModel, mTvShake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rockermode);

        mRockerView = (RockerView) findViewById(R.id.my_rocker);
        mTvAngle = (TextView) findViewById(R.id.tv_now_angle);
        mTvLevel = (TextView) findViewById(R.id.tv_now_level);
        mTvModel = (TextView) findViewById(R.id.tv_now_model);
        mTvShake = (TextView) findViewById(R.id.tv_now_shake);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        btName = intent.getStringExtra("btData");

        context = this;

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        btAdapter.cancelDiscovery();

        mChatService = new BTChatService(context, mHandler);

        // 建立 TTS
        createLanguageTTS();

        setTitle("Joystick mode");
        mode = RockerMode;
        btText = (TextView) findViewById(R.id.Text_Rocker);
        btText.setText(btName);
        ButtonLink = (Button) findViewById(R.id.Link_Btn_Rocker);

        ButtonLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (btName != null) {
                btMacAddress = btName.substring(btName.length() - 17);
                BluetoothDevice device = btAdapter.getRemoteDevice(btMacAddress);
                mChatService.connect(device);

            } else {
                Toast.makeText(context, "No paired BT device.", Toast.LENGTH_SHORT).show();
            }
            }
        });

        //mTvModel.setText("當前模式：方向有改變時回調；4個方向");

        mRockerView.setOnShakeListener(DIRECTION_4_ROTATE_45, new RockerView.OnShakeListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void direction(RockerView.Direction direction) {
                if (direction == RockerView.Direction.DIRECTION_CENTER){
                    mTvShake.setText("停止");
                    tts.speak(mTvShake.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);     //念出Text內容
                    directionCmd = CAR_STOP;
                    sendCMD(directionCmd);
                }else if (direction == RockerView.Direction.DIRECTION_DOWN){
                    mTvShake.setText("後退");
                    tts.speak(mTvShake.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);     //念出Text內容
                    directionCmd = GO_BACKWARD;
                    sendCMD(directionCmd);
                }else if (direction == RockerView.Direction.DIRECTION_LEFT){
                    mTvShake.setText("左轉");
                    tts.speak(mTvShake.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);     //念出Text內容
                    directionCmd = TURN_LEFT;
                    sendCMD(directionCmd);
                }else if (direction == RockerView.Direction.DIRECTION_UP){
                    mTvShake.setText("前進");
                    tts.speak(mTvShake.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);     //念出Text內容
                    directionCmd = GO_FORWARD;
                    sendCMD(directionCmd);
                }else if (direction == RockerView.Direction.DIRECTION_RIGHT){
                    mTvShake.setText("右轉");
                    tts.speak(mTvShake.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);     //念出Text內容
                    directionCmd = TURN_RIGHT;
                    sendCMD(directionCmd);
                }
            }

            @Override
            public void onFinish() {
            }
        });

        mRockerView.setOnAngleChangeListener(new RockerView.OnAngleChangeListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void angle(double angle) {
                //mTvAngle.setText("當前角度："+angle);
            }

            @Override
            public void onFinish() {
            }
        });

        mRockerView.setOnDistanceLevelListener(new RockerView.OnDistanceLevelListener() {
            @Override
            public void onDistanceLevel(int level) {
                //mTvLevel.setText("當前距離級別："+level);
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
                    Toast.makeText(context, "Connected to " + mConnectedDevice, Toast.LENGTH_SHORT).show();
                    break;

                case Constants.MESSAGE_TOAST:
                    Toast.makeText(context, msg.getData().getString(Constants.TOAST), Toast.LENGTH_SHORT).show();
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

        switch (item.getItemId()) {
            case R.id.car_exit:
                finish();
                break;

            case R.id.song1:
                songCmd = Song_1;
                sendCMD(songCmd);
                break;

            case R.id.song2:
                songCmd = Song_2;
                sendCMD(songCmd);
                break;

            case R.id.song3:
                songCmd = Song_3;
                sendCMD(songCmd);
                break;

            case R.id.song4:
                songCmd = Song_4;
                sendCMD(songCmd);
                break;

            case R.id.song0:
                songCmd = Song_OFF;
                sendCMD(songCmd);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createLanguageTTS() {

        if (tts == null) {
            tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int arg0) {
                    // TTS 初始化成功
                    if (arg0 == TextToSpeech.SUCCESS) {
                        // 目前指定的【語系+國家】TTS, 已下載離線語音檔, 可以離線發音
                        if (tts.isLanguageAvailable(Locale.CHINESE) == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                            tts.setLanguage(Locale.CHINESE);    //中文
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        directionCmd = CAR_STOP;
        sendCMD(directionCmd);

        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }

        super.onDestroy();
    }
}