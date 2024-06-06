package com.example.aurora;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aurora.Bean.Usuario;
import com.example.aurora.databinding.ActivityCrearSupervisorBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


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


        //inicio foto

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
                //imagenUri = getImageUri(imageBitmap);
                setImageUriWithCircularTransformation(imagenUri);
            } else if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                imagenUri = data.getData();
                imagen.setImageURI(imagenUri);
                setImageUriWithCircularTransformation(imagenUri);
            }
        }
    }

    private Uri getImageUri(Bitmap bitmap) {
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }


    private void setImageUriWithCircularTransformation(Uri uri) {
        Picasso.get()
                .load(uri)
                .placeholder(R.drawable.baseline_account_circle_24) // Reemplaza con tu imagen por defecto
                .transform(new CropCircleTransformation())
                .into(imagen);
    }

    private void uploadImageAndSaveUserData() {
        if (imagenUri != null) {

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

            if(!nombreStr.isEmpty() && !apellidoStr.isEmpty() && !dniStr.isEmpty() && !correoStr.isEmpty() && !domicilioStr.isEmpty() && !telefonoStr.isEmpty()) {
                if(dniStr.length()!=8){
                    Toast.makeText(CrearSupervisorActivity.this, "DNI debe ser de 8 digitos", Toast.LENGTH_LONG).show();
                }else if(telefonoStr.length()!=9) {
                    Toast.makeText(CrearSupervisorActivity.this, "Telefono debe ser de 9 digitos", Toast.LENGTH_LONG).show();
                }else if(!correoValido(correoStr)) {
                    Toast.makeText(CrearSupervisorActivity.this, "Correo No Valido", Toast.LENGTH_LONG).show();
                }else{
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                    //StorageReference imageReference = storageReference.child("images/" + System.currentTimeMillis() + ".jpg");
                    StorageReference imageReference = storageReference.child("fotosUsuario/*" + System.currentTimeMillis() + ".jpg");

                    UploadTask uploadTask = imageReference.putFile(imagenUri);
                    uploadTask.addOnSuccessListener(taskSnapshot -> {
                        imageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            //saveUserDataToFirestore(imageUrl);
                            guardarSupervisor(imageUrl,idUsuarioSupervisor,nombreStr,apellidoStr,dniStr,correoStr,domicilioStr,telefonoStr);
                        });
                    }).addOnFailureListener(e -> {
                        Toast.makeText(CrearSupervisorActivity.this, "Falla en subir foto: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }
            } else{
                Toast.makeText(CrearSupervisorActivity.this, "No Debe Haber Campos Vacios", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Foto no seleccionada", Toast.LENGTH_LONG).show();
        }
    }

    //fin foto


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



    public void guardarSupervisor(String imageUrl,String idUsuarioSupervisor,String nombreStr,String apellidoStr,
                                  String dniStr,String correoStr,String domicilioStr,String telefonoStr) {

        /*nombre = findViewById(R.id.editText);
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
        String telefonoStr = telefono.getEditableText().toString();*/
        String rol = "supervisor";
        String estado = "activo";
        //ArrayList<Sitio> listaSitios = new ArrayList<>();
        ArrayList<String> listaSitios = new ArrayList<>();
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

    //basado en gpt
    private boolean correoValido(String correoStr) {
        return Patterns.EMAIL_ADDRESS.matcher(correoStr).matches();
    }
}


