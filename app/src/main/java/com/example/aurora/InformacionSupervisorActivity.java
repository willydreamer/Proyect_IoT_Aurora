package com.example.aurora;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Adapter.ListaSitiosAdapter;
import com.example.aurora.Bean.Sitio;
import com.example.aurora.Bean.Usuario;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class InformacionSupervisorActivity extends AppCompatActivity {

    ArrayList<Sitio> listaSitios;

    RecyclerView recyclerView;

    TextView nombre;
    TextView apellido;
    TextView dni;
    TextView correo ;
    TextView domicilio;
    TextView telefono;

    FirebaseFirestore db;

    ListaSitiosAdapter adapter;

    Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_informacion_supervisor);

        Usuario supervisor = (Usuario) getIntent().getSerializableExtra("supervisor");


        nombre = findViewById(R.id.editText);
        apellido = findViewById(R.id.editText1);
        dni = findViewById(R.id.editText2);
        correo = findViewById(R.id.editText3);
        domicilio = findViewById(R.id.editText4);
        telefono = findViewById(R.id.editText5);


        String nombreStr = supervisor.getNombre();
        String apellidoStr = supervisor.getApellido();
        String dniStr = supervisor.getDni();
        String correoStr = supervisor.getCorreo();
        String domicilioStr = supervisor.getDomicilio();
        String telefonoStr = supervisor.getTelefono();

        nombre.setText(nombreStr);
        apellido.setText(apellidoStr);
        dni.setText(dniStr);
        correo.setText(correoStr);
        domicilio.setText(domicilioStr);
        telefono.setText(telefonoStr);

        recyclerView = findViewById(R.id.recyclerview_listasitios);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        //recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));


        db = FirebaseFirestore.getInstance();
        listaSitios = new ArrayList<>();
        /*listaSitios.add(new Sitio("NW000001", "Junín", "Huancayo", "Center", "Urbana", "-12.0651", "-75.2048", "Móvil","Cristiano Ronaldo"));
        listaSitios.add(new Sitio("NW000002", "Cusco", "Cusco", "Center", "Rural", "-13.5226", "-71.9673", "Fijo","Cristiano Ronaldo"));
        listaSitios.add(new Sitio("NW000003", "Arequipa", "Arequipa", "Center", "Urbana", "-16.4090", "-71.5375", "Móvil","Cristiano Ronaldo"));
        listaSitios.add(new Sitio("NW000004", "Lima", "Yauyos", "Center", "Rural", "-12.4841", "-75.7835", "Fijo","Cristiano Ronaldo"));
        ListaSitiosAdapter adapter = new ListaSitiosAdapter();
        adapter.setContext(InformacionSupervisorActivity.this);
        adapter.setListaSitios(listaSitios);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(InformacionSupervisorActivity.this));*/


        // Configurar el adapter y asociarlo al RecyclerView
        adapter = new ListaSitiosAdapter();
        adapter.setContext(getApplicationContext());
        adapter.setListaSitios(listaSitios);
        recyclerView.setAdapter(adapter);

        obtenerSitiosDeFirestore();


        Button estado = findViewById(R.id.button);

        if(supervisor.getEstado()!=null){
            if(supervisor.getEstado().equals("activo")){
                estado.setText("Estado: Activo");
            }else{
                estado.setText("Estado: Inactivo");
                estado.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F44336")));

            }
        }


        ImageButton cambiarEstado = findViewById(R.id.imageButton5);
        cambiarEstado.setOnClickListener(view->{
            mostrarDialog();
        });

    }

     public void irAsignarSitio(View view) {

        //primero crear el intento
        Intent intent = new Intent(this, ListaAsignacionSitiosActivity.class);
        //iniciar activity
        startActivity(intent);
    }

    public void mostrarDialog() {

        db = FirebaseFirestore.getInstance();


        Usuario supervisor = (Usuario) getIntent().getSerializableExtra("supervisor");

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Estado Supervisor:\n Se Cambiará el estado del supervisor\n ¿Estás seguro de continuar?");

        alertDialog.setPositiveButton("Si", (dialogInterface, i) -> {
            Button estado = findViewById(R.id.button);
            //if(estado.getText().equals("Estado: Activo")){
            if(supervisor.getEstado().equals("activo")){
                supervisor.setEstado("inactivo");
                estado.setText("Estado: Inactivo");
                estado.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F44336")));
            }else{
                supervisor.setEstado("activo");
                estado.setText("Estado: Activo");
                estado.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
            }
            //con db.collection se crea la colección o se guarda en la coleccion ya existente
            db.collection("usuarios")
                    //.add(supervisor) --> autogenera el id para el documento (objeto) supervisor
                    .document(supervisor.getIdUsuario())
                    .set(supervisor)
                    .addOnSuccessListener(unused -> {
                        Log.d("msg-test2", "Estado supervisor cambiado");
                    })
                    .addOnFailureListener(e -> {
                        Log.e("msg-test3", "error en cambio de estado", e);
                    });
            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(this);
            alertDialog2.setMessage("Estado Supervisor Modificado");
            alertDialog2.setPositiveButton("Listo", (dialogInterface2, i2) -> {
            });
            alertDialog2.show();

        });

        alertDialog.setNegativeButton("Cancelar", ((dialogInterface, i) -> {
            Log.d("msg-test", "Configuración presionado");
        }));

        alertDialog.show();

        // Guardar los datos en Firestore
        /*db.collection("usuarios")
                .document(supervisor.getIdUsuario())
                .set(supervisor)
                .addOnSuccessListener(unused -> {
                    Log.d("msg-test2", "Estado supervisor modificado");
                })
                .addOnFailureListener(e -> {
                    Log.e("msg-test3", "error en cambio de estado", e);
                });*/

    }

    private void obtenerSitiosDeFirestore() {
        db.collection("sitios")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Sitio sitio = document.toObject(Sitio.class);
                            listaSitios.add(sitio);
                        }
                        adapter.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error al obtener los sitios", Toast.LENGTH_SHORT).show();
                });
    }

}

