package com.example.aurora.Supervisor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Adapter.ListaSitiosAdapter;
import com.example.aurora.Adapter.ListaSitiosAdapter2;
import com.example.aurora.Bean.EquipoAdmin;
import com.example.aurora.Bean.Sitio;
import com.example.aurora.Bean.Usuario;
import com.example.aurora.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class SupervisorListaSitios extends AppCompatActivity {

    private ArrayList<Sitio> listaSitios;
    private RecyclerView recyclerView;
    private ListaSitiosAdapter2 adaptador;
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
        adaptador = new ListaSitiosAdapter2();
        adaptador.setListaSitios(listaSitios);
        adaptador.setContext(this);
        recyclerView.setAdapter(adaptador);

        obtenerSitiosDeFirestore();
        encontrarmeComoUsuario();

        SearchView buscadorSitios = findViewById(R.id.buscadorSitios);
        buscadorSitios.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                buscarSitioPorID(newText);
                return true;
            }
        });
    }

    private void obtenerSitiosDeFirestore() {
        databaseReference.collection("sitios")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Sitio sitio = document.toObject(Sitio.class);
                            String dueno = sitio.getEncargado();
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

    ArrayList<Sitio> sitioArrayList = new ArrayList<>();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String userId = currentUser.getUid();
    private void buscarSitioPorID(String searchText) {
        if (searchText.isEmpty()) {
            adaptador.updateList(listaSitios);
            return;
        }

        databaseReference.collection("sitios")
                .orderBy("idSitio")
                .startAt(searchText)
                .endAt(searchText + "\uf8ff")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Sitio> filteredList = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            Sitio sitio = document.toObject(Sitio.class);
                            filteredList.add(sitio);
                        }
                        adaptador.updateList(filteredList);
                    }
                });
    }


    ArrayList<Usuario> filteredList2 = new ArrayList<>();
    private void encontrarmeComoUsuario() {

        if (userId.isEmpty()) {
            adaptador.updateList(listaSitios);
            return;
        }

        databaseReference.collection("usuarios")
                .orderBy("idUsuario")
                .startAt(userId)
                .endAt(userId + "\uf8ff")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (DocumentSnapshot document : task.getResult()) {
                            Usuario usuario = document.toObject(Usuario.class);
                            filteredList2.add(usuario);
                        }

                    }
                });
    }




}