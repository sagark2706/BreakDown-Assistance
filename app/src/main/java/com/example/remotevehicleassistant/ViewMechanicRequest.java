package com.example.remotevehicleassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.remotevehicleassistant.Adapter.RecycleViewAdapterMechanic;
import com.example.remotevehicleassistant.Adapter.RecycleViewAdapterViewMechanicRequest;
import com.example.remotevehicleassistant.Model.MechanicModel;
import com.example.remotevehicleassistant.Model.RequestModel;

import java.util.List;

public class ViewMechanicRequest extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecycleViewAdapterViewMechanicRequest mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    TextView email,name;
    ImageView profile;
    private SearchView searchmechanicrequest;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(R.string.appname);
        setContentView(R.layout.activity_view_mechanic_request);

        email = findViewById(R.id.mechanicemail1);
        name = findViewById(R.id.mechanicname1);
        profile = findViewById(R.id.img12);
        searchmechanicrequest = findViewById(R.id.search_mechrequest);


        Intent intent = getIntent();
        String str = intent.getStringExtra("id");
        int mechid = Integer.parseInt(str);
        String str1 = intent.getStringExtra("email");
        String str2 = intent.getStringExtra("name");
        Bitmap str3 = intent.getParcelableExtra("photo");
        email.setText(str1);
        name.setText(str2);

        if(str3 == null){

        }
        else{
            profile.setImageBitmap(str3);
        }

        recyclerView=(RecyclerView) findViewById(R.id.recyclerviewmechrequest);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DatabaseHelper dbhelper = new DatabaseHelper(ViewMechanicRequest.this);
        List<RequestModel> all=dbhelper.viewMechanicRequest(mechid);
        mAdapter = new RecycleViewAdapterViewMechanicRequest(all, ViewMechanicRequest.this);
        recyclerView.setAdapter(mAdapter);

        searchmechanicrequest.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
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
                Intent intent = new Intent(ViewMechanicRequest.this,MechanicDashboard.class);
                startActivity(intent);
                return true;
            case R.id.item2:
                Intent intent1 = new Intent(ViewMechanicRequest.this,MechanicLogin.class);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
