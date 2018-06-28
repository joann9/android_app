package com.example.yvtc.a20180326_text04_list_view_checkout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;



public class MainActivity extends Activity {

    private Context context;
    private ListView list_view;
    private ArrayList<HashMap<String,Object>> item_list;
    private SimpleAdapter adapter;
    private Button check_out;
    private TextView text_result;
    private boolean[] checkList; //Boolean是物件Integer, 用小寫b是data Type
    private static final String TAG = "Main";
    private StringBuilder checkOutList;
    private  int sum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("點餐");

        context=this;

        list_view=(ListView)findViewById(R.id.listview);

        HashMap<String,Object> hamburger = new HashMap<>();     //建立HashMap類別hamburger物件
        hamburger.put("name","Hamburger");                      //加入key:name 設定資料為 Hamburger
        hamburger.put("price",80);                              //加入key:price 設定資料為 80
        hamburger.put("image",R.drawable.hamburger);            //加入key:price 設定資料為圖片id R.drawable.hamburger


        HashMap<String,Object> french= new HashMap<>();
        french.put("name","French fries");
        french.put("price",50);
        french.put("image",R.drawable.french_fries);

        HashMap<String,Object> coca= new HashMap<>();
        coca.put("name","Coca cola");
        coca.put("price",25);
        coca.put("image",R.drawable.coca_cola);

        HashMap<String,Object> coca_light= new HashMap<>();
        coca_light.put("name","Coca cola Light");
        coca_light.put("price",30);
        coca_light.put("image",R.drawable.coca_cola_light);

        HashMap<String,Object> coca_zero= new HashMap<>();
        coca_zero.put("name","Coca Zero");
        coca_zero.put("price",30);
        coca_zero.put("image",R.drawable.coca_cola_zero);

        HashMap<String,Object> KFC= new HashMap<>();
        KFC.put("name","KFC");
        KFC.put("price",120);
        KFC.put("image",R.drawable.kfc_small);

        item_list= new  ArrayList<>();              //建立 ArrayList 類別 item_list 物件
        item_list.add(hamburger);                   //加入HashMap類別hamburger物件
        item_list.add(french);
        item_list.add(coca);
        item_list.add(coca_light);
        item_list.add(coca_zero);
        item_list.add(KFC);

        checkList = new boolean[item_list.size()];

        adapter = new SimpleAdapter(context, item_list, R.layout.item_layout,
                new String[]{"name","price","image"} ,
                new int[]{R.id.textView_name,R.id.textView_price,R.id.imageView});{
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                super.getView(position, convertView, parent);
                CheckedTextView checkedText = (CheckedTextView) list_view.findViewById(R.id.textView_price);
                checkedText.setChecked(false);


            }
        };


        //adapter=new SimpleAdapter(context , item_list , R.layout.item_layout ,
         //       new String[]{"name","price","image"} ,
         //       new int[]{R.id.textView_name,R.id.textView_price,R.id.imageView});
        //建立SimpleAdapter類別adapter物件，裏頭設定 在這個 MainActivity 中 使用 ArrayList 類別 item_list 物件 資料， 使用 item_layout ，並分別取出
        //name、price、image 資料，給 textView_name、textView_price、imageView
        list_view.setAdapter(adapter);      //將設定給list_view

        check_out=(Button) findViewById(R.id.button);
        text_result=(TextView) findViewById(R.id.textView2);

        check_out.setOnClickListener(new checkOutClick());

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,Object> item = (HashMap<String, Object>) parent.getItemAtPosition(position);
                CheckedTextView check_item = (CheckedTextView) view.findViewById(R.id.textView_price);
                boolean tv=check_item.isChecked();
                check_item.setChecked(!tv);
                checkList[position]=!tv;
                Log.d(TAG,"checkList[]="+checkList[position]);
            }
        });
    }

    private class checkOutClick implements View.OnClickListener {

        private  int sum;

        @Override
        public void onClick(View view) {
            checkOutList = new StringBuilder("You selection are:\n");

            sum = 0;
            for(int i = 0; i < checkList.length ; i++){
                if(checkList[i]){
                    HashMap<String,Object> item = item_list.get(i);
                    String msg = item.get("name")+" ,price"+item.get("price");
                    Log.d(TAG,"msg="+msg);
                    sum += Integer.parseInt(item.get("price").toString());
                    checkOutList.append(msg+"\n");
                    Log.d(TAG,"sum="+sum);
                    Log.d(TAG,"checkOutList="+checkOutList);
        }
    }

            checkOutList.append("The Total price = "+sum);
            //清除勾勾
            for(int i=0; i<checkList.length;i++){
                if(checkList[i]){
                    adapter.getView(i,null,null);
                    checkList[i]=false;
                }
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Check out");
            builder.setIcon(android.R.drawable.ic_dialog_info);
            builder.setMessage(checkOutList);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    text_result.setText(checkOutList);
                    dialogInterface.dismiss();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    text_result.setText("");
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
        }
    }
}
