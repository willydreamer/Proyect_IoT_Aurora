package com.example.aurora.Admin;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Adapter.SelectedImagesAdapter;
import com.example.aurora.Bean.EquipoAdmin;
import com.example.aurora.NotificationDismissReceiver;
import com.example.aurora.R;
import com.example.aurora.databinding.ActivityCrearEquipoBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class CrearEquipoActivity extends AppCompatActivity {
    private ActivityCrearEquipoBinding binding;

    //private Button botonSubirFoto;

    FirebaseFirestore db;

    private RecyclerView recyclerViewSelectedImages;
    private SelectedImagesAdapter selectedImagesAdapter;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private ImageView imagen;

    private List<Uri> selectedImageUris;
    private Button buttonSelectFotos;

    private List<String> imageUrls;


    //datos equipo

    private EditText SKU;
    private EditText numeroDeSerie;
    private Spinner tipoDeEquipo;

    private EditText marca;
    private EditText modelo;

    private EditText descripcion;

    private Date fechaDeRegistro;

    private TextView fecha;

    private Button botonGuardar;

    private Button botonFecha;

    private static final String canal1 = "canal_default";

    //private static final String GROUP_KEY_EQUIPO = "equipo_group";

    private static final String GROUP_KEY = "notis_group";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCrearEquipoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayAdapter<CharSequence> adapterTipoEquipo = ArrayAdapter.createFromResource(this,
                R.array.tipo_equipo_options, android.R.layout.simple_spinner_item);
        adapterTipoEquipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTipoEquipo.setAdapter(adapterTipoEquipo);

        fecha = findViewById(R.id.fechaRegistro);




        //botonSubirFoto = findViewById(R.id.buttonSubirFoto);

        botonGuardar = findViewById(R.id.buttonGuardar);

        botonFecha = findViewById(R.id.botonFecha);

        imagen = findViewById(R.id.imageView3);

        // Instanciar Firebase
        db = FirebaseFirestore.getInstance();

        buttonSelectFotos = findViewById(R.id.buttonSelectFotos);



        // Botón para abrir la galería
        /*botonSubirFoto.setOnClickListener(v -> {
            Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //todas las imagenes
            pickPhotoIntent.setType("image/*");
            startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK);
        });*/

        recyclerViewSelectedImages = findViewById(R.id.recyclerViewSelectedImages);
        selectedImageUris = new ArrayList<>();
        imageUrls = new ArrayList<>();
        selectedImagesAdapter = new SelectedImagesAdapter(this, selectedImageUris);
        //recyclerViewSelectedImages.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerViewSelectedImages.setLayoutManager((new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)));
        recyclerViewSelectedImages.setAdapter(selectedImagesAdapter);

        buttonSelectFotos.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(intent, REQUEST_IMAGE_PICK);
        });

        botonFecha.setOnClickListener(v ->
                fechaDialog()
        );

        botonGuardar.setOnClickListener(view -> {
            //guardarSupervisor();
            uploadImagesToFirebase();
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
                        selectedImagesAdapter.addPhoto(imageUri);
                    }
                } else if (data.getData() != null) {
                    Uri imageUri = data.getData();
                    selectedImagesAdapter.addPhoto(imageUri);
                }
            }
        }
    }


    private void uploadImagesToFirebase() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

        for (Uri imageUri : selectedImageUris) {
            StorageReference storageReference = storage.getReference().child("fotosEquipo/" + System.currentTimeMillis() + ".jpg");
            storageReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        imageUrls.add(uri.toString());
                        if (imageUrls.size() == selectedImageUris.size()) {
                            // Todas las imágenes se han subido, ahora guarda las URLs en Firestore
                            //Toast.makeText(CrearEquipoActivity.this,"Equipo Guardado Correctamente", Toast.LENGTH_LONG).show();
                            uploadImageAndSaveUserData();
                        }
                    }))
                    .addOnFailureListener(e -> Toast.makeText(this, "Error al Subir Fotos del Equipo: " + e.getMessage(), Toast.LENGTH_LONG).show());
        }
    }





    //basado en gpt
    /*@Override
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
    }*/



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
        if (!imageUrls.isEmpty()) {
            SKU = findViewById(R.id.editSKU);
            numeroDeSerie = findViewById(R.id.editNumeroSerie);
            tipoDeEquipo = findViewById(R.id.spinnerTipoEquipo);
            marca = findViewById(R.id.editMarca);
            modelo = findViewById(R.id.editModelo);
            descripcion = findViewById(R.id.editDescripcion);


            String tipoDeEquipoStr = tipoDeEquipo.getSelectedItem().toString();
            String idEquipo = generarIdEquipo(tipoDeEquipoStr);
            String SKUstr = SKU.getEditableText().toString();
            String numeroDeSerieStr = numeroDeSerie.getEditableText().toString();
            String marcaStr = marca.getEditableText().toString();
            String modeloStr = modelo.getEditableText().toString();
            String descripcionStr = descripcion.getEditableText().toString();

            if(!tipoDeEquipoStr.isEmpty() && !SKUstr.isEmpty() && !numeroDeSerieStr.isEmpty() && !marcaStr.isEmpty() && !modeloStr.isEmpty() && !descripcionStr.isEmpty() && fechaDeRegistro!=null) {
                if(descripcionStr.length()>250) {
                    Toast.makeText(CrearEquipoActivity.this, "Máximo 250 Caracteres para Descripción", Toast.LENGTH_LONG).show();
                }else{
                    /*
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                    StorageReference imageReference = storageReference.child("images/" + System.currentTimeMillis() + ".jpg");
                    StorageReference imageReference = storageReference.child("fotosEquipo/" + System.currentTimeMillis() + ".jpg");

                    UploadTask uploadTask = imageReference.putFile(imageUris);
                    uploadTask.addOnSuccessListener(taskSnapshot -> {
                        imageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            //saveUserDataToFirestore(imageUrl);
                            guardarEquipo(imageUrl, idEquipo, tipoDeEquipoStr, SKUstr, numeroDeSerieStr, marcaStr, modeloStr, descripcionStr,imageUrls);
                        });
                    }).addOnFailureListener(e -> {
                        Toast.makeText(CrearEquipoActivity.this, "Falla en subir Fotos del Equipo: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });*/
                    guardarEquipo(idEquipo, tipoDeEquipoStr, SKUstr, numeroDeSerieStr, marcaStr, modeloStr, descripcionStr,imageUrls);
                }
            }else{
                Toast.makeText(this, "No Deben haber Campos vacios", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Fotos no seleccionadas", Toast.LENGTH_LONG).show();
        }
    }

    public void guardarEquipo(String idEquipo,String tipoDeEquipoStr,String SKUstr,String numeroDeSerieStr,String marcaStr,String modeloStr,String descripcionStr,List<String> imageUrls) {


        ArrayList<String> sitios = new ArrayList<>();
        String estado = "operativo";

        //EquipoAdmin equipo = new EquipoAdmin(idEquipo,SKUstr,numeroDeSerieStr ,tipoDeEquipoStr,marcaStr,modeloStr,descripcionStr,fechaDeRegistro,imageUrl,sitios,estado);
        EquipoAdmin equipo = new EquipoAdmin(idEquipo,SKUstr,numeroDeSerieStr ,tipoDeEquipoStr,marcaStr,modeloStr,descripcionStr,fechaDeRegistro,sitios,estado,imageUrls);


        // Guardar los datos en Firestore
        db.collection("equipos")
                .document(idEquipo)
                .set(equipo)
                .addOnSuccessListener(unused -> {
                    Log.d("msg-test2", "equipo guardado exitosamente");
                    Toast.makeText(CrearEquipoActivity.this, "equipo creado exitosamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CrearEquipoActivity.this, FragmentEquipos.class);
                     startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Log.e("msg-test3", "Error al guardar el equipo", e);
                    Toast.makeText(CrearEquipoActivity.this, "Error al crear el equipo", Toast.LENGTH_SHORT).show();
                });

        notificarImportanceDefault(tipoDeEquipoStr,idEquipo);


    }


    private String generarIdEquipo(String tipoDeEquipoStr) {
        String letrasEquipo = tipoDeEquipoStr.substring(0, Math.min(tipoDeEquipoStr.length(), 3)).toUpperCase();
        Random random = new Random();
        int numeroAleatorio = random.nextInt(900) + 100; // Generar un número entre 100 y 999
        return letrasEquipo + numeroAleatorio;
    }

    private void fechaDialog() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    fechaDeRegistro = calendar.getTime();
                    fecha.setText(fechaDeRegistro.toString());
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void notificarImportanceDefault(String tipoDeEquipoStr, String idEquipo){

        //Crear notificación
        //Agregar información a la notificación que luego sea enviada a la actividad que se abre
        Intent intent = new Intent(this, NotificationDismissReceiver.class);
        intent.putExtra("pid","4616");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        //
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, canal1)
                .setSmallIcon(R.drawable.splitscreen_add_24px)
                .setContentTitle("Nuevo Equipo Creado")
                .setContentText("Se creó con exito un nuevo equipo: "+ tipoDeEquipoStr+": "+idEquipo)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setGroup(GROUP_KEY)
                .setDeleteIntent(pendingIntent);

        Notification notification = builder.build();

        //Lanzar notificación
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify((int) System.currentTimeMillis(), notification);
        }

        // Crear una notificación de resumen para el grupo
        Notification summaryNotification = new NotificationCompat.Builder(this, canal1)
                .setContentTitle("Notificaciones")
                .setContentText("Tienes Nuevas notificaciones")
                .setSmallIcon(R.drawable.netwise_1000)
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine("Notificaciones")
                        .setBigContentTitle("Notificaciones")
                        .setSummaryText("Resúmen de notificaciones"))
                .setGroup(GROUP_KEY)
                .setGroupSummary(true)
                .build();

        notificationManager.notify(0, summaryNotification);

    }

}
