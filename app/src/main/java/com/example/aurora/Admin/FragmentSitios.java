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

        // Configurar el adapter y asociarlo al RecyclerView
        adapter = new ListaSitiosAdapter();
        adapter.setContext(getContext());
        adapter.setListaSitios(listaSitios);
        recyclerView.setAdapter(adapter);
        buscador = view.findViewById(R.id.search1);


        obtenerSitiosDeFirestore();

        AppCompatImageButton btnCrear = view.findViewById(R.id.bottonCrearSitio);
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar CrearSitioActivity
                Intent intent = new Intent(getActivity(), CrearSitioActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void obtenerSitiosDeFirestore() {
        db.collection("sitios")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Sitio sitio = document.toObject(Sitio.class);
                            Log.d("sitio", String.valueOf(sitio.getSupervisor()));
                            listaSitios.add(sitio);
                        }
                        adapter.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al obtener los sitios", Toast.LENGTH_SHORT).show();
                });
    }

}
