package com.example.yvtc.intent_ex;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private EditText idEdit, nameEdit;
    private ImageButton imgBtn;
    private TextView texViewResult;
    private Context context;
    private static final String TAG = "Main";
    private final int IntentRequestCode = 1; //收結果才需要 IntentRequestCode
    private final int resultOKCode = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        idEdit = (EditText)findViewById(R.id.edTe_id_id);
        nameEdit = (EditText)findViewById(R.id.edTe_name_id);
        imgBtn = (ImageButton)findViewById(R.id.imgBt_id);
        texViewResult = (TextView)findViewById(R.id.teVi_resu_id);

        idEdit.setText("");
        nameEdit.setText("");
        texViewResult.setText("");

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String name = nameEdit.getText().toString();
                    int id = Integer.parseInt(idEdit.getText().toString());
                    Log.d(TAG,"name = "+name);
                    Log.d(TAG,"age = "+id);
                    
                    Intent calIntent = new Intent(context, OptionsActivity.class);
                    startActivityForResult(calIntent,IntentRequestCode); //final int IntentRequestCode = 1

                }catch (Exception e){
                    Toast.makeText(context,"Please input data",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG,"requestCode = "+requestCode);
        Log.d(TAG,"resultCode = "+resultCode);
        String s = "";

        if(requestCode == IntentRequestCode){
            String sc1 = data.getStringExtra("sc1");
            String sc2 = data.getStringExtra("sc2");
            String sc3 = data.getStringExtra("sc3");
            String sc4 = data.getStringExtra("sc4");
            String sr = data.getStringExtra("sr");

            int sid = Integer.parseInt(idEdit.getText().toString());
            String sname = nameEdit.getText().toString();

            texViewResult.setText("~ 學生資料 ~\n\n");
            texViewResult.append("ID : "+sid+"\n\nName : "+sname+"\n\n");
            texViewResult.append("性別 : "+sr+"\n\n");
            texViewResult.append("興趣 : "+"\t");
            if(sc1 != null) {
                //texViewResult.append(sc1 + "\t");
                s = s + sc1 + "\t";
            }if(sc2 != null) {
                //texViewResult.append(sc2 + "\t");
                s = s + sc2 + "\t";
            }if(sc3 != null) {
                //texViewResult.append(sc3 + "\t");
                s = s + sc3 + "\t";
            }if(sc4 != null) {
                //texViewResult.append(sc4 + "\t");
                s = s + sc4 + "\t";
            }

            texViewResult.append(s);
        }
    }
}
