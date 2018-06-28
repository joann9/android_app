package com.example.yvtc.listview_ex;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;
import android.widget.Switch;

public class MainActivity extends Activity {

    private Context context;
    private Switch switch_m;
    private ListView listView_m;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        switch_m = (Switch) findViewById(R.id.switch_m);
        listView_m = (ListView) findViewById(R.id.switch_m);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();



        return true;


    }
}
