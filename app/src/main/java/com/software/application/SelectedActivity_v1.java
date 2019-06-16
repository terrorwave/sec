package com.software.application;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SelectedActivity_v1 extends AppCompatActivity {
    private LinearLayout container;
    private ArrayList<DataModel_v1> checkedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_v1);

        container = (LinearLayout) findViewById(R.id.layout);
        checkedList = new ArrayList<DataModel_v1>(); // initializing list
        getDataFromIntent(); // receive data from intent (put by MainActivity)
        generateDataToContainerLayout();
    }

    private void getDataFromIntent() {
        checkedList = getIntent().getParcelableArrayListExtra("Checked List");
        Log.i("ListActivity", "size" + checkedList.size());
    }
    @SuppressLint("InflateParams")
    private void generateDataToContainerLayout() {
        int i = 0;
        if (checkedList.size() == i) {}
        while (checkedList.size() > i) {
            final DataModel_v1 friend = checkedList.get(i);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_listview_v1, null, false);
            TextView friendName = (TextView) view.findViewById(R.id.name);
            CheckBox checked = (CheckBox)view.findViewById(R.id.check);
            checked.setVisibility(View.GONE);
            if (friend.isSelected()) {
                Log.i("ListActivity", "here" + friend.getName());
                friendName.setText(friend.getName());
                // add view after all
                container.addView(view);
            }
            i++; // rise i
        }
    }
}




