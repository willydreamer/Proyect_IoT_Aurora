package com.example.aurora;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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

public class ListaAsignacionSitiosActivity extends AppCompatActivity {

    FirebaseFirestore db;

    ListaSitiosAdapter adapter;

    ArrayList<Sitio> listaSitios;

    Context context;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_asignacion_sitios);


        recyclerView = findViewById(R.id.recyclerview_lista_asignacion_sitios);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        db = FirebaseFirestore.getInstance();
        listaSitios = new ArrayList<>();

        adapter = new ListaSitiosAdapter();
        adapter.setContext(getApplicationContext());
        adapter.setListaSitios(listaSitios);
        recyclerView.setAdapter(adapter);

        obtenerSitiosDeFirestore();


    }

     public void irAsignarSitio(View view) {



        //primero crear el intento
        Intent intent = new Intent(ListaAsignacionSitiosActivity.this, AsignarSitioActivity.class);
        //iniciar activity
        startActivity(intent);
    }

    private void obtenerSitiosDeFirestore() {
        db.collection("sitios")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Sitio sitio = document.toObject(Sitio.class);
                            if(sitio.getSupervisor() == null){
                                listaSitios.add(sitio);
                            }
                        }
                        adapter.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error al obtener los sitios", Toast.LENGTH_SHORT).show();
                });
    }
}
