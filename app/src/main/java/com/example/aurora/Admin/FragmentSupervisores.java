package com.example.aurora.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Adapter.ListaSupervisoresAdapter;
import com.example.aurora.Bean.EquipoAdmin;
import com.example.aurora.Bean.Sitio;
import com.example.aurora.Bean.Usuario;
import com.example.aurora.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FragmentSupervisores extends Fragment {


    ArrayList<Usuario> listaSupervisores;

    RecyclerView recyclerView;

    FirebaseFirestore db;

    ListaSupervisoresAdapter adapter;

    SearchView buscador;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_supervisor, container, false);


        FloatingActionButton crearBtn = view.findViewById(R.id.button19);

        buscador = view.findViewById(R.id.search1);

        listaSupervisores = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recyclerview_listasupervisores);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();
        listaSupervisores = new ArrayList<>();

        // Configurar el adapter y asociarlo al RecyclerView
        adapter = new ListaSupervisoresAdapter(getContext(), listaSupervisores);
        recyclerView.setAdapter(adapter);

        obtenerSupervisoresDeFirestore();


        //basado en gpt
        buscador.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                buscarSupervisores(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    obtenerSupervisoresDeFirestore();
                } else {
                    buscarSupervisores(newText);
                }
                return true;
            }
        });
        /*buscador.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        });*/
        buscador.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                obtenerSupervisoresDeFirestore();
                return false;
            }
        });

        obtenerSupervisoresDeFirestore();

        crearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CrearSupervisorActivity.class); // Reemplaza "TuActivity" con el nombre de tu Activity
                startActivity(intent);
            }
        });

        return view;

    }

    /*private void obtenerSupervisoresDeFirestore() {
        db.collection("usuarios")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Usuario supervisor = document.toObject(Usuario.class);
                            int validador=1;
                            if(supervisor.getRol().equals("supervisor") && validador==1) {
                                listaSupervisores.add(supervisor);
                                validador = 0;
                            }
                        }
                        adapter.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al obtener los Supervisores", Toast.LENGTH_LONG).show();
                });
    }*/
    private void obtenerSupervisoresDeFirestore() {
        db.collection("usuarios")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Toast.makeText(getContext(), "Error al obtener los supervisores", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (snapshots != null) {
                            listaSupervisores.clear(); // Limpiar la lista antes de agregar nuevos datos
                            for (DocumentSnapshot document : snapshots.getDocuments()) {
                                Usuario supervisor = document.toObject(Usuario.class);
                                if(supervisor.getRol().equals("supervisor")) {
                                    listaSupervisores.add(supervisor);
                                }
                            }
                            adapter.setListaSupervisores(listaSupervisores);
                            //adapter.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                        }
                    }
                });
    }


    /*private void obtenerSupervisoresDeFirestore() {
        db.collection("usuarios")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Usuario supervisor = document.toObject(Usuario.class);
                            if (supervisor.getRol().equals("supervisor")) {
                                listaSupervisores.add(supervisor);
                            }
                        }
                        adapter.setListaSupervisores(listaSupervisores);
                        // Notificar al adapter que los datos han cambiado
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al obtener los supervisores", Toast.LENGTH_LONG).show();
                });
    }*/

    /*private void buscarporID(String searchText) {
        if (searchText.isEmpty()) {
            adapter.updateList(listaSupervisores);
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
                            if(equipoAdmin.getTipoDeEquipo().equals("Router")){
                                filteredList.add(equipoAdmin);
                            }
                        }
                        adapter.updateList(filteredList);
                    }
                });
    }*/

    /*private void buscarSupervisores(String searchText) {
        if (searchText.isEmpty()) {
            adapter.updateList(listaSupervisores);
            return;
        }

        db.collection("usuarios")
                .orderBy("nombre")
                .startAt(searchText)
                .endAt(searchText + "\uf8ff")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<Usuario> filteredList = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            Usuario supervisor = document.toObject(Usuario.class);
                            if(supervisor.getRol().equals("supervisor")){
                                filteredList.add(supervisor);
                            }
                        }
                        adapter.updateList(filteredList);
                    }
                });
    }*/

    private void buscarSupervisores(String searchText) {
        db.collection("usuarios")
                .orderBy("nombre") // Suponiendo que tienes un campo 'idSitio' en tu colección
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
                            ArrayList<Usuario> supers= new ArrayList<>();
                            for (DocumentSnapshot document : snapshots.getDocuments()) {
                                Usuario supervisor = document.toObject(Usuario.class);
                                supers.add(supervisor);
                            }
                            adapter.setListaSupervisores(supers);
                            //adapter.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                        }
                    }
                });
    }
}


    /*private void buscarUsuarios(String searchText) {
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
    }*/


