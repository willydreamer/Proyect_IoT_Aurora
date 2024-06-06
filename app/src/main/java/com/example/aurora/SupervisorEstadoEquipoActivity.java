package com.example.aurora;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Adapter.ListaEquiposAdapterAdmin;
import com.example.aurora.Bean.EquipoAdmin;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class SupervisorEstadoEquipoActivity extends AppCompatActivity {
    ArrayList<EquipoAdmin> listaRouters;
    RecyclerView recyclerView;
    FirebaseFirestore db;
    ListaEquiposAdapterAdmin adapter;
    EditText editTextText6;
    EditText editTextText7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_supervisor_estado_equipo);

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

        editTextText6 = findViewById(R.id.editTextText6);
        editTextText7 = findViewById(R.id.editTextText7);

        Button buttonRegistrar = findViewById(R.id.buttonRegistrar);
        buttonRegistrar.setOnClickListener(v -> {
            String editTextText66 = String.valueOf(editTextText6.getText());
            buscarEquipo(editTextText66, new OnEquipoEncontradoListener() {
                @Override
                public void onEquipoEncontrado(EquipoAdmin equipo) {
                    if (equipo != null) {
                        String editTextText77 = String.valueOf(editTextText7.getText());
                        actualizarEquipo(equipo, editTextText77);
                    } else {
                        Toast.makeText(SupervisorEstadoEquipoActivity.this, "Equipo no encontrado", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }

    private void obtenerEquiposRouterDeFirestore() {
        db.collection("equipos")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            EquipoAdmin equipo = document.toObject(EquipoAdmin.class);
                            Log.d("TAG", "obtenerEquiposRouterDeFirestore: " + equipo.getIdEquipo());
                            Log.d("TAG", "obtenerEquiposRouterDeFirestore: " + equipo.getDescripcion());
                            if (equipo.getTipoDeEquipo().equals("Router")) {
                                listaRouters.add(equipo);
                            }
                        }
                        adapter.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al obtener los sitios", Toast.LENGTH_SHORT).show();
                });
    }

    private void buscarEquipo(String idEquipo, OnEquipoEncontradoListener listener) {
        db.collection("equipos")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            EquipoAdmin equipo = document.toObject(EquipoAdmin.class);
                            if (equipo.getIdEquipo().equals(idEquipo)) {
                                Log.d("asdsad", "buscarEquipo: encontrado");
                                listener.onEquipoEncontrado(equipo);
                                return;
                            }
                        }
                        listener.onEquipoEncontrado(null); // No se encontró el equipo
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al encontrar el sitio", Toast.LENGTH_SHORT).show();
                    listener.onEquipoEncontrado(null); // Error en la búsqueda
                });
    }

    private void actualizarEquipo(EquipoAdmin equipobuscado, String nuevaDescripcion) {
        String idEquipo = equipobuscado.getIdEquipo();
        equipobuscado.setDescripcion(nuevaDescripcion);
        // Guardar los datos en Firestore
        db.collection("equipos")
                .document(idEquipo)
                .update("Descripcion", nuevaDescripcion)
                .addOnSuccessListener(unused -> {
                    Log.d("msg-test2", "equipo actualizado exitosamente");
                    Toast.makeText(this, "Equipo actualizado exitosamente", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("msg-test3", "Error al actualizar el equipo", e);
                    Toast.makeText(this, "Error al actualizar el equipo", Toast.LENGTH_SHORT).show();
                });
    }

    interface OnEquipoEncontradoListener {
        void onEquipoEncontrado(EquipoAdmin equipo);
    }
}
