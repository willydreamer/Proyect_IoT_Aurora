package com.example.aurora.Supervisor;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.aurora.R;


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
                Fragment secondFragment = new SupervisorListaSitiosFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.containerFrame, secondFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        flecha2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment secondFragment2 = new SupervisorListaEquiposFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.containerFrame, secondFragment2)
                        .addToBackStack(null)
                        .commit();
            }
        });
        flecha3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment secondFragment3 = new SupervisorEstadoEquipoFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.containerFrame, secondFragment3)
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;

    }


}