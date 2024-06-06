package com.example.aurora;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SupervisorAgregarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SupervisorAgregarFragment extends Fragment {

    TextView textView36;
    public SupervisorAgregarFragment() {
        // Required empty public constructor
    }

    public static SupervisorAgregarFragment newInstance(String param1, String param2) {
        SupervisorAgregarFragment fragment = new SupervisorAgregarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new IntentIntegrator(this).initiateScan();
        textView36 = getView().findViewById(R.id.textView36);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_supervisor_agregar, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        String idEquipo = result.getContents();
        textView36.setText(idEquipo);
    }
}