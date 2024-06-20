package com.example.aurora;

import static java.security.AccessController.getContext;

import android.util.Log;
import android.widget.Toast;

import com.example.aurora.Bean.Sitio;
import com.example.aurora.Bean.Usuario;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class LogFunctions {


    private FirebaseFirestore db;
/*
    public void crearLog(){
        Date fechaActual = new Date();

        Log nuevoLog = new Log;//(Integer idLog, String fecha, String actividad, String description, Usuario usuario, Sitio sitio);
        db.collection("usuarios")
                .add(nuevoLog)
                .addOnSuccessListener(documentReference -> {
                    Log.d("msg-test2", "Administrador guardado exitosamente");
                    Toast.makeText(getContext(), "Administrador creado exitosamente", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("msg-test3", "Error al guardar el administrador", e);
                    Toast.makeText(getContext(), "Error al crear el administrador", Toast.LENGTH_SHORT).show();
                });
    }*/

}
