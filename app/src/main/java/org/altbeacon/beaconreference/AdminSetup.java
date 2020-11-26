package org.altbeacon.beaconreference;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class AdminSetup extends Activity {

    EditText editTextOrganiserName, editTextBeaconID,editTextGroupName;
    Button buttonAddOrganiser, buttonAddGroup;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_setup);

        editTextOrganiserName=(EditText) findViewById(R.id.editTextOrganiserName);
        editTextBeaconID=(EditText) findViewById(R.id.editTextOrganiserBeaconId);
        editTextGroupName=(EditText) findViewById(R.id.editTextGroupName);
        buttonAddOrganiser=(Button) findViewById(R.id.buttonAddOrganiser);
        buttonAddGroup=(Button) findViewById(R.id.buttonAddGroup);
    }
    public void onAddOrganiserClicked(View view){
    addOrganiser();
    }

    private void addOrganiser(){
        String organiserName=editTextOrganiserName.getText().toString().trim();
        String beaconId=editTextBeaconID.getText().toString().trim();
        if(!TextUtils.isEmpty(organiserName)&&!TextUtils.isEmpty(beaconId)){
        }
        else{
            Toast.makeText(this,"Name and Id cannot be empty!", Toast.LENGTH_LONG).show();
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
}
