package com.example.aurora.Admin;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.aurora.Bean.Sitio;
import com.example.aurora.Bean.Usuario;
import com.example.aurora.NotificationDismissReceiver;
import com.example.aurora.R;
import com.example.aurora.databinding.ActivityCrearSitioBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class CrearSitioActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener{
    private ActivityCrearSitioBinding binding;
    FirebaseFirestore db;
    EditText txtLatitud, txtLongitud;
    GoogleMap mMap;
    Dialog mapDialog;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private ImageView imagen;
    private String imageUrl;
    private Uri imagenUri;

    private static final String canal1 = "canal_default";

    private static final String GROUP_KEY = "notis_group";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCrearSitioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Mostrar las opciones por defecto en los Spinners

        ArrayAdapter<CharSequence> adapterTipoZona = ArrayAdapter.createFromResource(this,
                R.array.tipo_zona_options, android.R.layout.simple_spinner_item);
        adapterTipoZona.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTipoZona.setAdapter(adapterTipoZona);


        ArrayAdapter<CharSequence> adapterOperadora = ArrayAdapter.createFromResource(this,
                R.array.operadora_options, android.R.layout.simple_spinner_item);
        adapterOperadora.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerOperadora.setAdapter(adapterOperadora);


        binding.buttonGuardar.setOnClickListener(v -> {
            Intent intent = new Intent(CrearSitioActivity.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Sitio creado exitosamente", Toast.LENGTH_SHORT).show();
        });

        // Instanciar Firebase
        db = FirebaseFirestore.getInstance();
        binding.buttonGuardar.setOnClickListener(v -> {
            guardarSitio();
        });

        //Mapa
        // Initialize EditText fields
        txtLatitud = binding.editLatitud;
        txtLongitud = binding.editLongitud;
        binding.btnSelectCoordinates.setOnClickListener(v -> openMapDialog());

        //Imagen
        // Botón para abrir la galería

        imagen = findViewById(R.id.imageView3);
        Button botonSubirFoto =  findViewById(R.id.buttonSubirFoto);
        botonSubirFoto.setOnClickListener(v -> {
            Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //todas las imagenes
            pickPhotoIntent.setType("image/*");
            startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK);
        });

    }

    private void guardarSitio() {
        if (imagenUri != null) {
            // Obtener los datos ingresados por el usuario
            String departamento = binding.editDepartamento.getText().toString();
            String idSitio = generarIdSitio(departamento);
            String provincia = binding.editProvincia.getText().toString();
            String distrito = binding.editDistrito.getText().toString();
            String tipoDeZona = binding.spinnerTipoZona.getSelectedItem().toString();
            String latitud = binding.editLatitud.getText().toString();
            String longitud = binding.editLongitud.getText().toString();
            String operadora = binding.spinnerOperadora.getSelectedItem().toString();
            //ArrayList<Usuario> supervisor = new ArrayList<>();
            String encargado = "";

            // Crear un objeto Sitio con los datos obtenidos

            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference imageReference = storageReference.child("fotosUsuario/*" + System.currentTimeMillis() + ".jpg");

            UploadTask uploadTask = imageReference.putFile(imagenUri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                imageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    imageUrl = uri.toString();
                    guardarSitio(idSitio, departamento, provincia, distrito, tipoDeZona, latitud, longitud, operadora, encargado, imageUrl);

                });
            }).addOnFailureListener(e -> {
                Toast.makeText(CrearSitioActivity.this, "Falla en subir foto: " + e.getMessage(), Toast.LENGTH_LONG).show();
            });

        }else {
                Toast.makeText(this, "Foto no seleccionada", Toast.LENGTH_LONG).show();
            }
    }
    private String generarIdSitio(String departamento) {
        String letrasDepartamento = departamento.substring(0, Math.min(departamento.length(), 3)).toUpperCase();
        Random random = new Random();
        int numeroAleatorio = random.nextInt(900) + 100; // Generar un número entre 100 y 999
        return letrasDepartamento + numeroAleatorio;
    }

    public void guardarSitio(String idSitio, String departamento, String provincia, String distrito, String tipoDeZona, String latitud, String longitud,String operadora,String encargado,String imageUrl) {
        Sitio sitio = new Sitio(idSitio, departamento, provincia, distrito, tipoDeZona, latitud, longitud, operadora, encargado, imageUrl);

        // Guardar los datos en Firestore
        db.collection("sitios")
                .document(idSitio)
                .set(sitio)
                .addOnSuccessListener(unused -> {
                    Log.d("msg-test", "Sitio guardado exitosamente");
                    Toast.makeText(CrearSitioActivity.this, "Sitio creado exitosamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CrearSitioActivity.this, MainActivity.class);
                    intent.putExtra("fragment", "sitios");
                    startActivity(intent);
                    notificarImportanceDefault("Nuevo Sitio Creado", "Se creó con exito el nuevo sitio con ID:"+idSitio);
                    finish(); // Cerrar la actividad actual
                })
                .addOnFailureListener(e -> {
                    Log.e("msg-test", "Error al guardar el sitio", e);
                    Toast.makeText(CrearSitioActivity.this, "Error al crear el sitio", Toast.LENGTH_SHORT).show();
                });

    }

    private void openMapDialog() {

        mapDialog = new Dialog(this);
        mapDialog.setContentView(R.layout.dialog_map);
        mapDialog.setCancelable(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.map_container, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        mapDialog.show();
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Set initial location and zoom level
        LatLng initialLocation = new LatLng(-13.515207, -71.968307);
        float zoomLevel = 10.0f;

        mMap.addMarker(new MarkerOptions()
                .position(initialLocation)
                .title("Perú"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, zoomLevel));

        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        // Format latitude and longitude to display only 7 decimal places
        DecimalFormat df = new DecimalFormat("#0.0000000");
        String formattedLatitude = df.format(latLng.latitude);
        String formattedLongitude = df.format(latLng.longitude);

        // Update text views with formatted values
        txtLatitud.setText(formattedLatitude);
        txtLongitud.setText(formattedLongitude);

        // Rest of your code for marker placement and camera movement
        mMap.clear();
        LatLng selectedLocation = new LatLng(latLng.latitude, latLng.longitude);
        mMap.addMarker(new MarkerOptions().position(selectedLocation).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(selectedLocation));
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        // Reuse the formatted latitude and longitude from onMapClick
        DecimalFormat df = new DecimalFormat("#0.0000000");
        String formattedLatitude = df.format(latLng.latitude);
        String formattedLongitude = df.format(latLng.longitude);

        // Update text views with formatted values
        txtLatitud.setText(formattedLatitude);
        txtLongitud.setText(formattedLongitude);

        // Rest of your code for marker placement and camera movement
        mMap.clear();
        LatLng selectedLocation = new LatLng(latLng.latitude, latLng.longitude);
        mMap.addMarker(new MarkerOptions().position(selectedLocation).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(selectedLocation));
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

    private void setImageUriWithCircularTransformation(Uri uri) {
        Picasso.get()
                .load(uri)
                .placeholder(R.drawable.baseline_account_circle_24) // Reemplaza con tu imagen por defecto
                .transform(new CropCircleTransformation())
                .into(imagen);
    }

    public void notificarImportanceDefault(String title , String description) {

        //Crear notificación
        //Agregar información a la notificación que luego sea enviada a la actividad que se abre
        Intent intent = new Intent(this, NotificationDismissReceiver.class);
        intent.putExtra("pid", "4616");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        //
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, canal1)
                .setSmallIcon(R.drawable.editicon)
                .setContentTitle(title)
                .setContentText(description)
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