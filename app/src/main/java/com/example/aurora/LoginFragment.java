package com.example.aurora;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class LoginFragment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_fragment);

        EditText usuario = findViewById(R.id.usuario);
        EditText contrasena = findViewById(R.id.contrasena);


        Button buttonIngresarbotonlogin = findViewById(R.id.ingresarbotonlogin);

        buttonIngresarbotonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuarioobtenido = usuario.getText().toString();
                String contrasenaobtenido = contrasena.getText().toString();
                if (usuarioobtenido.equals("supervisor")) {
                    Intent intent = new Intent(LoginFragment.this, Supervisor.class);
                    startActivity(intent);
                    finish();
                } else if (usuarioobtenido.equals("superadmin")) {
                    Intent intent = new Intent(LoginFragment.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (usuarioobtenido.equals("administrador")) {
                    Intent intent = new Intent(LoginFragment.this, Administrador.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }
}