package com.example.yvtc.flagment3_control_0628;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class LottoFragment extends Fragment {

    private TextView lotto_num;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.lotto, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lotto_num = (TextView) getView().findViewById(R.id.lotto_num);
        int lottoNumber = (int) (Math.random() * 100 % 50);
        lotto_num.setText(String.valueOf(lottoNumber));

    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
