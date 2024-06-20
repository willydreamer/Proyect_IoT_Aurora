package com.example.aurora.Admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.aurora.Adapter.ListaSitiosAdapter;
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


public class FragmentSitios extends Fragment {

    ArrayList<Sitio> listaSitios;
    RecyclerView recyclerView;
    FirebaseFirestore db;
    ListaSitiosAdapter adapter;
    SearchView buscador;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.admin_fragment_sitios, container, false);

        recyclerView = view.findViewById(R.id.recyclerview_lista_asignacion_sitios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();
        listaSitios = new ArrayList<>();

        buscador = view.findViewById(R.id.search1);

        // Configurar el adapter y asociarlo al RecyclerView
        adapter = new ListaSitiosAdapter();
        adapter.setContext(getContext());
        adapter.setListaSitios(listaSitios);
        recyclerView.setAdapter(adapter);

        FloatingActionButton btnCrear = view.findViewById(R.id.bottonCrearSitio);
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar CrearSitioActivity
                Intent intent = new Intent(getActivity(), CrearSitioActivity.class);
                startActivity(intent);
            }
        });

        // Buscador
        buscador.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                buscarSitios(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    obtenerSitiosDeFirestore();
                } else {
                    buscarSitios(newText);
                }
                return true;
            }
        });

        // Listener para detectar cuándo se limpia el texto del SearchView
        buscador.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                obtenerSitiosDeFirestore();
                return false;
            }
        });

        obtenerSitiosDeFirestore(); // Asegúrate de llamar a este método para cargar los datos iniciales
        return view;
    }

    private void obtenerSitiosDeFirestore() {
        db.collection("sitios")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Toast.makeText(getContext(), "Error al obtener los sitios", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (snapshots != null) {
                            listaSitios.clear(); // Limpiar la lista antes de agregar nuevos datos
                            for (DocumentSnapshot document : snapshots.getDocuments()) {
                                Sitio sitio = document.toObject(Sitio.class);
                                listaSitios.add(sitio);
                            }
                            adapter.setListaSitios(listaSitios);
                            //adapter.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                        }
                    }
                });
    }

    private void buscarSitios(String searchText) {
        db.collection("sitios")
                .orderBy("idSitio") // Suponiendo que tienes un campo 'idSitio' en tu colección
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
                            ArrayList<Sitio> sitios = new ArrayList<>();
                            for (DocumentSnapshot document : snapshots.getDocuments()) {
                                Sitio sitio = document.toObject(Sitio.class);
                                sitios.add(sitio);
                            }
                            adapter.setListaSitios(sitios);
                            //adapter.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                        }
                    }
                });
    }
}
