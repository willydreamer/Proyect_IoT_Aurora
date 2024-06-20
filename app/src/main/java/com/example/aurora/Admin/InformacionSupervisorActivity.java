package com.example.aurora.Admin;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Adapter.ListaSitiosAdapter;
import com.example.aurora.Bean.Sitio;
import com.example.aurora.Bean.Usuario;
import com.example.aurora.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

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
    ImageView fotoSupervisor;
    SearchView buscador;
    Usuario supervisor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_informacion_supervisor);

        supervisor = (Usuario) getIntent().getSerializableExtra("supervisor");


        nombre = findViewById(R.id.nombre);
        apellido = findViewById(R.id.apellido);
        dni = findViewById(R.id.dni);
        correo = findViewById(R.id.correo);
        domicilio = findViewById(R.id.domicilio);
        telefono = findViewById(R.id.telefono);
        fotoSupervisor = findViewById(R.id.imageView3);


        buscador = findViewById(R.id.search2);


        String nombreStr = supervisor.getNombre();
        String apellidoStr = supervisor.getApellido();
        String dniStr = supervisor.getDni();
        String correoStr = supervisor.getCorreo();
        String domicilioStr = supervisor.getDomicilio();
        String telefonoStr = supervisor.getTelefono();

        if (supervisor.getFotoURL() != null && !supervisor.getFotoURL().isEmpty()) {
            Picasso.get()
                    .load(supervisor.getFotoURL())
                    .placeholder(R.drawable.perfil_icono) // Reemplaza con tu imagen por defecto
                    .transform(new CropCircleTransformation())
                    .into(fotoSupervisor);
        } else {
            Picasso.get()
                    .load(R.drawable.perfil_icono) // Imagen por defecto
                    .transform(new CropCircleTransformation())
                    .into(fotoSupervisor);
        }

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

        // Configurar el adapter y asociarlo al RecyclerView
        adapter = new ListaSitiosAdapter();
        adapter.setContext(getApplicationContext());
        adapter.setListaSitios(listaSitios);
        recyclerView.setAdapter(adapter);

        //basado en gpt
        buscador.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                buscarSitios(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    obtenerSitiosDeFirestore();
                } else {
                    buscarSitios(newText);
                }
                return true;
            }
        });

        // Listener para detectar cuándo se limpia el texto del SearchView

        buscador.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                obtenerSitiosDeFirestore();
                return false;
            }
        });

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

        AppCompatImageButton asignarSitio = findViewById(R.id.button9);
        asignarSitio.setOnClickListener(view->{
            if(supervisor.getSitios().size()==5){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setMessage("El Supervisor Solo Puede Tener 5 Sitios Asignados");
                alertDialog.setPositiveButton("De Acuerdo", (dialogInterface, i) -> {
                });
                alertDialog.show();
            }else{
            Intent intent = new Intent(this, ListaAsignacionSitiosActivity.class);
            intent.putExtra("supervisor",supervisor);
            //iniciar activity
            startActivity(intent);
            }
        });
    }

    private void buscarSitios(String searchText) {
        db.collection("sitios")
                .orderBy("departamento")
                .startAt(searchText)
                .endAt(searchText + "\uf8ff")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            // Manejo de errores
                            return;
                        }

                        if (snapshots != null) {
                            ArrayList<Sitio> sitios = new ArrayList<>();
                            for (DocumentSnapshot document : snapshots.getDocuments()) {
                                Sitio sitio = document.toObject(Sitio.class);
                                if(sitio.getEncargado().equals(supervisor.getIdUsuario())){
                                    Log.d("sitio",sitio.getIdSitio());
                                    sitios.add(sitio);
                                }
                            }
                            adapter.setListaSitios(sitios);
                            //adapter.notifyDataSetChanged();
                        }
                    }
                });
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
                            //if(sitio.getSupervisor() == null || sitio.getSupervisor().size() == 1){
                            if(sitio.getEncargado().equals(supervisor.getIdUsuario())){
                                Log.d("sitio",sitio.getIdSitio());
                                listaSitios.add(sitio);
                            }
                            //listaSitios.add(sitio);
                        }
                        adapter.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error al obtener los sitios", Toast.LENGTH_SHORT).show();
                });
    }

}

