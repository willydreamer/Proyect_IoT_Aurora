package com.example.aurora;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;


public class AdminSitiosFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.admin_fragment_sitios, container, false);

        ImageButton flecha1 = view.findViewById(R.id.flecha1);
        ImageButton crear = view.findViewById(R.id.bottonCrear);

        flecha1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AdminInformacionSitioActivity.class); // Reemplaza "TuActivity" con el nombre de tu Activity
                startActivity(intent);
            }
        });
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CrearSitioActivity.class); // Reemplaza "TuActivity" con el nombre de tu Activity
                startActivity(intent);
            }
        });
        return view;
    }
}