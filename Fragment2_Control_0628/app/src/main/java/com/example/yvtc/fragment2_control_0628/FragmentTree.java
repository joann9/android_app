package com.example.yvtc.fragment2_control_0628;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentTree extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.tree, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView textViewTree = (TextView) getView().findViewById(R.id.textView_treeID);
        textViewTree.setText("Santa Claus");

        ImageView imageViewTree = (ImageView) getView().findViewById(R.id.imageView_treeID);
        imageViewTree.setImageResource(R.drawable.tree);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
