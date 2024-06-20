

package com.example.aurora.Supervisor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aurora.Bean.Sitio;
import com.example.aurora.R;
import com.example.aurora.databinding.ActivityInformacionSitioBinding;
import com.example.aurora.databinding.ActivitySupervisorInfoSitioBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class SupervisorInfoSitio extends AppCompatActivity {

    private ActivitySupervisorInfoSitioBinding binding;
    private FirebaseFirestore db;
    private Sitio sitio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_supervisor_info_sitio);
        binding = ActivitySupervisorInfoSitioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Recibir sitio
        Intent intent = getIntent();
        sitio = (Sitio) intent.getSerializableExtra("sitio");

        if(sitio != null){
            binding.textViewNamePrincipal.setText(sitio.getIdSitio());
            binding.editDepartamento.setText(sitio.getDepartamento());
            binding.editProvincia.setText(sitio.getProvincia());
            binding.editDistrito.setText(sitio.getDistrito());
            binding.editLatitud.setText(sitio.getLatitud());
            binding.editLongitud.setText(sitio.getLongitud());
            Log.d("supervisor-uu", "onCreate: " + sitio.getEncargado());

            setupSpinner(binding.spinnerTipoZona, R.array.tipo_zona_options);
            setupSpinner(binding.spinnerOperadora, R.array.operadora_options);


            binding.spinnerTipoZona.setSelection(getIndexFromArray(R.array.tipo_zona_options, sitio.getTipoDeZona()));
            binding.spinnerOperadora.setSelection(getIndexFromArray(R.array.operadora_options, sitio.getOperadora()));
        }
    }

    private void setupSpinner(Spinner spinner, int arrayId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                arrayId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setEnabled(false);
        spinner.setAdapter(adapter);
    }

    private int getIndexFromArray(int arrayResId, String value) {
        String[] array = getResources().getStringArray(arrayResId);
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(value)) {
                return i;
            }
        }
        return 0;
    }
}