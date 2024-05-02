package com.example.aurora;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aurora.databinding.ActivityCrearSitioBinding;

public class CrearSitioActivity extends AppCompatActivity {
    private ActivityCrearSitioBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCrearSitioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Mostrar las opciones por defecto en los Spinners

        ArrayAdapter<CharSequence> adapterTipoZona = ArrayAdapter.createFromResource(this,
                R.array.tipo_zona_options, android.R.layout.simple_spinner_item);
        adapterTipoZona.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTipoZona.setAdapter(adapterTipoZona);


        ArrayAdapter<CharSequence> adapterOperadora = ArrayAdapter.createFromResource(this,
                R.array.operadora_options, android.R.layout.simple_spinner_item);
        adapterOperadora.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerOperadora.setAdapter(adapterOperadora);


        binding.buttonGuardar.setOnClickListener(v -> {
            Intent intent = new Intent(CrearSitioActivity.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Sitio creado exitosamente", Toast.LENGTH_SHORT).show();
        });


    }
}