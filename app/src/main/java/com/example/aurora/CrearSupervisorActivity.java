package com.example.aurora;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.aurora.Bean.Sitio;
import com.example.aurora.Bean.Supervisor;
import com.example.aurora.Bean.Usuario;
import com.example.aurora.databinding.ActivityCrearSupervisorBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_supervisor);

        // Instanciar Firebase
        db = FirebaseFirestore.getInstance();

        botonGuardar = findViewById(R.id.button);

        botonGuardar.setOnClickListener(view->{
            guardarSupervisor();
        });

    }

    public void guardarSupervisor() {

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

        Usuario usuarioSupervisor = new Usuario(idUsuarioSupervisor, nombreStr, apellidoStr, dniStr, correoStr, domicilioStr, telefonoStr,rol);


        // Guardar los datos en Firestore
        db.collection("usuarios")
                .document(idUsuarioSupervisor)
                .set(usuarioSupervisor)
                .addOnSuccessListener(unused -> {
                    Log.d("msg-test2", "Supervisor guardado exitosamente");
                    Toast.makeText(CrearSupervisorActivity.this, "Supervisor creado exitosamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CrearSupervisorActivity.this, AdminSupervisoresFragment.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Log.e("msg-test3", "Error al guardar el sitio", e);
                    Toast.makeText(CrearSupervisorActivity.this, "Error al crear el supervisor", Toast.LENGTH_SHORT).show();
                });

    }


    private String generarIdSupervisor() {
        String letrasAdmin = "SUPER";
        Random random = new Random();
        int numeroAleatorio = random.nextInt(900) + 100; // Generar un número entre 100 y 999
        return letrasAdmin+numeroAleatorio;
    }
}

   /* public void listarArchivosGuardados(){
        String[] archivosGuardados = fileList();

        for(String archivo: archivosGuardados){
            Log.d("archivo",archivo);
        }
    }

    public void leerArchivoObjeto() {
        String fileName = "listaSupervisores";
        try (FileInputStream fileInputStream = openFileInput(fileName);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            ArrayList<Supervisor> arregloSupervisores = (ArrayList<Supervisor>) objectInputStream.readObject();
            for(Supervisor s: arregloSupervisores ){
                Log.d("super:",s.getNombre());
            }
        } catch (FileNotFoundException | ClassNotFoundException e) {
            e.printStackTrace();
            Log.e("super", "Error al leer el supervisor guardado", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    } */


