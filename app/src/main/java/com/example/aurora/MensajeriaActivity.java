package com.example.aurora;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Adapter.ListaMensajesAdapter;
import com.example.aurora.Adapter.ListaSitiosAdapter;
import com.example.aurora.Bean.Sitio;

import java.util.ArrayList;

public class MensajeriaActivity extends AppCompatActivity {

    ArrayList<String> listaMensajes;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mensajeria);

        recyclerView = findViewById(R.id.recyclerview_listamensajes);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        listaMensajes = new ArrayList<>();
        listaMensajes.add("Hola Admin , debo preguntarle algo");
        listaMensajes.add("Hola , todo bien?");
        listaMensajes.add("Hay nuevas noticias");
        listaMensajes.add("Todo bien");

        ListaMensajesAdapter adapter = new ListaMensajesAdapter(listaMensajes);
        adapter.setContext(MensajeriaActivity.this);
        adapter.setListaMensajes(listaMensajes);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MensajeriaActivity.this));

    }
}