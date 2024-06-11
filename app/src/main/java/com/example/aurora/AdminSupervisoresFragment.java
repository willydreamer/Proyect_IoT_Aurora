package com.example.aurora;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Adapter.ListaSupervisoresAdapter;
import com.example.aurora.Bean.Usuario;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class AdminSupervisoresFragment extends Fragment {

    //ArrayList<String> listaSupervisores;

    ArrayList<Usuario> listaSupervisores;
    
    RecyclerView recyclerView;

    FirebaseFirestore db;

    ListaSupervisoresAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_supervisor, container, false);


        ImageButton crearBtn = view.findViewById(R.id.button19);


        listaSupervisores = new ArrayList<>();



        recyclerView = view.findViewById(R.id.recyclerview_listasupervisores);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        /*listaSupervisores = new ArrayList<>();
        listaSupervisores.add("Christian Luis Gonzales Fernández");
        listaSupervisores.add("Willy Huallpa");
        listaSupervisores.add("Alejandro Gutierrez");
        listaSupervisores.add("Dana Nolasco");
        listaSupervisores.add("Alfredo Benavides");*/


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();
        listaSupervisores = new ArrayList<>();

        // Configurar el adapter y asociarlo al RecyclerView
        adapter = new ListaSupervisoresAdapter();
        adapter.setContext(getContext());
        adapter.setListaSupervisores(listaSupervisores);
        recyclerView.setAdapter(adapter);


        obtenerSupervisoresDeFirestore();


        crearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CrearSupervisorActivity.class); // Reemplaza "TuActivity" con el nombre de tu Activity
                startActivity(intent);
            }
        });

        return view;


        //seteamos
        //ListaSupervisoresAdapter adapter = new ListaSupervisoresAdapter(listaSupervisores);
        //recyclerView.setAdapter(adapter);


        //Crear instancia adapter
        //ListaSupervisoresAdapter adapter = new ListaSupervisoresAdapter();
        //adapter.setContext(AdminSupervisoresFragment.this);
        //binding.recyclerView.setAdapter(adapter);
        //binding.recyclerView.setLayoutManager(new LinearLayoutManager(AdminSupervisoresFragment.this);

    }


    private void obtenerSupervisoresDeFirestore() {
        db.collection("usuarios")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Usuario supervisor = document.toObject(Usuario.class);
                            if(supervisor.getRol().equals("supervisor")) {
                                listaSupervisores.add(supervisor);
                            }
                        }
                        adapter.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al obtener los supervisores", Toast.LENGTH_SHORT).show();
                });
    }

    /*public void irInfoSupervisor(View view) {

        //primero crear el intento
        Intent intent = new Intent(Getactivity.this, InformacionSupervisorActivity.class);
        //iniciar activity
        Log.d("Iot", "si");
        startActivity(intent);
    }*/


}
