package com.example.toni.pjesmarrjaestudenteve_v1;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TONI on 5/3/2018.
 */

public class listview_studentat extends ArrayAdapter<String> {

    private LayoutInflater mLayoutInflater;
    private ArrayList<String> studentat;
    private int  mViewResourceId;

    public listview_studentat(Context context, int tvResourceId, ArrayList<String> studentat){
        super(context, tvResourceId,studentat);
        this.studentat = studentat;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = tvResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mLayoutInflater.inflate(mViewResourceId, null);
        String _studentat = studentat.get(position);
        if (studentat != null) {
            TextView studentatName = (TextView) convertView.findViewById(R.id.tv_studentiView);
            if (studentatName != null) {
                studentatName.setText(_studentat);
            }
        }

        return convertView;
    }

}
