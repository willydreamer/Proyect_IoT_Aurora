package com.example.aurora.Supervisor;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.aurora.Adapter.ListaSitiosAdapter2;
import com.example.aurora.Bean.Sitio;
import com.example.aurora.Bean.Usuario;
import com.example.aurora.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SupervisorListaSitiosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SupervisorListaSitiosFragment extends Fragment {

    private ArrayList<Sitio> listaSitios;
    private RecyclerView recyclerView;
    private ListaSitiosAdapter2 adaptador;
    private FirebaseFirestore databaseReference;
    public static SupervisorListaSitiosFragment newInstance(String param1, String param2) {
        SupervisorListaSitiosFragment fragment = new SupervisorListaSitiosFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_supervisor_lista_sitios, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewSitios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseReference = FirebaseFirestore.getInstance();

        listaSitios = new ArrayList<>();

        // Configurar el adapter y asociarlo al RecyclerView
        adaptador = new ListaSitiosAdapter2();
        adaptador.setListaSitios(listaSitios);
        adaptador.setContext(getContext());
        recyclerView.setAdapter(adaptador);

        obtenerSitiosDeFirestore();
        encontrarmeComoUsuario();

        SearchView buscadorSitios = view.findViewById(R.id.buscadorSitios);
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
        return view;
    }

    private void obtenerSitiosDeFirestore() {
        databaseReference.collection("sitios")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {

                            return;
                        }

                        if (snapshots != null) {
                            Log.d("TAG", "uwu");
                            listaSitios.clear(); // Limpiar la lista antes de agregar nuevos datos
                            for (DocumentSnapshot document : snapshots.getDocuments()) {
                                Sitio sitio = document.toObject(Sitio.class);
                                listaSitios.add(sitio);
                                Log.d("TAG", "onEvent: "+sitio.getIdSitio());
                            }
                            adaptador.setListaSitios(listaSitios);
                            adaptador.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                        }else {
                            Log.d("TAG", "onEvent: asdasfasdad no hay nada");
                        }
                    }
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