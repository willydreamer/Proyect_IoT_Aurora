package com.example.aurora.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.aurora.MensajeriaActivity;
import com.example.aurora.R;

public class FragmentHome extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View homeView = inflater.inflate(R.layout.admin_fragment_home, container, false);

        //navegaciones
        ImageButton goSitios = homeView.findViewById(R.id.flecha1);
        goSitios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear una instancia del segundo fragmento
                FragmentSitios sitios = new FragmentSitios();

                // Obtener el FragmentManager y comenzar la transacción
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // Reemplazar el fragmento actual por el segundo fragmento
                fragmentTransaction.replace(R.id.container,sitios);

                // Agregar la transacción a la pila de retroceso (opcional)
                fragmentTransaction.addToBackStack(null);

                // Confirmar la transacción
                fragmentTransaction.commit();
            }
        });

        ImageButton goSupervisores = homeView.findViewById(R.id.flecha2);
        goSupervisores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear una instancia del segundo fragmento
                FragmentSupervisores supervisores = new FragmentSupervisores();

                // Obtener el FragmentManager y comenzar la transacción
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // Reemplazar el fragmento actual por el segundo fragmento
                fragmentTransaction.replace(R.id.container,supervisores);

                // Agregar la transacción a la pila de retroceso (opcional)
                fragmentTransaction.addToBackStack(null);

                // Confirmar la transacción
                fragmentTransaction.commit();
            }
        });

        ImageButton goEquipos = homeView.findViewById(R.id.flecha3);
        goEquipos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear una instancia del segundo fragmento
                FragmentEquipos equipos = new FragmentEquipos();

                // Obtener el FragmentManager y comenzar la transacción
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // Reemplazar el fragmento actual por el segundo fragmento
                fragmentTransaction.replace(R.id.container,equipos);

                // Agregar la transacción a la pila de retroceso (opcional)
                fragmentTransaction.addToBackStack(null);

                // Confirmar la transacción
                fragmentTransaction.commit();
            }
        });

        ImageButton goMensajes = homeView.findViewById(R.id.imageButton2);
        goMensajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //primero crear el intento
                Intent intent = new Intent(getActivity(), MensajeriaActivity.class);
                //iniciar activity
                startActivity(intent);
            }
        });

        return homeView;
        //Con navigation
        /*NavController navController = NavHostFragment.findNavController(HomeFragment.this);




        ImageButton goSitios = homeView.findViewById(R.id.flecha1);
        goSitios.setOnClickListener(view -> {
            navController.navigate(R.id.action_homeFragment_to_adminSitiosFragment);
        });

        ImageButton goSupervisores = homeView.findViewById(R.id.flecha2);
        goSupervisores.setOnClickListener(view -> {
            navController.navigate(R.id.action_homeFragment_to_adminSupervisoresFragment);
        });

        ImageButton goEquipos = homeView.findViewById(R.id.flecha3);
        goEquipos.setOnClickListener(view -> {
            navController.navigate(R.id.action_homeFragment_to_equiposFragment);
        });*/


    }
}