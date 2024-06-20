package com.example.aurora.Supervisor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.widget.Toast;

import com.example.aurora.Adapter.ListaEquiposAdapterAdmin;
import com.example.aurora.Bean.EquipoAdmin;
import com.example.aurora.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class SupervisorAgregarFragment extends Fragment {

    private String numSerie;
    FirebaseFirestore db;
    private FirebaseAuth auth;

    ListaEquiposAdapterAdmin adapter;
    ArrayList<EquipoAdmin> equipoAdminArrayList;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_supervisor_agregar, container, false);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        view.findViewById(R.id.scan_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(SupervisorAgregarFragment.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                integrator.setPrompt("Escanear un código QR");
                integrator.setCameraId(0);  // Usa la cámara trasera
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        ArrayList<EquipoAdmin> equipoAdmins = new ArrayList<>();
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("ScanFragment", "Escaneo cancelado");
            } else {
                Log.d("ScanFragment", "Escaneo exitoso: " + result.getContents());
                numSerie = result.getContents();
                String contenidoEscaneado = numSerie.toString();
                //((Supervisor) getActivity()).handleScanResult(contenidoEscaneado);
                Log.d("ScanFragment", "Escaneo string: " + contenidoEscaneado);
                buscarEquiposPorNumSerie(contenidoEscaneado,equipoAdmins);

            }
        }
    }


    private void buscarEquiposPorNumSerie(String searchText, ArrayList<EquipoAdmin> equipoAdmins1) {
        db.collection("equipos")
                .whereEqualTo("idEquipo",searchText)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        if (snapshots != null) {
                            Log.d("DBCOLLECTION", "HAY SNAPSHOT ");

                            for (DocumentSnapshot document : snapshots.getDocuments()) {
                                Log.d("DBCOLLECTION:", document.toString());
                                EquipoAdmin equipoAdmin = document.toObject(EquipoAdmin.class);
                                Log.d("AOISDNAONDASOND",equipoAdmin.getIdEquipo() + " " + equipoAdmin.getDescripcion());
                                equipoAdmins1.add(equipoAdmin);
                            }
                            if(equipoAdmins1.isEmpty()){
                                showToast("Ups no pudimos encontrarlo.");
                            } else if (equipoAdmins1.size() == 1) {
                                showToast("¡Equipo encontrado! Visualiza su información a continuación.");
                                Intent intent = new Intent(getActivity(), SupervisorInfoEquiposActivity.class);
                                intent.putExtra("equipoEncontrado", equipoAdmins1.get(0));
                                startActivity(intent);
                            }else{
                                showToast("Ups, hemos encontrado más de 1 equipo con ese código. Comunícate con Soporte para arreglarlo.");
                            }

                        }
                    }
                });
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
