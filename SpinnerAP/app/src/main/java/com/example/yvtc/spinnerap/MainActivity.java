package com.example.yvtc.spinnerap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private Context context;
    private Spinner spinner1, spinner2, spinner3;
    private TextView textView;
    private ArrayAdapter<String> codeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);

        textView = (TextView) findViewById(R.id.textView2);

        //"AdapterView.OnItemSelectedListener()" 就算不用, 還是要實作
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Object obj = parent.getItemAtPosition(position);
                Toast.makeText(context, "Select : "+obj,Toast.LENGTH_SHORT).show();
                textView.setText("Select : "+obj.toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        String[] data = getResources().getStringArray(R.array.country_code);
        //codeAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, data); //context:顯示那一個畫面,
        codeAdapter = new ArrayAdapter<String>(context, R.layout.simple_spinner_item, data); //用local, 不加 android
        codeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//下拉格式用系統標準:simple_spinner_dropdown_item
        spinner2.setAdapter(codeAdapter);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object obj = codeAdapter.getItem(i);
                textView.setText("Select : "+obj.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<CharSequence> planetAdapter = ArrayAdapter.createFromResource(context, R.array.planets, R.layout.simple_spinner_item);
        planetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(planetAdapter);

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = spinner3.getSelectedItem().toString();
                textView.setText("Select :"+text);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}
