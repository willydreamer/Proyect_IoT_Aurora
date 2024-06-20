package com.example.aurora;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aurora.Adapter.MessageAdapter;
import com.example.aurora.Bean.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private EditText editTextMessage;
    private Button buttonSend;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;

    private FirebaseFirestore db;
    private CollectionReference messagesRef;

    private String currentUserId;
    private String user1; // ID del chat entre los dos usuarios
    private String user2; // ID del usuario receptor

    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance(String user1, String user2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString("user1", user1);
        args.putString("user2", user2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user1 = getArguments().getString("user1");
            user2 = getArguments().getString("user2");
        }
        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("chat")
                .whereEqualTo("user1", "SUPER111")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(getContext(), "Error al cargar mensajes", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                messageList.add(dc.getDocument().toObject(Message.class));
                                messageAdapter.notifyItemInserted(messageList.size() - 1);
                                recyclerView.scrollToPosition(messageList.size() - 1);
                            }
                        }
                    }
                });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view);
        editTextMessage = view.findViewById(R.id.edit_text_message);
        buttonSend = view.findViewById(R.id.button_send);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList,currentUserId);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(messageAdapter);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        messagesRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        messageList.add(dc.getDocument().toObject(Message.class));
                        messageAdapter.notifyItemInserted(messageList.size() - 1);
                        recyclerView.scrollToPosition(messageList.size() - 1);
                    }
                }
            }
        });
    }

    private void sendMessage() {
        String messageText = editTextMessage.getText().toString();
        if (!messageText.isEmpty()) {
            long timestamp = System.currentTimeMillis();
            Date date = new Date(timestamp);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = formatter.format(date);

            Message message = new Message(currentUserId, messageList.size(), formattedDate, messageText);
            messagesRef.add(message);
            editTextMessage.setText("");
        }
    }
}
