package com.example.yvtc.menu1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {

    private TextView showMenu1, showMenu2, showMenu3;
    private final int groupID_1 = 1;
    private final int groupID_2 = 2;
    private final int groupID_3 = 3;
    private final int playVideo = 2;
    private final int rtspVideo = 3;
    private final int dialog1 = 4;
    private final int dialog2 = 5;
    private final int dialog3 = 6;
    private final int dialog4 = 7;
    private Context context;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        showMenu1 = (TextView) findViewById(R.id.textView1);
        showMenu2 = (TextView) findViewById(R.id.textView2);
        showMenu3 = (TextView) findViewById(R.id.textView3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(groupID_1, 1, Menu.NONE, "新增");
        menu.add(groupID_2, playVideo, Menu.NONE, "播放影片");
        menu.add(groupID_2, rtspVideo, Menu.NONE, "RTSP影片");
        menu.add(groupID_3, dialog1, Menu.NONE, "Dialog1");
        menu.add(groupID_3, dialog2, Menu.NONE, "Dialog2");
        menu.add(groupID_3, dialog3, Menu.NONE, "Dialog3");
        menu.add(groupID_3, dialog4, Menu.NONE, "Dialog4");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemNum = item.getGroupId();
        if(itemNum == groupID_1){
            showMenu1.setText(item.getItemId()+":"+item.getTitle());
        } else if(itemNum == groupID_2){
            showMenu2.setText(item.getItemId()+":"+item.getTitle());

            Intent intent = new Intent(context, PlayActivity.class);

            if(item.getItemId() == playVideo){
                intent.putExtra("videoMode",playVideo);
                intent.putExtra("title", "PlayVideo");
            }else{
                intent.putExtra("videoMode",rtspVideo);
                intent.putExtra("title", "RTSPVideo");
            }
            startActivity(intent);


        } else if(itemNum == groupID_3){
            showMenu3.setText(item.getItemId()+":"+item.getTitle()+"\n");

            switch (item.getItemId()){
                case dialog1:
                    showDialog1();
                    break;
                case dialog2:
                    showDialog2();
                    break;
                case dialog3:
                    showDialog3();
                    break;
                case dialog4:
                    showDialog4();
                    break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDialog1() {
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Change display");
        builder.setMessage("Please confirm to change the text.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showMenu3.append("雙鍵對話框 was pressed");
                dialogInterface.dismiss();

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
    private void showDialog2() {
        builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.button_text2);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setMessage(R.string.like_android);
        showMenu3.append("多鍵對話框 was pressed. \n");
        showMenu3.append(getString(R.string.like_android));

        builder.setPositiveButton(R.string.like, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showMenu3.append(getString(R.string.like));
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(R.string.not, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showMenu3.append(getString(R.string.not));
                dialogInterface.dismiss();
            }
        });
        builder.setNeutralButton(R.string.no_idea, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showMenu3.append(getString(R.string.no_idea));
                dialogInterface.dismiss();
            }
        });
        builder.create().show();

    }
    private void showDialog3() {
        builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.button_text3);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        showMenu3.append("單選項對話框 was pressed.\n");
        showMenu3.append("You select drink : ");
        final List<String> list = Arrays.asList(getResources().getStringArray(R.array.drink));
        //0 : 第一項被選到
        builder.setSingleChoiceItems(R.array.drink,0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                Toast.makeText(context,list.get(which),Toast.LENGTH_SHORT).show();
                showMenu3.append(list.get(which));
                dialogInterface.dismiss();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();

    }
    private void showDialog4() {
        builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.button_text4);
        builder.setIcon(android.R.drawable.ic_dialog_info);

        final String[] foodList = getResources().getStringArray(R.array.food);
        final boolean[] checkList = new boolean[foodList.length];

        showMenu3.append("多選項對話框 was pressed.\n");
        showMenu3.append("You select food :\n");
        //以上設定基本外框

        builder.setMultiChoiceItems(foodList, checkList, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                checkList[which]=isChecked;
            }
        });
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                StringBuilder msg = new StringBuilder();
                for(int i=0; i<checkList.length;i++){
                    if(checkList[i]){
                        msg.append("         "+foodList[i]+"\n");
                    }
                }
                showMenu3.append(msg.toString());
                dialogInterface.dismiss(); //關掉視窗
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();


    }

}
