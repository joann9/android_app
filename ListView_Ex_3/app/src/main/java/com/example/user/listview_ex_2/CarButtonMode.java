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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CarButtonMode extends Activity {

    private Context context;
    private TextView textView;
    private Button btnLink;
    private final int carButton = 3;
    //private final int carSensor = 4;
    private AlertDialog.Builder builder;
    private final int modeCode = 2;
    private ImageButton imageButton1,imageButton2,imageButton3,imageButton4;
    //private Button button_up_1,button_down_2,button_left_3,button_right_4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_button_mode);

        context = this;
        textView = (TextView) findViewById(R.id.textViewID3);
        textView.setText("");
        imageButton1 = (ImageButton)findViewById(R.id.imageButton1);
        imageButton2 = (ImageButton)findViewById(R.id.imageButton2);
        imageButton3 = (ImageButton)findViewById(R.id.imageButton3);
        imageButton4 = (ImageButton)findViewById(R.id.imageButton4);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        int carMode = intent.getIntExtra("mode",carButton);
        String btItem = intent.getStringExtra("btItem");

        btnLink = (Button)findViewById(R.id.btnLink);
        btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Link pressed",Toast.LENGTH_SHORT).show();
            }
        });

        setTitle(title);
        textView.setText(btItem+" \n");
        setResult(carButton, getIntent());

        imageButton1.setOnClickListener(new onClickButton());
        imageButton2.setOnClickListener(new onClickButton());
        imageButton3.setOnClickListener(new onClickButton());
        imageButton4.setOnClickListener(new onClickButton());

    }

    private class onClickButton implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.imageButton1:

                    Toast.makeText(context,"imageButton1,up",Toast.LENGTH_SHORT).show();

                    break;
                case R.id.imageButton2:

                    Toast.makeText(context,"imageButton2,down",Toast.LENGTH_SHORT).show();

                    break;
                case R.id.imageButton3:
                    Toast.makeText(context,"imageButton3,left",Toast.LENGTH_SHORT).show();

                    break;
                case R.id.imageButton4:
                    Toast.makeText(context,"imageButton4,right",Toast.LENGTH_SHORT).show();

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
                CarButtonMode.this.finish();

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
