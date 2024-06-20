package com.example.aurora;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aurora.Adapter.MessageAdapter;
import com.example.aurora.Adapter.MessageAdapter2;
import com.example.aurora.Bean.Chat2;
import com.example.aurora.Bean.Message;
import com.example.aurora.Bean.Message2;
import com.example.aurora.Bean.Usuario;
import com.example.aurora.databinding.ActivityMensajeriaChatBinding;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MensajeriaChatActivity extends AppCompatActivity {
    private ActivityMensajeriaChatBinding binding;

    private RecyclerView recyclerViewMessages;
    private MessageAdapter2 messageAdapter;
    private ArrayList<Message2> listaMensajes;
    private EditText editTextMensaje;
    private Button buttonEnviar;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private Usuario usuario;
    private FirebaseUser usuarioLogueado;

    private Chat2 chat;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMensajeriaChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        usuarioLogueado = mAuth.getCurrentUser();


        chat = (Chat2) getIntent().getSerializableExtra("chat");
        Log.d("chatID2",chat.getUsuario2());

        db.collection("usuarios")
                .whereEqualTo("idUsuario", chat.getUsuario1())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Usuario supervisor = document.toObject(Usuario.class);
                                binding.contactName.setText(supervisor.getNombre()+" "+supervisor.getApellido());
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

        if (usuarioLogueado != null) {
            String correo = usuarioLogueado.getEmail();
            Log.d("correo", correo);
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
                                    Log.d("usuario", usuario.getNombre());
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
        }


        recyclerViewMessages = findViewById(R.id.recyclerViewChat);
        editTextMensaje = findViewById(R.id.messageInput);
        buttonEnviar = findViewById(R.id.botonEnviar);

        listaMensajes = new ArrayList<>();
        messageAdapter = new MessageAdapter2(MensajeriaChatActivity.this,listaMensajes);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(MensajeriaChatActivity.this));
        recyclerViewMessages.setAdapter(messageAdapter);


        cargarMensajes();

        buttonEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMensaje();
            }
        });
    }

    private void cargarMensajes() {
        db.collection("Mensajes")
                .whereEqualTo("idChat", chat.getIdChat())
                .orderBy("fecha", Query.Direction.ASCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        //Toast.makeText(this, "Error al cargar mensajes", Toast.LENGTH_LONG).show();
                        return;
                    }
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        switch (dc.getType()) {
                            case ADDED:
                                Message2 message = dc.getDocument().toObject(Message2.class);
                                listaMensajes.add(message);
                                messageAdapter.notifyItemInserted(listaMensajes.size() - 1);
                                recyclerViewMessages.scrollToPosition(listaMensajes.size() - 1);
                                break;
                            case MODIFIED:
                                // Manejar mensajes modificados si es necesario
                                break;
                            case REMOVED:
                                // Manejar mensajes eliminados si es necesario
                                break;
                        }
                    }
                });
                /*
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Message2 msg = document.toObject(Message2.class);
                                Log.d("msg", msg.getMensaje());
                                listaMensajes.add(msg);
                            }
                            messageAdapter.notifyDataSetChanged();
                            //recyclerViewMessages.scrollToPosition(listaMensajes.size() - 1);
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
                //.orderBy("timestamp", Query.Direction.ASCENDING)
                /*
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Toast.makeText(this, "Error al cargar mensajes", Toast.LENGTH_LONG).show();
                        return;
                    }

                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        switch (dc.getType()) {
                            case ADDED:
                                listaMensajes.add(dc.getDocument().toObject(Message2.class));
                                break;
                            case MODIFIED:
                                // Manejar mensajes modificados si es necesario
                                break;
                            case REMOVED:
                                // Manejar mensajes eliminados si es necesario
                                break;
                        }
                    }
                    messageAdapter.notifyDataSetChanged();
                    recyclerViewMessages.scrollToPosition(listaMensajes.size() - 1);
                });
                */

    }

    private void  enviarMensaje() {
        String mensaje = editTextMensaje.getText().toString().trim();
        if (mensaje.isEmpty()) {
            Toast.makeText(this, "No puedes enviar un mensaje vacío", Toast.LENGTH_LONG).show();
            return;
        }
        String idMensaje = generarIdMensaje();
        Message2 message = new Message2(idMensaje, chat.getIdChat(), mensaje, usuario.getIdUsuario(), chat.getUsuario1(),new Date());
        db.collection("Mensajes")
                .document(idMensaje)
                .set(message)
                .addOnSuccessListener(documentReference -> {
                    editTextMensaje.setText("");
                    listaMensajes.add(message);
                    messageAdapter.notifyItemInserted(listaMensajes.size() - 1);
                    recyclerViewMessages.scrollToPosition(listaMensajes.size() - 1);
                    chat.getListaMensajes().add(idMensaje);
                    guardarMensajeEnChat(chat);
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al enviar mensaje", Toast.LENGTH_LONG).show());


    }

    private String generarIdMensaje() {
        String letrasAdmin = "MSG";
        Random random = new Random();
        int numeroAleatorio = random.nextInt(100000) + 100; // Generar un número entre 100 y 999
        return letrasAdmin + numeroAleatorio;
    }

    private void  guardarMensajeEnChat(Chat2 chat) {
        db.collection("chats")
                .document(chat.getIdChat())
                .set(chat)
                .addOnSuccessListener(unused -> {
                    Log.d("msg_send","mensaje guardado");
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al enviar mensaje", Toast.LENGTH_LONG).show());


    }
}