package com.example.aurora.Supervisor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Adapter.ListaEquiposAdapter;
import com.example.aurora.Adapter.ListaEquiposAdapterAdmin;
import com.example.aurora.Adapter.ListaEquiposAdapterSupervisor;
import com.example.aurora.Adapter.ListaEquiposAdapterSupervisor2;
import com.example.aurora.Adapter.ListaSitiosAdapter;
import com.example.aurora.Admin.CrearEquipoActivity;
import com.example.aurora.Bean.Equipo;
import com.example.aurora.Bean.EquipoAdmin;
import com.example.aurora.Bean.Sitio;
import com.example.aurora.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.checkerframework.common.subtyping.qual.Bottom;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SupervisorListaDeEquipos extends AppCompatActivity {

    ArrayList<EquipoAdmin> listaRouters;
    ArrayList<EquipoAdmin> listaSwitches;
    ArrayList<EquipoAdmin> listaPanel;
    ArrayList<EquipoAdmin> listaRectificador;
    ArrayList<EquipoAdmin> listaOtros;
    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    RecyclerView recyclerView3;
    RecyclerView recyclerView4;
    RecyclerView recyclerView5;
    FirebaseFirestore db;
    ListaEquiposAdapterSupervisor2 adapter;

    ListaEquiposAdapterSupervisor2 adapter2;
    ListaEquiposAdapterSupervisor2 adapter3;

    ListaEquiposAdapterSupervisor2 adapter4;
    ListaEquiposAdapterSupervisor2 adapter5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_supervisor_lista_de_equipos);

        // Routers
        recyclerView = findViewById(R.id.recyclerViewEquipos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        db = FirebaseFirestore.getInstance();
        listaRouters = new ArrayList<>();
        adapter = new ListaEquiposAdapterSupervisor2();
        adapter.setContext(this);
        adapter.setListaEquipos(listaRouters);
        recyclerView.setAdapter(adapter);

        obtenerEquiposRouterDeFirestore();

        // Switch
        recyclerView2 = findViewById(R.id.recyclerViewEquipos2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        listaSwitches = new ArrayList<>();
        adapter2 = new ListaEquiposAdapterSupervisor2();
        adapter2.setContext(this);
        adapter2.setListaEquipos(listaSwitches);
        recyclerView2.setAdapter(adapter2);

        obtenerEquiposSwitchesDeFirestore();

        // Rectificador
        recyclerView3 = findViewById(R.id.recyclerViewRectificador2);
        recyclerView3.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        listaRectificador = new ArrayList<>();
        adapter3 = new ListaEquiposAdapterSupervisor2();
        adapter3.setContext(this);
        adapter3.setListaEquipos(listaRectificador);
        recyclerView3.setAdapter(adapter3);

        obtenerEquiposRectificadoresDeFirestore();

        // Panelde Energia
        recyclerView4 = findViewById(R.id.recyclerViewPanel2);
        recyclerView4.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        listaPanel = new ArrayList<>();
        adapter4 = new ListaEquiposAdapterSupervisor2();
        adapter4.setContext(this);
        adapter4.setListaEquipos(listaPanel);
        recyclerView4.setAdapter(adapter4);

        obtenerEquiposEnergiaDeFirestore();

        // Otros
        recyclerView5 = findViewById(R.id.recyclerViewOtros);
        recyclerView5.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        listaOtros = new ArrayList<>();
        adapter5 = new ListaEquiposAdapterSupervisor2();
        adapter5.setContext(this);
        adapter5.setListaEquipos(listaOtros);
        recyclerView5.setAdapter(adapter5);

        obtenerEquiposOtrosDeFirestore();

        FloatingActionButton botonCrear = findViewById(R.id.bottonCrear);
        botonCrear.setOnClickListener(v-> {
            Intent intent = new Intent(this, CrearEquipoActivity.class);
            startActivity(intent);
        });

        SearchView buscadorEquipos = findViewById(R.id.buscadorEquipos);
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
                buscarEquipoPorIDOtros(newText);
                return true;
            }
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
            adapter3.updateList(listaRectificador);
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
            adapter4.updateList(listaPanel);
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

    private void buscarEquipoPorIDOtros(String searchText) {
        if (searchText.isEmpty()) {
            adapter5.updateList(listaOtros);
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
                            EquipoAdmin equipo = document.toObject(EquipoAdmin.class);
                            if(!Objects.equals(equipo.getTipoDeEquipo(), "Router") && !Objects.equals(equipo.getTipoDeEquipo(), "Switch") && !Objects.equals(equipo.getTipoDeEquipo(), "Rectificador") && !Objects.equals(equipo.getTipoDeEquipo(), "Panel de Energia")){
                                filteredList.add(equipo);
                            }
                        }
                        adapter5.updateList(filteredList);
                    }
                });
    }

    private void obtenerEquiposRouterDeFirestore() {
        db.collection("equipos")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            EquipoAdmin equipo = document.toObject(EquipoAdmin.class);
                            int validador=1;
                            ArrayList<String> sitiosList = equipo.getSitios();
                            int cant = sitiosList.size();
                            Log.d("TAG", "obtenerEquiposRouterDeFirestore: "+cant);
                            for (int i=0;i<sitiosList.size();i++){
                                String sitio = sitiosList.get(i);
                                Log.d("TAG", "obtenerEquiposRouterDeFirestore: "+sitio);
                            }
                            if(equipo.getTipoDeEquipo().equals("Router") && validador==1) {
                                listaRouters.add(equipo);
                                validador = 0;
                            }
                        }
                        adapter.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al obtener los sitios", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(this, "Error al obtener los sitios", Toast.LENGTH_SHORT).show();
                });
    }

    private void obtenerEquiposRectificadoresDeFirestore() {
        db.collection("equipos")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            EquipoAdmin equipo = document.toObject(EquipoAdmin.class);
                            if(equipo.getTipoDeEquipo().equals("Rectificador")) {
                                listaRectificador.add(equipo);
                            }
                        }
                        adapter3.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al obtener los sitios", Toast.LENGTH_SHORT).show();
                });
    }

    private void obtenerEquiposEnergiaDeFirestore() {
        db.collection("equipos")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            EquipoAdmin equipo = document.toObject(EquipoAdmin.class);
                            if(equipo.getTipoDeEquipo().equals("Panel de Energia")) {
                                listaPanel.add(equipo);
                            }
                        }
                        adapter4.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al obtener los sitios", Toast.LENGTH_SHORT).show();
                });
    }

    private void obtenerEquiposOtrosDeFirestore() {
        db.collection("equipos")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            EquipoAdmin equipo = document.toObject(EquipoAdmin.class);
                            if(!Objects.equals(equipo.getTipoDeEquipo(), "Router") && !Objects.equals(equipo.getTipoDeEquipo(), "Switch") && !Objects.equals(equipo.getTipoDeEquipo(), "Rectificador") && !Objects.equals(equipo.getTipoDeEquipo(), "Panel de Energia")) {
                                listaOtros.add(equipo);
                            }
                        }
                        adapter5.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al obtener los sitios", Toast.LENGTH_SHORT).show();
                });
    }
}