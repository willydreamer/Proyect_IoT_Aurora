package com.example.aurora.Supervisor;

import android.content.Intent;
import android.os.Bundle;

import com.example.aurora.Adapter.ListaEquiposAdapterSupervisor;
import com.example.aurora.Adapter.ListaEquiposAdapterSupervisor22;
import com.example.aurora.Adapter.ListaSitiosAdapter2;
import com.example.aurora.Adapter.ListaSitiosAdapterSupervisor22;
import com.example.aurora.Bean.EquipoAdmin;
import com.example.aurora.Bean.Sitio;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.databinding.ActivitySupervisorAsignarSitioBinding;

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
import java.util.Arrays;

public class SupervisorAsignarSitioActivity extends AppCompatActivity {

    ArrayList<EquipoAdmin> listaRouters;
    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    FirebaseFirestore db;
    ListaEquiposAdapterSupervisor22 adapter;
    ArrayList<Sitio> listaSitios;
    ListaSitiosAdapterSupervisor22 adaptador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_asignar_sitio);

        db = FirebaseFirestore.getInstance();

        // Equipos
        recyclerView = findViewById(R.id.recyclerViewEquipos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        listaRouters = new ArrayList<>();

        adapter = new ListaEquiposAdapterSupervisor22(listaRouters,this);
        adapter.setContext(this);
        adapter.setListaEquipos(listaRouters);
        recyclerView.setAdapter(adapter);

        obtenerEquiposDeFirestore();

        // Sitios
        listaSitios = new ArrayList<>();
        recyclerView2 = findViewById(R.id.recyclerViewSitios2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adaptador = new ListaSitiosAdapterSupervisor22(listaSitios,this);
        adaptador.setListaEquipos(listaSitios);
        adaptador.setContext(this);
        recyclerView2.setAdapter(adaptador);

        obtenerSitiosDeFirestore();

        SearchView buscadorEquipos = findViewById(R.id.buscadorEquipos);
        buscadorEquipos.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                buscarEquipoPorID(newText);
                return true;
            }
        });

        textViewSeleccione = findViewById(R.id.textViewSeleccione);

        Button buttonRegistrar = findViewById(R.id.buttonRegistrar);
        buttonRegistrar.setOnClickListener(v -> {
            if(equipoId1 != null){
                String opcionSeleccionada = "";
                Log.d("opcionSeleccionada", "onCreate: "+opcionSeleccionada);
                actualizarAtributo(equipoId1,opcionSeleccionada);
            }
        });

    }

    String equipoId1;
    TextView textViewSeleccione;
    public void updateTextView(String equipoId) {
        textViewSeleccione.setText("ID Equipo: " + equipoId); // Actualizar el TextView con el ID del equipo
        equipoId1 = equipoId;
    }

    private void buscarEquipoPorID(String searchText) {
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
                            filteredList.add(equipoAdmin);
                        }
                        adapter.updateList(filteredList);
                    }
                });
    }

    private void obtenerSitiosDeFirestore() {
        db.collection("sitios")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Sitio sitio = document.toObject(Sitio.class);
                            listaSitios.add(sitio);
                        }
                        adaptador.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al obtener los sitios", Toast.LENGTH_SHORT).show();
                });
    }

    private void obtenerEquiposDeFirestore() {
        db.collection("equipos")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            EquipoAdmin equipo = document.toObject(EquipoAdmin.class);
                            listaRouters.add(equipo);
                        }
                        adapter.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al obtener los sitios", Toast.LENGTH_SHORT).show();
                });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void actualizarAtributo(String idDocumento, String nuevoValor) {
        db.collection("equipos")
                .whereEqualTo("idEquipo", idDocumento)  // Filtra por el ID del equipo
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            EquipoAdmin equipoAdmin = document.toObject(EquipoAdmin.class);
                            equipoAdmin.setEstado(nuevoValor);
                            actualizarDocumento(idDocumento, equipoAdmin);
                        }
                    } else {
                        Log.e("Error", "Error al obtener documentos: ", task.getException());
                    }
                });
    }

    private void actualizarDocumento(String idDocumento, EquipoAdmin equipoAdmin) {
        db.collection("equipos")
                .document(idDocumento)
                .set(equipoAdmin)
                .addOnSuccessListener(aVoid -> {
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    //crearLog("Se edito el estado del Equipo "+idDocumento,"Debido a cambios con los equipos, se ha realizado el cambio de estado del equipo mencionado a fin de brindar información actualizada",currentUser.getUid(),equipoAdmin);
                    Toast.makeText(this, "Cambios guardados con éxito", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, SupervisorListaDeEquipos.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Log.e("Error equipo", "Error al guardar equipo", e);
                    Toast.makeText(this, "Error al guardar cambios", Toast.LENGTH_LONG).show();
                });
    }
}