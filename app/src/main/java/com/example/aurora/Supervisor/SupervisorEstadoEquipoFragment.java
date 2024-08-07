package com.example.aurora.Supervisor;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.example.aurora.Adapter.ListaEquiposAdapterSupervisor;
import com.example.aurora.Bean.EquipoAdmin;
import com.example.aurora.R;
import com.example.aurora.databinding.ActivitySupervisorEstadoEquipoBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class SupervisorEstadoEquipoFragment extends Fragment {

    ArrayList<EquipoAdmin> listaRouters;
    RecyclerView recyclerView;
    FirebaseFirestore db;
    ListaEquiposAdapterSupervisor adapter;
    EditText editTextText6;
    EditText editTextText7;
    ActivitySupervisorEstadoEquipoBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_supervisor_estado_equipo, container, false);

        Spinner spinner = view.findViewById(R.id.spinner);
        ArrayList<String> spinnerItems = new ArrayList<>(Arrays.asList("Operativo", "En arreglo", "Descompuesto","Fuera de servicio"));
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinnerItems);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);

        recyclerView = view.findViewById(R.id.recyclerViewEquipos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        db = FirebaseFirestore.getInstance();
        listaRouters = new ArrayList<>();

        // Configurar el adapter y asociarlo al RecyclerView
        adapter = new ListaEquiposAdapterSupervisor(listaRouters,this);
        adapter.setContext(getContext());
        adapter.setListaEquipos(listaRouters);
        recyclerView.setAdapter(adapter);

        obtenerEquiposDeFirestore();

        SearchView buscadorEquipos = view.findViewById(R.id.buscadorEquipos);
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

        textViewSeleccione = view.findViewById(R.id.textViewSeleccione);


        Button buttonRegistrar = view.findViewById(R.id.buttonRegistrar);
        buttonRegistrar.setOnClickListener(v -> {
            if(equipoId1 != null){
                String opcionSeleccionada = spinner.getSelectedItem().toString();
                Log.d("opcionSeleccionada", "onCreate: "+opcionSeleccionada);
                actualizarAtributo(equipoId1,opcionSeleccionada);
            }
        });
        return view;
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
                    Toast.makeText(getContext(), "Error al obtener los sitios", Toast.LENGTH_SHORT).show();
                });
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), "Cambios guardados con éxito", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(), SupervisorListaDeEquipos.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Log.e("Error equipo", "Error al guardar equipo", e);
                    Toast.makeText(getContext(), "Error al guardar cambios", Toast.LENGTH_LONG).show();
                });
    }
}