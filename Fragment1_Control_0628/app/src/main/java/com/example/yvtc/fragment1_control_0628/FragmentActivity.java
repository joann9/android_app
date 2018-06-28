package com.example.yvtc.fragment1_control_0628;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class FragmentActivity extends Fragment {
    private static final String TAG = "Data";
    private TextView textViewFrag1, textViewFrag2;
    private Button button1, button2;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "[F] onAttach");

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "[F] onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_layout, container, false);
        Log.d(TAG, "[F] onCreateView");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, "[F] onActivityCreated");

        View view = getView();
        textViewFrag1 = (TextView) view.findViewById(R.id.textView1_fragID);
        textViewFrag2 = (TextView) view.findViewById(R.id.textView2_fragID);
        textViewFrag1.setText("Change Fragment 1");
        textViewFrag2.setText("Change Fragment 2");
        button1 =(Button) view.findViewById(R.id.button1_fragID);
        button2 = (Button) view.findViewById(R.id.button2_fragID);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewFrag1.setText("Button 1 was pressed");
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewFrag2.setText("Button 2 was pressed");
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "[F] onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "[F] onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "[F] onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "[F] onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "[F] onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "[F] onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "[F] onDetach");
    }
}
