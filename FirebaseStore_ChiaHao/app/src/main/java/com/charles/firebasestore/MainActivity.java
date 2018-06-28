package com.charles.firebasestore;

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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private String[] coffeeName = {"Latte","Capuccino","Mocca","MilkCoffee"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);
        context =this;
        listViewData = (ListView) findViewById(R.id.listView);

        listViewData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> item = (Map<String, Object>) parent.getItemAtPosition(position);

                String msg = "id="+item.get("id")+"name="+item.get("name");
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                modifyCoffeeFialog(item);
                
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                addCoffeeDialog();
            }
        });

        dataList = new ArrayList<Map<String,Object>>();

        adapter = new SimpleAdapter(context, dataList, R.layout.item_layout,new String[]{"id","name","price","image"},
                new int[]{R.id.DB_ID,R.id.name,R.id.price,R.id.imageView});

        listViewData.setAdapter(adapter);

        firebaseReference = FirebaseDatabase.getInstance();
        coffeeRef = firebaseReference.getReference("Coffee");
        coffeeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> mapData;
                Object data;

                ID = (int) dataSnapshot.getChildrenCount();
                dataList.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){  //foreach method
                    mapData = new HashMap<String, Object>();
                    data = (String) ds.child("name").getValue();
                    if(data==null){
                        mapData.put("name","error");
                    }else{
                        mapData.put("name",data);
                    }

                    data = (Long) ds.child("price").getValue();   // in Firebase only Long, no Integer(Int)
                    if(data ==null){
                        mapData.put("price",0);
                    }else{
                        mapData.put("price",data);
                    }

                    data = (Long) ds.child("image").getValue();
                    if(data ==null){
                        mapData.put("image",R.drawable.add);
                    }else{
                        mapData.put("image",data);
                    }

                    data = (Long) ds.child("id").getValue();
                    if(data ==null){
                        mapData.put("id",0);
                    }else{
                        mapData.put("id",data);
                    }

                    String tempID = data.toString();
                    int idValue = Integer.parseInt(tempID);
                    if(ID< idValue){
                        ID = idValue;
                    }

                    dataList.add(mapData);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void modifyCoffeeFialog(Map<String, Object> item) {
        final Spinner coffee_spinner;
        final EditText coffee_price;
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_layout, (ViewGroup) findViewById(R.id.dialogLayout_ID));
        coffee_spinner = (Spinner) layout.findViewById(R.id.coffee_spinner);
        coffee_price = (EditText) layout.findViewById(R.id.coffee_price);

        ArrayAdapter<String> coffeeAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, coffeeName);
        coffee_spinner.setAdapter(coffeeAdapter);

        coffee_price.setText(item.get("price").toString());
        coffee_spinner.setSelection(coffeeAdapter.getPosition((item.get("name").toString())));
        final String itemID = item.get("id").toString();  //要加 final

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Modify item");
        builder.setIcon(android.R.drawable.ic_input_add);
        builder.setView(layout);

        builder.setPositiveButton("Modify", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String coffee = (String) coffee_spinner.getSelectedItem();
                long imageNo = coffee_spinner.getSelectedItemId();
                String price = (String) coffee_price.getText().toString();

                Map<String, Object> data = new HashMap<String, Object>();
                data.put("id", Integer.parseInt(itemID));
                data.put("price", Integer.parseInt(price));
                data.put("name", coffee);

                switch ((int) imageNo){
                    case 0 :
                        data.put("image", R.drawable.latte);
                        break;
                    case 1 :
                        data.put("image", R.drawable.coffee_cappuccino);
                        break;
                    case 2 :
                        data.put("image", R.drawable.macchiato);
                        break;
                    case 3 :
                        data.put("image", R.drawable.coffee_mocha);
                        break;
                }

                coffeeRef.child(itemID).updateChildren(data);

                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                coffeeRef.child(itemID).removeValue();
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

    private void addCoffeeDialog() {
        final Spinner coffee_spinner;
        final EditText coffee_price;
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_layout, (ViewGroup) findViewById(R.id.dialogLayout_ID));
        coffee_spinner = (Spinner) layout.findViewById(R.id.coffee_spinner);
        coffee_price = (EditText) layout.findViewById(R.id.coffee_price);

        ArrayAdapter<String> coffeeAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, coffeeName);
        coffee_spinner.setAdapter(coffeeAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add new item");
        builder.setIcon(android.R.drawable.ic_input_add);
        builder.setView(layout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String coffee = (String) coffee_spinner.getSelectedItem();
                long imageNo = coffee_spinner.getSelectedItemId();
                String price = coffee_price.getText().toString();
                Toast.makeText(context, coffee+price, Toast.LENGTH_SHORT).show();

                Map<String, Object> data = new HashMap<String, Object>();
                ID++;
                data.put("id", ID);
                data.put("price", Integer.parseInt(price));
                data.put("name", coffee);

                switch ((int) imageNo){
                    case 0 :
                        data.put("image", R.drawable.latte);
                        break;
                    case 1 :
                        data.put("image", R.drawable.coffee_cappuccino);
                        break;
                    case 2 :
                        data.put("image", R.drawable.macchiato);
                        break;
                    case 3 :
                        data.put("image", R.drawable.coffee_mocha);
                        break;
                }

                coffeeRef.child(Integer.toString(ID)).updateChildren(data);


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
