package com.example.aurora;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aurora.Bean.Usuario;
import com.example.aurora.databinding.ActivityEditarPerfilBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class EditarPerfilActivity extends AppCompatActivity {

    private ActivityEditarPerfilBinding binding;
    private FirebaseFirestore db;
    private Usuario usuario;

    private ImageView fotoUsuario;

    private FirebaseUser usuarioLogueado;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    private Uri imagenUri;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar_perfil);


        db = FirebaseFirestore.getInstance();

        fotoUsuario = findViewById(R.id.imageUsuario);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        usuarioLogueado = firebaseAuth.getCurrentUser();
        if (usuarioLogueado != null) { //user logged-in
                //Ojo uid del firebase user =! id del Document
                Log.d("correo",usuarioLogueado.getEmail());
                String correo = usuarioLogueado.getEmail();
                obtenerUsuarioPorCorreo(correo);
                //obtener foto de usuario
                Log.d("usuario",usuario.getNombre());
                if (usuario.getFotoURL() != null && !usuario.getFotoURL().isEmpty()) {
                    Picasso.get()
                            .load(usuario.getFotoURL())
                            .placeholder(R.drawable.profile) // Reemplaza con tu imagen por defecto
                            .transform(new CropCircleTransformation())
                            .into(fotoUsuario);
                } else {
                    Picasso.get()
                            .load(R.drawable.profile) // Imagen por defecto
                            .transform(new CropCircleTransformation())
                            .into(fotoUsuario);
                }
                binding.textViewNamePrincipal.setText(usuarioLogueado.getUid());
                binding.nombre.setText(usuario.getNombre());
                binding.apellido.setText(usuario.getApellido());
                binding.dni.setText(usuario.getDni());
                binding.correo.setText(usuario.getCorreo());
                binding.domicilio.setText(usuario.getDomicilio());
                binding.telefono.setText(usuario.getTelefono());
        }

        binding.editar.setOnClickListener(v -> toggleEditMode(true));
        binding.btnGuardar.setOnClickListener(v -> {
            toggleEditMode(false);
            saveChanges();
        });

        //inicio foto

        // Botón para abrir la galería
        binding.buttonSubirFoto.setOnClickListener(v -> {
            Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //todas las imagenes
            pickPhotoIntent.setType("image/*");
            startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK);
        });
    }


        private void toggleEditMode(boolean enable) {
            binding.telefono.setEnabled(enable);

            // Mostrar el botón de guardar solo cuando esté en modo edición
            binding.btnGuardar.setVisibility(enable ? View.VISIBLE : View.GONE);
            // Ocultar el botón de editar cuando esté en modo
            binding.buttonSubirFoto.setVisibility(enable ? View.VISIBLE : View.GONE);
            // Ocultar el botón de editar cuando esté en modo edición
            binding.editar.setVisibility(enable ? View.GONE : View.VISIBLE);
        }

        private void saveChanges() {

            db=FirebaseFirestore.getInstance();
            String telefono = binding.telefono.getText().toString();

            // Actualizar el telefono en el usuario
            usuario.setTelefono(telefono);

            // Guardar los cambios en la base de datos
            db.collection("usuarios")
                    .document(usuarioLogueado.getUid())
                    .set(usuario)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(EditarPerfilActivity.this, "Cambios guardados con éxito", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("AdminInformacionSitio", "Error al guardar Usuario", e);
                            Toast.makeText(EditarPerfilActivity.this, "Error al guardar cambios", Toast.LENGTH_LONG).show();
                        }
                    });
        }

    private void obtenerUsuarioPorCorreo(String correo) {
        CollectionReference usersRef = db.collection("usuarios");
        Query query = usersRef.whereEqualTo("correo", correo);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (!querySnapshot.isEmpty()) {
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            // El documento existe, deserializa a un objeto Usuario
                            usuario = document.toObject(Usuario.class);

                            Log.d(TAG, "Usuario data: " + usuario.getNombre());
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }
    }




    /*private void obtenerUsuarioDeFirestore(String correo) {
        db=FirebaseFirestore.getInstance();
        db.collection("usuarios")
                .document()
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            usuario = documentSnapshot.toObject(Usuario.class);
                            if(usuario != null){
                                Log.d("msg-test", "Nombre: " + usuario.getNombre());
                                Log.d("msg-test", "Correo: " + usuario.getCorreo());
                            }
                        } else {
                            Log.d("nsg-test", "No Existe el Usuario");
                        }
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error al obtener datos personales", Toast.LENGTH_LONG).show();
                    }
                });

        }*/



