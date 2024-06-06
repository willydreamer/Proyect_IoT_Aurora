package com.example.aurora;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Adapter.ListaEquiposAdapter;
import com.example.aurora.Adapter.ListaSitiosAdapter;
import com.example.aurora.Bean.Sitio;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SupervisorListaDeEquipos extends AppCompatActivity {

    ArrayList<Sitio> listaSitios;
    RecyclerView recyclerView;
    FirebaseFirestore db;
    ListaEquiposAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_supervisor_lista_de_equipos);
        recyclerView = findViewById(R.id.recyclerViewEquipos);

    }
}