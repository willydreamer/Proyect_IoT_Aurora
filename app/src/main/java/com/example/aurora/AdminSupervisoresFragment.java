package com.example.aurora;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.aurora.Adapter.ListaSupervisoresAdapter;

import java.util.ArrayList;

public class AdminSupervisoresFragment extends Fragment {

    ArrayList<String> listaSupervisores;
    
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_supervisor, container, false);


        Button crearBtn = view.findViewById(R.id.button19);
        crearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CrearSupervisorActivity.class); // Reemplaza "TuActivity" con el nombre de tu Activity
                startActivity(intent);
            }
        });

        recyclerView = view.findViewById(R.id.recyclerview_listasupervisores);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        listaSupervisores = new ArrayList<>();
        listaSupervisores.add("Christian Luis Gonzales Fernández");
        listaSupervisores.add("Willy Huallpa");
        listaSupervisores.add("Alejandro Gutierrez");
        listaSupervisores.add("Dana Nolasco");
        listaSupervisores.add("Alfredo Benavides");


        //seteamos
        ListaSupervisoresAdapter adapter = new ListaSupervisoresAdapter(listaSupervisores);
        recyclerView.setAdapter(adapter);
        return view;


        //Crear instancia adapter
        //ListaSupervisoresAdapter adapter = new ListaSupervisoresAdapter();
        //adapter.setContext(AdminSupervisoresFragment.this);
        //binding.recyclerView.setAdapter(adapter);
        //binding.recyclerView.setLayoutManager(new LinearLayoutManager(AdminSupervisoresFragment.this);

    }

    /*public void irInfoSupervisor(View view) {

        //primero crear el intento
        Intent intent = new Intent(Getactivity.this, InformacionSupervisorActivity.class);
        //iniciar activity
        Log.d("Iot", "si");
        startActivity(intent);
    }*/
}
