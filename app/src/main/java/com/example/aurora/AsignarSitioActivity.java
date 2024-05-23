package com.example.aurora;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aurora.databinding.ActivityAsignarSitioBinding;

public class AsignarSitioActivity extends AppCompatActivity {

    ActivityAsignarSitioBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_asignar_sitio);

        Button asignarSitio = findViewById(R.id.button11);
        asignarSitio.setOnClickListener(view->{
            mostrarDialog2();
        });
    }

    public void mostrarDialog2() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("¿Estas Seguro de Continuar?");

        alertDialog.setPositiveButton("Si", (dialogInterface, i) -> {
            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(this);
            alertDialog2.setMessage("Sitio Asignado Correctamente");
            alertDialog2.setPositiveButton("Listo", (dialogInterface2, i2) -> {
                Intent intent = new Intent(this, InformacionSupervisorActivity.class); // Reemplaza "TuActivity" con el nombre de tu Activity
                startActivity(intent);
            });
            alertDialog2.show();
        });

        alertDialog.setNegativeButton("Cancelar", (dialogInterface, i) -> {
            Log.d("msg-test", "Configuración presionado");
        });

        AlertDialog dialog = alertDialog.create();

        dialog.show();
    }
}