package com.example.aurora.Supervisor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Adapter.ListaEquiposAdapterAdmin;
import com.example.aurora.Adapter.ListaEquiposAdapterSupervisor;
import com.example.aurora.Admin.InformacionEquipoActivity;
import com.example.aurora.Bean.EquipoAdmin;
import com.example.aurora.Bean.Sitio;
import com.example.aurora.Bean.Usuario;
import com.example.aurora.R;
import com.example.aurora.databinding.ActivitySupervisorEstadoEquipoBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import com.google.firebase.Timestamp;
import android.util.Log;

public class SupervisorEstadoEquipoActivity extends AppCompatActivity {
    ArrayList<EquipoAdmin> listaRouters;
    RecyclerView recyclerView;
    FirebaseFirestore db;
    ListaEquiposAdapterSupervisor adapter;
    EditText editTextText6;
    EditText editTextText7;
    ActivitySupervisorEstadoEquipoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_supervisor_estado_equipo);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayList<String> spinnerItems = new ArrayList<>(Arrays.asList("Operativo", "En arreglo", "Descompuesto","Fuera de servicio"));
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerItems);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);

        recyclerView = findViewById(R.id.recyclerViewEquipos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        db = FirebaseFirestore.getInstance();
        listaRouters = new ArrayList<>();

        // Configurar el adapter y asociarlo al RecyclerView
        adapter = new ListaEquiposAdapterSupervisor(listaRouters,this);
        adapter.setContext(this);
        adapter.setListaEquipos(listaRouters);
        recyclerView.setAdapter(adapter);

        obtenerEquiposDeFirestore();

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
               String opcionSeleccionada = spinner.getSelectedItem().toString();
               Log.d("opcionSeleccionada", "onCreate: "+opcionSeleccionada);
               actualizarAtributo(equipoId1,opcionSeleccionada);
           }
        });
    }

    String equipoId1;
    TextView textViewSeleccione;
    public void updateTextView(String equipoId) {
        textViewSeleccione.setText("ID Equipo: "+equipoId); // Actualizar el TextView con el ID del equipo
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
                    Toast.makeText(SupervisorEstadoEquipoActivity.this, "Cambios guardados con éxito", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SupervisorEstadoEquipoActivity.this, SupervisorListaDeEquipos.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Log.e("Error equipo", "Error al guardar equipo", e);
                    Toast.makeText(SupervisorEstadoEquipoActivity.this, "Error al guardar cambios", Toast.LENGTH_LONG).show();
                });
    }
    /*
    private void crearLog(String actividad, String descripcion, String idUsuario, EquipoAdmin equipoAdmin) {
        Timestamp timestamp = Timestamp.now();
        Random rand = new Random();
        int idLog = rand.nextInt(100000) + 1;

        Log log = new Log(idLog, timestamp.toString(), actividad, descripcion, Usuario usuario, Sitio sitio);

        // Añade el documento a la colección "logs" en Firestore
        db.collection("logs")
                .add(log)
                .addOnSuccessListener(documentReference -> {
                    // Éxito al añadir el documento
                    Toast.makeText(SupervisorEstadoEquipoActivity.this, "Log creado correctamente", Toast.LENGTH_SHORT).show();
                    // Puedes realizar otras acciones aquí después de crear el log, como navegar a otra actividad
                    Intent intent = new Intent(SupervisorEstadoEquipoActivity.this, OtraActividad.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    // Error al añadir el documento
                    Log.e("Firebase", "Error al crear el log", e);
                    Toast.makeText(SupervisorEstadoEquipoActivity.this, "Error al crear el log", Toast.LENGTH_SHORT).show();
                });
    }

     */
}
