package org.altbeacon.beaconreference;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AttendanceDisplay extends Activity {
    ListView listViewAttendance;
    TextView textViewGroupNameAttendance,textViewDateAttendance;
    String groupId,groupName,attendanceId,attendanceDate,organiserId;
    DatabaseReference databaseActivity,databaseUsers;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);
        textViewGroupNameAttendance=(TextView) findViewById(R.id.textViewGroupNameAttendance);
        textViewDateAttendance=(TextView) findViewById(R.id.textViewDateAttendance);
        listViewAttendance=(ListView) findViewById(R.id.ListViewPresentPeople);
        groupId=getIntent().getStringExtra("GROUP_ID");
        groupName=getIntent().getStringExtra("GROUP_NAME");
        attendanceId=getIntent().getStringExtra("ATTENDANCE_ID");
        attendanceDate=getIntent().getStringExtra("ATTENDANCE_DATE");
        organiserId=getIntent().getStringExtra("ORGANISER_ID");

        textViewGroupNameAttendance.setText(groupName);
        textViewDateAttendance.setText(attendanceDate);
        databaseActivity= FirebaseDatabase.getInstance().getReference("AttendanceActivity").child(groupId).child(organiserId).child(attendanceId);
        databaseUsers=FirebaseDatabase.getInstance().getReference("users").child(groupId);
    }

/**
    @Override
    protected void onStart() {
        super.onStart();
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for(DataSnapshot userSnapshot : snapshot.getChildren()){
                    User user= userSnapshot.getValue(User.class);
                    users.add(user);
                }
                UserList userListAdapter=new UserList(AddUser.this,users);
                listViewUsers.setAdapter(userListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    */
}
