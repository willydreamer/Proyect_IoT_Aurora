package com.example.aurora.Admin;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Adapter.ListaSitiosAdapter;
import com.example.aurora.Bean.Sitio;
import com.example.aurora.Bean.Usuario;
import com.example.aurora.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;

public class ListaAsignacionSitiosActivity extends AppCompatActivity {

    FirebaseFirestore db;

    ListaSitiosAdapter adapter;

    ArrayList<Sitio> listaSitios;

    Context context;

    RecyclerView recyclerView;

    SearchView buscador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_asignacion_sitios);

        buscador = findViewById(R.id.search1);

        Usuario supervisor = (Usuario) getIntent().getSerializableExtra("supervisor");

        recyclerView = findViewById(R.id.recyclerview_lista_asignacion_sitios);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        db = FirebaseFirestore.getInstance();
        listaSitios = new ArrayList<>();

        adapter = new ListaSitiosAdapter();
        adapter.setContext(getApplicationContext());
        adapter.setListaSitios(listaSitios);
        adapter.setSupervisor(supervisor);
        recyclerView.setAdapter(adapter);

        //basado en gpt
        buscador.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                buscarSitiosDisponibles(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    obtenerSitiosDeFirestore();
                } else {
                    buscarSitiosDisponibles(newText);
                }
                return true;
            }
        });
        obtenerSitiosDeFirestore();


    }

    private void obtenerSitiosDeFirestore() {
        db.collection("sitios")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Toast.makeText(ListaAsignacionSitiosActivity.this, "Error al obtener los sitios", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (snapshots != null) {
                            listaSitios.clear(); // Limpiar la lista antes de agregar nuevos datos
                            for (DocumentSnapshot document : snapshots.getDocuments()) {
                                Sitio sitio = document.toObject(Sitio.class);
                                if(sitio.getEncargado() == null || sitio.getEncargado().isEmpty()) {
                                    listaSitios.add(sitio);
                                }
                            }
                            adapter.setListaSitios(listaSitios);
                            //adapter.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                        }
                    }
                });
    }

    private void buscarSitiosDisponibles(String searchText) {
        db.collection("sitios")
                .orderBy("idSitio") // Suponiendo que tienes un campo 'idSitio' en tu colección
                .startAt(searchText)
                .endAt(searchText + "\uf8ff")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            // Manejo de errores
                            return;
                        }

                        if (snapshots != null) {
                            ArrayList<Sitio> sitios = new ArrayList<>();
                            for (DocumentSnapshot document : snapshots.getDocuments()) {
                                Sitio sitio = document.toObject(Sitio.class);
                                if(sitio.getEncargado() == null || sitio.getEncargado().isEmpty()) {
                                    sitios.add(sitio);
                                }
                            }
                            adapter.setListaSitios(sitios);
                            //adapter.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                        }
                    }
                });
    }

     /*public void irAsignarSitio(View view) {

         Usuario supervisor = (Usuario) getIntent().getSerializableExtra("supervisor");

         //primero crear el intento
        Intent intent = new Intent(ListaAsignacionSitiosActivity.this, AsignarSitioActivity.class);
        intent.putExtra("supervisor",supervisor);
        //iniciar activity
        startActivity(intent);
    }*/

    /*private void obtenerSitiosDeFirestore() {

        Usuario supervisor = (Usuario) getIntent().getSerializableExtra("supervisor");

        db.collection("sitios")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Sitio sitio = document.toObject(Sitio.class);
                            //if(sitio.getSupervisor() == null || sitio.getSupervisor().isEmpty()){
                            //if(sitio.getEncargado() == null){
                            if(sitio.getEncargado() == null || sitio.getEncargado().isEmpty()){
                                listaSitios.add(sitio);
                            }
                        }
                        adapter.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error al obtener los sitios", Toast.LENGTH_SHORT).show();
                });
    }*/
}
