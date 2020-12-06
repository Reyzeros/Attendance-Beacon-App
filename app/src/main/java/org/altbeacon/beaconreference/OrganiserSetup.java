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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrganiserSetup extends Activity {
    DatabaseReference databaseGroups, databaseActivity,databaseActivity2, databaseOrganisers,databaseGroups2, databaseOrganisersUnRelated,databaseOrganiserGroupRelation;
    TextView textViewSetupOrganiserName,textViewGroupChoice,textViewDateShown;
    Button buttonAddDate,buttonStartAttendance,buttonStopAttendance,ButtonResults;
    ListView listViewGroups,listViewAttendanceActivity;
    List<Group> groupList;
    List<AttendanceActivity> activityList;
    String temporaryOrganiserId, temporaryOrganiserName, groupId ,groupName,attendanceId,attendanceDate;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organiser_setup);

        temporaryOrganiserId="";
        temporaryOrganiserName="";
        temporaryOrganiserId=getIntent().getStringExtra("USER_ID");
        fAuth=FirebaseAuth.getInstance();
        Query query=FirebaseDatabase.getInstance().getReference("Organisers").orderByChild("organiserId").equalTo(temporaryOrganiserId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot datasnapshot:snapshot.getChildren()) {
                        Organiser organiser = datasnapshot.getValue(Organiser.class);
                        temporaryOrganiserName = organiser.getOrganiserName();
                        textViewSetupOrganiserName.setText(temporaryOrganiserName);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        groupId="";
        attendanceId="";
        databaseGroups= FirebaseDatabase.getInstance().getReference("Groups");
        databaseActivity=FirebaseDatabase.getInstance().getReference("AttendanceActivity").child(groupId).child(temporaryOrganiserId);
        textViewSetupOrganiserName=(TextView) findViewById(R.id.textViewSetupOrganiserName);
        textViewGroupChoice=(TextView) findViewById(R.id.textViewGroupChoice);
        textViewDateShown=(TextView) findViewById(R.id.textViewDateShown);
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
                groupName=group.getGroupName();
                databaseActivity=FirebaseDatabase.getInstance().getReference("AttendanceActivity").child(groupId).child(temporaryOrganiserId);
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
            }});

        listViewAttendanceActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                              @Override
                                                              public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                                    AttendanceActivity attendanceActivity=activityList.get(i);
                                                                  attendanceId=attendanceActivity.getActivityId();
                                                                  attendanceDate=attendanceActivity.getActivityDate();
                                                                  textViewDateShown.setText(attendanceDate);
                                                              }
                                                          });

    }


    public void onAddDateClicked(View view){
        addDate();
    }
    public void onButtonStartAttendanceClicked(View view){
        startAttendance();
    }
    public void onButtonStopAttendanceClicked(View view){
        stopAttendance();
    }
    public  void onButtonResultsClicked(View view){
        Intent intent=new Intent(getApplicationContext(),AttendanceDisplay.class);
        intent.putExtra("GROUP_ID",groupId);
        intent.putExtra("GROUP_NAME",groupName);
        intent.putExtra("ATTENDANCE_ID",attendanceId);
        intent.putExtra("ATTENDANCE_DATE",attendanceDate);
        intent.putExtra("ORGANISER_ID",temporaryOrganiserId);
        startActivity(intent);
    }

    private void addDate() {
        if (!TextUtils.isEmpty(groupId)) {
            String currentDateTime = java.text.DateFormat.getDateTimeInstance().format(new Date());
            String activityId = databaseActivity.push().getKey();
            AttendanceActivity attendanceActivity = new AttendanceActivity(activityId, currentDateTime);
            databaseActivity.child(activityId).setValue(attendanceActivity);
            Toast.makeText(this, "Organiser Added to Group and Activity created!", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "You must choose Group!", Toast.LENGTH_LONG).show();

        }
    }

    private void startAttendance(){
        if(!TextUtils.isEmpty(attendanceId)) {
            databaseActivity2 = FirebaseDatabase.getInstance().getReference("AttendanceActivity").child(groupId).child(temporaryOrganiserId).child(attendanceId);
            AttendanceActivity attendanceActivity = new AttendanceActivity(attendanceId, attendanceDate);
            attendanceActivity.setActivityIsChecking(true);
            databaseActivity2.setValue(attendanceActivity);
            Toast.makeText(this, "Attendance Check is On!", Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(this, "You need to choose Date!", Toast.LENGTH_LONG).show();
    }

    private void stopAttendance(){
        if(!TextUtils.isEmpty(attendanceId)){
        databaseActivity2=FirebaseDatabase.getInstance().getReference("AttendanceActivity").child(groupId).child(temporaryOrganiserId).child(attendanceId);
        AttendanceActivity attendanceActivity= new AttendanceActivity(attendanceId,attendanceDate);
        attendanceActivity.setActivityIsChecking(false);
        databaseActivity2.setValue(attendanceActivity);
        Toast.makeText(this, "Attendance Check is Off!", Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(this, "You need to choose Date!", Toast.LENGTH_LONG).show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fAuth.signOut();
    }
}
