package com.example.instaclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView nav;
    private Fragment selectorFrag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nav=findViewById(R.id.m_bottom_nav);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home :
                        selectorFrag=new HomeFragment();
                        break;
                    case R.id.heart:
                        selectorFrag=new NotificationFragment();
                        break;
                    case R.id.profile:
                        selectorFrag=new ProfileFragment();
                        break;
                    case R.id.search:
                        selectorFrag=new SearchFragment();
                        break;
                    case R.id.add :
                        startActivity(new Intent(MainActivity.this,PostActivity.class));
                        break;

                }
                if(selectorFrag!=null)
                    getSupportFragmentManager().beginTransaction().replace(R.id.m_contain,selectorFrag).commit();
                return true;
            }
        });

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            String profileId = intent.getString("publisherId");

            getSharedPreferences("PROFILE", MODE_PRIVATE).edit().putString("profileId", profileId).apply();

            getSupportFragmentManager().beginTransaction().replace(R.id.m_contain, new ProfileFragment()).commit();
            nav.setSelectedItemId(R.id.profile);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.m_contain , new HomeFragment()).commit();
        }
    }

}