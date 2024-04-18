package com.example.aurora;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InicioFragment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio_fragment);

        Button buttonIngresar = findViewById(R.id.buttonIngresar);
        buttonIngresar.setOnClickListener(v -> {
            Intent intent = new Intent(InicioFragment.this, LoginFragment.class);
            startActivity(intent);
        });
    }
}