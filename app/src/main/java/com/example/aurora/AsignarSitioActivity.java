package com.example.aurora;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aurora.Adapter.JsonUtils;
import com.example.aurora.Bean.Sitio;
import com.example.aurora.Bean.Usuario;
import com.example.aurora.databinding.ActivityAsignarSitioBinding;
import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Map;

public class AsignarSitioActivity extends AppCompatActivity {

    ActivityAsignarSitioBinding binding;

    FirebaseFirestore db;

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
                supervisor.setSitios(new ArrayList<>()); // Inicializa la lista si es nula
            }
            if (sitio.getSupervisor() == null) {
                sitio.setSupervisor(new ArrayList<>()); // Inicializa la lista si es nula
            }


            supervisor.getSitios().add(sitio);
            sitio.getSupervisor().add(supervisor);


            //JsonObject supervisorJson = JsonUtils.serializeUsuario(supervisor);
            //JsonObject sitioJson = JsonUtils.serializeSitio(sitio);

            // Convertir JsonObject a Map<String, Object>
            //Map<String, Object> supervisorMap = JsonUtils.convertJsonObjectToMap(supervisorJson);
            //Map<String, Object> sitioMap = JsonUtils.convertJsonObjectToMap(sitioJson);
            //Usuario supervisor_copy = supervisor;
            //sitio.getSupervisor().add(supervisor);
            // Guardar los datos en Firestore
//            db.collection("usuarios")
//                    .document(supervisor.getIdUsuario())
//                    //.set(supervisor)
//                    .set(supervisorMap)
//                    .addOnSuccessListener(unused -> {
//                        Log.d("msg-test", "Sitio asignado exitosamente");
//                        Toast.makeText(this, "Sitio asignado exitosamente", Toast.LENGTH_SHORT).show();
//                    })
//                    .addOnFailureListener(e -> {
//                        Log.e("msg-test", "Error al asignar el sitio", e);
//                        Toast.makeText(this, "Error  al asignar el sitio", Toast.LENGTH_SHORT).show();
//                    });

            /*db.collection("sitios")
                    .document(sitio.getIdSitio())
                    .set(sitio)
                    .addOnSuccessListener(unused -> {
                        Log.d("msg-test", "supervisor asignado exitosamente");
                    })
                    .addOnFailureListener(e -> {
                        Log.e("msg-test", "Error al asignar supervisor al sitio", e);
                    });*/
            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(this);
            alertDialog2.setMessage("Sitio Asignado Correctamente");
            alertDialog2.setPositiveButton("Listo", (dialogInterface2, i2) -> {
                Intent intent = new Intent(this, InformacionSupervisorActivity.class); // Reemplaza "TuActivity" con el nombre de tu Activity
                intent.putExtra("supervisor",supervisor);
                startActivity(intent);
            });
            alertDialog2.show();
            if (sitio.getSupervisor() == null) {
                sitio.setSupervisor(new ArrayList<>());
            }
            //sitio.getSupervisor().add(supervisor_copy);
//            db.collection("sitios")
//                    .document(sitio.getIdSitio())
//                    //.set(sitio)
//                    .set(sitioMap)
//                    .addOnSuccessListener(unused -> {
//                        Log.d("msg-test", "supervisor asignado exitosamente");
//                    })
//                    .addOnFailureListener(e -> {
//                        Log.e("msg-test", "Error al asignar supervisor al sitio", e);
//                    });
        });

        alertDialog.setNegativeButton("Cancelar", (dialogInterface, i) -> {
            Log.d("msg-test", "Configuración presionado");
        });

        AlertDialog dialog = alertDialog.create();

        dialog.show();
    }
}