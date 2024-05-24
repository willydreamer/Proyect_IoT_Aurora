package com.example.aurora;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class SupervisorHomeFragmentVista extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_supervisor_home_vista, container, false);

        ImageButton flecha1 = view.findViewById(R.id.flecha1);
        ImageButton flecha2 = view.findViewById(R.id.flecha2);
        ImageButton flecha3 = view.findViewById(R.id.flecha3);

        flecha1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SupervisorListaDeEquipos.class); // Reemplaza "TuActivity" con el nombre de tu Activity
                startActivity(intent);
            }
        });
        flecha2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SupervisorListaSupervisores.class); // Reemplaza "TuActivity" con el nombre de tu Activity
                startActivity(intent);
            }
        });
        flecha3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SupervisorListaSitios.class); // Reemplaza "TuActivity" con el nombre de tu Activity
                startActivity(intent);
            }
        });
        return view;

    }
}