package com.example.aurora.Admin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.aurora.R;
import com.example.aurora.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        replaceFragment(new FragmentHome());

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int menuItemId =  item.getItemId();

            if (menuItemId ==  R.id.home) {
                replaceFragment(new FragmentHome());
                return true;
            }
            else if (menuItemId ==  R.id.equipos) {
                replaceFragment(new FragmentEquipos());
                return true;
            }
            else if (menuItemId ==  R.id.supervisor) {
                replaceFragment(new FragmentSupervisores());
                return true;
            }
            else if (menuItemId ==  R.id.configuration) {
                replaceFragment(new FragmentConfiguration());
                return true;
            }
            else if (menuItemId ==  R.id.sites) {
                replaceFragment(new FragmentSitios());
                return true;
            }
            return true;
        });

        String fragmentToLoad = getIntent().getStringExtra("fragment");
        if (fragmentToLoad != null && fragmentToLoad.equals("sitios")) {
            replaceFragment(new FragmentSitios());
        }

    }
    private  void replaceFragment (Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();

    }
}