package com.valentinfilatov.voptimizer;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    public static final int REQUEST_AUTH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() == null){
            Intent i = new Intent(this, LoginActivity.class);
            startActivityForResult(i, REQUEST_AUTH);
        }else{
            startService();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_AUTH: {
                if(resultCode == RESULT_OK){
                    Toast.makeText(this, "Hello, " + data.getStringExtra("username"), Toast.LENGTH_SHORT).show();
                    startService();
                }
            }
            break;
        }
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void startService(){
        Intent startServiceIntent = new Intent(this, GpsService.class);
        startServiceIntent.putExtra("username", mAuth.getCurrentUser().getEmail());
        if(!isServiceRunning(GpsService.class))startService(startServiceIntent);
    }
}
