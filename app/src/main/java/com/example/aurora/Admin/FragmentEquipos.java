package com.example.aurora.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Adapter.ListaEquiposAdapterAdmin;
import com.example.aurora.Bean.EquipoAdmin;
import com.example.aurora.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class FragmentEquipos extends Fragment {

    ArrayList<EquipoAdmin> listaRouters;
    ArrayList<EquipoAdmin> listaSwitches;

    ArrayList<EquipoAdmin> listaPaneles;

    ArrayList<EquipoAdmin> listaRectificadores;

    ArrayList<EquipoAdmin> listaGabinetes;
    RecyclerView recyclerView;
    RecyclerView recyclerView2;

    RecyclerView recyclerView3;

    RecyclerView recyclerView4;

    RecyclerView recyclerView5;
    FirebaseFirestore db;
    ListaEquiposAdapterAdmin adapter;

    ListaEquiposAdapterAdmin adapter2;

    ListaEquiposAdapterAdmin adapter3;

    ListaEquiposAdapterAdmin adapter4;

    ListaEquiposAdapterAdmin adapter5;

    androidx.appcompat.widget.SearchView buscador;

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
        adapter = new ListaEquiposAdapterAdmin();
        adapter.setContext(getContext());
        adapter.setListaEquipos(listaRouters);
        recyclerView.setAdapter(adapter);

        obtenerEquiposRouterDeFirestore();

        //cargar switches

        recyclerView2 = equiposView.findViewById(R.id.recyclerViewEquipos2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        listaSwitches = new ArrayList<>();

        // Configurar el adapter y asociarlo al RecyclerView
        adapter2 = new ListaEquiposAdapterAdmin();
        adapter2.setContext(getContext());
        adapter2.setListaEquipos(listaSwitches);
        recyclerView2.setAdapter(adapter2);

        obtenerEquiposSwitchesDeFirestore();

        //cargar paneles

        recyclerView3 = equiposView.findViewById(R.id.recyclerViewEquipos3);
        recyclerView3.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        listaPaneles = new ArrayList<>();

        // Configurar el adapter y asociarlo al RecyclerView
        adapter3 = new ListaEquiposAdapterAdmin();
        adapter3.setContext(getContext());
        adapter3.setListaEquipos(listaPaneles);
        recyclerView3.setAdapter(adapter3);

        obtenerEquiposPanelesDeFirestore();

        //cargar rectificadores

        recyclerView4 = equiposView.findViewById(R.id.recyclerViewEquipos4);
        recyclerView4.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        listaRectificadores = new ArrayList<>();

        // Configurar el adapter y asociarlo al RecyclerView
        adapter4 = new ListaEquiposAdapterAdmin();
        adapter4.setContext(getContext());
        adapter4.setListaEquipos(listaRectificadores);
        recyclerView4.setAdapter(adapter4);

        obtenerEquiposRectificadoresDeFirestores();


        //cargar gabinetes

        recyclerView5 = equiposView.findViewById(R.id.recyclerViewEquipos5);
        recyclerView5.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        listaGabinetes= new ArrayList<>();

        // Configurar el adapter y asociarlo al RecyclerView
        adapter5 = new ListaEquiposAdapterAdmin();
        adapter5.setContext(getContext());
        adapter5.setListaEquipos(listaGabinetes);
        recyclerView4.setAdapter(adapter5);

        obtenerEquiposGabinetesDeFirestore();

        SearchView buscadorEquipos =  equiposView.findViewById(R.id.buscadorEquipos);
        buscadorEquipos.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                buscarEquipoPorIDRouter(newText);
                buscarEquipoPorIDSwitch(newText);
                buscarEquipoPorIDRectificador(newText);
                buscarEquipoPorIDPanel(newText);
                return true;
            }
        });


        FloatingActionButton botonCrear = equiposView.findViewById(R.id.bottonCrear);
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
                            EquipoAdmin equipo = document.toObject(EquipoAdmin.class);
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
                            EquipoAdmin equipo = document.toObject(EquipoAdmin.class);
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

    private void obtenerEquiposPanelesDeFirestore() {
        db.collection("equipos")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            EquipoAdmin equipo = document.toObject(EquipoAdmin.class);
                            if(equipo.getTipoDeEquipo().equals("Panel de Energia")) {
                                listaPaneles.add(equipo);
                            }
                        }
                        if(listaPaneles.size()>=1){
                            recyclerView3.setVisibility(true ? View.VISIBLE : View.GONE);
                        }
                        adapter3.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al obtener los sitios", Toast.LENGTH_SHORT).show();
                });
    }

    private void obtenerEquiposRectificadoresDeFirestores() {
        db.collection("equipos")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            EquipoAdmin equipo = document.toObject(EquipoAdmin.class);
                            if(equipo.getTipoDeEquipo().equals("Rectificador")) {
                                listaRectificadores.add(equipo);
                            }
                        }
                        if(listaRectificadores.size()>=1){
                            recyclerView4.setVisibility(true ? View.VISIBLE : View.GONE);
                        }
                        adapter4.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al obtener los rectificadores", Toast.LENGTH_SHORT).show();
                });
    }

    private void obtenerEquiposGabinetesDeFirestore() {
        db.collection("equipos")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            EquipoAdmin equipo = document.toObject(EquipoAdmin.class);
                            if(equipo.getTipoDeEquipo().equals("Gabinete")) {
                                listaGabinetes.add(equipo);
                            }
                        }
                        if(listaGabinetes.size()>=1){
                            recyclerView5.setVisibility(true ? View.VISIBLE : View.GONE);
                        }
                        adapter5.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al obtener los sitios", Toast.LENGTH_SHORT).show();
                });
    }

    private void buscarEquipoPorIDRouter(String searchText) {
        if (searchText.isEmpty()) {
            adapter.updateList(listaRouters);
            return;
        }

        db.collection("equipos")
                .orderBy("numeroDeSerie")
                .startAt(searchText)
                .endAt(searchText + "\uf8ff")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<EquipoAdmin> filteredList = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            EquipoAdmin equipoAdmin = document.toObject(EquipoAdmin.class);
                            if(equipoAdmin.getTipoDeEquipo().equals("Router")){
                                filteredList.add(equipoAdmin);
                            }
                        }
                        adapter.updateList(filteredList);
                    }
                });
    }

    private void buscarEquipoPorIDSwitch(String searchText) {
        if (searchText.isEmpty()) {
            adapter2.updateList(listaSwitches);
            return;
        }

        db.collection("equipos")
                .orderBy("numeroDeSerie")
                .startAt(searchText)
                .endAt(searchText + "\uf8ff")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<EquipoAdmin> filteredList = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            EquipoAdmin equipoAdmin = document.toObject(EquipoAdmin.class);
                            if(equipoAdmin.getTipoDeEquipo().equals("Switch")){
                                filteredList.add(equipoAdmin);
                            }
                        }
                        adapter2.updateList(filteredList);
                    }
                });
    }

    private void buscarEquipoPorIDRectificador(String searchText) {
        if (searchText.isEmpty()) {
            adapter3.updateList(listaRectificadores);
            return;
        }

        db.collection("equipos")
                .orderBy("numeroDeSerie")
                .startAt(searchText)
                .endAt(searchText + "\uf8ff")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<EquipoAdmin> filteredList = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            EquipoAdmin equipoAdmin = document.toObject(EquipoAdmin.class);
                            if(equipoAdmin.getTipoDeEquipo().equals("Rectificador")){
                                filteredList.add(equipoAdmin);
                            }
                        }
                        adapter3.updateList(filteredList);
                    }
                });
    }

    private void buscarEquipoPorIDPanel(String searchText) {
        if (searchText.isEmpty()) {
            adapter4.updateList(listaPaneles);
            return;
        }

        db.collection("equipos")
                .orderBy("numeroDeSerie")
                .startAt(searchText)
                .endAt(searchText + "\uf8ff")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<EquipoAdmin> filteredList = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            EquipoAdmin equipoAdmin = document.toObject(EquipoAdmin.class);
                            if(equipoAdmin.getTipoDeEquipo().equals("Panel de Energia")){
                                filteredList.add(equipoAdmin);
                            }
                        }
                        adapter4.updateList(filteredList);
                    }
                });
    }

}