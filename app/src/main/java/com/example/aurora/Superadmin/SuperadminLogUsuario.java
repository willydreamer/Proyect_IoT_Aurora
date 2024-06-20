package com.example.aurora.Superadmin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aurora.Adapter.ListaLogAdapter;
import com.example.aurora.Bean.Log;
import com.example.aurora.R;
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
 * Use the {@link SuperadminLogUsuario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SuperadminLogUsuario extends Fragment {

    private static final String ARG_LOG = "id";

    private String id;
    private RecyclerView recyclerView;
    private ListaLogAdapter adapter;
    private List<Log> logList;

    private FirebaseFirestore db;
    private Query logsRef;

    public SuperadminLogUsuario() {
        // Required empty public constructor
    }

    public static SuperadminLogUsuario newInstance(String id) {
        SuperadminLogUsuario fragment = new SuperadminLogUsuario();
        Bundle args = new Bundle();
        args.putString(ARG_LOG, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ARG_LOG);
            db = FirebaseFirestore.getInstance();
            logsRef = db.collection("logs").whereEqualTo("idUsuario", id);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_superadmin_log_usuario, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.logsr);
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
                        logList.add(log);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}
