package org.altbeacon.beaconreference;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.altbeacon.beacon.AltBeacon;
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

public class RangingActivity extends Activity implements BeaconConsumer{
    protected static final String TAG = "RangingActivity";
    static String beaconId;
    TextView textViewOrganiserUser,textViewDateUser;
    ListView listViewOrganisers,listViewAttendanceActivity;
    List<Organiser> organiserList;
    List<AttendanceActivity> activityList;
    Button buttonConfirm;
    DatabaseReference databaseOrganisers,databaseDates, datebaseActivity;
    String temporaryGroupId;
    String temporaryUserId, temporaryUserName;
    String chosenOrganiserId,chosenOrganiserName,chosenOrganiserBeaconId;
    String chosenActivityId, chosenActivityDate;
    boolean isChecking;
    boolean isChosen;
    int posI;
    FirebaseAuth fAuth;

    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beaconId="0";
        chosenOrganiserId="";
        chosenActivityId="";
        isChosen=false;
        setContentView(R.layout.activity_ranging);
        textViewDateUser=(TextView) findViewById(R.id.textViewDateUser);
        textViewOrganiserUser=(TextView) findViewById(R.id.textViewOrganiserUser);
        listViewAttendanceActivity=(ListView) findViewById(R.id.listViewDateUser);
        listViewOrganisers=(ListView) findViewById(R.id.listViewOrganisersUser);
        fAuth=FirebaseAuth.getInstance();

        buttonConfirm=(Button) findViewById(R.id.buttonConfirmAttendance);
        activityList=new ArrayList<>();
        organiserList=new ArrayList<>();
        temporaryGroupId="";
        temporaryUserId="";
        temporaryUserName="";
        temporaryUserId= getIntent().getStringExtra("USER_ID");
        Query query=FirebaseDatabase.getInstance().getReference("Users").orderByChild("userId").equalTo(temporaryUserId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot datasnapshot:snapshot.getChildren()) {
                        User user = datasnapshot.getValue(User.class);
                        temporaryGroupId = user.getGroupId();
                        temporaryUserName = user.getUserName();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseOrganisers=FirebaseDatabase.getInstance().getReference("Organisers");
        listViewOrganisers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Organiser organiser= organiserList.get(i);
                chosenOrganiserId=organiser.getOrganiserId();
                chosenOrganiserName=organiser.getOrganiserName();
                chosenOrganiserBeaconId=organiser.getOrganiserBeaconId();
                textViewOrganiserUser.setText(chosenOrganiserName);
                databaseDates= FirebaseDatabase.getInstance().getReference("AttendanceActivity").child(temporaryGroupId).child(chosenOrganiserId);
                databaseDates.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        activityList.clear();
                        for(DataSnapshot activitySnapshot: snapshot.getChildren()){
                            AttendanceActivity attendanceActivity= activitySnapshot.getValue(AttendanceActivity.class);
                            activityList.add(attendanceActivity);
                        }
                        AttendanceActivityList adapter= new AttendanceActivityList(RangingActivity.this,activityList);
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
               AttendanceActivity attendanceActivity = activityList.get(i);
               posI=i;
               chosenActivityId = attendanceActivity.getActivityId();
               chosenActivityDate = attendanceActivity.getActivityDate();
               isChecking=attendanceActivity.getActivityIsChecking();
               textViewDateUser.setText(chosenActivityDate);
               new Timer().schedule(new TimerTask() {
                   @Override
                   public void run() {
                       try {
                           AttendanceActivity attendanceActivity = activityList.get(posI);
                           isChecking = attendanceActivity.getActivityIsChecking();
                       }
                       catch (Exception e){
                            System.out.println("Exception:"+e+"caught");
                       }
                   }
               },0,100);
           }
       });
    }
    public void onConfirmAttendanceClicked(View view){
        ConfirmAttendance();
    }

    private void ConfirmAttendance(){
        if(!TextUtils.isEmpty(chosenOrganiserId)&&!TextUtils.isEmpty(chosenActivityId)) {
            if (TextUtils.equals(chosenOrganiserBeaconId, beaconId)) {
                if (isChecking) {
                    datebaseActivity = FirebaseDatabase.getInstance().getReference("AttendanceActivityUser").child(temporaryGroupId).child(chosenOrganiserId).child(chosenActivityId);
                    String isPresentId = datebaseActivity.push().getKey();
                    IsPresent isPresent = new IsPresent(isPresentId, temporaryUserId,temporaryUserName);
                    datebaseActivity.child(isPresentId).setValue(isPresent);
                    Toast.makeText(this, "You are Present!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Attendance Check is not in progress", Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(this, "You are not in the range of the organizer Beacon", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(this, "You must choose Organiser and Date!", Toast.LENGTH_LONG).show();
        }
    }


    @Override 
    protected void onDestroy() {
        beaconId="0";
        fAuth.signOut();
        super.onDestroy();
    }

    @Override 
    protected void onPause() {
        super.onPause();
        beaconId="0";
        beaconManager.unbind(this);
    }

    @Override 
    protected void onResume() {
        beaconId="0";
        super.onResume();
        beaconManager.bind(this);
    }
    @Override
    public void onBeaconServiceConnect() {

        RangeNotifier rangeNotifier = new RangeNotifier() {
           @Override
           public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
              if (beacons.size() > 0) {
                  Log.d(TAG, "didRangeBeaconsInRegion called with beacon count:  "+beacons.size());
                  Beacon firstBeacon = beacons.iterator().next();
                  logToDisplay("The beacon id: " + firstBeacon.getId1().toString() + " is about " + firstBeacon.getDistance() + " meters away.");
                  beaconId=firstBeacon.getId1().toString();
              }
           }

        };
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            beaconManager.addRangeNotifier(rangeNotifier);
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            beaconManager.addRangeNotifier(rangeNotifier);
        } catch (RemoteException e) {   }
    }

    private void logToDisplay(final String line) {
        runOnUiThread(new Runnable() {
            public void run() {
                EditText editText = (EditText)RangingActivity.this.findViewById(R.id.rangingText);
                editText.setText(line);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseOrganisers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                organiserList.clear();
                for(DataSnapshot organiserSnapshot: snapshot.getChildren()){
                    Organiser organiser=organiserSnapshot.getValue(Organiser.class);
                    organiserList.add(organiser);
                }
                OrganiserList adapter=new OrganiserList(RangingActivity.this,organiserList);
                listViewOrganisers.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}
