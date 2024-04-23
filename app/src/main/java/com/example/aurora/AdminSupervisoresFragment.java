package com.example.aurora;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class AdminSupervisoresFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_fragment_supervisor, container, false);

        ImageButton flecha1 = view.findViewById(R.id.flecha1);
        flecha1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InformacionSupervisorActivity.class); // Reemplaza "TuActivity" con el nombre de tu Activity
                startActivity(intent);
            }
        });

        Button crearBtn = view.findViewById(R.id.button19);
        crearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CrearSupervisorActivity.class); // Reemplaza "TuActivity" con el nombre de tu Activity
                startActivity(intent);
            }
        });
        return view;
    }


    //para transladar de una vista a otra
    /*@Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configura el botón o acción que iniciará la transición al otro fragmento
        view.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtiene el FragmentManager
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                // Inicia una transacción de fragmento
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // Reemplaza el fragmento actual con el nuevo fragmento
                Fragment nuevoFragmento = new InformacionSupervisorActivity();
                fragmentTransaction.replace(R.id.infosupers, nuevoFragmento);

                // Puedes agregar el fragmento actual a la pila de retroceso
                // fragmentTransaction.addToBackStack(null);

                // Realiza la transacción
                fragmentTransaction.commit();
            }
        });
    }
    /*public void irInfoSupervisor(View view) {

        //primero crear el intento
        Intent intent = new Intent(Getactivity.this, InformacionSupervisorActivity.class);
        //iniciar activity
        Log.d("Iot", "si");
        startActivity(intent);
    }*/
}
