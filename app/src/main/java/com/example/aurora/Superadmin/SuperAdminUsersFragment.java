package com.example.aurora.Superadmin;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.aurora.Adapter.ListaUsuariosAdapter;
import com.example.aurora.Bean.Usuario;
import com.example.aurora.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SuperAdminUsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SuperAdminUsersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private ListaUsuariosAdapter adapter;
    private List<Usuario> listaUsuarios;
    FirebaseFirestore db;

    SearchView buscador;

    public SuperAdminUsersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SuperAdminUsersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SuperAdminUsersFragment newInstance(String param1, String param2) {
        SuperAdminUsersFragment fragment = new SuperAdminUsersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        db = FirebaseFirestore.getInstance(); // I
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_super_admin_users, container, false);
        recyclerView = view.findViewById(R.id.recycleruser);

        // Configurar el RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializar la lista de usuarios (esto normalmente vendría de una fuente de datos)
        listaUsuarios = new ArrayList<>();
        /*listaUsuarios.add(new Usuario("1","Alejandro","Gutierrez","75663999","a20200638@pucp.edu.pe","hola","912817406","administrador","activo",null,"asd"));
        listaUsuarios.add(new Usuario("2", "ASDA","ANSD","8529674","a65255875@pucp.edu.pe","asdadad","985274163","supervisor","activo",null,"asdnadsk"));
        */
        // Configurar el adaptador


        adapter = new ListaUsuariosAdapter(listaUsuarios, getContext());
        recyclerView.setAdapter(adapter);
        obtenerSupervisoresDeFirestore();

        buscador = view.findViewById(R.id.search1);
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
        buscador.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                obtenerSupervisoresDeFirestore();
                return false;
            }
        });



        return view;
    }

    private void obtenerSupervisoresDeFirestore() {
        db.collection("usuarios")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Usuario supervisor = document.toObject(Usuario.class);
                            listaUsuarios.add(supervisor);
                        }
                        adapter.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al obtener los usuarios", Toast.LENGTH_SHORT).show();
                });
    }

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
                            adapter.notifyDataSetChanged();
                            //adapter.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                        }
                    }
                });
    }
}