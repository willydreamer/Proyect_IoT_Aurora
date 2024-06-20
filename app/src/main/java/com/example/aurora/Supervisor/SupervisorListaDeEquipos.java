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

public class SupervisorListaDeEquipos extends AppCompatActivity {

    ArrayList<EquipoAdmin> listaRouters;
    ArrayList<EquipoAdmin> listaSwitches;
    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    FirebaseFirestore db;
    ListaEquiposAdapterAdmin adapter;

    ListaEquiposAdapterAdmin adapter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_supervisor_lista_de_equipos);
        recyclerView = findViewById(R.id.recyclerViewEquipos);

        recyclerView = findViewById(R.id.recyclerViewEquipos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        db = FirebaseFirestore.getInstance();
        listaRouters = new ArrayList<>();

        // Configurar el adapter y asociarlo al RecyclerView
        adapter = new ListaEquiposAdapterAdmin();
        adapter.setContext(this);
        adapter.setListaEquipos(listaRouters);
        recyclerView.setAdapter(adapter);

        obtenerEquiposRouterDeFirestore();

        //cargar switches

        recyclerView2 = findViewById(R.id.recyclerViewEquipos2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        listaSwitches = new ArrayList<>();

        // Configurar el adapter y asociarlo al RecyclerView
        adapter2 = new ListaEquiposAdapterAdmin();
        adapter2.setContext(this);
        adapter2.setListaEquipos(listaSwitches);
        recyclerView2.setAdapter(adapter2);

        obtenerEquiposSwitchesDeFirestore();

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
}