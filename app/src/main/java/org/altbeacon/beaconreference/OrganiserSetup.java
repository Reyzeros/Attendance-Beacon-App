package org.altbeacon.beaconreference;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrganiserSetup extends Activity {
    DatabaseReference databaseGroups, databaseDates;
    TextView textViewSetupOrganiserName,textViewGroupChoice;
    EditText editTextDateOrganiserSetup;
    Button buttonAddDate,buttonStartAttendance,buttonStopAttendance,ButtonResults;
    ListView listViewGroups;
    List<Group> groupList;
    String temporaryOrganiserId, temporaryOrganiserName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organiser_setup);

        temporaryOrganiserId="-MNE5SA2KIGi9EOnmgji";
        temporaryOrganiserName="Patryk Wyczesany";


        databaseGroups= FirebaseDatabase.getInstance().getReference("Groups");
        textViewSetupOrganiserName=(TextView) findViewById(R.id.textViewSetupOrganiserName);
        textViewGroupChoice=(TextView) findViewById(R.id.textViewGroupChoice);
        buttonAddDate=(Button) findViewById(R.id.buttonAddDate);
        buttonStartAttendance=(Button) findViewById(R.id.buttonStartAttendance);
        buttonStopAttendance=(Button) findViewById(R.id.buttonStopAttendance);
        ButtonResults=(Button) findViewById(R.id.buttonResults);
        groupList=new ArrayList<>();
        listViewGroups=(ListView) findViewById(R.id.listViewGroupsOrganiserSetup);
        listViewGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Group group= groupList.get(i);
               textViewGroupChoice.setText(group.getGroupName());
            }});
        textViewSetupOrganiserName.setText(temporaryOrganiserName);
    }


    public void onAddDateClicked(View view){
        addDate();
    }

    private void addDate(){
        String currentDateTime= java.text.DateFormat.getDateTimeInstance().format(new Date());
        System.out.println(currentDateTime+" TO TU");
    }
    @Override
    protected void onStart() {
        super.onStart();
        databaseGroups.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupList.clear();
                for (DataSnapshot groupSnapshot : snapshot.getChildren()) {
                    Group group = groupSnapshot.getValue(Group.class);
                    groupList.add(group);
                }
                GroupList adapter = new GroupList(OrganiserSetup.this, groupList);
                listViewGroups.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}
