package com.example.aurora;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aurora.databinding.ActivityCrearEquipoBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class CrearEquipoActivity extends AppCompatActivity {
    private ActivityCrearEquipoBinding binding;

    private Button botonSubirFoto;

    FirebaseFirestore db;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private ImageView imagen;
    private Uri imagenUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCrearEquipoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayAdapter<CharSequence> adapterTipoEquipo = ArrayAdapter.createFromResource(this,
                R.array.tipo_equipo_options, android.R.layout.simple_spinner_item);
        adapterTipoEquipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTipoEquipo.setAdapter(adapterTipoEquipo);


        botonSubirFoto = findViewById(R.id.buttonSubirFoto);

        imagen = findViewById(R.id.imageView3);

        // Instanciar Firebase
        db = FirebaseFirestore.getInstance();


        // Botón para abrir la galería
        botonSubirFoto.setOnClickListener(v -> {
            Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //todas las imagenes
            pickPhotoIntent.setType("image/*");
            startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK);
        });

    }
}

    /*

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
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            //StorageReference imageReference = storageReference.child("images/" + System.currentTimeMillis() + ".jpg");
            StorageReference imageReference = storageReference.child("fotosEquipo/*" + System.currentTimeMillis() + ".jpg");

            UploadTask uploadTask = imageReference.putFile(imagenUri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                imageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    //saveUserDataToFirestore(imageUrl);
                    guardarEquipo(imageUrl);
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(CrearEquipoActivity.this, "Falla en subir foto: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(this, "Foto no seleccionada", Toast.LENGTH_SHORT).show();
        }
    }

    public void guardarEquipo(String imageUrl) {

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
        //ArrayList<Sitio> listaSitios = new ArrayList<>();
        ArrayList<String> listaSitios = new ArrayList<>();
        Usuario usuarioSupervisor = new Usuario(idUsuarioSupervisor, nombreStr, apellidoStr, dniStr, correoStr, domicilioStr, telefonoStr,rol,estado,listaSitios,imageUrl);


        // Guardar los datos en Firestore
        db.collection("equipos")
                .document(idUsuarioSupervisor)
                .set(usuarioSupervisor)
                .addOnSuccessListener(unused -> {
                    Log.d("msg-test2", "Supervisor guardado exitosamente");
                    Toast.makeText(CrearEquipoActivity.this, "Supervisor creado exitosamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CrearEquipoActivity.this, AdminSupervisoresFragment.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Log.e("msg-test3", "Error al guardar el supervisor", e);
                    Toast.makeText(CrearEquipoActivity.this, "Error al crear el supervisor", Toast.LENGTH_SHORT).show();
                });

    }


    private String generarIdEquipo() {
        String letrasAdmin = "SUPER";
        Random random = new Random();
        int numeroAleatorio = random.nextInt(900) + 100; // Generar un número entre 100 y 999
        return letrasAdmin+numeroAleatorio;
    }
}
*/