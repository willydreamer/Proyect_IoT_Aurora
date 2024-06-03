package com.example.aurora;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aurora.Bean.Sitio;
import com.example.aurora.Bean.Usuario;
import com.example.aurora.databinding.ActivityCrearSitioBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Random;

public class CrearSitioActivity extends AppCompatActivity {
    private ActivityCrearSitioBinding binding;
    FirebaseFirestore db;
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

        // Instanciar Firebase
        db = FirebaseFirestore.getInstance();
        binding.buttonGuardar.setOnClickListener(v -> {
            guardarSitio();
        });

    }

    private void guardarSitio() {
        // Obtener los datos ingresados por el usuario
        String departamento = binding.editDepartamento.getText().toString();
        String idSitio = generarIdSitio(departamento);
        String provincia = binding.editProvincia.getText().toString();
        String distrito = binding.editDistrito.getText().toString();
        String tipoDeZona = binding.spinnerTipoZona.getSelectedItem().toString();
        //String codigoUbigeo = binding.editCodigoUbigeo.getText().toString();
        String latitud = binding.editLatitud.getText().toString();
        String longitud = binding.editLongitud.getText().toString();
        String operadora = binding.spinnerOperadora.getSelectedItem().toString();
        ArrayList<Usuario> supervisor = new ArrayList<>();
        String encargado = "";

        // Crear un objeto Sitio con los datos obtenidos
        //Sitio sitio = new Sitio(idSitio, departamento, provincia, distrito, tipoDeZona, latitud, longitud, operadora, encargado);
        Sitio sitio = new Sitio(idSitio, departamento, provincia, distrito, tipoDeZona, latitud, longitud, operadora,encargado,supervisor);


        // Guardar los datos en Firestore
        db.collection("sitios")
                .document(idSitio)
                .set(sitio)
                .addOnSuccessListener(unused -> {
                    Log.d("msg-test", "Sitio guardado exitosamente");
                    Toast.makeText(CrearSitioActivity.this, "Sitio creado exitosamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CrearSitioActivity.this, MainActivity.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Log.e("msg-test", "Error al guardar el sitio", e);
                    Toast.makeText(CrearSitioActivity.this, "Error al crear el sitio", Toast.LENGTH_SHORT).show();
                });
    }
    private String generarIdSitio(String departamento) {
        String letrasDepartamento = departamento.substring(0, Math.min(departamento.length(), 3)).toUpperCase();
        Random random = new Random();
        int numeroAleatorio = random.nextInt(900) + 100; // Generar un número entre 100 y 999
        return letrasDepartamento + numeroAleatorio;
    }
}