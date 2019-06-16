package com.software.application;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter_v1 extends ArrayAdapter<DataModel_v1> {
    private Activity activity;
    private ArrayList<DataModel_v1> Friends;
    private final String TAG = ListViewAdapter_v1.class.getSimpleName();

    public ListViewAdapter_v1(Activity activity, int resource, ArrayList<DataModel_v1> Friends) {
        super(activity, resource, Friends);
        this.activity = activity;
        this.Friends = Friends;
        Log.i(TAG, "init adapter");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        // inflate layout from xml
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.item_listview_v1, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        DataModel_v1 friend = Friends.get(position);
        //set Friend data to views
        holder.name.setText(friend.getName());


        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        holder.name.setOnClickListener(OnClickListener(friend));

        //set event for checkbox
        holder.check.setOnCheckedChangeListener(onCheckedChangeListener(friend));

        return convertView;

    }
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener(final DataModel_v1 f) {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    f.setSelected(true);
                } else {
                    f.setSelected(false);
                }
            }
        };
    }

    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private CompoundButton.OnClickListener OnClickListener (final DataModel_v1 f) {
        return new CompoundButton.OnClickListener(){
            @Override
            public void onClick(View textView) {
                Intent intent = new Intent(activity, WatchInterview.class);
                intent.putExtra("_id", f.getName().substring(0,2));
                //Log.d("ListView",f.getName().substring(0,1));
                activity.startActivity(intent);
            }
        };
    }


    private class ViewHolder {
        private TextView name;
        private CheckBox check;

        public ViewHolder(View v) {
            name = (TextView) v.findViewById(R.id.name);
            check = (CheckBox) v.findViewById(R.id.check);
        }
    }
}
