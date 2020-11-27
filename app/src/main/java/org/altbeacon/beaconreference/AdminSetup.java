package org.altbeacon.beaconreference;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminSetup extends Activity {
public static final String GROUP_NAME="groupName";
public static final String GROUP_ID="groupID";
    DatabaseReference databaseOrganisers;
    DatabaseReference databaseGroups;
    EditText editTextOrganiserName, editTextBeaconID,editTextGroupName;
    Button buttonAddOrganiser, buttonAddGroup;
    ListView listViewOrganisers;
    ListView listViewGroups;
    List<Organiser> organiserList;
    List<Group> groupList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_setup);

        databaseOrganisers= FirebaseDatabase.getInstance().getReference("Organisers");
        databaseGroups=FirebaseDatabase.getInstance().getReference("Groups");

        editTextOrganiserName=(EditText) findViewById(R.id.editTextOrganiserName);
        editTextBeaconID=(EditText) findViewById(R.id.editTextOrganiserBeaconId);
        organiserList=new ArrayList<>();
        groupList=new ArrayList<>();
        buttonAddOrganiser=(Button) findViewById(R.id.buttonAddOrganiser);
        listViewOrganisers=(ListView) findViewById(R.id.listViewOrganisers);
        listViewGroups=(ListView) findViewById(R.id.listViewGroups);
        buttonAddGroup=(Button) findViewById(R.id.buttonAddGroup);
        editTextGroupName=(EditText) findViewById(R.id.editTextGroupName);
        listViewGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Group group= groupList.get(i);
                Intent intent=new Intent(getApplicationContext(),AddUser.class);
                intent.putExtra("GROUP_ID",group.getGroupId());
                intent.putExtra("GROUP_NAME",group.getGroupName());
                startActivity(intent);
            }});



    }
    public void onAddOrganiserClicked(View view){
    addOrganiser();
    }

    private void addOrganiser(){
        String organiserName=editTextOrganiserName.getText().toString().trim();
        String beaconId=editTextBeaconID.getText().toString().trim();
        if(!TextUtils.isEmpty(organiserName)&&!TextUtils.isEmpty(beaconId)){
            String organiserId=databaseOrganisers.push().getKey();
            Organiser organiser= new Organiser(organiserId,organiserName,beaconId);
            databaseOrganisers.child(organiserId).setValue(organiser);
            Toast.makeText(this,"Organiser Added!", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this,"Name and Id cannot be empty!", Toast.LENGTH_LONG).show();
        }
    }
    public void onAddGroupClicked(View view){
        addGroup();
    }

    private void addGroup(){
        String groupName=editTextGroupName.getText().toString().trim();
        if(!TextUtils.isEmpty(groupName)){
            String groupId=databaseGroups.push().getKey();
            Group group= new Group(groupId,groupName);
            databaseGroups.child(groupId).setValue(group);
            Toast.makeText(this,"Group Added!", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this,"Name cannot be empty!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
                OrganiserList adapter=new OrganiserList(AdminSetup.this,organiserList);
                listViewOrganisers.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseGroups.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupList.clear();
                for(DataSnapshot groupSnapshot: snapshot.getChildren()){
                    Group group=groupSnapshot.getValue(Group.class);
                    groupList.add(group);
                }
                GroupList adapter=new GroupList(AdminSetup.this,groupList);
                listViewGroups.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
