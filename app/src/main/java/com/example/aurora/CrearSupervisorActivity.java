package com.example.aurora;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.aurora.Bean.Sitio;
import com.example.aurora.Bean.Supervisor;
import com.example.aurora.Bean.Usuario;
import com.example.aurora.databinding.ActivityCrearSupervisorBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;


public class CrearSupervisorActivity extends AppCompatActivity {

    private EditText nombre;
    private EditText apellido;
    private EditText dni;

    private EditText correo;
    private EditText telefono;

    private EditText domicilio;

    private Button botonGuardar;

    FirebaseFirestore db;


    ActivityCrearSupervisorBinding binding;


    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private ImageView imagen;
    private Uri imagenUri;

    //String storage_path = "fotosUsuario/*";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_supervisor);

        // Instanciar Firebase
        db = FirebaseFirestore.getInstance();

        botonGuardar = findViewById(R.id.buttonGuardar);

        imagen = findViewById(R.id.imageView3);

        Button botonSubirFoto =  findViewById(R.id.buttonSubirFoto);

        botonGuardar.setOnClickListener(view->{
            //guardarSupervisor();
            uploadImageAndSaveUserData();
        });

        // Botón para abrir la cámara
        /*findViewById(R.id.btnCamera).setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });*/

        // Botón para abrir la galería
        botonSubirFoto.setOnClickListener(v -> {
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
                imagen.setImageBitmap(imageBitmap);
                imagenUri = getImageUri(imageBitmap);
            } else if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                imagenUri = data.getData();
                imagen.setImageURI(imagenUri);
            }
        }
    }

    private Uri getImageUri(Bitmap bitmap) {
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    private void uploadImageAndSaveUserData() {
        if (imagenUri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            //StorageReference imageReference = storageReference.child("images/" + System.currentTimeMillis() + ".jpg");
            StorageReference imageReference = storageReference.child("fotosUsuario/*" + System.currentTimeMillis() + ".jpg");

            UploadTask uploadTask = imageReference.putFile(imagenUri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                imageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    //saveUserDataToFirestore(imageUrl);
                    guardarSupervisor(imageUrl);
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(CrearSupervisorActivity.this, "Falla en subir foto: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(this, "Foto no seleccionada", Toast.LENGTH_SHORT).show();
        }
    }

    /*private void saveUserDataToFirestore(String userId, String userName, String imageUrl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Usuario usuario = new Usuario(userId, userName, imageUrl);

        db.collection("usuarios").document(userId)
                .set(usuario)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ProfileActivity.this, "User data saved to Firestore", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ProfileActivity.this, "Failed to save user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }*/



    public void guardarSupervisor(String imageUrl) {

        nombre = findViewById(R.id.editText);
        apellido = findViewById(R.id.editText1);
        dni = findViewById(R.id.editText2);
        correo = findViewById(R.id.editText3);
        domicilio = findViewById(R.id.editText4);
        telefono = findViewById(R.id.editText5);

        String idUsuarioSupervisor = generarIdSupervisor();
        String nombreStr = nombre.getEditableText().toString();
        String apellidoStr = apellido.getEditableText().toString();
        String dniStr = dni.getEditableText().toString();
        String correoStr = correo.getEditableText().toString();
        String domicilioStr = domicilio.getEditableText().toString();
        String telefonoStr = telefono.getEditableText().toString();
        String rol = "supervisor";
        String estado = "activo";
        ArrayList<Sitio> listaSitios = new ArrayList<>();
        Usuario usuarioSupervisor = new Usuario(idUsuarioSupervisor, nombreStr, apellidoStr, dniStr, correoStr, domicilioStr, telefonoStr,rol,estado,listaSitios,imageUrl);


        // Guardar los datos en Firestore
        db.collection("usuarios")
                .document(idUsuarioSupervisor)
                .set(usuarioSupervisor)
                .addOnSuccessListener(unused -> {
                    Log.d("msg-test2", "Supervisor guardado exitosamente");
                    Toast.makeText(CrearSupervisorActivity.this, "Supervisor creado exitosamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CrearSupervisorActivity.this, AdminSupervisoresFragment.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Log.e("msg-test3", "Error al guardar el supervisor", e);
                    Toast.makeText(CrearSupervisorActivity.this, "Error al crear el supervisor", Toast.LENGTH_SHORT).show();
                });

    }


    private String generarIdSupervisor() {
        String letrasAdmin = "SUPER";
        Random random = new Random();
        int numeroAleatorio = random.nextInt(900) + 100; // Generar un número entre 100 y 999
        return letrasAdmin+numeroAleatorio;
    }
}

   /* public void listarArchivosGuardados(){
        String[] archivosGuardados = fileList();

        for(String archivo: archivosGuardados){
            Log.d("archivo",archivo);
        }
    }

    public void leerArchivoObjeto() {
        String fileName = "listaSupervisores";
        try (FileInputStream fileInputStream = openFileInput(fileName);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            ArrayList<Supervisor> arregloSupervisores = (ArrayList<Supervisor>) objectInputStream.readObject();
            for(Supervisor s: arregloSupervisores ){
                Log.d("super:",s.getNombre());
            }
        } catch (FileNotFoundException | ClassNotFoundException e) {
            e.printStackTrace();
            Log.e("super", "Error al leer el supervisor guardado", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    } */


