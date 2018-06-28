package com.example.yvtc.listview3;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity {

    private Context context;
    private ListView listView;
    private ArrayList<HashMap<String,Object>> itemList;
    private String[] key = {"name","price","image","content"};
    private int[] itemID = {R.id.name, R.id.price, R.id.imageView, R.id.textView4};
    private SimpleAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        listView = (ListView) findViewById(R.id.listViewID);

        HashMap<String, Object> hamburger = new HashMap<>();
        hamburger.put("name","Hamburger");
        hamburger.put("price", 80);
        hamburger.put("image",R.drawable.hamburger);
        hamburger.put("content","this is hamburger");

        HashMap<String, Object> french = new HashMap<>();
        french.put("name","French fries");
        french.put("price", 50);
        french.put("image",R.drawable.french_fries);
        french.put("content","this is French fries");

        HashMap<String, Object> coca = new HashMap<>();
        coca.put("name","Coca cola");
        coca.put("price",25);
        coca.put("image",R.drawable.coca_cola);
        coca.put("content","this is Cocacola");

        HashMap<String, Object> cocaLight = new HashMap<>();
        cocaLight.put("name","Coca Light");
        cocaLight.put("price",30);
        cocaLight.put("image",R.drawable.coca_cola_light);
        cocaLight.put("content","this is Coca Light");

        HashMap<String, Object> cocaZero = new HashMap<>();
        cocaZero.put("name","Coca Zero");
        cocaZero.put("price",30);
        cocaZero.put("image",R.drawable.coca_cola_zero);
        cocaZero.put("content","this is Coca Zero");

        HashMap<String, Object> kfc = new HashMap<>();
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

        //呼叫simpleAdapter  兩種方式可參考, 要簡潔可把參數定義在最上面
        //adapter = new SimpleAdapter(context, itemList,R.layout.item_layout, new String[]{"name","price","image"},
                 // new int[]{R.id.name, R.id.price, R.id.imageView});
        adapter = new SimpleAdapter(context, itemList,R.layout.item_layout, key, itemID);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String,Object> item = (HashMap<String,Object>)adapterView.getItemAtPosition(i);
                String msg="You select:"+item.get("name")+"price is:"+item.get("price");
                Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
                
            }
        });






    }
}
