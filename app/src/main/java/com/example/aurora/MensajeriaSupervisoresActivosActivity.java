package com.example.aurora;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Adapter.ListaSupervisoresActivosAdapter;
import com.example.aurora.Adapter.ListaSupervisoresAdapter;
import com.example.aurora.Admin.CrearSupervisorActivity;
import com.example.aurora.Bean.Usuario;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MensajeriaSupervisoresActivosActivity extends AppCompatActivity {

    ArrayList<Usuario> listaSupervisores;

    RecyclerView recyclerView;

    FirebaseFirestore db;

    ListaSupervisoresActivosAdapter adapter;

    SearchView buscador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mensajeria_supervisores_activos);



        buscador = findViewById(R.id.search1);


        listaSupervisores = new ArrayList<>();



        recyclerView = findViewById(R.id.recyclerview_listasupervisoresactivos);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        listaSupervisores = new ArrayList<>();

        // Configurar el adapter y asociarlo al RecyclerView
        adapter = new ListaSupervisoresActivosAdapter(this,listaSupervisores);
        recyclerView.setAdapter(adapter);


        //basado en gpt
        buscador.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                buscarUsuarios(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    obtenerSupervisoresDeFirestore();
                } else {
                    buscarUsuarios(newText);
                }
                return true;
            }
        });

        // Listener para detectar cuándo se limpia el texto del SearchView
        buscador.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                obtenerSupervisoresDeFirestore();
                return false;
            }
        });



        obtenerSupervisoresDeFirestore();



    }


    private void obtenerSupervisoresDeFirestore() {
        db.collection("usuarios")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Usuario supervisor = document.toObject(Usuario.class);
                            if(supervisor.getRol().equals("supervisor") && supervisor.getEstado().equals("activo")) {
                                listaSupervisores.add(supervisor);
                            }
                        }
                        adapter.setListaSupervisores(listaSupervisores);
                        // Notificar al adapter que los datos han cambiado
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al obtener los supervisores", Toast.LENGTH_LONG).show();
                });
    }

    private void buscarUsuarios(String searchText) {
        db.collection("usuarios")
                .orderBy("nombre")
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
                            ArrayList<Usuario> supervisores = new ArrayList<>();
                            for (DocumentSnapshot document : snapshots.getDocuments()) {
                                Usuario supervisor = document.toObject(Usuario.class);
                                supervisores.add(supervisor);
                            }
                            adapter.setListaSupervisores(supervisores);
                        }
                    }
                });
    }


}