package com.example.yvtc.listview2_1;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

    private Context context;
    private String[] fastFood;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        context = this;

        fastFood = getResources().getStringArray(R.array.fastfood);

        adapter = new ArrayAdapter<String>(context, R.layout.new_list_layout, fastFood); //顯示在畫面上
        setListAdapter(adapter);

        //監聽
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String message = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(context,message,Toast.LENGTH_SHORT).show();

                CheckedTextView checkItem = (CheckedTextView) view.findViewById(R.id.textcheck);
                //check 有沒有被打勾
                Boolean tv = checkItem.isChecked();
                checkItem.setChecked(!tv);

            }
        });
    }
}
