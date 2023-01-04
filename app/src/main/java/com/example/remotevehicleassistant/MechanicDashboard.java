package com.example.remotevehicleassistant;

import android.content.Intent;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.remotevehicleassistant.Model.MechanicModel;

import java.util.List;

public class MechanicDashboard extends AppCompatActivity {
    TextView email,name;
    ImageView profile,viewrequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(R.string.appname);
        setContentView(R.layout.activity_mechanic_dashboard);

        email = findViewById(R.id.mechanicemail);
        name = findViewById(R.id.mechanicname);
        profile = findViewById(R.id.img11);
        viewrequest = findViewById(R.id.mech_request);

        Intent i = getIntent();
        String str = i.getStringExtra("email");
        email.setText(str);

        DatabaseHelper dbhelper = new DatabaseHelper(MechanicDashboard.this);
        List<MechanicModel> all=dbhelper.viewMechanic(str);
        int id = all.get(0).getMid();
        String mid = String.valueOf(id);
        String str1 = all.get(0).getMfname()+" "+all.get(0).getMlname();
        name.setText(str1);
        Bitmap pt = all.get(0).getMprofileimage();
        if(pt == null){

        }
        else{
            profile.setImageBitmap(all.get(0).getMprofileimage());
        }
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MechanicDashboard.this,MechanicProfile.class);
                i.putExtra("email",str);
                startActivity(i);
            }
        });

        viewrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MechanicDashboard.this,ViewMechanicRequest.class);
                i.putExtra("id",mid);
                i.putExtra("email",str);
                i.putExtra("name",str1);
                i.putExtra("photo",pt);
                startActivity(i);
            }
        });

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
                Intent intent = new Intent(MechanicDashboard.this,RVA_HomePage.class);
                startActivity(intent);
                return true;
            case R.id.item2:
                Intent intent1 = new Intent(MechanicDashboard.this,MechanicLogin.class);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}