package org.altbeacon.beaconreference;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    DatabaseReference databaseGroups, databaseActivity, databaseOrganisers,databaseGroups2, databaseOrganisersUnRelated,databaseOrganiserGroupRelation;
    TextView textViewSetupOrganiserName,textViewGroupChoice;
    EditText editTextDateOrganiserSetup;
    Button buttonAddDate,buttonStartAttendance,buttonStopAttendance,ButtonResults;
    ListView listViewGroups,listViewAttendanceActivity;
    List<Group> groupList;
    List<AttendanceActivity> activityList;
    String temporaryOrganiserId, temporaryOrganiserName, groupId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organiser_setup);

        temporaryOrganiserId="-MNE5SA2KIGi9EOnmgji";
        temporaryOrganiserName="Patryk Wyczesany";

        groupId="";
        databaseGroups= FirebaseDatabase.getInstance().getReference("Groups");
        databaseOrganiserGroupRelation=FirebaseDatabase.getInstance().getReference("Organiser Group Relation");
        databaseActivity=FirebaseDatabase.getInstance().getReference("AttendanceActivity");
        textViewSetupOrganiserName=(TextView) findViewById(R.id.textViewSetupOrganiserName);
        textViewGroupChoice=(TextView) findViewById(R.id.textViewGroupChoice);
        buttonAddDate=(Button) findViewById(R.id.buttonAddDate);
        buttonStartAttendance=(Button) findViewById(R.id.buttonStartAttendance);
        buttonStopAttendance=(Button) findViewById(R.id.buttonStopAttendance);
        ButtonResults=(Button) findViewById(R.id.buttonResults);
        groupList=new ArrayList<>();
        activityList=new ArrayList<>();
        listViewGroups=(ListView) findViewById(R.id.listViewGroupsOrganiserSetup);
        listViewAttendanceActivity=(ListView) findViewById(R.id.ListViewDate);
        listViewGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Group group = groupList.get(i);
                textViewGroupChoice.setText(group.getGroupName());
                groupId=group.getGroupId();
            }});

        textViewSetupOrganiserName.setText(temporaryOrganiserName);
    }


    public void onAddDateClicked(View view){
        addDate();
    }

    private void addDate(){
        if(!TextUtils.isEmpty(groupId)) {
            String currentDateTime = java.text.DateFormat.getDateTimeInstance().format(new Date());
            String relationId=databaseOrganiserGroupRelation.push().getKey();
            OrganiserGroupRelation organiserGroupRelation=new OrganiserGroupRelation(relationId,temporaryOrganiserId,groupId);
            databaseOrganiserGroupRelation.child(relationId).setValue(organiserGroupRelation);
            String activityId=databaseActivity.push().getKey();
            AttendanceActivity attendanceActivity= new AttendanceActivity(activityId,relationId,currentDateTime);
            databaseActivity.child(activityId).setValue(attendanceActivity);

          /**  databaseOrganisers=FirebaseDatabase.getInstance().getReference("Organisers").child(groupId);
            databaseOrganisersUnRelated.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                   Organiser organiser=snapshot.getValue(Organiser.class);
                   databaseOrganisers.child(groupId).setValue(organiser);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });**/
            Toast.makeText(this,"Organiser Added to Group and Activity created!", Toast.LENGTH_LONG).show();

        }
        else{
            Toast.makeText(this,"You must choose Group!", Toast.LENGTH_LONG).show();

        }


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
        databaseActivity.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                activityList.clear();
                for(DataSnapshot activitySnapshot: snapshot.getChildren()){
                    AttendanceActivity attendanceActivity= activitySnapshot.getValue(AttendanceActivity.class);
                    activityList.add(attendanceActivity);
                }
                AttendanceActivityList adapter= new AttendanceActivityList(OrganiserSetup.this,activityList);
                listViewAttendanceActivity.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
