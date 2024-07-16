package com.example.aurora.Admin;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.aurora.Bean.Sitio;
import com.example.aurora.Bean.Usuario;
import com.example.aurora.NotificationDismissReceiver;
import com.example.aurora.R;
import com.example.aurora.databinding.ActivityAsignarSitioBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

public class AsignarSitioActivity extends AppCompatActivity {

    ActivityAsignarSitioBinding binding;

    FirebaseFirestore db;

    private static final String canal1 = "canal_default";

    private static final String GROUP_KEY = "notis_group";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_asignar_sitio);

        // Instanciar Firebase
        db = FirebaseFirestore.getInstance();

        Button asignarSitio = findViewById(R.id.button11);
        asignarSitio.setOnClickListener(view->{
            mostrarDialog2();
        });

        Usuario supervisor = (Usuario) getIntent().getSerializableExtra("supervisor");

        Sitio sitio = (Sitio) getIntent().getSerializableExtra("sitio");
    }

    public void mostrarDialog2() {

        //Usuario supervisor2 = (Usuario) getIntent().getSerializableExtra("supervisor");

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("¿Estas Seguro de Continuar?");

        alertDialog.setPositiveButton("Si", (dialogInterface, i) -> {
            Sitio sitio = (Sitio) getIntent().getSerializableExtra("sitio");
            Usuario supervisor = (Usuario) getIntent().getSerializableExtra("supervisor");
            //Log.d("listasitios", String.valueOf(supervisor.getSitios()));
            if (supervisor.getSitios() == null) {
                ArrayList<String> sitios = new ArrayList<>();
                supervisor.setSitios(sitios); // Inicializa la lista si es nula
            }
            /*if (sitio.getEncargado() == null) {
                sitio.setSupervisor(new ArrayList<>()); // Inicializa la lista si es nula
            }*/

            supervisor.getSitios().add(sitio.getIdSitio());
            sitio.setEncargado(supervisor.getIdUsuario());


            //JsonObject supervisorJson = JsonUtils.serializeUsuario(supervisor);
            //JsonObject sitioJson = JsonUtils.serializeSitio(sitio);

            // Convertir JsonObject a Map<String, Object>
            //Map<String, Object> supervisorMap = JsonUtils.convertJsonObjectToMap(supervisorJson);
            //Map<String, Object> sitioMap = JsonUtils.convertJsonObjectToMap(sitioJson);
            //Usuario supervisor_copy = supervisor;
            //sitio.getSupervisor().add(supervisor);
            //Guardar los datos en Firestore
            db.collection("usuarios")
                    .document(supervisor.getIdUsuario())
                    //.set(supervisor)
                    .set(supervisor)
                    .addOnSuccessListener(unused -> {
                       Log.d("msg-test", "Sitio asignado exitosamente");
                        Toast.makeText(this, "Sitio asignado exitosamente", Toast.LENGTH_SHORT).show();
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            String user1 = user.getUid();
                            Log.d("USUARIO", user1);
                            db.collection("usuarios")
                                    .whereEqualTo("idUsuario", user1)  // Buscar documentos donde el campo 'idUsuario' sea igual a log.getIdUsuario()
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful() && task1.getResult() != null && !task1.getResult().isEmpty()) {
                                            // Obtener el primer documento que coincide con la consulta
                                            DocumentSnapshot document = task1.getResult().getDocuments().get(0);
                                            Usuario usuario = document.toObject(Usuario.class);
                                            if (usuario != null) {
                                                usuario.setIdUsuario(document.getId());
                                                crearLog("Sitio asignado", "Se ha asignado el sitio"+" "+sitio+" al supervisor "+supervisor.getNombre(), user1, null,usuario);
                                            }
                                        } else {
                                            android.util.Log.d("msg-test", "Error getting document: ", task1.getException());
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(e -> {
                       Log.e("msg-test", "Error al asignar el sitio", e);
                       Toast.makeText(this, "Error  al asignar el sitio", Toast.LENGTH_SHORT).show();
                    });

            db.collection("sitios")
                    .document(sitio.getIdSitio())
                    .set(sitio)
                    .addOnSuccessListener(unused -> {
                        Log.d("msg-test", "supervisor asignado exitosamente");

                    })
                    .addOnFailureListener(e -> {
                        Log.e("msg-test", "Error al asignar el supervisor al sitio", e);
                    });
            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(this);
            alertDialog2.setMessage("Sitio Asignado Correctamente");
            alertDialog2.setPositiveButton("Listo", (dialogInterface2, i2) -> {
                Intent intent = new Intent(this, InformacionSupervisorActivity.class); // Reemplaza "TuActivity" con el nombre de tu Activity
                intent.putExtra("supervisor",supervisor);
                startActivity(intent);
            });
            alertDialog2.show();

            String title="Nuevo Sitio Asignado";
            String description = "Se asignó el sitio: "+sitio.getIdSitio()+" al Supervisor:"+supervisor.getIdUsuario()+"-"+supervisor.getNombre()+" "+supervisor.getApellido();

            notificarImportanceDefault(title , description);
            
        });

        alertDialog.setNegativeButton("Cancelar", (dialogInterface, i) -> {
            Log.d("msg-test", "Configuración presionado");
        });

        AlertDialog dialog = alertDialog.create();

        dialog.show();
    }

    public void notificarImportanceDefault(String title , String description){

        //Crear notificación
        //Agregar información a la notificación que luego sea enviada a la actividad que se abre
        Intent intent = new Intent(this, NotificationDismissReceiver.class);
        intent.putExtra("pid","4616");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        //
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, canal1)
                .setSmallIcon(R.drawable.add_location_alt_24px)
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

    public void crearLog(String actividad, String descripcion, String idUsuario, Sitio sitio, Usuario usuario){
        Date fechaActual = new Date();

        com.example.aurora.Bean.Log nuevoLog = new com.example.aurora.Bean.Log(fechaActual,  actividad,  descripcion, idUsuario, sitio, usuario);

        db.collection("logs")
                .add(nuevoLog)
                .addOnSuccessListener(documentReference -> {
                    Log.d("msg-test2", "Log guardado exitosamente");
                    Toast.makeText(AsignarSitioActivity.this, "Actividad Registrada", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("msg-test3", "Error al guardar el log", e);
                    Toast.makeText(AsignarSitioActivity.this, "Error al registrar la activada", Toast.LENGTH_SHORT).show();
                });
    }

}