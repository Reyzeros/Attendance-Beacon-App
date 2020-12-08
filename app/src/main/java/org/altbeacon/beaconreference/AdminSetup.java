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
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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
    FirebaseAuth fAuth;
    EditText editTextOrganiserName, editTextBeaconID,editTextGroupName,editTextOrganiserPassword,editTextOrganiserEmail ;
    Button buttonAddOrganiser, buttonAddGroup;
    ListView listViewOrganisers;
    ListView listViewGroups;
    List<Organiser> organiserList;
    List<Group> groupList;
    ProgressBar progressBar;
    private String organiserEmail,organiserPassword,organiserName,beaconId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_setup);
        getWindow().setBackgroundDrawableResource(R.drawable.background);
        databaseOrganisers= FirebaseDatabase.getInstance().getReference("Organisers");
        databaseGroups=FirebaseDatabase.getInstance().getReference("Groups");
        fAuth=FirebaseAuth.getInstance();
        progressBar=(ProgressBar) findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.INVISIBLE);
        editTextOrganiserName=(EditText) findViewById(R.id.editTextOrganiserName);
        editTextBeaconID=(EditText) findViewById(R.id.editTextOrganiserBeaconId);
        editTextOrganiserEmail=(EditText) findViewById(R.id.editTextOrganiserEmail);
        organiserList=new ArrayList<>();
        groupList=new ArrayList<>();
        buttonAddOrganiser=(Button) findViewById(R.id.buttonAddOrganiser);
        listViewOrganisers=(ListView) findViewById(R.id.listViewOrganisers);
        listViewGroups=(ListView) findViewById(R.id.listViewGroups);
        buttonAddGroup=(Button) findViewById(R.id.buttonAddGroup);
        editTextGroupName=(EditText) findViewById(R.id.editTextGroupName);
        editTextOrganiserPassword=(EditText) findViewById(R.id.editTextOrganiserPassword);
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
        progressBar.setVisibility(View.VISIBLE);
         organiserName=editTextOrganiserName.getText().toString().trim();
         organiserEmail=editTextOrganiserEmail.getText().toString().trim();
         organiserPassword=editTextOrganiserPassword.getText().toString().trim();
         beaconId=editTextBeaconID.getText().toString().trim();
        if(!TextUtils.isEmpty(organiserName)&&!TextUtils.isEmpty(beaconId)&&!TextUtils.isEmpty(organiserPassword)&&organiserPassword.length()>=6&&!TextUtils.isEmpty(organiserEmail)){
            fAuth.createUserWithEmailAndPassword(organiserEmail,organiserPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                                                             @Override
                                                                                                             public void onComplete(@NonNull Task<AuthResult> task) {
                                                                                                                 if(task.isSuccessful()){
                                                                                                                     progressBar.setVisibility(View.INVISIBLE);
                                                                                                                     String organiserId=fAuth.getCurrentUser().getUid();
                                                                                                                     Organiser organiser= new Organiser(organiserId,organiserName, organiserEmail, beaconId);
                                                                                                                     databaseOrganisers.child(organiserId).setValue(organiser);
                                                                                                                     Toast.makeText(AdminSetup.this,"Organiser Added!", Toast.LENGTH_LONG).show();
                                                                                                                     editTextOrganiserName.getText().clear();
                                                                                                                     editTextOrganiserEmail.getText().clear();
                                                                                                                     editTextOrganiserPassword.getText().clear();
                                                                                                                     editTextBeaconID.getText().clear();
                                                                                                                     fAuth.signOut();
                                                                                                                 }
                                                                                                                 else {
                                                                                                                     progressBar.setVisibility(View.INVISIBLE);
                                                                                                                 }
                                                                                                             }
                                                                                                         });

        }
        else{
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this,"Name, Email, Password and Id cannot be empty!", Toast.LENGTH_LONG).show();
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
            editTextGroupName.getText().clear();
        }
        else{
            Toast.makeText(this,"Name cannot be empty!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fAuth.signOut();
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
