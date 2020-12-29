package org.altbeacon.beaconapp;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddUser extends Activity {
    TextView textViewGroupName;
    EditText editTextUserName,editTextUserPassword, editTextUserEmail;
    ListView listViewUsers;
    DatabaseReference databaseUsers;
    FirebaseAuth fAuth;
    List<User> users;
    ProgressBar progressBar;
    private String userName,userPassword,userEmail,groupId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        getWindow().setBackgroundDrawableResource(R.drawable.background);
        textViewGroupName=(TextView) findViewById(R.id.textViewGroupNameUser);
        editTextUserName=(EditText) findViewById(R.id.editTextUserName);
        editTextUserPassword=(EditText) findViewById(R.id.editTextUserPassword);
        editTextUserEmail=(EditText) findViewById(R.id.editTextUserEmail);
        listViewUsers=(ListView) findViewById(R.id.listViewUsers);
        progressBar=(ProgressBar) findViewById(R.id.progressBar);
        fAuth=FirebaseAuth.getInstance();
        userName="";
        userPassword="";
        userEmail="";


        groupId=getIntent().getStringExtra("GROUP_ID");
        String name=getIntent().getStringExtra("GROUP_NAME");
        textViewGroupName.setText(name);
        databaseUsers= FirebaseDatabase.getInstance().getReference("Users");
        users=new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query=FirebaseDatabase.getInstance().getReference("Users").orderByChild("groupId").equalTo(groupId);
        query.addValueEventListener(new ValueEventListener() {
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

    public void onAddUserClicked(View view){
        AddUser();
    }

    private void AddUser(){

         userName=editTextUserName.getText().toString().trim();
         userPassword=editTextUserPassword.getText().toString().trim();
         userEmail=editTextUserEmail.getText().toString().trim();

        if(!TextUtils.isEmpty(userName)&&!TextUtils.isEmpty(userPassword)&&!TextUtils.isEmpty(userEmail)&&userPassword.length()>=6){
            progressBar.setVisibility(View.VISIBLE);
            fAuth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                          String userId=fAuth.getCurrentUser().getUid();
                        progressBar.setVisibility(View.INVISIBLE);
                        User user=new User(userId,userName,userEmail,groupId);
                        databaseUsers.child(userId).setValue(user);
                        Toast.makeText(AddUser.this,"User Added!", Toast.LENGTH_LONG).show();
                        editTextUserName.getText().clear();
                        editTextUserPassword.getText().clear();
                        editTextUserEmail.getText().clear();
                        fAuth.signOut();
                      }
                    else {
                        Toast.makeText(AddUser.this,"Error"+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }}
            });
        }

        else{
            Toast.makeText(this,"Name and Password cannot be empty!", Toast.LENGTH_LONG).show();
        }
    }
}
