package com.example.aurora.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aurora.Bean.Chat2;
import com.example.aurora.Bean.Message2;
import com.example.aurora.Bean.Sitio;
import com.example.aurora.Bean.Usuario;
import com.example.aurora.MensajeriaActivity;
import com.example.aurora.MensajeriaChatActivity;
import com.example.aurora.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ListaChatsAdapter extends RecyclerView.Adapter<ListaChatsAdapter.MensajeViewHolder>{
    private Context context;

    private ArrayList<Chat2> listaChats;

    private FirebaseFirestore db;



    public ListaChatsAdapter(Context context,ArrayList<Chat2> listaChats){
        this.context=context;
        this.listaChats= listaChats;
    }



    public class MensajeViewHolder extends RecyclerView.ViewHolder{

        Chat2 chat;
        public MensajeViewHolder(@NonNull View itemView) {

            super(itemView);
            //mensaje = itemView.findViewById(R.id.textView1);

        }

    }

    @NonNull
    @Override
    //Para inflar la vista
    public ListaChatsAdapter.MensajeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lista_mensaje, parent, false);
        db = FirebaseFirestore.getInstance();
        return new ListaChatsAdapter.MensajeViewHolder(view);

    }
    @Override
    public void onBindViewHolder(@NonNull ListaChatsAdapter.MensajeViewHolder holder, int position) {

        Chat2 ch = listaChats.get(position);
        holder.chat = ch;
        TextView nombreUsuarioChat = holder.itemView.findViewById(R.id.textTitle1);
        TextView ultimoMensaje = holder.itemView.findViewById(R.id.textView1);
        TextView hora = holder.itemView.findViewById(R.id.textView16);
        Log.d("idChatOf",ch.getUsuario1());
        db.collection("usuarios")
                .whereEqualTo("idUsuario", ch.getUsuario1())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Usuario usuarioChat = document.toObject(Usuario.class);
                                Log.d("usuario", usuarioChat.getNombre());

                                nombreUsuarioChat.setText(usuarioChat.getNombre()+" "+usuarioChat.getApellido());
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

        String ultimoMensajeID = ch.getListaMensajes().get(listaChats.size()-1);
        Log.d("lastmsg",ultimoMensajeID);

        db.collection("Mensajes")
                .whereEqualTo("idChat",ch.getIdChat())
                .orderBy("fecha" , Query.Direction.DESCENDING)
                .limit(1)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Toast.makeText(context, "Error al obtener los mensajes", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (snapshots != null) {
                            for (DocumentSnapshot document : snapshots.getDocuments()) {
                                Message2 msg = document.toObject(Message2.class);
                                ultimoMensaje.setText(msg.getMensaje());
                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                                hora.setText(sdf.format(msg.getFecha()));
                            }
                        }
                    }
                });

        /*db.collection("Mensajes")
                .whereEqualTo("idMensaje", ultimoMensajeID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Message2 msg = document.toObject(Message2.class);

                                ultimoMensaje.setText(msg.getMensaje());
                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                                hora.setText(sdf.format(msg.getFecha()));

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
                });*/




        context = holder.itemView.getContext();
        ImageButton flecha1 = holder.itemView.findViewById(R.id.flechaGo);
        Log.d("idChatOf2",ch.getUsuario1());
        flecha1.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), MensajeriaChatActivity.class); // Reemplaza "TuActivity" con el nombre de tu Activity
            intent.putExtra("chat", ch);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        //Este método debe indicar la cantidad total de elementos, en nuestro caso, del
        //arreglo “data”.
        return listaChats.size();
    }

    public void updateList(ArrayList<Chat2> newList) {
        listaChats = newList;
        notifyDataSetChanged();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<Chat2> getListaChats() {
        return listaChats;
    }

    public void setListaChats(ArrayList<Chat2> listaChats) {

        this.listaChats = listaChats;
        notifyDataSetChanged();
    }
}


