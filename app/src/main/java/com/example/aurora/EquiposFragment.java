package com.example.aurora;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.aurora.databinding.ActivityCrearSitioBinding;

public class EquiposFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View equiposView =  inflater.inflate(R.layout.admin_fragment_equipos, container, false);

        ImageButton botonCrear = equiposView.findViewById(R.id.bottonCrear);
        botonCrear.setOnClickListener(v-> {
            Intent intent = new Intent(getActivity(), CrearEquipoActivity.class);
            startActivity(intent);
        });
        return equiposView;
    }
}