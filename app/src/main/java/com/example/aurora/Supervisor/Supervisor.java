package com.example.aurora.Supervisor;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.aurora.R;
import com.example.aurora.databinding.ActivitySupervisorBinding;

public class Supervisor extends AppCompatActivity {

    ActivitySupervisorBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySupervisorBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        replaceFragment(new SupervisorHomeFragmentVista());

        binding.bottomNavigationSupervisor.setOnItemSelectedListener(item -> {
            int menuItemId =  item.getItemId();

            if (menuItemId ==  R.id.home) {
                replaceFragment(new SupervisorHomeFragmentVista());
                return true;
            }
            else if (menuItemId ==  R.id.mensajeria) {
                replaceFragment(new SupervisorMensajeriaFragment());
                return true;
            }
            else if (menuItemId ==  R.id.agregar) {
                replaceFragment(new SupervisorAgregarFragment());
                return true;
            }
            else if (menuItemId ==  R.id.historial) {
                replaceFragment(new SupervisorHistorialFragment());
                return true;
            }
            else if (menuItemId ==  R.id.ajustes) {
                replaceFragment(new SupervisorAjustesFragment());
                return true;
            }
            return true;
        });

    }
    private  void replaceFragment (Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.containerFrame, fragment);
        transaction.commit();

    }

    public void handleScanResult(String scanResult) {
        Toast.makeText(this, "Resultado del escaneo: " + scanResult, Toast.LENGTH_LONG).show();
        // Aquí puedes manejar el resultado del escaneo
    }
}