package com.example.concertmate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.concertmate.Fragments.BaseFragment;
import com.example.concertmate.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
        FirebaseAuth auth;
        ImageView avatar;
        TextView username,email;
        BaseFragment baseFragment;
        DatabaseReference mDatabase;
        User dbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        baseFragment = new BaseFragment();
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
        setContentView(R.layout.activity_main);
        baseFragment.tabFragment(this);
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer =findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView =findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        View headerView = navigationView.getHeaderView(0);
        email = headerView.findViewById(R.id.nav_email_text);
        username = headerView.findViewById(R.id.nav_username_text);
        avatar = headerView.findViewById(R.id.nav_avatar);

        mDatabase.child("Users").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dbUser = dataSnapshot.getValue(User.class);
                username.setText(dbUser.getUsername());
                email.setText(dbUser.getEmail());
                Picasso.get().load(dbUser.getImageUrl()).fit().into(avatar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            baseFragment.profileFragment(this);
        } else if (id == R.id.nav_concerts) {
            baseFragment.tabFragment(this);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_sign_out) {
            auth.signOut();
            startActivity(new Intent(BaseActivity.this, MainLoginActivity.class));
            finish();

        }

        DrawerLayout drawer =findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
