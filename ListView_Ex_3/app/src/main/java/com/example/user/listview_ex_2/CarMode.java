package com.example.user.listview_ex_2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CarMode extends Activity {

    private Context context;
    private TextView textView;
    private ImageView imageView1,imageView2,imageView3,imageView4;
    private Button btnLink;
    private final int carButton = 3;
    private final int carSensor = 4;
    private AlertDialog.Builder builder;
    private final int modeCode = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_mode);

        context = this;
        textView = (TextView) findViewById(R.id.textViewID3);
        textView.setText("");
        imageView1 = (ImageView)findViewById(R.id.imageView1);
        imageView2 = (ImageView)findViewById(R.id.imageView2);
        imageView3 = (ImageView)findViewById(R.id.imageView3);
        imageView4 = (ImageView)findViewById(R.id.imageView4);
        btnLink = (Button)findViewById(R.id.btnLink);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String title2 = intent.getStringExtra("titleS");
        int carMode = intent.getIntExtra("mode",carButton);
        int carSMode = intent.getIntExtra("modeS",carSensor);
        if(carMode == carButton){
            //setTitle("Car Button Mode");
            setTitle(title);
            //textView.setText("Car Button Mode \n");
            textView.setText(title+" \n");
            setResult(carButton, getIntent());


        } else if((carSMode == carSensor)){
            //setTitle("Car Sensor Mode");
            setTitle(title2);
            //textView.setText("Car Sensor Mode \n");
            textView.setText(title2+" \n");
            setResult(carSensor, intent);


        }


    }

    @Override
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
                CarMode.this.finish();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }
}
