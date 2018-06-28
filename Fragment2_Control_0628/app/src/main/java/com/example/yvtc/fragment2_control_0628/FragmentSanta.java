package com.example.yvtc.fragment2_control_0628;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentSanta extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.santa, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView textViewSanta = (TextView) getView().findViewById(R.id.textView_santaID);
        textViewSanta.setText("Santa Claus");

        ImageView imageViewSanta = (ImageView) getView().findViewById(R.id.imageView_santaID);
        imageViewSanta.setImageResource(R.drawable.santa);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
