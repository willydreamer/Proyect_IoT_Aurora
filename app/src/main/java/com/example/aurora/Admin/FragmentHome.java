package com.example.aurora.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Adapter.ListaEquiposAdapterAdmin;
import com.example.aurora.Adapter.ListaSitiosAdapter;
import com.example.aurora.Bean.EquipoAdmin;
import com.example.aurora.Bean.Sitio;
import com.example.aurora.MensajeriaActivity;
import com.example.aurora.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FragmentHome extends Fragment {

    ArrayList<Sitio> listaSitios;

    ArrayList<EquipoAdmin> listaEquipos;
    RecyclerView recyclerView;

    RecyclerView recyclerView2;
    FirebaseFirestore db;
    ListaSitiosAdapter adapter;

    ListaEquiposAdapterAdmin adapter2;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View homeView = inflater.inflate(R.layout.admin_fragment_home, container, false);

        db = FirebaseFirestore.getInstance();
        //cargar routers

        /*recyclerView = homeView.findViewById(R.id.recyclerViewSitios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        db = FirebaseFirestore.getInstance();
        listaSitios = new ArrayList<>();

        // Configurar el adapter y asociarlo al RecyclerView
        adapter = new ListaSitiosAdapter();
        adapter.setContext(getContext());
        adapter.setListaSitios(listaSitios);
        recyclerView.setAdapter(adapter);

        obtenerSitiosDeFirestore();*/

        //cargar equipos

        recyclerView2 = homeView.findViewById(R.id.recyclerViewEquipos);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        listaEquipos = new ArrayList<>();

        // Configurar el adapter y asociarlo al RecyclerView
        adapter2 = new ListaEquiposAdapterAdmin();
        adapter2.setContext(getContext());
        adapter2.setListaEquipos(listaEquipos);
        recyclerView2.setAdapter(adapter2);

        obtenerEquiposDeFirestore();

        //navegaciones
        ImageButton goSitios = homeView.findViewById(R.id.flecha1);
        goSitios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear una instancia del segundo fragmento
                FragmentSitios sitios = new FragmentSitios();

                // Obtener el FragmentManager y comenzar la transacción
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // Reemplazar el fragmento actual por el segundo fragmento
                fragmentTransaction.replace(R.id.container,sitios);

                // Agregar la transacción a la pila de retroceso (opcional)
                fragmentTransaction.addToBackStack(null);

                // Confirmar la transacción
                fragmentTransaction.commit();
            }
        });

        ImageButton goSupervisores = homeView.findViewById(R.id.flecha2);
        goSupervisores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear una instancia del segundo fragmento
                FragmentSupervisores supervisores = new FragmentSupervisores();

                // Obtener el FragmentManager y comenzar la transacción
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // Reemplazar el fragmento actual por el segundo fragmento
                fragmentTransaction.replace(R.id.container,supervisores);

                // Agregar la transacción a la pila de retroceso (opcional)
                fragmentTransaction.addToBackStack(null);

                // Confirmar la transacción
                fragmentTransaction.commit();
            }
        });

        ImageButton goEquipos = homeView.findViewById(R.id.flecha3);
        goEquipos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear una instancia del segundo fragmento
                FragmentEquipos equipos = new FragmentEquipos();

                // Obtener el FragmentManager y comenzar la transacción
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // Reemplazar el fragmento actual por el segundo fragmento
                fragmentTransaction.replace(R.id.container,equipos);

                // Agregar la transacción a la pila de retroceso (opcional)
                fragmentTransaction.addToBackStack(null);

                // Confirmar la transacción
                fragmentTransaction.commit();
            }
        });

        ImageButton goMensajes = homeView.findViewById(R.id.imageButton2);
        goMensajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //primero crear el intento
                Intent intent = new Intent(getActivity(), MensajeriaActivity.class);
                //iniciar activity
                startActivity(intent);
            }
        });

        return homeView;
        //Con navigation
        /*NavController navController = NavHostFragment.findNavController(HomeFragment.this);




        ImageButton goSitios = homeView.findViewById(R.id.flecha1);
        goSitios.setOnClickListener(view -> {
            navController.navigate(R.id.action_homeFragment_to_adminSitiosFragment);
        });

        ImageButton goSupervisores = homeView.findViewById(R.id.flecha2);
        goSupervisores.setOnClickListener(view -> {
            navController.navigate(R.id.action_homeFragment_to_adminSupervisoresFragment);
        });

        ImageButton goEquipos = homeView.findViewById(R.id.flecha3);
        goEquipos.setOnClickListener(view -> {
            navController.navigate(R.id.action_homeFragment_to_equiposFragment);
        });*/


    }

    private void obtenerSitiosDeFirestore() {
        db.collection("sitios")
                .orderBy("idSitio")
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

    private void obtenerEquiposDeFirestore() {
        db.collection("equipos")
                .orderBy("fechaDeRegistro" , Query.Direction.DESCENDING)
                .limit(7)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Toast.makeText(getContext(), "Error al obtener los equipos", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (snapshots != null) {
                            listaEquipos.clear(); // Limpiar la lista antes de agregar nuevos datos
                            for (DocumentSnapshot document : snapshots.getDocuments()) {
                                EquipoAdmin equipo = document.toObject(EquipoAdmin.class);
                                listaEquipos.add(equipo);
                            }
                            adapter2.setListaEquipos(listaEquipos);
                            //adapter.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                        }
                    }
                });
    }

}