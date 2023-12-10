package com.example.googlemap;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.googlemap.databinding.ActivityHomeBinding;
import com.example.googlemap.fragment.MarkersScreen;
import com.example.googlemap.fragment.SearchScreen;
import com.example.googlemap.fragment.ProfileScreen;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity  {

    ActivityHomeBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ReplaceFragment(new MarkersScreen());

        binding.navBottomBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.addLocker:
                        ReplaceFragment(new SearchScreen());
                        break;

                    case R.id.manage:
                        ReplaceFragment(new MarkersScreen());
                        break;


                    case R.id.Profile:
                        ReplaceFragment(new ProfileScreen());
                        break;

                }
                return true;
            }
        });
    }

    private void ReplaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();

    }
}