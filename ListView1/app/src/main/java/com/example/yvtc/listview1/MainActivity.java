package com.example.yvtc.listview1;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private Context context;
    private ListView listView;
    private Button addButton;
    private ArrayList<String> dataList;
    private String[] fastfood, newfastfood;
    private String TAG;
    private ArrayAdapter<String> adapter;
    private int addNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        context = this;
        
        listView = (ListView) findViewById(R.id.listViewID);
        addButton = (Button) findViewById(R.id.buttonID);
        addButton.setOnClickListener(new ButtonOnClick());
        
        fastfood = getResources().getStringArray(R.array.fastfood);
        newfastfood = getResources().getStringArray(R.array.newFastfood);
        dataList = new ArrayList<String>();
        for(String food : fastfood){
            Log.d(TAG, "food = "+food);
            dataList.add(food); //把資料一筆筆放到ArrayList
        }

        adapter = new ArrayAdapter<String>(context, R.layout.simple_expandable_list_item_1, dataList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String message = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(context, message,Toast.LENGTH_SHORT).show();
                //資料移除, adapter要更新
                dataList.remove(i);
                adapter.notifyDataSetChanged();

            }
        });
    }

    private class ButtonOnClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if(dataList.isEmpty()){
                addNum = 0;
            }
            if(addNum < newfastfood.length){
                dataList.add(newfastfood[addNum]);
                addNum++;
                adapter.notifyDataSetChanged();
                listView.setSelection(adapter.getCount()-1);
            } else{
                Toast.makeText(context,"Allnew data has been added.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
