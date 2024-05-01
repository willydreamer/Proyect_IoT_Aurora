package com.example.aurora;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ListaAsignacionSitiosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_asignacion_sitios);


    }

     public void irAsignarSitio(View view) {

        //primero crear el intento
        Intent intent = new Intent(ListaAsignacionSitiosActivity.this, AsignarSitioActivity.class);
        //iniciar activity
        startActivity(intent);
    }
}
