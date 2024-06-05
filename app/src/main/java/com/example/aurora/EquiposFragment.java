package com.example.aurora;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Adapter.ListaEquiposAdapter;
import com.example.aurora.Bean.Equipo;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class EquiposFragment extends Fragment {

    ArrayList<Equipo> listaRouters;

    ArrayList<Equipo> listaSwitches;
    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    FirebaseFirestore db;
    ListaEquiposAdapter adapter;

    ListaEquiposAdapter adapter2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View equiposView =  inflater.inflate(R.layout.admin_fragment_equipos, container, false);



        //cargar routers

        recyclerView = equiposView.findViewById(R.id.recyclerViewEquipos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        db = FirebaseFirestore.getInstance();
        listaRouters = new ArrayList<>();

        // Configurar el adapter y asociarlo al RecyclerView
        adapter = new ListaEquiposAdapter();
        adapter.setContext(getContext());
        adapter.setListaEquipos(listaRouters);
        recyclerView.setAdapter(adapter);

        obtenerEquiposRouterDeFirestore();

        //cargar switches

        recyclerView2 = equiposView.findViewById(R.id.recyclerViewEquipos2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        listaSwitches = new ArrayList<>();

        // Configurar el adapter y asociarlo al RecyclerView
        adapter2 = new ListaEquiposAdapter();
        adapter2.setContext(getContext());
        adapter2.setListaEquipos(listaSwitches);
        recyclerView2.setAdapter(adapter2);

        obtenerEquiposSwitchesDeFirestore();


        ImageButton botonCrear = equiposView.findViewById(R.id.bottonCrear);
        botonCrear.setOnClickListener(v-> {
            Intent intent = new Intent(getActivity(), CrearEquipoActivity.class);
            startActivity(intent);
        });
        return equiposView;
    }


    private void obtenerEquiposRouterDeFirestore() {
        db.collection("equipos")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Equipo equipo = document.toObject(Equipo.class);
                            if(equipo.getTipoDeEquipo().equals("Router")) {
                                listaRouters.add(equipo);
                            }
                        }
                        adapter.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al obtener los sitios", Toast.LENGTH_SHORT).show();
                });
    }

    private void obtenerEquiposSwitchesDeFirestore() {
        db.collection("equipos")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Equipo equipo = document.toObject(Equipo.class);
                            if(equipo.getTipoDeEquipo().equals("Switch")) {
                                listaSwitches.add(equipo);
                            }
                        }
                        adapter2.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al obtener los sitios", Toast.LENGTH_SHORT).show();
                });
    }
}