package com.example.aurora;

//Importaciones

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
import com.example.aurora.R;
import com.example.aurora.databinding.ActivityCrearSupervisorBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;



//basado en "CrearSupervisorActivity" de Admin
public class SubiryGuardarFotos {

    //atributos para el activity

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private ImageView imagen;

    private Uri imagenUri;
    private Button botonSubirFoto;

    private Button botonGuardar;


    /* en onCreate():

    //Instanciar Firebase_
    db = FirebaseFirestore.getInstance();

    //obtenemos la imagen
    imagen = findViewById(R.id.imageView3);


    // obtenemos los botones:


    botonSubirFoto =  findViewById(R.id.buttonSubirFoto);

    botonGuardar = findViewById(R.id.buttonGuardar);



    //Para abrir galería:
        botonSubirFoto.setOnClickListener(v -> {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //todas las imagenes
        pickPhotoIntent.setType("image/*");
        startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK);
    });

    //para guardar con foto:
    botonGuardar.setOnClickListener(view->{
            uploadImageAndSaveUserData();
        });

    //Fin de OnCreate()



    //Agregar los siguientes metodos:


    1)
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


    2)
    private void setImageUriWithCircularTransformation(Uri uri) {
        Picasso.get()
                .load(uri)
                .placeholder(R.drawable.baseline_account_circle_24) // Reemplaza con tu imagen por defecto
                .transform(new CropCircleTransformation())
                .into(imagen);
    }


    3)
    private void uploadImageAndSaveUserData() {
        //se valida si se subió una imagen
        if (imagenUri != null) {

            //Esta parte es mas que nada para
            validar los atributos del supervisor antes de guardar en bd



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


            //1* validacion
            if(!nombreStr.isEmpty() && !apellidoStr.isEmpty() && !dniStr.isEmpty() && !correoStr.isEmpty() && !domicilioStr.isEmpty() && !telefonoStr.isEmpty()) {
             //2* validacion
                if(dniStr.length()!=8){
                    Toast.makeText(CrearSupervisorActivity.this, "DNI debe ser de 8 digitos", Toast.LENGTH_LONG).show();
                //3* validacion
                }else if(telefonoStr.length()!=9) {
                    Toast.makeText(CrearSupervisorActivity.this, "Telefono debe ser de 9 digitos", Toast.LENGTH_LONG).show();
                 //4* validacion
                }else if(!correoValido(correoStr)) {
                    Toast.makeText(CrearSupervisorActivity.this, "Correo No Valido", Toast.LENGTH_LONG).show();
                }else{
                    //Subimos la foto en BD
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                    StorageReference imageReference = storageReference.child("fotosUsuario/" + System.currentTimeMillis() + ".jpg");
                                                                            ("fotosEquipo/"  + System.currentTimeMillis() + ".jpg");
                                                                            (si se sube foto para equipo)

                    UploadTask uploadTask = imageReference.putFile(imagenUri);
                    uploadTask.addOnSuccessListener(taskSnapshot -> {
                        imageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            //Una vez que ya se subio la imagen en BD , obtenemos la URL de la imagen:
                            --> String imageUrl = uri.toString();
                            //y guardarmos el objeto en BD.
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


    4)
    public void guardarSupervisor(String imageUrl,String idUsuarioSupervisor,String nombreStr,String apellidoStr,
                                  String dniStr,String correoStr,String domicilioStr,String telefonoStr) {

        String rol = "supervisor";
        String estado = "activo";
        ArrayList<String> listaSitios = new ArrayList<>();
        Usuario usuarioSupervisor = new Usuario(idUsuarioSupervisor, nombreStr, apellidoStr, dniStr, correoStr, domicilioStr, telefonoStr,rol,estado,listaSitios,imageUrl);


        // Guardar los datos en Firestore
        db.collection("usuarios")
                .document(idUsuarioSupervisor)
                .set(usuarioSupervisor)
                .addOnSuccessListener(unused -> {
                    Log.d("msg-test2", "Supervisor guardado exitosamente");
                    Toast.makeText(CrearSupervisorActivity.this, "Supervisor creado exitosamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CrearSupervisorActivity.this, FragmentSupervisores.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Log.e("msg-test3", "Error al guardar el supervisor", e);
                    Toast.makeText(CrearSupervisorActivity.this, "Error al crear el supervisor", Toast.LENGTH_SHORT).show();
                });

    }

    //ADICIONAL: CARGAR LA FOTO DEL USUARIO GUARDADO EN OTR0 ACTIVITY:
    //Basado en InformacionSupervisorActivity

    //En OnCreate():

    //Ejm: supervisor:
    //obtenemos
    Usuario supervisor = (Usuario) getIntent().getSerializableExtra("supervisor");

    //si el usuario tiene foto
    if (supervisor.getFotoURL() != null && !supervisor.getFotoURL().isEmpty()) {
            Picasso.get()
                    .load(supervisor.getFotoURL())
                    .placeholder(R.drawable.perfil_icono) // Reemplaza con tu imagen por defecto
                    .transform(new CropCircleTransformation())
                    .into(fotoSupervisor);
        } else {
        //si no tiene tiene foto
            Picasso.get()
                    .load(R.drawable.perfil_icono) // Imagen por defecto
                    .transform(new CropCircleTransformation())
                    .into(fotoSupervisor);
        }


     */
}





