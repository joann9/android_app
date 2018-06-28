package com.example.yvtc.listview6;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {

    private Context context;
    private ListView listView;
    private List<Map<String,Object>> itemList;
    private String[] key = {"name","price","image"};
    private int[] itemID = {R.id.name, R.id.price, R.id.imageView};
    private boolean[] checkList;
    private CheckedAdapter adapter;
    private Button checkOut;
    private TextView textResult;
    private StringBuilder checkOutList;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        listView = (ListView) findViewById(R.id.listViewID);

        Map<String, Object> hamburger = new HashMap<>();
        hamburger.put("name","Hamburger");
        hamburger.put("price", 80);
        hamburger.put("image",R.drawable.hamburger);
        hamburger.put("content","this is hamburger");

        Map<String, Object> french = new HashMap<>();
        french.put("name","French fries");
        french.put("price", 50);
        french.put("image",R.drawable.french_fries);
        french.put("content","this is French fries");

        Map<String, Object> coca = new HashMap<>();
        coca.put("name","Coca cola");
        coca.put("price",25);
        coca.put("image",R.drawable.coca_cola);
        coca.put("content","this is Cocacola");

        Map<String, Object> cocaLight = new HashMap<>();
        cocaLight.put("name","Coca Light");
        cocaLight.put("price",30);
        cocaLight.put("image",R.drawable.coca_cola_light);
        cocaLight.put("content","this is Coca Light");

        Map<String, Object> cocaZero = new HashMap<>();
        cocaZero.put("name","Coca Zero");
        cocaZero.put("price",30);
        cocaZero.put("image",R.drawable.coca_cola_zero);
        cocaZero.put("content","this is Coca Zero");

        Map<String, Object> kfc = new HashMap<>();
        kfc.put("name","KFC");
        kfc.put("price",120);
        kfc.put("image",R.drawable.kfc_small);
        kfc.put("content","this is KFC");

        itemList = new ArrayList<>();
        itemList.add(hamburger);
        itemList.add(french);
        itemList.add(coca);
        itemList.add(cocaLight);
        itemList.add(cocaZero);
        itemList.add(kfc);

        checkList = new boolean[itemList.size()];

        adapter = new CheckedAdapter(context, itemList,R.layout.item_layout);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                Map<String,Object> item = (Map<String,Object>) parent.getItemAtPosition(position);
                String msg = "You select" + item.get("name") + " ,price = " + item.get("price").toString();
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                CheckedTextView checkitem = (CheckedTextView) view.findViewById(R.id.price);
                boolean ck = checkitem.isChecked();
                checkitem.setChecked(!ck);
                checkList[position] = !ck;

            }
        });

        checkOut = (Button) findViewById(R.id.button);
        checkOut.setOnClickListener(new CheckoutClick());
        textResult = (TextView) findViewById(R.id.textViewResult);
        textResult.setText("");

    }

    private class CheckoutClick implements View.OnClickListener {
        private  int sum, suml;

        @Override
        public void onClick(View view) {
            checkOutList = new StringBuilder("You select : \n");
            sum = 0;
            for(int i=0; i< checkList.length; i++){ //確認勾勾有沒有被打勾, 一個一個確認
                if(checkList[i]){
                    Map<String, Object> item = itemList.get(i);
                    String msg = item.get("name") + " ,price "+ item.get("price").toString();
                    sum += Integer.parseInt(item.get("price").toString());
                    suml += (int)item.get("price");
                    Log.d(TAG,"sumel ="+suml);
                    checkOutList.append(msg+"\n");
                }

            }
            checkOutList.append("The total price = "+sum);
            //Toast.makeText(context,checkOutList,Toast.LENGTH_SHORT).show();
            builder = new AlertDialog.Builder(context);
            builder.setTitle("Check Out");
            builder.setIcon(android.R.drawable.ic_dialog_info);
            builder.setMessage(checkOutList);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    textResult.setText(checkOutList);
                    for(int i=0;i<checkList.length;i++){
                        if(checkList[i]){
                            checkList[i] = false;
                        }
                    }
                    adapter = new CheckedAdapter(context,itemList,R.layout.item_layout);
                    listView.setAdapter(adapter);
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    textResult.setText("");
                    dialog.dismiss();
                    
                }
            });

            builder.create().show();

        }
    }
}
