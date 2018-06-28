package com.example.yvtc.menu3;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private Context context;
    private TextView gameText, setupText;
    private ImageView gameImage;
    private final int itemID_1 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        gameText = (TextView) findViewById(R.id.textView);
        setupText = (TextView) findViewById(R.id.textView2);
        gameImage = (ImageView) findViewById(R.id.imageView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionmenu, menu);
        //如要在 Activity 中使用XML選單，您必須使用 MenuInflater.inflate() 擴大選單資源
        //(將 XML 轉換成可程式化的物件)。

        MenuItem item = menu.add(0, itemID_1, Menu.NONE, "Exit");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS); //三個點不見, 直接顯示 [Exit] 在右上角
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //透過item取得item ID
        switch (item.getItemId()){
            case R.id.menu_mario:
                gameText.setText("Mario game is enabled.");
                gameImage.setImageResource(R.drawable.mario);
                break;
            case R.id.menu_sonic:
                gameText.setText("Sonic game is enabled.");
                gameImage.setImageResource(R.drawable.sonic);
                break;
            case R.id.menu_setup:
                setupText.setText("Add setup menu.");
                break;
            case itemID_1:
                gameText.setText("");
                setupText.setText("");
                gameImage.setImageResource(R.drawable.android_background);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
