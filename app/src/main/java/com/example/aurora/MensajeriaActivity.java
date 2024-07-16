package com.example.aurora;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import com.example.aurora.Adapter.ListaChatsAdapter;
import com.example.aurora.Adapter.ListaSupervisoresActivosAdapter;
import com.example.aurora.Bean.Chat;
import com.example.aurora.Bean.Chat2;
import com.example.aurora.Bean.Sitio;
import com.example.aurora.Bean.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MensajeriaActivity extends AppCompatActivity {

    private ArrayList<Chat2> listaChats;

    private RecyclerView recyclerView;

    private FirebaseFirestore db;

    private FirebaseUser usuarioLogueado;

    private Usuario usuario;

    private ListaChatsAdapter adapter;

    private Context context;

    private SearchView buscador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mensajeria);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        usuarioLogueado = firebaseAuth.getCurrentUser();

        db=FirebaseFirestore.getInstance();

        buscador =  findViewById(R.id.search1);

         

        recyclerView = findViewById(R.id.recyclerview_listamensajes);

        listaChats = new ArrayList<>();

        adapter = new ListaChatsAdapter(this,listaChats);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MensajeriaActivity.this));

        obtenerChatsDeFirestore(usuarioLogueado.getUid());

        AppCompatImageButton NuevoMensaje = findViewById(R.id.bottonNuevoMensaje);

        NuevoMensaje.setOnClickListener(v->{
            Intent intent = new Intent(this, MensajeriaSupervisoresActivosActivity.class); // Reemplaza "TuActivity" con el nombre de tu Activity
            this.startActivity(intent);
        });


    }

    private void  obtenerChatsDeFirestore(String usuarioLogueadoID) {
        db.collection("chats")
                .whereEqualTo("usuario2", usuarioLogueadoID)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            //Toast.makeText(MensajeriaActivity.this, "Error al obtener los mensajes", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (snapshots != null) {
                            listaChats.clear(); // Limpiar la lista antes de agregar nuevos datos
                            for (DocumentSnapshot document : snapshots.getDocuments()) {
                                Chat2 chat = document.toObject(Chat2.class);
                                listaChats.add(chat);
                            }
                            adapter.setListaChats(listaChats);
                            //adapter.notifyDataSetChanged(); // Notificar al adapter que los datos han cambiado
                        }
                    }
                });
    }


    /*private void obtenerChatsDeFirestore(FirebaseUser usuarioLogueado) {
        //Ojo uid del firebase user =! id del Document
        String correo = usuarioLogueado.getEmail();
        Log.d("correo", correo);
        //obtenerUsuarioPorCorreo(correo);
        db.collection("usuarios")
                .whereEqualTo("correo", correo)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("document", document.toString());
                                usuario = document.toObject(Usuario.class);
                                // Consultar chats donde el usuario es idUser1
                                db.collection("chats")
                                        .whereEqualTo("usuario2", usuario.getIdUsuario())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        Log.d("document2", document.toString());
                                                        Chat2 chat = document.toObject(Chat2.class);
                                                        listaChats.add(chat);
                                                    }
                                                    adapter.notifyDataSetChanged();
                                                } else {
                                                    Log.d("Error", "no se encontraron los chats");
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                e.printStackTrace();
                                            }
                                        });
                            }
                        } else {
                            Log.d("Error", "no se encontro usuario");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }*/
}