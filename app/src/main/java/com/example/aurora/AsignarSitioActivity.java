package com.example.aurora;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aurora.Bean.Sitio;
import com.example.aurora.Bean.Usuario;
import com.example.aurora.databinding.ActivityAsignarSitioBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

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
            supervisor.getSitios().add(sitio);
            Usuario supervisor_copy = supervisor;
            //sitio.getSupervisor().add(supervisor);
            //Log.d("sitio",sitio.getSupervisor().getNombre());
            // Guardar los datos en Firestore
            db.collection("usuarios")
                    .document(supervisor.getIdUsuario())
                    .set(supervisor)
                    .addOnSuccessListener(unused -> {
                        Log.d("msg-test", "Sitio asignado exitosamente");
                        Toast.makeText(this, "Sitio asignado exitosamente", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("msg-test", "Error al asignar el sitio", e);
                        Toast.makeText(this, "Error  al asignar el sitio", Toast.LENGTH_SHORT).show();
                    });

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
            sitio.getSupervisor().add(supervisor_copy);
            db.collection("sitios")
                    .document(sitio.getIdSitio())
                    .set(sitio)
                    .addOnSuccessListener(unused -> {
                        Log.d("msg-test", "supervisor asignado exitosamente");
                    })
                    .addOnFailureListener(e -> {
                        Log.e("msg-test", "Error al asignar supervisor al sitio", e);
                    });
        });

        alertDialog.setNegativeButton("Cancelar", (dialogInterface, i) -> {
            Log.d("msg-test", "Configuración presionado");
        });

        AlertDialog dialog = alertDialog.create();

        dialog.show();
    }
}