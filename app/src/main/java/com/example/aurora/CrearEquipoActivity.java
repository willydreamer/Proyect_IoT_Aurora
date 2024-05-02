package com.example.aurora;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aurora.databinding.ActivityCrearEquipoBinding;
import com.example.aurora.databinding.ActivityCrearSitioBinding;

public class CrearEquipoActivity extends AppCompatActivity {
    private ActivityCrearEquipoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCrearEquipoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayAdapter<CharSequence> adapterTipoEquipo = ArrayAdapter.createFromResource(this,
                R.array.tipo_equipo_options, android.R.layout.simple_spinner_item);
        adapterTipoEquipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTipoEquipo.setAdapter(adapterTipoEquipo);
    }
}