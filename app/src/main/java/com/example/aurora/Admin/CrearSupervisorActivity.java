package com.example.aurora.Admin;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import static java.security.AccessController.getContext;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.aurora.Bean.Sitio;
import com.example.aurora.Bean.Usuario;
import com.example.aurora.NotificationDismissReceiver;
import com.example.aurora.R;
import com.example.aurora.databinding.ActivityCrearSupervisorBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
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

    private static final String canal1 = "canal_default";

    private static final String GROUP_KEY = "notis_group";


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

            //String idUsuarioSupervisor = generarIdSupervisor();
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
                    StorageReference imageReference = storageReference.child("fotosUsuario/*" + System.currentTimeMillis() + ".jpg");

                    UploadTask uploadTask = imageReference.putFile(imagenUri);
                    uploadTask.addOnSuccessListener(taskSnapshot -> {
                        imageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            guardarSupervisor(imageUrl,nombreStr,apellidoStr,dniStr,correoStr,domicilioStr,telefonoStr);
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



    public void guardarSupervisor(String imageUrl,String nombreStr,String apellidoStr,
                                  String dniStr,String correoStr,String domicilioStr,String telefonoStr) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser originalUser = auth.getCurrentUser(); // Guardar el usuario actual
        auth.createUserWithEmailAndPassword(correoStr, "123456")
                .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                String userId = task.getResult().getUser().getUid();
                                String rol = "supervisor";
                                String estado = "activo";
                                ArrayList<String> listaSitios = new ArrayList<>();
                                Usuario usuarioSupervisor = new Usuario(userId, nombreStr, apellidoStr, dniStr, correoStr, domicilioStr, telefonoStr, rol, estado, listaSitios, imageUrl);
                                db.collection("usuarios")
                                        .document(userId)
                                        .set(usuarioSupervisor)
                                        .addOnSuccessListener(unused -> {
                                            Log.d("msg-test2", "Supervisor guardado exitosamente");
                                            Toast.makeText(CrearSupervisorActivity.this, "Supervisor creado exitosamente", Toast.LENGTH_SHORT).show();

                                            String title = "Nuevo Supervisor Creado";
                                            String description = "Se creó con exito un nuevo supervisor: " + userId + "-" + nombreStr + " " + apellidoStr;




                                            // Volver a autenticar al usuario original
                                            if (originalUser != null) {
                                                auth.signInWithEmailAndPassword(originalUser.getEmail(), "123456")
                                                        .addOnCompleteListener(signInTask -> {
                                                            if (signInTask.isSuccessful()) {
                                                                Log.d("msg-test4", "Usuario original reautenticado exitosamente");
                                                            } else {
                                                                Log.e("msg-test4", "Error al reautenticar al usuario original", signInTask.getException());
                                                            }
                                                        });
                                            }

                                            // Crear el log de la operación con el UID del usuario original
                                            if (originalUser != null) {
                                                db.collection("usuarios")
                                                        .whereEqualTo("idUsuario", originalUser.getUid())  // Buscar documentos donde el campo 'idUsuario' sea igual a log.getIdUsuario()
                                                        .get()
                                                        .addOnCompleteListener(task1 -> {
                                                            if (task1.isSuccessful() && task1.getResult() != null && !task1.getResult().isEmpty()) {
                                                                // Obtener el primer documento que coincide con la consulta
                                                                DocumentSnapshot document = task1.getResult().getDocuments().get(0);
                                                                Usuario usuario = document.toObject(Usuario.class);
                                                                if (usuario != null) {
                                                                    usuario.setIdUsuario(document.getId());
                                                                    crearLog("Se creó un Supervisor", "Se creó un nuevo supervisor", originalUser.getUid(), null,usuario);
                                                                }
                                                            } else {
                                                                android.util.Log.d("msg-test", "Error getting document: ", task1.getException());
                                                            }
                                                        });

                                            }
                                            notificarImportanceDefault(title, description);
                                            Intent intent = new Intent(CrearSupervisorActivity.this, FragmentSupervisores.class);
                                            startActivity(intent);

                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("msg-test3", "Error al guardar el supervisor", e);
                                            Toast.makeText(CrearSupervisorActivity.this, "Error al crear el supervisor", Toast.LENGTH_SHORT).show();
                                        });

                            } else {
                                Toast.makeText(CrearSupervisorActivity.this, "Error al crear usuario: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CrearSupervisorActivity.this, "Falla en crear usuario: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });

    }

    /*private String generarIdSupervisor() {
        String letrasAdmin = "SUPER";
        Random random = new Random();
        int numeroAleatorio = random.nextInt(900) + 100; // Generar un número entre 100 y 999
        return letrasAdmin+numeroAleatorio;
    }*/

    //basado en gpt
    private boolean correoValido(String correoStr) {
        return Patterns.EMAIL_ADDRESS.matcher(correoStr).matches();
    }

    public void crearLog(String actividad, String descripcion, String idUsuario, Sitio sitio, Usuario usuario){
        Date fechaActual = new Date();

        com.example.aurora.Bean.Log nuevoLog = new com.example.aurora.Bean.Log(fechaActual,  actividad,  descripcion, idUsuario, sitio, usuario);

        db.collection("logs")
                .add(nuevoLog)
                .addOnSuccessListener(documentReference -> {
                    Log.d("msg-test2", "Log guardado exitosamente");
                    Toast.makeText(CrearSupervisorActivity.this, "Actividad Registrada", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("msg-test3", "Error al guardar el log", e);
                    Toast.makeText(CrearSupervisorActivity.this, "Error al registrar la activada", Toast.LENGTH_SHORT).show();
                });
    }


    public void notificarImportanceDefault(String title , String description){

        //Crear notificación
        //Agregar información a la notificación que luego sea enviada a la actividad que se abre
        Intent intent = new Intent(this, NotificationDismissReceiver.class);
        intent.putExtra("pid","4616");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        //
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, canal1)
                .setSmallIcon(R.drawable.baseline_account_circle_24)
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


