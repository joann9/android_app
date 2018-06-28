package com.example.user.listview_ex_2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class ControlMode extends Activity {

    private Context context;
    private AlertDialog.Builder builder;
    private Spinner spinner;
    private final int modeCode = 2;
    private final int control = 5;
    private TextView textView;
    private Button btnLink,btnPlay;
    private Switch switch_lamp1,switch_lamp2,switch_motor1,switch_motor2;
    private TextView lamp1_text,lamp2_text,mortor1_text,mortor2_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_mode);

        context = this;

        textView = (TextView)findViewById(R.id.txt_itemx_x);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        setTitle(title);
        int controlMode = intent.getIntExtra("mode", control);
        String btItem = intent.getStringExtra("btItem");
        textView.setText(btItem+" \n");

        // Link Button,連接藍芽
        btnLink = (Button)findViewById(R.id.btn_c_link);
        btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Link pressed",Toast.LENGTH_SHORT).show();
            }
        });

        // spinner 下拉式選單, 3首歌
        spinner = (Spinner) findViewById(R.id.spinner);
        //ArrayAdapter<CharSequence> songAdapter = ArrayAdapter.createFromResource(context, R.array.song, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> songAdapter = ArrayAdapter.createFromResource(context, R.array.song, R.layout.simple_spinner_item);
        songAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(songAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = spinner.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //播放 spinner 歌曲的 Button
        btnPlay = (Button)findViewById(R.id.btn_c_play);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Play pressed",Toast.LENGTH_SHORT).show();
            }
        });
        // switch 設定
        switch_lamp1 = (Switch) findViewById(R.id.switch_lamp1);
        switch_lamp2 = (Switch) findViewById(R.id.switch_lamp2);
        switch_motor1 = (Switch) findViewById(R.id.switch_motor1);
        switch_motor2 = (Switch) findViewById(R.id.switch_motor2);
        switch_lamp1.setOnCheckedChangeListener(new switchChangeListener());
        switch_lamp2.setOnCheckedChangeListener(new switchChangeListener());
        switch_motor1.setOnCheckedChangeListener(new switchChangeListener());
        switch_motor2.setOnCheckedChangeListener(new switchChangeListener());

    }
    //switch 方法的 Inner class (switchChangeListener)
    private class switchChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            lamp1_text=(TextView)findViewById(R.id.txt_lamp1);
            lamp2_text=(TextView)findViewById(R.id.txt_lamp2);
            mortor1_text=(TextView)findViewById(R.id.txt_motor1);
            mortor2_text=(TextView)findViewById(R.id.txt_motor2);
            switch (buttonView.getId()){
                case R.id.switch_lamp1:
                    if(isChecked){   //switch ON 時
                        Toast.makeText(context,"lamp1 on",Toast.LENGTH_SHORT).show();

                    } else{
                        Toast.makeText(context,"lamp1 off",Toast.LENGTH_SHORT).show();

                    }
                    break;

                case R.id.switch_lamp2:
                    if(isChecked){
                        Toast.makeText(context,"lamp2 on",Toast.LENGTH_SHORT).show();

                    } else{
                        Toast.makeText(context,"lamp2 off",Toast.LENGTH_SHORT).show();

                    }
                    break;

                case R.id.switch_motor1:
                    if(isChecked){
                        Toast.makeText(context,"motor1 on",Toast.LENGTH_SHORT).show();

                    } else{
                        Toast.makeText(context,"motor1 off",Toast.LENGTH_SHORT).show();

                    }
                    break;

                case R.id.switch_motor2:
                    if(isChecked){
                        Toast.makeText(context,"motor2 on",Toast.LENGTH_SHORT).show();

                    } else{
                        Toast.makeText(context,"motor2 off",Toast.LENGTH_SHORT).show();

                    }
                    break;
            }
        }
    }


    @Override  //Menu設定開始
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mode_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_exit) {
            showDialog1();
        }
        return super.onOptionsItemSelected(item);
    }
    private void showDialog1() {
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Exit");
        builder.setMessage("Sure to Exit?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                /*//int flag = 0;
                Intent flagIntent = new Intent();
                //flagIntent.putExtra("flag", flag);
                setResult(modeCode, flagIntent);*/

                dialogInterface.dismiss();
                ControlMode.this.finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }  //Menu設定結束


}
