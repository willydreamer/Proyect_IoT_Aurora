package com.example.aurora;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Adapter.ListaSitiosAdapter;
import com.example.aurora.Bean.Sitio;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class SupervisorListaSitios extends AppCompatActivity {

    private ArrayList<Sitio> listaSitios;
    private RecyclerView recyclerView;
    private ListaSitiosAdapter adaptador;
    private FirebaseFirestore databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_supervisor_lista_sitios);

        recyclerView = findViewById(R.id.recyclerViewSitios);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = FirebaseFirestore.getInstance();

        listaSitios = new ArrayList<>();

        // Configurar el adapter y asociarlo al RecyclerView
        adaptador = new ListaSitiosAdapter();
        adaptador.setListaSitios(listaSitios);
        adaptador.setContext(this);
        recyclerView.setAdapter(adaptador);

        obtenerSitiosDeFirestore();
    }

    private void obtenerSitiosDeFirestore() {
        databaseReference.collection("sitios")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Sitio sitio = document.toObject(Sitio.class);
                            String dueno = sitio.getEncargado();
                            Log.d("TAG", "obtenerSitiosDeFirestore: "+dueno);
                            if(Objects.equals(dueno, "ADMIN111")) {
                                listaSitios.add(sitio);
                            }

                        }
                        adaptador.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this,"Error al obtener los sitios", Toast.LENGTH_SHORT).show();
                });
    }
}