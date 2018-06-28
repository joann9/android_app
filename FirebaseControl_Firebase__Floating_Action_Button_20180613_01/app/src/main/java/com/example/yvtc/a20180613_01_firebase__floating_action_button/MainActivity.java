package com.example.yvtc.a20180613_01_firebase__floating_action_button;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ListView listViewData;
    private Context context;
    private FloatingActionButton fab;
    private ArrayList<Map<String, Object>> dataList;
    private SimpleAdapter adapter;
    private FirebaseDatabase firebaseReference;
    private DatabaseReference coffeeRef;
    private int ID;
    private String[] coffeeName={"Latte", "Cappuccino", "Macchiato", "Mocha"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_firebase);

        context = this;

//      ------------------------- 列表按鈕 -------------------------------------------------------------
        listViewData = (ListView) findViewById(R.id.list_view_ID);
        listViewData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
//      --------------------------------------------------------------------------------------------

//      --------------------------------- 列表資料 -------------------------------------------------
        dataList = new ArrayList<Map<String, Object>>();
        adapter = new SimpleAdapter(context,
                dataList,
                R.layout.item_layout,
                new String[]{"id", "name", "price", "image"},
                new int[]{
                        R.id.DB_ID,
                        R.id.name,
                        R.id.price,
                        R.id.image}
        );
        listViewData.setAdapter(adapter);

        //****** firebase *************************
        firebaseReference = FirebaseDatabase.getInstance();
        coffeeRef = firebaseReference.getReference("Coffee");
        coffeeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap<String, Object> mapData;
                Object data;

                ID =(int) dataSnapshot.getChildrenCount();       // 取得有多少筆資料
                dataList.clear();

                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    mapData = new HashMap<String, Object>();

                    data = (String) ds.child("name").getValue();
                    if (data == null){
                        mapData.put("name","error");
                    }else{
                        mapData.put("name",data);
                    }

                    data = (Long) ds.child("price").getValue();
                    if(data==null){
                        mapData.put("price",0);
                    }else{
                        mapData.put("price",data);
                    }

                    data = (Long) ds.child("image").getValue();
                    if(data==null){
                        mapData.put("image",R.drawable.add);
                    }else{
                        mapData.put("image",data);
                    }

                    data = (Long) ds.child("id").getValue();
                    if(data==null){
                        mapData.put("id",0);
                    }else{
                        mapData.put("id",data);
                    }

                    String temp_IP = data.toString();
                    int idValue = Integer.parseInt(temp_IP);
                    if(ID<idValue){     //
                        ID=idValue;
                    }

                    dataList.add(mapData);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//      --------------------------------------------------------------------------------------------

//      --------------------------- 浮動按鈕 -------------------------------------------------------
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCoffeeDialog();

            }
        });
//      --------------------------------------------------------------------------------------------


    }

    private void addCoffeeDialog() {
        final Spinner coffee_spinner;
        final EditText coffee_price;

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_layout, (ViewGroup) findViewById(R.id.dialogLayout_ID));
        coffee_spinner = (Spinner) layout.findViewById(R.id.coffee_spinner);
        coffee_price = (EditText) layout.findViewById(R.id.coffee_price);

        ArrayAdapter<String> coffeeAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, coffeeName); //只有一行選simple_dropdown_item_1line
        coffee_spinner.setAdapter(coffeeAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add new item");
        builder.setIcon(android.R.drawable.ic_input_add);
        builder.setView(layout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();


    }
}
