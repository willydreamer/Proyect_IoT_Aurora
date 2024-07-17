package com.example.aurora.Supervisor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aurora.Adapter.ListaLogAdapter;
import com.example.aurora.Bean.Log;
import com.example.aurora.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SupervisorHistorialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SupervisorHistorialFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private ListaLogAdapter adapter;
    private List<Log> logList;

   // SearchView buscador;

    private FirebaseFirestore db;

    private Query logsRef;

    public SupervisorHistorialFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SupervisorHistorialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SupervisorHistorialFragment newInstance(String param1, String param2) {
        SupervisorHistorialFragment fragment = new SupervisorHistorialFragment();
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

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String user1 = user.getUid();
            android.util.Log.d("USUARIO", user1);
            db = FirebaseFirestore.getInstance();

            // Crear la consulta para filtrar los logs por idUsuario
            logsRef = db.collection("logs").whereEqualTo("idUsuario", user1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_supervisor_historial, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.holaaa);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        logList = new ArrayList<>();
        adapter = new ListaLogAdapter(logList, getContext());
        recyclerView.setAdapter(adapter);

        logsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }

                logList.clear();
                if (value != null) {
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        Log log = doc.toObject(Log.class);
                        /*db.collection("usuarios")
                                .whereEqualTo("idUsuario", log.getIdUsuario())  // Buscar documentos donde el campo 'idUsuario' sea igual a log.getIdUsuario()
                                .get()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                                        // Obtener el primer documento que coincide con la consulta
                                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                        Usuario usuario = document.toObject(Usuario.class);
                                        if (usuario != null) {
                                            usuario.setIdUsuario(document.getId());
                                            log.setUsuario(usuario);
                                        }
                                    } else {
                                        android.util.Log.d("msg-test", "Error getting document: ", task.getException());
                                    }
                                });*/
                        logList.add(log);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }


    private void buscarSupervisores(String searchText) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String user1 = user.getUid();
            android.util.Log.d("USUARIO", user1);
            db.collection("logs")
                    .orderBy("actividad")//.whereEqualTo("idUsuario", user1)// Suponiendo que tienes un campo 'idSitio' en tu colección
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
                                logList.clear();
                                for (DocumentSnapshot doc : snapshots.getDocuments()) {
                                    Log log = doc.toObject(Log.class);
                                    logList.add(log);
                                }
                                adapter.notifyDataSetChanged();
                                //adapter.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                            }
                        }
                    });
        }
    }

}