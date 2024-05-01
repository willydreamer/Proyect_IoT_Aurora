package com.example.aurora;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Adapter.ListaSitiosAdapter;
import com.example.aurora.Bean.Sitio;

import java.util.ArrayList;

public class InformacionSupervisorActivity extends AppCompatActivity {

    ArrayList<Sitio> listaSitios;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_informacion_supervisor);

        recyclerView = findViewById(R.id.recyclerview_listasitios);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        listaSitios = new ArrayList<>();
        listaSitios.add(new Sitio("NW000001","Junín","Huancayo","Center","Urbana","Móvil"));
        listaSitios.add(new Sitio("NW000002","Cusco","Cusco","Center","Rural","Fijo"));
        listaSitios.add(new Sitio("NW000003","Arequipa","Arequipa","Center","Urbana","Móvil"));

        ListaSitiosAdapter adapter = new ListaSitiosAdapter();
        adapter.setContext(InformacionSupervisorActivity.this);
        adapter.setListaSitios(listaSitios);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(InformacionSupervisorActivity.this));


        //recyclerView.setAdapter(adapter);

    }

    /* Como Fragment
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla el layout de tu fragmento
        return inflater.inflate(R.layout.activity_informacion_supervisor, container, false);
    }*/

     public void irAsignarSitio(View view) {

        //primero crear el intento
        Intent intent = new Intent(this, ListaAsignacionSitiosActivity.class);
        //iniciar activity
        startActivity(intent);
    }



}

