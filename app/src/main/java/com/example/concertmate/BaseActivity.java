package com.example.concertmate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.concertmate.Fragments.BaseFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
        FirebaseAuth.AuthStateListener authListener;
        FirebaseUser user;
        FirebaseAuth auth;
        ImageView avatar;
        TextView username,email;
        BaseFragment baseFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        baseFragment = new BaseFragment();
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        //check if by user is still signed in.
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(BaseActivity.this, MainLoginActivity.class));
                    finish();
                }
            }
        };
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

        View headerView = navigationView.getHeaderView(0);
        email = headerView.findViewById(R.id.nav_email_text);
        username = headerView.findViewById(R.id.nav_username_text);
        avatar = headerView.findViewById(R.id.nav_avatar);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

//        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                .setDisplayName(username).setPhotoUri(photoUrl).build();
       // auth.getCurrentUser().updateProfile(profileUpdates);

        Log.i("INFO",user.getPhotoUrl().toString());
        setUsername(user.getDisplayName());
        setEmail(user.getEmail());
        Picasso.get().load(user.getPhotoUrl()).fit().into(avatar);

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

    public void setEmail(String emailText){
        email.setText(emailText);
    }
    public void setUsername(String usernameText){
        username.setText(usernameText);
    }
}
