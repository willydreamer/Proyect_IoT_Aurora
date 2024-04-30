package com.example.aurora;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class HomeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.admin_fragment_home, container, false);

        ImageButton goSupervisores = view.findViewById(R.id.flecha1);
        /*goSupervisores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reemplazar el fragmento actual con el segundo fragmento
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id., new AdminSupervisoresFragment())
                        .addToBackStack(null) // Opcional: agregar el fragmento actual a la pila de retroceso
                        .commit();
            }
        });*/
        return view;
    }
}