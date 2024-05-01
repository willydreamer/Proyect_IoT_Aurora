package com.example.aurora;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aurora.databinding.ActivityInformacionSitioBinding;

public class AdminInformacionSitioActivity extends AppCompatActivity {

    private ActivityInformacionSitioBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInformacionSitioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupSpinner(binding.spinnerTipoZona, R.array.tipo_zona_options);
        setupSpinner(binding.spinnerOperadora, R.array.operadora_options);


        binding.btnEditar.setOnClickListener(v -> toggleEditMode(true));

    }

    private void setupSpinner(Spinner spinner, int arrayId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                arrayId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    private void toggleEditMode(boolean enable) {
        binding.editDepartamento.setEnabled(enable);
        binding.editProvincia.setEnabled(enable);
        binding.editDistrito.setEnabled(enable);
        binding.editLatitud.setEnabled(enable);
        binding.editLongitud.setEnabled(enable);
        // Repite para los demás EditTexts
        binding.spinnerTipoZona.setEnabled(enable);
        binding.spinnerOperadora.setEnabled(enable);
        // Incluye otros campos si es necesario
    }
}