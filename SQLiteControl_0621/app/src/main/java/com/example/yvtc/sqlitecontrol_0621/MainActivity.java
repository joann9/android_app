package com.example.yvtc.sqlitecontrol_0621;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private DBHelper dbHelp;
    private SQLiteDatabase db;
    private FloatingActionButton fab;
    private ListView listData;
    private Cursor mainCursor;
    private SimpleCursorAdapter adapter;
    private String [] coffeeName = {"Latte","Cappuccino","Macchiato","Mocha"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sql_main);

        context = this;

        dbHelp = new DBHelper(context);
        db =dbHelp.getWritableDatabase();

        fab = (FloatingActionButton) findViewById(R.id.fab_ID);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCoffeeDialog();
            }


        });

        listData = (ListView) findViewById(R.id.listView_ID);
        listData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);  //建立游標
                modifyCoffeeDialog(cursor);
            }
        });

        refreshListView();

    } //end of coCreate

    private void refreshListView() {
        //設變數為主要cursor,若空(null)表示沒有資料,放資料進去

        if(mainCursor == null) {
            mainCursor = db.rawQuery("SELECT _id,title,price,image FROM coffee_list", null);
            adapter = new SimpleCursorAdapter(context, R.layout.item_layout, mainCursor,
                    new String[]{"_id","title","price","image"},
                    new int[]{R.id.DB_ID, R.id.title, R.id.price, R.id.image},
                    CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER) ; //監聽cursor
            listData.setAdapter(adapter);

        } else {

            if(mainCursor.isClosed()) {
                mainCursor = null;  // 回到上面 if(mainCursor == null)
                refreshListView();

            } else {
                mainCursor.requery();
                adapter.changeCursor(mainCursor);
                adapter.notifyDataSetChanged();
            }

        }



    }

    private void addCoffeeDialog() {
        final Spinner coffee_spinner;   //沒有加 final 會有紅字
        final EditText coffee_price;

        LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(R.layout.dialog_layout, (ViewGroup) findViewById(R.id.dialogLayout_ID));

        coffee_spinner = (Spinner) layout.findViewById(R.id.coffee_spinner);
        coffee_price = (EditText) layout.findViewById(R.id.coffee_price);

        ArrayAdapter<String> coffeeAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, coffeeName);
        coffee_spinner.setAdapter(coffeeAdapter);

        //建 dialog
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

                ContentValues cv = new ContentValues(); //android 提供存放"資料庫"的類別
                cv.put("title",coffee);
                cv.put("price", Integer.parseInt(price));

                switch ((int)imageNo) {
                    case 0:
                        cv.put("image",R.drawable.latte);
                        break;

                    case 1:
                        cv.put("image",R.drawable.coffee_cappuccino);
                        break;

                    case 2:
                        cv.put("image",R.drawable.macchiato);
                        break;

                    case 3:
                        cv.put("image",R.drawable.coffee_mocha);
                        break;
                }

                long id = db.insert("coffee_list", null, cv);

                refreshListView();

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

     private void modifyCoffeeDialog(final Cursor itemCursor) {
         final Spinner coffee_spinner;   //沒有加 final 會有紅字
         final EditText coffee_price;
         int colIndex;

         LayoutInflater inflater = getLayoutInflater();

         View layout = inflater.inflate(R.layout.dialog_layout, (ViewGroup) findViewById(R.id.dialogLayout_ID));

         coffee_spinner = (Spinner) layout.findViewById(R.id.coffee_spinner);
         coffee_price = (EditText) layout.findViewById(R.id.coffee_price);

         ArrayAdapter<String> coffeeAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, coffeeName);
         coffee_spinner.setAdapter(coffeeAdapter);

         //------------------------------------------------------以上同addCoffeeDialog();

         colIndex = itemCursor.getColumnIndex("price");
         coffee_price.setText(itemCursor.getString(colIndex));

         colIndex = itemCursor.getColumnIndex("title");
         coffee_spinner.setSelection(coffeeAdapter.getPosition(itemCursor.getString(colIndex)));

         colIndex = itemCursor.getColumnIndex("_id");
         final String itemID = itemCursor.getString(colIndex);  //要加 final

         //建 Dialog
         AlertDialog.Builder builder = new AlertDialog.Builder(context);
         builder.setTitle("Modify item");
         builder.setIcon(android.R.drawable.ic_input_add);
         builder.setView(layout);
         builder.setPositiveButton("Modify", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 String coffee = (String) coffee_spinner.getSelectedItem();
                 long imageNo = coffee_spinner.getSelectedItemId();
                 String price = coffee_price.getText().toString();

                 ContentValues cv = new ContentValues(); //android 提供存放"資料庫"的類別
                 cv.put("title",coffee);
                 cv.put("price", Integer.parseInt(price));

                 switch ((int)imageNo) {
                     case 0:
                         cv.put("image",R.drawable.latte);
                         break;

                     case 1:
                         cv.put("image",R.drawable.coffee_cappuccino);
                         break;

                     case 2:
                         cv.put("image",R.drawable.macchiato);
                         break;

                     case 3:
                         cv.put("image",R.drawable.coffee_mocha);
                         break;
                 }
                 //-------------------------------以上同addCoffeeDialog();

                 int rowCount = db.update("coffee_list", cv, "_id=?", new String[]{itemID});
                 refreshListView();
                 dialog.dismiss();

             }
         });

         builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 dialog.dismiss();
             }
         });

         builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {

                 //需要知道item ID
                 int rowCount = db.delete("coffee_list", "_id=?", new String[]{itemID});
                 refreshListView();   //重新 update 畫面
             }
         });

         builder.create().show();
     }


}
