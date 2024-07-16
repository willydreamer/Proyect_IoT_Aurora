package com.example.aurora.Superadmin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Adapter.ListaLogAdapter;
import com.example.aurora.Bean.Log;
import com.example.aurora.Bean.Usuario;
import com.example.aurora.General.LoginFragment;
import com.example.aurora.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SuperAdminLogsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SuperAdminLogsFragment extends Fragment {

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

    SearchView buscador;

    private FirebaseFirestore db;
    private CollectionReference logsRef;

    public SuperAdminLogsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SuperAdminLogsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SuperAdminLogsFragment newInstance(String param1, String param2) {
        SuperAdminLogsFragment fragment = new SuperAdminLogsFragment();
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
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
        db = FirebaseFirestore.getInstance();
        logsRef = db.collection("logs");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_super_admin_logs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.logsr);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        logList = new ArrayList<>();
        adapter = new ListaLogAdapter(logList, getContext());
        recyclerView.setAdapter(adapter);


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
                    logsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {
                                return;
                            }
                            logList.clear();
                            for (DocumentSnapshot doc : value.getDocuments()) {
                                Log log = doc.toObject(Log.class);
                                logList.add(log);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    buscarSupervisores(newText);
                }
                return true;
            }
        });
        buscador.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                logsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }
                        logList.clear();
                        for (DocumentSnapshot doc : value.getDocuments()) {
                            Log log = doc.toObject(Log.class);
                            logList.add(log);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
                return false;
            }
        });

        logsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                logList.clear();
                for (DocumentSnapshot doc : value.getDocuments()) {
                    Log log = doc.toObject(Log.class);
                    logList.add(log);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void buscarSupervisores(String searchText) {
        db.collection("logs")
                .orderBy("actividad") // Suponiendo que tienes un campo 'idSitio' en tu colección
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

