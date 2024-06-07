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
import android.widget.Toast;

import com.example.aurora.Adapter.ChatsListAdapter;
import com.example.aurora.Bean.Chat;
import com.example.aurora.Bean.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatsListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChatsListAdapter chatsListAdapter;
    private List<Chat> chatsList;

    private FirebaseFirestore db;
    private CollectionReference chatsRef;
    private String currentUserId;

    public ChatsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        chatsRef = db.collection("usuarios").document(currentUserId).collection("chats");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view_chats);
        chatsList = new ArrayList<>();
        chatsListAdapter = new ChatsListAdapter(chatsList, new ChatsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Chat chat) {
                // Navigate to the ChatFragment
                ChatFragment chatFragment = ChatFragment.newInstance(chat.getUser1(), chat.getUser2());
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.container1, chatFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(chatsListAdapter);

        chatsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }

                chatsList.clear();
                if (value != null) {
                    for (com.google.firebase.firestore.DocumentSnapshot doc : value.getDocuments()) {
                        chatsList.add(doc.toObject(Chat.class));
                    }
                    chatsListAdapter.notifyDataSetChanged();
                }
            }
        });
        }
}
