package com.example.aurora;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;


import com.example.aurora.Bean.Supervisor;
import com.example.aurora.databinding.ActivityCrearSupervisorBinding;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class CrearSupervisorActivity extends AppCompatActivity {

    private EditText nombre;
    private EditText apellido;
    private EditText dni;

    private EditText correo;
    private EditText telefono;

    private EditText domicilio;

    ActivityCrearSupervisorBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_supervisor);

        listarArchivosGuardados();
        leerArchivoObjeto();

    }

    public void guardarSupervisor () {

        nombre = findViewById(R.id.editText);
        apellido = findViewById(R.id.editText1);
        dni = findViewById(R.id.editText2);
        correo = findViewById(R.id.editText3);
        domicilio = findViewById(R.id.editText4);
        telefono = findViewById(R.id.editText5);

        String nombreStr = nombre.getEditableText().toString();
        String apellidoStr = apellido.getEditableText().toString();
        String dniStr = dni.getEditableText().toString();
        String correoStr = correo.getEditableText().toString();
        String domicilioStr = domicilio.getEditableText().toString();
        String telefonoStr = telefono.getEditableText().toString();

        Supervisor supervisor = new Supervisor(1,nombreStr,apellidoStr,dniStr,correoStr,domicilioStr,telefonoStr);

        //nombre del archivo a guardar
        String fileName = "listaSupervisores" ;
        //Se utiliza la clase FileOutputStream para poder almacenar en Android
        try (FileOutputStream fileOutputStream = this.openFileOutput(fileName , Context.MODE_PRIVATE);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            //con objectOutputStream se realiza la escritura como objeto
            objectOutputStream.writeObject(supervisor) ;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("guardarSupervisor", "IOException al guardar el supervisor", e);
        }

        Intent intent = new Intent(this, AdminSupervisoresFragment.class); // Reemplaza "TuActivity" con el nombre de tu Activity
        startActivity(intent);
    }

    public void listarArchivosGuardados(){
        String[] archivosGuardados = fileList();

        for(String archivo: archivosGuardados){
            Log.d("archivo",archivo);
        }
    }

    public void leerArchivoObjeto() {
        String fileName = "listaSupervisores";
        try (FileInputStream fileInputStream = openFileInput(fileName);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            Supervisor[] arregloSupervisores = (Supervisor[]) objectInputStream.readObject();
            for(Supervisor s: arregloSupervisores ){
                Log.d("super:",s.getNombre());
            }
        } catch (FileNotFoundException | ClassNotFoundException e) {
            e.printStackTrace();
            Log.e("guardarSupervisor", "Error al leer el supervisor guardado", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}