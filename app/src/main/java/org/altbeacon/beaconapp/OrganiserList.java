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

public class OrganiserList extends ArrayAdapter<Organiser> {
    private Activity context;
    private List<Organiser> organizerList;

    public OrganiserList(Activity context,List<Organiser> organizerList){
        super(context,R.layout.list_layout,organizerList);
    this.context=context;
    this.organizerList=organizerList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View listViewItem=inflater.inflate(R.layout.list_layout,null,true);
        TextView textViewName=(TextView)listViewItem.findViewById(R.id.textViewName);
        TextView textViewBeaconId=(TextView)listViewItem.findViewById(R.id.textViewName2);
        Organiser organiser=organizerList.get(position);
        textViewName.setText(organiser.getOrganiserName());
        textViewBeaconId.setText(organiser.getOrganiserBeaconId());
        return listViewItem;
    }
}
