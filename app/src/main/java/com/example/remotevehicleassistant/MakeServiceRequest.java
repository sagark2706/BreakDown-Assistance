package com.example.remotevehicleassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
//imported by niranjan


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import fr.ganfra.materialspinner.MaterialSpinner;

// import completed
import com.example.remotevehicleassistant.Adapter.RecycleViewAdapterRequestMechanic;
import com.example.remotevehicleassistant.Model.RequestModel;
import com.example.remotevehicleassistant.Model.UserModel;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//import fr.ganfra.materialspinner.MaterialSpinner;

public class MakeServiceRequest extends AppCompatActivity {
    //MaterialSpinner vehicletype;
TextInputEditText sname,vehicleno,scharges,Ulocation,mechanic,desc;
Button getlocation,makerequest,getmechanic;
int PERMISSION_ID =44;
    TextView name,email,vehicletype;
    ImageView img;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    TextView txtLat;
    //String latitude,longitude;
    String provider;
    protected String latitude,longitude;
    protected boolean gps_enabled,network_enabled;
    FusedLocationProviderClient mFusedLocationClient;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(R.string.appname);
        setContentView(R.layout.activity_make_service_request);

        img=findViewById(R.id.imageView16);
        name=findViewById(R.id.username5);
        email=findViewById(R.id.useremail5);

        sname=findViewById(R.id.request_servicename);
        vehicleno=findViewById(R.id.request_vehicleno);
        vehicletype=findViewById(R.id.bikeTxt);
        scharges=findViewById(R.id.request_charges);
        Ulocation=findViewById(R.id.request_location);
        getlocation=findViewById(R.id.request_getlocation);
        desc=findViewById(R.id.request_description);
        makerequest=findViewById(R.id.request_make);
        mechanic=findViewById(R.id.request_mechanic);
        getmechanic=findViewById(R.id.request_getmechanic);
//        Location Section
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, (float) 0, (LocationListener) this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();


        desc.setSelection(0);

        Intent intent = getIntent();
        String sid = intent.getStringExtra("id");
        String snm = intent.getStringExtra("name");
        String sch = intent.getStringExtra("charges");
        String uemail = intent.getStringExtra("useremail");
        String mechid = intent.getStringExtra("mechid");
        String fname = intent.getStringExtra("fname");
        String lname = intent.getStringExtra("lname");
       // String latitude = intent.getStringExtra("latitude");
    //    String longitude = intent.getStringExtra("longitude");

        if((fname==null) && (lname==null))
        {
            mechanic.setText("");
        }else{
            mechanic.setText(fname+" "+lname);
        }
        if((latitude==null) && (longitude==null))
        {
           // Ulocation.setText("");
        }else{
            //Ulocation.setText(latitude+","+longitude);
        }

        DatabaseHelper dbhelper = new DatabaseHelper(MakeServiceRequest.this);
        List<UserModel> all=dbhelper.viewAllUser(uemail);
        String vno = all.get(0).getVehicleno();
        int userid = all.get(0).getId();
        String uid = String.valueOf(userid);
        String str2 = all.get(0).getFname()+""+all.get(0).getLname();
        Bitmap str3 = all.get(0).getPhoto();
        email.setText(uemail);
        name.setText(str2);

        if(str3 == null){

        }
        else{
            img.setImageBitmap(str3);
        }

        vehicleno.setText(vno);
        sname.setText(snm);
        scharges.setText(sch);

        getmechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MakeServiceRequest.this,ViewRequestMechanic.class);
                i.putExtra("id",uid);
                i.putExtra("sid",sid);
                System.out.println(sid);
                i.putExtra("email",uemail);
                i.putExtra("name",str2);
                i.putExtra("photo",str3);
                startActivity(i);
            }
        });

        getlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MakeServiceRequest.this,MapsActivity.class);
                i.putExtra("id",sid);
                i.putExtra("name",snm);
                i.putExtra("charges",sch);
                i.putExtra("useremail",uemail);
                i.putExtra("mechid",mechid);
                i.putExtra("fname",fname);
                i.putExtra("lname",lname);
                startActivity(i);
            }
        });

        makerequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String currentDateandTime = sdf.format(new Date());

                RequestModel requestModel;
                DatabaseHelper DB = new DatabaseHelper(MakeServiceRequest.this);
                String mnm=mechanic.getText().toString();
                String vn=vehicleno.getText().toString();
                String vt=  vehicletype.getText().toString();
                String snm=sname.getText().toString();
                String sch=scharges.getText().toString();
                String loc=Ulocation.getText().toString();
                String des=desc.getText().toString();

                if(TextUtils.isEmpty(mnm) || TextUtils.isEmpty(vn) || TextUtils.isEmpty(vt) || TextUtils.isEmpty(snm) || TextUtils.isEmpty(sch) || TextUtils.isEmpty(loc) || TextUtils.isEmpty(des))
                    Toast.makeText(MakeServiceRequest.this,"Some fields are empty..",Toast.LENGTH_SHORT).show();
                else{
                    requestModel = new RequestModel(-1,Integer.parseInt(mechid), userid, vn, vt,snm,sch,loc,des,currentDateandTime,false);
                            boolean success = DB.makeRequest(requestModel);
                            if(success==true){
                                Intent i = new Intent(MakeServiceRequest.this,UserDashboard.class);
                                startActivity(i);
                                Toast.makeText(MakeServiceRequest.this, "Request Made Successfully...!", Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(MakeServiceRequest.this, "Request Failed...!", Toast.LENGTH_SHORT).show();
                            }
                }
            }
        });

    }
    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if(checkPermissions())
        {
            if (isLocationEnabled())
            {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if(location == null){
                            requestNewLocationData();
                        }
                        else
                        {
                            Ulocation.setText(location.getLongitude()+"");
                            Ulocation.setText(location.getLatitude()+","+Ulocation.getText().toString());

                        }
                    }
                });
            }
            else
            {
                Toast.makeText(this,"Please turn on location ",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        }
        else
        {
            requestPermissions();
        }
    }
    @SuppressLint("MissingPermissions")
    private void requestNewLocationData()
    {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,mLocationCallback,Looper.myLooper());

    }
    private  LocationCallback mLocationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult)
        {
            Location mLastLocation = locationResult.getLastLocation();
            Ulocation.setText(mLastLocation.getLongitude()+"");
            Ulocation.setText(mLastLocation.getLatitude()+Ulocation.getText().toString());


        }
    };
    private boolean checkPermissions(){
        return ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED;


    }
    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void requestPermissions()
    {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);

    }
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.item1:
                Intent intent = new Intent(MakeServiceRequest.this,UserDashboard.class);
                startActivity(intent);
                return true;
            case R.id.item2:
                Intent intent1 = new Intent(MakeServiceRequest.this,Login.class);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}