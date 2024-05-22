package com.example.aurora;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.aurora.databinding.ActivityMainBinding;
import com.example.aurora.databinding.ActivitySupervisorBinding;

public class Supervisor extends AppCompatActivity {

    ActivitySupervisorBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySupervisorBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNavigationSupervisor.setOnItemSelectedListener(item -> {
            int menuItemId =  item.getItemId();

            if (menuItemId ==  R.id.home) {
                replaceFragment(new HomeFragment());
                return true;
            }
            else if (menuItemId ==  R.id.mensajeria) {
                replaceFragment(new EquiposFragment());
                return true;
            }
            else if (menuItemId ==  R.id.supervisor) {
                replaceFragment(new AdminSupervisoresFragment());
                return true;
            }
            else if (menuItemId ==  R.id.configuration) {
                replaceFragment(new ConfigurationFragment());
                return true;
            }
            else if (menuItemId ==  R.id.sites) {
                replaceFragment(new AdminSitiosFragment());
                return true;
            }
            return true;
        });

    }
    private  void replaceFragment (Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();

    }
}