package org.altbeacon.beaconreference;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class IsPresentList extends ArrayAdapter<IsPresent> {
    private Activity context;
    private List<IsPresent> isPresentList;

    public IsPresentList(Activity context,List<IsPresent> isPresentList){
        super(context,R.layout.list_layout,isPresentList);
        this.context=context;
        this.isPresentList=isPresentList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View listViewItem=inflater.inflate(R.layout.list_layout,null,true);
        TextView textViewName=(TextView)listViewItem.findViewById(R.id.textViewOrganiserName);
        IsPresent isPresent=isPresentList.get(position);
        textViewName.setText(isPresent.getUserName());
        return listViewItem;
    }
}


