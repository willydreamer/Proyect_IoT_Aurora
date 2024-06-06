package com.example.aurora;

import android.os.Bundle;

import com.example.aurora.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.aurora.databinding.ActivitySuperAdminBinding;

public class SuperAdmin extends AppCompatActivity {

    private ActivitySuperAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySuperAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new SuperAdminUsersFragment());

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int menuItemId =  item.getItemId();

            if (menuItemId ==  R.id.navigation_home) {
                replaceFragment(new SuperAdminUsersFragment());
                return true;
            }
            else if (menuItemId ==  R.id.agregar) {
                replaceFragment(new SuperAdminCrearAdministradorFragment());
                return true;
            }
            else if (menuItemId ==  R.id.navigation_dashboard) {
                replaceFragment(new SuperAdminLogsFragment());
                return true;
            }
            else if (menuItemId ==  R.id.navigation_notifications) {
                replaceFragment(new NotificationsFragment());
                return true;
            }
            else if (menuItemId ==  R.id.navigation_message) {
                replaceFragment(new ChatsListFragment());
                return true;
            }
            return true;
        });

    }

    private  void replaceFragment (Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container1, fragment);
        transaction.commit();

    }

}