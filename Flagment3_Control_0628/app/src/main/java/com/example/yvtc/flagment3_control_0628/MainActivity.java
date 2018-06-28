package com.example.yvtc.flagment3_control_0628;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private Button button1, button2, button3, button4;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        button1 = (Button) findViewById(R.id.button1_ID);
        button2 = (Button) findViewById(R.id.button2_ID);
        button3 = (Button) findViewById(R.id.button3_ID);
        button4 = (Button) findViewById(R.id.button4_ID);

        button1.setOnClickListener(new myOnClick());
        button2.setOnClickListener(new myOnClick());
        button3.setOnClickListener(new myOnClick());
        button4.setOnClickListener(new myOnClick());

    }

    private class myOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = getFragmentManager().beginTransaction();

            switch (v.getId()) {

                case R.id.button1_ID:
                    ft.add(R.id.fragment_layoutID, new LottoFragment(), "f_lotto");
                    ft.addToBackStack(null); //每按一個就產生一個fragment, 每產生一個fragment就存起來
                    break;

                case R.id.button2_ID:  //因為是Stack, 所以只能 Get 到最後一筆(最新)資料
                    Fragment getFrag = fm.findFragmentByTag("f_lotto");  //取出fragment
                    if(getFrag != null) {
                        TextView lotto_num = (TextView) getFrag.getView().findViewById(R.id.lotto_num);
                        Toast.makeText(context, lotto_num.getText().toString(),Toast.LENGTH_SHORT).show();
                    }

                    break;

                case R.id.button3_ID:
                    fm.popBackStack(); //刪掉一筆

                    break;

                case R.id.button4_ID:
                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); //全部刪掉

                    break;

            }
            ft.commit();
        }
    }
}
