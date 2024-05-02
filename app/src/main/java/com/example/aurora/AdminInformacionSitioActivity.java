package com.example.aurora;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aurora.databinding.ActivityInformacionSitioBinding;

public class AdminInformacionSitioActivity extends AppCompatActivity {

    private ActivityInformacionSitioBinding binding;
    private ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInformacionSitioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupSpinner(binding.spinnerTipoZona, R.array.tipo_zona_options);
        setupSpinner(binding.spinnerOperadora, R.array.operadora_options);


        binding.btnEditar.setOnClickListener(v -> toggleEditMode(true));

        binding.btnGuardar.setOnClickListener(v -> {
            toggleEditMode(false);
            saveChanges(); // Método para guardar los cambios
        });

        // Configurar el adaptador para el Spinner
        adapter = ArrayAdapter.createFromResource(this,
                R.array.nombres_ficticios, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerNombres.setAdapter(adapter);

        Spinner spinner = findViewById(R.id.spinnerNombres);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Llamar a la función onNombreSeleccionado() cuando se seleccione un item
                onNombreSeleccionado(selectedItemView);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Método requerido, pero no necesitas hacer nada aquí
            }
        });

        binding.buttonEliminarSitio.setOnClickListener(v -> {
            Intent intent = new Intent(AdminInformacionSitioActivity.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Sitio eliminado exitosamente", Toast.LENGTH_SHORT).show();
        });

    }

    private void setupSpinner(Spinner spinner, int arrayId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                arrayId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setEnabled(false);
        spinner.setAdapter(adapter);
    }

    private void toggleEditMode(boolean enable) {
        binding.editDepartamento.setEnabled(enable);
        binding.editProvincia.setEnabled(enable);
        binding.editDistrito.setEnabled(enable);
        binding.editCodigoUbigeo.setEnabled(enable);
        binding.editLatitud.setEnabled(enable);
        binding.editLongitud.setEnabled(enable);
        binding.spinnerTipoZona.setEnabled(enable);
        binding.spinnerOperadora.setEnabled(enable);

        //Botones de editar y guardar
        binding.btnEditar.setVisibility(enable ? View.GONE : View.VISIBLE);
        binding.btnGuardar.setVisibility(enable ? View.VISIBLE : View.GONE);

    }

    private void saveChanges() {
        // Aquí va el código para guardar los cambios

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Desea guardar los cambios?")
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Aquí se ejecuta la acción de guardar los cambios
                        guardarCambios();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Aquí se cancela la acción de guardar los cambios
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void guardarCambios() {
        // Aquí va el código para guardar los cambios
        Toast.makeText(this, "Cambios guardados con éxito", Toast.LENGTH_SHORT).show();
    }

    public void irInfoSupervisor(View view) {
        // Mostrar un AlertDialog de confirmación
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Desea continuar?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        binding.cardViewResponsable.setVisibility(View.GONE);
                        binding.spinnerNombres.setVisibility(View.VISIBLE);

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // No hacer nada
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onNombreSeleccionado(View view) {
        // Obtener el nombre seleccionado del Spinner
        String nombreSeleccionado = binding.spinnerNombres.getSelectedItem().toString();

        binding.textTitle1.setText(nombreSeleccionado);
        binding.cardViewResponsable.setVisibility(View.VISIBLE);
        binding.spinnerNombres.setVisibility(View.GONE);
    }

}