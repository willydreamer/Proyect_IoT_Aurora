package com.example.aurora;

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

import java.util.ArrayList;

public class InformacionSupervisorActivity extends AppCompatActivity {

    ArrayList<Sitio> listaSitios;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_informacion_supervisor);

        recyclerView = findViewById(R.id.recyclerview_listasitios);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        listaSitios = new ArrayList<>();
        listaSitios.add(new Sitio("NW000001", "Junín", "Huancayo", "Center", "Urbana", "-12.0651", "-75.2048", "Móvil","Cristiano Ronaldo"));
        listaSitios.add(new Sitio("NW000002", "Cusco", "Cusco", "Center", "Rural", "-13.5226", "-71.9673", "Fijo","Cristiano Ronaldo"));
        listaSitios.add(new Sitio("NW000003", "Arequipa", "Arequipa", "Center", "Urbana", "-16.4090", "-71.5375", "Móvil","Cristiano Ronaldo"));
        listaSitios.add(new Sitio("NW000004", "Lima", "Yauyos", "Center", "Rural", "-12.4841", "-75.7835", "Fijo","Cristiano Ronaldo"));
        ListaSitiosAdapter adapter = new ListaSitiosAdapter();
        adapter.setContext(InformacionSupervisorActivity.this);
        adapter.setListaSitios(listaSitios);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(InformacionSupervisorActivity.this));


        //recyclerView.setAdapter(adapter);

        ImageButton cambiarEstado = findViewById(R.id.imageButton5);
        cambiarEstado.setOnClickListener(view->{
            mostrarDialog();
        });

    }

    /* Como Fragment
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla el layout de tu fragmento
        return inflater.inflate(R.layout.activity_informacion_supervisor, container, false);
    }*/

     public void irAsignarSitio(View view) {

        //primero crear el intento
        Intent intent = new Intent(this, ListaAsignacionSitiosActivity.class);
        //iniciar activity
        startActivity(intent);
    }

    public void mostrarDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Estado Supervisor:\n Se Cambiará el estado del supervisor\n ¿Estás seguro de continuar?");

        alertDialog.setPositiveButton("Si", (dialogInterface, i) -> {
            Button estado = findViewById(R.id.button);
            if(estado.getText().equals("Estado: Activo")){
                estado.setText("Estado: Desactivo");
                estado.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F44336")));
            }else{
                estado.setText("Estado: Activo");
                estado.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
            }
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
    }

}

