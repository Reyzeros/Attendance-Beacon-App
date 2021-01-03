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

public class GroupList extends ArrayAdapter<Group> {
    private Activity context;
    private List<Group> groupList;

    public GroupList(Activity context,List<Group> groupList){
        super(context,R.layout.list_layout,groupList);
        this.context=context;
        this.groupList=groupList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View listViewItem=inflater.inflate(R.layout.list_layout,null,true);
        TextView textViewName=(TextView)listViewItem.findViewById(R.id.textViewName);
        Group group=groupList.get(position);
        textViewName.setText(group.getGroupName());
        return listViewItem;
    }
}
