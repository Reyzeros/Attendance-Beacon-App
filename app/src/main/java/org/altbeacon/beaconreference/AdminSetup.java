package org.altbeacon.beaconreference;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

public class AdminSetup extends Activity {

    EditText editTextOrganiserName, editTextBeaconID,editTextGroupName;
    Button buttonAddOrganiser, buttonAddGroup;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_setup);

    //    editTextOrganiserName=(EditText) findViewById(R.id.editTextOrganiserName);
     //   editTextBeaconID=(EditText) findViewById(R.id.editTextOrganiserBeaconId);
     //   editTextGroupName=(EditText) findViewById(R.id.editTextGroupName);
     //   buttonAddOrganiser=(Button) findViewById(R.id.buttonAddOrganiser);
     //   buttonAddGroup=(Button) findViewById(R.id.buttonAddGroup);
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
