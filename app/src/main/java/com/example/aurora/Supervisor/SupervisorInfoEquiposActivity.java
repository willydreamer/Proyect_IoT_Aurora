package com.example.aurora.Supervisor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Adapter.ListaEquiposAdapterAdmin;
import com.example.aurora.Adapter.ListaSupervisoresAdapter;
import com.example.aurora.Adapter.SelectedImagesAdapter;
import com.example.aurora.Bean.EquipoAdmin;
import com.example.aurora.Bean.Usuario;
import com.example.aurora.R;
import com.example.aurora.databinding.ActivitySupervisorInfoEquiposBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class SupervisorInfoEquiposActivity extends AppCompatActivity {
    ActivitySupervisorInfoEquiposBinding binding;
    FirebaseFirestore db;

    ListaEquiposAdapterAdmin adapter;
    SelectedImagesAdapter adapter1;
    ArrayList<EquipoAdmin> equipoAdminArrayList;
    EquipoAdmin equipo;
    ImageView fotoEquipo;
    RecyclerView recyclerViewFotosEquipo;
    List<Uri> imageUris;

    List<String> imageUrls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_supervisor_info_equipos);

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        equipo = (EquipoAdmin) intent.getSerializableExtra("equipo");
        fotoEquipo = findViewById(R.id.imageEquipo);

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
            adapter1 = new SelectedImagesAdapter(this, imageUris);
            recyclerViewFotosEquipo.setLayoutManager((new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)));
            recyclerViewFotosEquipo.setAdapter(adapter);

            cargarFotosEquipo();

        }
    }

    private void cargarFotosEquipo() {
        db = FirebaseFirestore.getInstance();
        db.collection("equipos").document(equipo.getIdEquipo()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    EquipoAdmin equipo1 = documentSnapshot.toObject(EquipoAdmin.class);
                    if (equipo1 != null && equipo1.getFotosEquipo() != null) {
                        imageUrls.addAll(equipo1.getFotosEquipo());
                        for (String imageUrl : imageUrls) {
                            imageUris.add(Uri.parse(imageUrl));
                        }
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al cargar imágenes del Equipo", Toast.LENGTH_LONG).show());
    }


}