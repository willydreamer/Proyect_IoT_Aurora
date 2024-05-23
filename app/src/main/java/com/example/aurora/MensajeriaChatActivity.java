package com.example.aurora;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aurora.databinding.ActivityMainBinding;
import com.example.aurora.databinding.ActivityMensajeriaChatBinding;

public class MensajeriaChatActivity extends AppCompatActivity {
    ActivityMensajeriaChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMensajeriaChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}