package com.example.aurora.Admin;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Adapter.ListaFotosEquipoAdapter;
import com.example.aurora.Bean.EquipoAdmin;
import com.example.aurora.R;
import com.example.aurora.databinding.ActivityInformacionEquipoBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class InformacionEquipoActivity extends AppCompatActivity {

    private ActivityInformacionEquipoBinding binding;
    private FirebaseFirestore db;
    private EquipoAdmin equipo;

    ImageView fotoEquipo;

    private ListaFotosEquipoAdapter adapter;

    private RecyclerView  recyclerViewFotosEquipo;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    private List<Uri> imageUris;

    private List<String> imageUrls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInformacionEquipoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db=FirebaseFirestore.getInstance();
        //Recibir sitio
        Intent intent = getIntent();
        fotoEquipo = findViewById(R.id.imageEquipo);
        equipo = (EquipoAdmin) intent.getSerializableExtra("equipo");

        if (equipo != null) {
            binding.textViewNamePrincipal.setText(equipo.getIdEquipo());
            binding.editSKU.setText(equipo.getSKU());
            binding.editNumSerie.setText(equipo.getNumeroDeSerie());
            binding.editMarca.setText(equipo.getMarca());
            binding.editModelo.setText(equipo.getModelo());
            binding.spinnerTipoEquipo.setSelection(2);
            binding.editDescripcion.setText(equipo.getDescripcion());

            if (equipo.getFotosEquipo().get(0) != null && !equipo.getFotosEquipo().get(0).isEmpty()) {
                Picasso.get()
                        .load(equipo.getFotosEquipo().get(0))
                        .placeholder(R.drawable.perfil_icono) // Reemplaza con tu imagen por defecto
                        .transform(new CropCircleTransformation())
                        .into(fotoEquipo);
            } else {
                Picasso.get()
                        .load(R.drawable.perfil_icono) // Imagen por defecto
                        .transform(new CropCircleTransformation())
                        .into(fotoEquipo);
            }

            recyclerViewFotosEquipo = findViewById(R.id.recyclerViewFotosEquipos);
            imageUrls = equipo.getFotosEquipo();
            adapter = new ListaFotosEquipoAdapter(this, imageUrls);
            recyclerViewFotosEquipo.setLayoutManager((new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)));
            recyclerViewFotosEquipo.setAdapter(adapter);

        }

        setupSpinner(binding.spinnerTipoEquipo, R.array.tipo_equipo_options);

        binding.btnEditar.setOnClickListener(v -> toggleEditMode(true));
        binding.btnGuardar.setOnClickListener(v -> {
            toggleEditMode(false);
            saveChanges();
        });

        binding.buttonEliminarSitio.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(InformacionEquipoActivity.this);
            builder.setMessage("¿Desea eliminar definitivamente el equipo " + equipo.getIdEquipo() + "?")
                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Eliminar el sitio de la base de datos
                            db.collection("equipos")
                                    .document(equipo.getIdEquipo())
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Intent intent = new Intent(InformacionEquipoActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            Toast.makeText(InformacionEquipoActivity.this, "Equipo eliminado exitosamente", Toast.LENGTH_LONG).show();
                                            finish(); // Cerrar la actividad actual
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("AdminInformacionSitio", "Error al eliminar equipo", e);
                                            Toast.makeText(InformacionEquipoActivity.this, "Error al eliminar equipo", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
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
        binding.editSKU.setEnabled(enable);
        binding.editNumSerie.setEnabled(enable);
        binding.editMarca.setEnabled(enable);
        binding.editModelo.setEnabled(enable);
        binding.editDescripcion.setEnabled(enable);
        binding.spinnerTipoEquipo.setEnabled(enable);

        // Mostrar el botón de guardar solo cuando esté en modo edición
        binding.btnGuardar.setVisibility(enable ? View.VISIBLE : View.GONE);
        // Ocultar el botón de editar cuando esté en modo edición
        binding.btnEditar.setVisibility(enable ? View.GONE : View.VISIBLE);
    }

    private void saveChanges() {

        db=FirebaseFirestore.getInstance();
        String departamento = binding.editSKU.getText().toString();
        String provincia = binding.editNumSerie.getText().toString();
        String distrito = binding.editMarca.getText().toString();
        String latitud = binding.editModelo.getText().toString();
        String longitud = binding.editDescripcion.getText().toString();
        String tipoZona = binding.spinnerTipoEquipo.getSelectedItem().toString();

        // Actualizar los valores en el objeto sitio
        equipo.setSKU(departamento);
        equipo.setNumeroDeSerie(provincia);
        equipo.setMarca(distrito);
        equipo.setModelo(latitud);
        equipo.setDescripcion(longitud);
        equipo.setTipoDeEquipo(tipoZona);

        // Guardar los cambios en la base de datos
        db.collection("equipos")
                .document(equipo.getIdEquipo())
                .set(equipo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(InformacionEquipoActivity.this, "Cambios guardados con éxito", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("AdminInformacionSitio", "Error al guardar equipo", e);
                        Toast.makeText(InformacionEquipoActivity.this, "Error al guardar cambios", Toast.LENGTH_LONG).show();
                    }
                });
    }

}

