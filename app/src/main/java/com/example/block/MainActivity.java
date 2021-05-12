package com.example.block;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.role.RoleManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telecom.Call;
import android.telecom.TelecomManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int PHONE_STATE = 1;
    private static final int ANSWER_CALLS = 2;
    private static final int Call_Phone = 3;
    private static final int REQUEST_ID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)requestRole();
        ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_PHONE_STATE }, PHONE_STATE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == PHONE_STATE) {
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ANSWER_PHONE_CALLS }, ANSWER_CALLS);
            } else if (requestCode == ANSWER_CALLS) {
                ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CALL_PHONE }, Call_Phone);
            }else if(requestCode == Call_Phone){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void requestRole() {
        RoleManager roleManager = (RoleManager) getSystemService(ROLE_SERVICE);
        Intent intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING);
        startActivityForResult(intent, REQUEST_ID);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ID) {
            // Your app is now the call screening app
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "app is now the call screening app", Toast.LENGTH_SHORT).show();
            } else {
                // Your app is not the call screening app
                Toast.makeText(this, "app is NOT the call screening app", Toast.LENGTH_SHORT).show();
            }
        }
    }
}