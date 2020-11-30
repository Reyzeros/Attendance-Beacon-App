package org.altbeacon.beaconreference;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDisplay extends Activity {
    ListView listViewAttendance;
    TextView textViewGroupNameAttendance,textViewDateAttendance;
    String groupId,groupName,attendanceId,attendanceDate,organiserId;
    DatabaseReference databaseActivity;
    List<IsPresent> isPresents;
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
        isPresents=new ArrayList<>();
        textViewGroupNameAttendance.setText(groupName);
        textViewDateAttendance.setText(attendanceDate);
        databaseActivity= FirebaseDatabase.getInstance().getReference("AttendanceActivityUser").child(groupId).child(organiserId).child(attendanceId);
    }


    @Override
    protected void onStart() {
        super.onStart();
        databaseActivity.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                isPresents.clear();
                for(DataSnapshot isPresentSnapshot : snapshot.getChildren()){
                    IsPresent isPresent= isPresentSnapshot.getValue(IsPresent.class);
                    isPresents.add(isPresent);
                }
                IsPresentList isPresentList=new IsPresentList(AttendanceDisplay.this,isPresents);
                listViewAttendance.setAdapter(isPresentList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
