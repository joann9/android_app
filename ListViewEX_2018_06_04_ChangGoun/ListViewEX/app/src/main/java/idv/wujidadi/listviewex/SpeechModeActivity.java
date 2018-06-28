package idv.wujidadi.listviewex;

import android.Manifest;
import android.bluetooth.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.speech.*;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.*;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.List;

public class SpeechModeActivity extends AppCompatActivity {

    private BluetoothAdapter btAdapter;
    private BTChatService mChatService;
    private Button ButtonLink;
    private Context context;
    final Handler handler = new Handler();
    private ImageView speechUp, speechDown, speechLeft, speechRight;
    private int mode;
    private final int speechMode = 6;
    private Intent rcintent;
    private SpeechRecognizer recognizer;
    private static final String TAG = "Car",
            GO_FORWARD = "f", GO_BACKWARD = "b", TURN_LEFT = "l", TURN_RIGHT = "r", CAR_STOP = "p",
            Song_1 = "1",  Song_2 = "2", Song_3 = "3", Song_4 = "4", Song_OFF = "0";
    private String btName, btMacAddress, directionCmd, songCmd;
    private TextView btID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speechmode);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setTitle("Car Speech Mode");
        context = this;

        btID = (TextView) findViewById(R.id.Text_Speech);

        Intent getIntent = getIntent();
        btName = getIntent.getStringExtra("btData");
        btID.setText(btName);
        mode = speechMode;

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        btAdapter.cancelDiscovery();

        mChatService = new BTChatService(context, mHandler);

        recognizer = SpeechRecognizer.createSpeechRecognizer(context);
        recognizer.setRecognitionListener(new MyRecognizerListener());

        //檢查是否取得權限
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        //沒有權限時
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SpeechModeActivity.this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    1);
        } else {

        }

        //如果使用者第二次點擊功能呼叫權限視窗，就會跳出shouldShowRequestPermissionRationale
        if (ActivityCompat.shouldShowRequestPermissionRationale(SpeechModeActivity.this,
            Manifest.permission.RECORD_AUDIO)) {
            //創建Dialog解釋視窗
            new AlertDialog.Builder(SpeechModeActivity.this)
                    .setMessage("如不允許將無法使用語音辨識功能，程式將關閉！")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(SpeechModeActivity.this,
                                    new String[]{Manifest.permission.RECORD_AUDIO},
                                    1);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(SpeechModeActivity.this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    1);
        }

        rcintent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        rcintent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizer.startListening(rcintent);

        ButtonLink = (Button) findViewById(R.id.Link_Btn_Speech);
        ButtonLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btName != null) {
                    btMacAddress = btName.substring(btName.length() - 17);
                    BluetoothDevice device = btAdapter.getRemoteDevice(btMacAddress);
                    mChatService.connect(device);
                    Toast.makeText(context, "Start to speak.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "No Paired BT device!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        speechDown = (ImageButton) findViewById(R.id.SpeechDown);
        speechUp = (ImageButton) findViewById(R.id.SpeechTop);
        speechLeft = (ImageButton) findViewById(R.id.SpeechLeft);
        speechRight = (ImageButton) findViewById(R.id.SpeechRight);

        speechDown.setVisibility(View.INVISIBLE);  //讓imageView不要顯示
        speechUp.setVisibility(View.INVISIBLE);
        speechLeft.setVisibility(View.INVISIBLE);
        speechRight.setVisibility(View.INVISIBLE);
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

    private class MyRecognizerListener implements RecognitionListener {
        @Override
        public void onReadyForSpeech(Bundle params) {
        }

        @Override
        public void onBeginningOfSpeech() {
        }

        @Override
        public void onRmsChanged(float rmsdB) {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
        }

        @Override
        public void onEndOfSpeech() {
            //用handler製造delay效果
            //在上面宣告final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    //結束至onResults的時間差不多0.5秒左右，所以需延遲0.5秒以上再執行recognizer.startListening(intent);才可執行
                    //參考資料http://rach-chen.logdown.com/posts/829008-android-endless-conversations-listening-in-silence-he

                    Log.d("RECOGNIZER", "done");
                    recognizer.startListening(rcintent);  //使語音辨識不斷執行
                }
            }, 1000);  //延遲1秒
        }

        @Override
        public void onError(int error) {
            Log.d("RECOGNIZER", "Error Code: " + error);
        }

        @Override
        public void onResults(Bundle results) {
            List resList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            StringBuffer sb = new StringBuffer();
            for (Object res : resList) {
                sb.append(res + "\n");
            }

            boolean status1 = sb.toString().contains("前");
            if (status1) {
                directionCmd = GO_FORWARD;
                sendCMD(directionCmd);
                speechDown.setVisibility(View.INVISIBLE);
                speechUp.setVisibility(View.VISIBLE);
                speechLeft.setVisibility(View.INVISIBLE);
                speechRight.setVisibility(View.INVISIBLE);
                Log.d("Direction", "Dir: " + directionCmd);
            }
            boolean status2 = sb.toString().contains("後");
            if (status2) {
                directionCmd = GO_BACKWARD;
                sendCMD(directionCmd);
                speechDown.setVisibility(View.VISIBLE);
                speechUp.setVisibility(View.INVISIBLE);
                speechLeft.setVisibility(View.INVISIBLE);
                speechRight.setVisibility(View.INVISIBLE);
                Log.d("Direction", "Dir: " + directionCmd);
            }
            boolean status3 = sb.toString().contains("左");
            if (status3) {
                directionCmd = TURN_LEFT;
                sendCMD(directionCmd);
                speechDown.setVisibility(View.INVISIBLE);
                speechUp.setVisibility(View.INVISIBLE);
                speechLeft.setVisibility(View.VISIBLE);
                speechRight.setVisibility(View.INVISIBLE);
                Log.d("Direction", "Dir: " + directionCmd);
            }
            boolean status4 = sb.toString().contains("右");
            if (status4) {
                directionCmd = TURN_RIGHT;
                sendCMD(directionCmd);
                speechDown.setVisibility(View.INVISIBLE);
                speechUp.setVisibility(View.INVISIBLE);
                speechLeft.setVisibility(View.INVISIBLE);
                speechRight.setVisibility(View.VISIBLE);
                Log.d("Direction", "Dir: " + directionCmd);
            }
            boolean status5 = sb.toString().contains("停");
            if (status5) {
                directionCmd = CAR_STOP;
                sendCMD(directionCmd);
                speechDown.setVisibility(View.INVISIBLE);
                speechUp.setVisibility(View.INVISIBLE);
                speechLeft.setVisibility(View.INVISIBLE);
                speechRight.setVisibility(View.INVISIBLE);
                Log.d("Direction", "Dir: " + directionCmd);
            }

            Log.d("RECOGNIZER", "onResults: " + sb.toString());
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    }

    @Override
    protected void onDestroy() {
        directionCmd = CAR_STOP;
        sendCMD(directionCmd);
        recognizer.stopListening();
        recognizer.destroy();
        super.onDestroy();
    }
}