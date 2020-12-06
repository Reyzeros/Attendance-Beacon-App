package org.altbeacon.beaconreference;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Region;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collection;


public class MonitoringActivity extends Activity  {
	protected static final String TAG = "MonitoringActivity";
	private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
	private static final int PERMISSION_REQUEST_BACKGROUND_LOCATION = 2;
	public String beaconID="defaultID";
	TextView textViewInvalid;
	EditText editTextTextNameLogin,editTextTextPasswordLogin;
	RadioButton radioButtonUserLogin, radioButtonOrganiserLogin, radioButtonAdminLogin;
	Button buttonLogin;
	ProgressBar progressBar;
	int choice;
	private String email,password;
	FirebaseAuth fAuth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monitoring);

		progressBar=(ProgressBar) findViewById(R.id.progressBar2);
		progressBar.setVisibility(View.INVISIBLE);
		editTextTextNameLogin=(EditText) findViewById(R.id.editTextTextNameLogin);
		editTextTextPasswordLogin=(EditText) findViewById(R.id.editTextTextPasswordLogin);
		radioButtonUserLogin=(RadioButton) findViewById(R.id.radioButtonUserLogin);
		radioButtonOrganiserLogin=(RadioButton) findViewById(R.id.radioButtonOrganiserLogin);
		radioButtonAdminLogin=(RadioButton) findViewById(R.id.radioButtonAdminLogin);
		buttonLogin=(Button) findViewById(R.id.buttonLogin);
		radioButtonUserLogin.setChecked(true);
		fAuth= FirebaseAuth.getInstance();
		choice=1;
		email="";
		password="";
		verifyBluetooth();



		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
					== PackageManager.PERMISSION_GRANTED) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
					if (this.checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
							!= PackageManager.PERMISSION_GRANTED) {
						if (!this.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
							final AlertDialog.Builder builder = new AlertDialog.Builder(this);
							builder.setTitle("This app needs background location access");
							builder.setMessage("Please grant location access so this app can detect beacons in the background.");
							builder.setPositiveButton(android.R.string.ok, null);
							builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

								@TargetApi(23)
								@Override
								public void onDismiss(DialogInterface dialog) {
									requestPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
											PERMISSION_REQUEST_BACKGROUND_LOCATION);
								}

							});
							builder.show();
						}
						else {
							final AlertDialog.Builder builder = new AlertDialog.Builder(this);
							builder.setTitle("Functionality limited");
							builder.setMessage("Since background location access has not been granted, this app will not be able to discover beacons in the background.  Please go to Settings -> Applications -> Permissions and grant background location access to this app.");
							builder.setPositiveButton(android.R.string.ok, null);
							builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

								@Override
								public void onDismiss(DialogInterface dialog) {
								}

							});
							builder.show();
						}
					}
				}
			} else {
				if (!this.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
					requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
									Manifest.permission.ACCESS_BACKGROUND_LOCATION},
							PERMISSION_REQUEST_FINE_LOCATION);
				}
				else {
					final AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("Functionality limited");
					builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons.  Please go to Settings -> Applications -> Permissions and grant location access to this app.");
					builder.setPositiveButton(android.R.string.ok, null);
					builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

						@Override
						public void onDismiss(DialogInterface dialog) {
						}

					});
					builder.show();
				}

			}
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		switch (requestCode) {
			case PERMISSION_REQUEST_FINE_LOCATION: {
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Log.d(TAG, "fine location permission granted");
				} else {
					final AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("Functionality limited");
					builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons.");
					builder.setPositiveButton(android.R.string.ok, null);
					builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

						@Override
						public void onDismiss(DialogInterface dialog) {
						}

					});
					builder.show();
				}
				return;
			}
			case PERMISSION_REQUEST_BACKGROUND_LOCATION: {
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Log.d(TAG, "background location permission granted");
				} else {
					final AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("Functionality limited");
					builder.setMessage("Since background location access has not been granted, this app will not be able to discover beacons when in the background.");
					builder.setPositiveButton(android.R.string.ok, null);
					builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

						@Override
						public void onDismiss(DialogInterface dialog) {
						}

					});
					builder.show();
				}
				return;
			}
		}
	}

	public void onRangingClicked(View view) {
		Intent myIntent = new Intent(this, RangingActivity.class);
		myIntent.putExtra("beaconID",beaconID);
		this.startActivity(myIntent);
	}
	public void onAdminSetupClicked(View view){
		Intent myIntent = new Intent(this, AdminSetup.class);
		this.startActivity(myIntent);
	}
	public void onOrganiserSetupClicked(View view){
		Intent myIntent = new Intent(this, OrganiserSetup.class);
		this.startActivity(myIntent);
	}
	public void onEnableClicked(View view) {
		BeaconApplication application = ((BeaconApplication) this.getApplicationContext());
		if (BeaconManager.getInstanceForApplication(this).getMonitoredRegions().size() > 0) {
			application.disableMonitoring();
			((Button)findViewById(R.id.enableButton)).setText("Re-Enable Monitoring");
		}
		else {
			((Button)findViewById(R.id.enableButton)).setText("Disable Monitoring");
			application.enableMonitoring();
		}

	}
	public void onRadioButtonUserLoginClicked(View view){
		radioButtonOrganiserLogin.setChecked(false);
		radioButtonAdminLogin.setChecked(false);
		choice=1;

	}
	public void onRadioButtonOrganiserLoginClicked(View view){
		radioButtonUserLogin.setChecked(false);
		radioButtonAdminLogin.setChecked(false);
		choice=2;
	}
	public void onRadioButtonAdminLoginClicked(View view){
		radioButtonOrganiserLogin.setChecked(false);
		radioButtonUserLogin.setChecked(false);
		choice=3;
	}
	public void onButtonLoginClicked(View view){
		loginTo();
	}



	private void loginTo(){
		progressBar.setVisibility(View.VISIBLE);
		email=editTextTextNameLogin.getText().toString().trim();
		password=editTextTextPasswordLogin.getText().toString().trim();

		if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)&&password.length()>=6){
fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
	@Override
	public void onComplete(@NonNull Task<AuthResult> task) {
		if(task.isSuccessful()){
			progressBar.setVisibility(View.INVISIBLE);
			Toast.makeText(MonitoringActivity.this,"Logged in Successfully!", Toast.LENGTH_LONG).show();
			String userId=fAuth.getCurrentUser().getUid();
			Intent myIntent = new Intent(MonitoringActivity.this, RangingActivity.class);
			myIntent.putExtra("USER_ID",userId);
			MonitoringActivity.this.startActivity(myIntent);
		}
		else{
			progressBar.setVisibility(View.INVISIBLE);
			Toast.makeText(MonitoringActivity.this,"Error"+task.getException().getMessage(), Toast.LENGTH_LONG).show();
		}

	}
});
		}
		else {
			progressBar.setVisibility(View.INVISIBLE);
			Toast.makeText(this, "Name and Password cannot be empty!", Toast.LENGTH_LONG).show();
		}
	}


    @Override
    public void onResume() {
        super.onResume();
        BeaconApplication application = ((BeaconApplication) this.getApplicationContext());
        application.setMonitoringActivity(this);
        updateLog(application.getLog());
    }

    @Override
    public void onPause() {
        super.onPause();
        ((BeaconApplication) this.getApplicationContext()).setMonitoringActivity(null);
    }

	private void verifyBluetooth() {

		try {
			if (!BeaconManager.getInstanceForApplication(this).checkAvailability()) {
				final AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Bluetooth not enabled");
				builder.setMessage("Please enable bluetooth in settings and restart this application.");
				builder.setPositiveButton(android.R.string.ok, null);
				builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						//finish();
			            //System.exit(0);
					}
				});
				builder.show();
			}
		}
		catch (RuntimeException e) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Bluetooth LE not available");
			builder.setMessage("Sorry, this device does not support Bluetooth LE.");
			builder.setPositiveButton(android.R.string.ok, null);
			builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					//finish();
		            //System.exit(0);
				}

			});
			builder.show();

		}

	}

    public void updateLog(final String log) {
    	runOnUiThread(new Runnable() {
    	    public void run() {
    	    	EditText editText = (EditText)MonitoringActivity.this
    					.findViewById(R.id.monitoringText);
       	    	editText.setText(log);
    	    }
    	});
    }

}
