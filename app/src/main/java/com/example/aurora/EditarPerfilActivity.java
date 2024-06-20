package com.example.aurora;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aurora.Admin.CrearSupervisorActivity;
import com.example.aurora.Bean.Usuario;
import com.example.aurora.databinding.ActivityEditarPerfilBinding;
import com.example.aurora.databinding.ActivityInformacionEquipoBinding;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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

    private Button editar;

    private Button subirFoto;

    private Button guardar;


    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditarPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        db = FirebaseFirestore.getInstance();

        fotoUsuario = findViewById(R.id.imageUsuario);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        usuarioLogueado = firebaseAuth.getCurrentUser();
        if (usuarioLogueado != null) { //user logged-in
            //Ojo uid del firebase user =! id del Document
            String correo = usuarioLogueado.getEmail();
            Log.d("correo", correo);
            //obtenerUsuarioPorCorreo(correo);
            db.collection("usuarios")
                    .whereEqualTo("correo", correo)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("document", document.toString());
                                    usuario = document.toObject(Usuario.class);
                                    Log.d("usuario", usuario.getNombre());

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

                                    binding.textViewNamePrincipal.setText(usuario.getIdUsuario());
                                    binding.nombre.setText(usuario.getNombre());
                                    binding.apellido.setText(usuario.getApellido());
                                    binding.dni.setText(usuario.getDni());
                                    binding.correo.setText(usuario.getCorreo());
                                    binding.domicilio.setText(usuario.getDomicilio());
                                    binding.telefono.setText(usuario.getTelefono());

                                }
                            } else {
                                Log.d("Error", "no se encontro usuario");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
        }

        editar = findViewById(R.id.editar);
        guardar = findViewById(R.id.btnGuardar);
        subirFoto = findViewById(R.id.buttonSubirFoto);

        editar.setOnClickListener(v -> toggleEditMode(true));
        guardar.setOnClickListener(v -> {
            toggleEditMode(false);
            saveChanges();
        });

        //inicio foto

        // Botón para abrir la galería
        subirFoto.setOnClickListener(v -> {
            Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //todas las imagenes
            pickPhotoIntent.setType("image/*");
            startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK);
        });
    }

    //basado en gpt
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                fotoUsuario.setImageBitmap(imageBitmap);
                //imagenUri = getImageUri(imageBitmap);
                setImageUriWithCircularTransformation(imagenUri);
            } else if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                imagenUri = data.getData();
                fotoUsuario.setImageURI(imagenUri);
                setImageUriWithCircularTransformation(imagenUri);
            }
        }
    }

    private void setImageUriWithCircularTransformation(Uri uri) {
        Picasso.get()
                .load(uri)
                .placeholder(R.drawable.baseline_account_circle_24) // Reemplaza con tu imagen por defecto
                .transform(new CropCircleTransformation())
                .into(fotoUsuario);
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


        db = FirebaseFirestore.getInstance();
        String telefono = binding.telefono.getText().toString();

        //if (imagenUri != null) {
        if (telefono.isEmpty() || telefono.length() != 9) {
            Toast.makeText(this, "El Telefono debe ser de 9 digitos", Toast.LENGTH_LONG).show();
        } else {
            if (imagenUri != null) {
                // Actualizar el telefono en el usuario
                usuario.setTelefono(telefono);

                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference imageReference = storageReference.child("fotosUsuario/" + System.currentTimeMillis() + ".jpg");

                UploadTask uploadTask = imageReference.putFile(imagenUri);
                uploadTask.addOnSuccessListener(taskSnapshot -> {
                    imageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        usuario.setFotoURL(imageUrl);
                        //saveUserDataToFirestore(imageUrl);
                        //guardarSupervisor(imageUrl,idUsuarioSupervisor,nombreStr,apellidoStr,dniStr,correoStr,domicilioStr,telefonoStr);
                        // Guardar los cambios en la base de datos
                        db.collection("usuarios")
                                .document(usuario.getIdUsuario())
                                .set(usuario)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(EditarPerfilActivity.this, "Cambios guardados con Éxito", Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("AdminInformacionSitio", "Error al Guardar Usuario", e);
                                        Toast.makeText(EditarPerfilActivity.this, "Error al Guardar Cambios", Toast.LENGTH_LONG).show();
                                    }
                                });
                    });

                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Falla en Subir Foto: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            } else {
                usuario.setTelefono(telefono);
                db.collection("usuarios")
                        .document(usuario.getIdUsuario())
                        .set(usuario)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditarPerfilActivity.this, "Cambios guardados con Éxito", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("AdminInformacionSitio", "Error al Guardar Usuario", e);
                                Toast.makeText(EditarPerfilActivity.this, "Error al Guardar Cambios", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        }


    }
}

    /*private void obtenerUsuarioPorCorreo(String correo) {

        db = FirebaseFirestore.getInstance();

        db.collection("usuarios")
                .whereEqualTo("correo", correo)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Mapear los datos del documento a un objeto Usuario
                                Usuario usuarioBD = document.toObject(Usuario.class);
                                Log.d("msg-tes: " ,usuarioBD.getNombre());
                            }
                        } else {
                            System.out.println("Error al obtener los datos: " + task.getException());
                        }
                    }
                });
        /*CollectionReference usersRef = db.collection("usuarios");
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
        });*/
       // return usuarioBD;






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



