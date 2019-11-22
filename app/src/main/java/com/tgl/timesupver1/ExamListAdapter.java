package com.tgl.timesupver1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ExamListAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    String[] examName;
    String[] examCode;

    public ExamListAdapter(Context c, String[] N, String[] C){
        examName = N;
        examCode = C;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return examName.length;
    }

    @Override
    public Object getItem(int i) {
        return examName[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {

        View v = mInflater.inflate(R.layout.exam_list, null);
        TextView ENTextView = v.findViewById(R.id.ENTextView);
        TextView ECTextView = v.findViewById(R.id.ENTextView);

        String Ename = examName[i];
        String Ecode = examCode[i];

        ENTextView.setText(Ename);
        ECTextView.setText(Ecode);

        return v;
    }
}
