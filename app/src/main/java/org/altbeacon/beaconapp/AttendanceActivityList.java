package org.altbeacon.beaconapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class AttendanceActivityList extends ArrayAdapter<AttendanceActivity> {
    private Activity context;
    private List<AttendanceActivity> activityList;

    public AttendanceActivityList(Activity context,List<AttendanceActivity> activityList){
        super(context,R.layout.list_layout,activityList);
        this.context=context;
        this.activityList=activityList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View listViewItem=inflater.inflate(R.layout.list_layout,null,true);
        TextView textViewName=(TextView)listViewItem.findViewById(R.id.textViewName);
        AttendanceActivity attendanceActivity=activityList.get(position);
        textViewName.setText(attendanceActivity.getActivityDate());
        return listViewItem;
    }
}


