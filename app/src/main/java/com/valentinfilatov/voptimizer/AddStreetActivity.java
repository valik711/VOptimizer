package com.valentinfilatov.voptimizer;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.valentinfilatov.voptimizer.Adapters.StreetCoordRVAdapter;
import com.valentinfilatov.voptimizer.POJO.Street;
import com.valentinfilatov.voptimizer.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class AddStreetActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private StreetCoordRVAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Button save;
    EditText streetname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_street);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvCoords);
        save = findViewById(R.id.btnSave);
        streetname = findViewById(R.id.etName);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(AddStreetActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(AddStreetActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                }else {
                    saveFile();
                }
            }
        });

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new StreetCoordRVAdapter();
        mRecyclerView.setAdapter(mAdapter);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else {
            checkLocation();
        }

    }

    class GpsLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            mAdapter.addItem(loc.getLatitude(), loc.getLongitude());

        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

    @SuppressLint("MissingPermission")
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    void checkLocation(){
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new GpsLocationListener();

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 3000, 20, locationListener);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                checkLocation();

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
            break;
            case 2:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    saveFile();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    void saveFile(){

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        //String filename = "coords"+System.currentTimeMillis()+".txt";
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "coords.txt");
        FileOutputStream outputStream;

        PrintWriter pw;
        try {
            outputStream = new FileOutputStream(file);
            pw=new PrintWriter(outputStream);
            pw.println(streetname.getText());
            for(String coord: mAdapter.getItems()) pw.write(coord);
            pw.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String filename="contacts_sid.vcf";
        Uri path = Uri.fromFile(file);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
// set the type to 'email'
        emailIntent .setType("vnd.android.cursor.dir/email");
        String to[] = {""};
        emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
// the attachment
        emailIntent .putExtra(Intent.EXTRA_STREAM, path);
// the mail subject
        emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Route for street "+streetname.getText());
        startActivity(Intent.createChooser(emailIntent , "Send email..."));
        //Toast.makeText(this, getCacheDir()+"/coords/", Toast.LENGTH_LONG).show();
    }
}
