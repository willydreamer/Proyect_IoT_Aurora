package com.example.aurora.Admin;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Adapter.ListaFotosEquipoAdapter;
import com.example.aurora.Adapter.ListaFotosEquipoAdapter2;
import com.example.aurora.Adapter.SelectedImagesAdapter;
import com.example.aurora.Bean.EquipoAdmin;
import com.example.aurora.R;
import com.example.aurora.databinding.ActivityInformacionEquipoBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class InformacionEquipoActivity extends AppCompatActivity {

    private ActivityInformacionEquipoBinding binding;
    private FirebaseFirestore db;
    private EquipoAdmin equipo;

    private ImageView fotoEquipo;

    private ListaFotosEquipoAdapter2 adapter;
    //private SelectedImagesAdapter adapter;

    private RecyclerView recyclerViewFotosEquipo;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    private List<Uri> imageUris;

    private List<String> imageUrls;

    private Button buttonSelectFotos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInformacionEquipoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
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
            imageUris = new ArrayList<>();
            adapter = new ListaFotosEquipoAdapter2(this, imageUris);
            recyclerViewFotosEquipo.setLayoutManager((new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)));
            recyclerViewFotosEquipo.setAdapter(adapter);

            cargarFotosEquipo();

        }

        setupSpinner(binding.spinnerTipoEquipo, R.array.tipo_equipo_options);

        binding.btnEditar.setOnClickListener(v ->{
                toggleEditMode(true);
                boolean isEditMode = !adapter.isEditMode;
                adapter.setEditMode(isEditMode);
            });

        binding.btnGuardar.setOnClickListener(v -> {
            toggleEditMode(false);
            saveChangesToFirebase();
        });

        buttonSelectFotos = findViewById(R.id.botonSubir);

        buttonSelectFotos.setOnClickListener(v -> {
            Intent intent2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent2.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(intent2, REQUEST_IMAGE_PICK);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_PICK) {
            if (data != null) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        adapter.addPhoto(imageUri);
                    }
                } else if (data.getData() != null) {
                    Uri imageUri = data.getData();
                    adapter.addPhoto(imageUri);
                }
            }
        }
    }

    private void cargarFotosEquipo() {
        db = FirebaseFirestore.getInstance();
        db.collection("equipos").document(equipo.getIdEquipo()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    EquipoAdmin equipo1 = documentSnapshot.toObject(EquipoAdmin.class);
                    if (equipo1 != null && equipo1.getFotosEquipo() != null) {
                        for (String imageUrl : equipo1.getFotosEquipo()) {
                            imageUris.add(Uri.parse(imageUrl));
                        }
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al cargar imágenes del Equipo", Toast.LENGTH_LONG).show());
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
        binding.botonSubir.setVisibility(enable ? View.VISIBLE : View.GONE);
        //binding.botonBorrar.setVisibility(enable ? View.VISIBLE : View.GONE);
        // Ocultar el botón de editar cuando esté en modo edición
        binding.btnEditar.setVisibility(enable ? View.GONE : View.VISIBLE);
    }

    //basado gpt
    private void saveChangesToFirebase() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        List<String> updatedImageUrls = new ArrayList<>(imageUrls); // URLs existentes
        List<Uri> newImageUris = new ArrayList<>(); // Nuevas fotos seleccionadas

        // Separar las nuevas fotos de las existentes
        for (Object photo : adapter.getFotosEquipo()) {
            if (photo instanceof Uri) {
                newImageUris.add((Uri) photo);
            }
        }


        for (Uri imageUri : newImageUris) {
            StorageReference storageReference = storage.getReference().child("images/" + System.currentTimeMillis() + ".jpg");
            storageReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        updatedImageUrls.add(uri.toString());
                        if (updatedImageUrls.size() == adapter.getFotosEquipo().size()) {
                            saveChanges(updatedImageUrls);
                        }
                    }))
                    .addOnFailureListener(e -> Toast.makeText(this, "Error al subir foto: " + e.getMessage(), Toast.LENGTH_LONG).show());
        }

        // Eliminar las imágenes eliminadas
        /*for (Uri uri : adapter.getFotosEquipo()) {
            updatedImageUrls.remove(uri.toString());
        }*/

        // Si no hay nuevas fotos, guardar cambios directamente
        if (newImageUris.isEmpty()) {
            saveChanges(updatedImageUrls);
        }

        // Eliminar las imágenes eliminadas
        for (Object photo : imageUrls) {
            if (!adapter.getFotosEquipo().contains(photo)) {
                updatedImageUrls.remove(photo.toString());
            }
        }

        // Guardar los cambios finales
        saveChanges(updatedImageUrls);
    }

    /*private void updateUserImagesInFirestore(List<String> imageUrls) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("usuarios").document(userId)
                .update("imageUrls", imageUrls)
                .addOnSuccessListener(aVoid -> Toast.makeText(EditUserActivity.this, "Imágenes actualizadas correctamente", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(EditUserActivity.this, "Error al actualizar imágenes", Toast.LENGTH_SHORT).show());
    }*/

    private void saveChanges(List<String> updatedImageUrls) {

        db = FirebaseFirestore.getInstance();
        String sku = binding.editSKU.getText().toString();
        String num_Serie = binding.editNumSerie.getText().toString();
        String marca = binding.editMarca.getText().toString();
        String modelo = binding.editModelo.getText().toString();
        String descripcion = binding.editDescripcion.getText().toString();
        String tipoEquipo = binding.spinnerTipoEquipo.getSelectedItem().toString();



        if (!sku.isEmpty() && !num_Serie.isEmpty() && !marca.isEmpty() && !modelo.isEmpty() && !descripcion.isEmpty() && !tipoEquipo.isEmpty()) {
            if(updatedImageUrls.isEmpty()) {
                Toast.makeText(InformacionEquipoActivity.this, "Debe Seleccionar 1 o más Fotos", Toast.LENGTH_LONG).show();
            }else {
                // Actualizar los valores en el objeto sitio
                equipo.setSKU(sku);
                equipo.setNumeroDeSerie(num_Serie);
                equipo.setMarca(marca);
                equipo.setModelo(modelo);
                equipo.setDescripcion(descripcion);
                equipo.setTipoDeEquipo(tipoEquipo);
                equipo.setFotosEquipo(updatedImageUrls);

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
                                Log.e("Error equipo", "Error al guardar equipo", e);
                                Toast.makeText(InformacionEquipoActivity.this, "Error al guardar cambios", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        } else {
            Toast.makeText(InformacionEquipoActivity.this, "No deben haber campos vacios", Toast.LENGTH_LONG).show();
        }

    }
}

