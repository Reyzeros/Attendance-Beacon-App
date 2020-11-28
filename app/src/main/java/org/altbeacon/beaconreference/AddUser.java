package org.altbeacon.beaconreference;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import java.util.List;

public class AddUser extends Activity {
    TextView textViewGroupName;
    EditText editTextUserName,editTextUserPassword;
    ListView listViewUsers;
    DatabaseReference databaseUsers;
    List<User> users;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        textViewGroupName=(TextView) findViewById(R.id.textViewGroupNameUser);
        editTextUserName=(EditText) findViewById(R.id.editTextUserName);
        editTextUserPassword=(EditText) findViewById(R.id.editTextUserPassword);
        listViewUsers=(ListView) findViewById(R.id.listViewUsers);


        String id=getIntent().getStringExtra("GROUP_ID");
        String name=getIntent().getStringExtra("GROUP_NAME");
        textViewGroupName.setText(name);
        databaseUsers= FirebaseDatabase.getInstance().getReference("users").child(id);
        users=new ArrayList<>();
    }

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

    public void onAddUserClicked(View view){
        AddUser();
    }

    private void AddUser(){
        String userName=editTextUserName.getText().toString().trim();
        String userPassword=editTextUserPassword.getText().toString().trim();

        if(!TextUtils.isEmpty(userName)&&!TextUtils.isEmpty(userPassword)){
            String id=databaseUsers.push().getKey();
            User user=new User(id,userName,userPassword);
            databaseUsers.child(id).setValue(user);
            Toast.makeText(this,"User Added!", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this,"Name and Password cannot be empty!", Toast.LENGTH_LONG).show();
        }
    }
}
