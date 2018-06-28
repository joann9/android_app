package com.example.yvtc.fragment2_control_0628;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private Button button1, button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = (Button) findViewById(R.id.button1_ID);
        button2 = (Button) findViewById(R.id.button2_ID);

        button1.setOnClickListener(new myOnClick());
        button2.setOnClickListener(new myOnClick());
    }

    private class myOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            FragmentManager fragmentM = getFragmentManager(); //取得FragmentManager物件, 才能控制saction
            FragmentTransaction ft = fragmentM.beginTransaction();

            switch(v.getId()){

                case  R.id.button1_ID:
                    ft.replace(R.id.fragLayout_ID, new FragmentSanta(), "f_a");
                    break;

                case  R.id.button2_ID:
                    ft.replace(R.id.fragLayout_ID, new FragmentTree(), "f_b");

                    break;
            }
            ft.commit();  //要commit
        }
    }
}
