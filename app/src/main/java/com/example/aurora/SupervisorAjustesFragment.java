package com.example.aurora;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class SupervisorAjustesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_supervisor_ajustes, container, false);

        Button cerrarsesion = view.findViewById(R.id.cerrarsesion);

        cerrarsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InicioFragment.class); // Reemplaza "TuActivity" con el nombre de tu Activity
                startActivity(intent);
            }
        });

        return view;

    }
}