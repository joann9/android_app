package com.example.yvtc.menu1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import org.w3c.dom.Text;

public class PlayActivity extends Activity {

    private Context context;
    private TextView textView;
    private VideoView videoView;
    private final int playVideo = 2;
    private final int rtspVideo = 3;
    private Uri uri;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        context = this;

        textView = (TextView) findViewById(R.id.textView_id);
        videoView = (VideoView) findViewById(R.id.videoView_id);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        setTitle(title);

        int mode = intent.getIntExtra("videoMode",playVideo);
        if(mode == playVideo){
            textView.setText("Play local video. \n");
            String localpath = "android.resource://"+getPackageName()+"/"+R.raw.cartoon1;
            //在res按右鍵,第三個new 目錄, 選raw設路徑, 把影片copy到 raw 底下.
            textView.append(localpath);
            uri = Uri.parse(localpath); //轉uri

        } else {
            textView.setText("Play RTSP video.\n");
            String webPath = getResources().getString(R.string.rtsp);
            //rtsp 開網路 1.AndroidManifest.xml加 <uses-permission android:name="android.permission.INTERNET"></uses-permission>
            // 2.strings.xml 加 <string name="rtsp">rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov</string>
            textView.append(webPath);
            uri = Uri.parse(webPath);

        }

        //加下面三行設定媒體控制鍵
        mediaController = new MediaController(context);
        videoView.setVideoURI(uri);
        videoView.setMediaController(mediaController);

    }
}
