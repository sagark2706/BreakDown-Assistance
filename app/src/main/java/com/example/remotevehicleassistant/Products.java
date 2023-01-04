package com.example.remotevehicleassistant;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.remotevehicleassistant.Adapter.RecycleViewAdapterProduct;
import com.example.remotevehicleassistant.Model.ProductModel;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class Products extends AppCompatActivity  {
    private RecyclerView recyclerView;
    private RecycleViewAdapterProduct mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button checkout;
    ImageView img;
    TextView name, email;
    private SearchView searchProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setTitle(R.string.appname);
        setContentView(R.layout.activity_products);

        checkout = findViewById(R.id.btncheckout);
        img = findViewById(R.id.imageView12);
        name = findViewById(R.id.username1);
        email = findViewById(R.id.useremail1);
        searchProduct = findViewById(R.id.search_userproduct);


        Intent intent = getIntent();
        String str = intent.getStringExtra("id");
        String str1 = intent.getStringExtra("email");
        String str2 = intent.getStringExtra("name");
        Bitmap str3 = intent.getParcelableExtra("photo");
        email.setText(str1);
        name.setText(str2);
        if (str3 == null) {

        } else {
            img.setImageBitmap(str3);
        }

        Intent i = new Intent(Products.this, RecycleViewAdapterProduct.class);
        i.putExtra("id", str);

        recyclerView = (RecyclerView) findViewById(R.id.viewproduct);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayout = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayout);
        DatabaseHelper dbhelper = new DatabaseHelper(Products.this);
        List<ProductModel> all = dbhelper.viewAllProduct();
        mAdapter = new RecycleViewAdapterProduct(all, Products.this);
        recyclerView.setAdapter(mAdapter);

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Products.this, ViewCart.class);
                i.putExtra("id", str);
                i.putExtra("email", str1);
                i.putExtra("name", str2);
                i.putExtra("photo", str3);
                startActivity(i);
            }
        });

        searchProduct.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
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
                Intent intent = new Intent(Products.this,UserDashboard.class);
                startActivity(intent);
                return true;
            case R.id.item2:
                Intent intent1 = new Intent(Products.this,Login.class);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}


