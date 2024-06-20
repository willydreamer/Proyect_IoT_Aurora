package com.example.aurora.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aurora.Bean.Sitio;
import com.example.aurora.Bean.Usuario;
import com.example.aurora.R;
import com.example.aurora.databinding.ActivityInformacionSitioBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class AdminInformacionSitioActivity extends AppCompatActivity {

    private ActivityInformacionSitioBinding binding;
    private FirebaseFirestore db;
    private Sitio sitio;
    ImageView fotoSitio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInformacionSitioBinding.inflate(getLayoutInflater());
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
            fotoSitio = findViewById(R.id.imageViewProfilePrincipal);
            Log.d("supervisor-uu", "onCreate: " + sitio.getEncargado());

            setupSpinner(binding.spinnerTipoZona, R.array.tipo_zona_options);
            setupSpinner(binding.spinnerOperadora, R.array.operadora_options);


            binding.spinnerTipoZona.setSelection(getIndexFromArray(R.array.tipo_zona_options, sitio.getTipoDeZona()));
            binding.spinnerOperadora.setSelection(getIndexFromArray(R.array.operadora_options, sitio.getOperadora()));

            if (sitio.getEncargado() != null && !sitio.getEncargado().isEmpty()) {
                // Hay un encargado asignado, mostrar la información del encargado
                mostrarInformacionEncargado(sitio.getEncargado());
            } else {
                binding.elegirSupervisor.setVisibility(View.VISIBLE);
                binding.elegirSupervisor.setOnClickListener(v -> cargarSupervisoresDisponibles());
                binding.cardViewResponsable.setVisibility(View.GONE);
            }
            if (sitio.getFotoURL() != null && !sitio.getFotoURL().isEmpty()) {
                Picasso.get()
                        .load(sitio.getFotoURL())
                        .placeholder(R.drawable.perfil_icono) // Reemplaza con tu imagen por defecto
                        .transform(new CropCircleTransformation())
                        .into(fotoSitio);
            } else {
                Picasso.get()
                        .load(R.drawable.perfil_icono) // Imagen por defecto
                        .transform(new CropCircleTransformation())
                        .into(fotoSitio);
            }
        }

        binding.btnEditar.setOnClickListener(v -> toggleEditMode(true));
        binding.btnGuardar.setOnClickListener(v -> {
            toggleEditMode(false);
            saveChanges();
        });

        binding.buttonEliminarSitio.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(AdminInformacionSitioActivity.this);
            builder.setMessage("¿Desea eliminar definitivamente el sitio " + sitio.getIdSitio() + "?")
                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Eliminar el sitio de la base de datos
                            db.collection("sitios")
                                    .document(sitio.getIdSitio())
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Intent intent = new Intent(AdminInformacionSitioActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            Toast.makeText(AdminInformacionSitioActivity.this, "Sitio eliminado exitosamente", Toast.LENGTH_SHORT).show();
                                            finish(); // Cerrar la actividad actual
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("AdminInformacionSitio", "Error al eliminar sitio", e);
                                            Toast.makeText(AdminInformacionSitioActivity.this, "Error al eliminar sitio", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        binding.trash.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(AdminInformacionSitioActivity.this);
            builder.setMessage("¿Está seguro que quiere desasignar el encargado actual?")
                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Actualizar el campo 'encargado' del sitio a un string vacío
                            db.collection("sitios")
                                    .document(sitio.getIdSitio())
                                    .update("encargado", "")
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AdminInformacionSitioActivity.this, "Encargado desasignado con éxito", Toast.LENGTH_SHORT).show();
                                            // Ocultar el CardView del encargado y mostrar el Spinner
                                            binding.cardViewResponsable.setVisibility(View.GONE);
                                            binding.elegirSupervisor.setVisibility(View.VISIBLE);
                                            isSpinnerInitial = true;
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("AdminInformacionSitio", "Error al desasignar encargado", e);
                                            Toast.makeText(AdminInformacionSitioActivity.this, "Error al desasignar encargado", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

    }
    // Método para obtener el índice del valor en el array
    private int getIndexFromArray(int arrayResId, String value) {
        String[] array = getResources().getStringArray(arrayResId);
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(value)) {
                return i;
            }
        }
        return 0;
    }

    private void mostrarInformacionEncargado(String encargadoId) {
        db = FirebaseFirestore.getInstance();
        db.collection("usuarios")
                .document(encargadoId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        Usuario supervisor  = document.toObject(Usuario.class);
                        binding.textNombres.setText(supervisor.getNombre() + " " + supervisor.getApellido());
                        binding.textDni.setText("Dni: " + supervisor.getDni());
                        binding.textCorreo.setText("Correo: " + supervisor.getCorreo());
                    } else {
                        Log.d("msg-test", "get failed with ", task.getException());
                    }
                });
    }

    private boolean isSpinnerInitial = true;

    private void cargarSupervisoresDisponibles() {
        db = FirebaseFirestore.getInstance();
        db.collection("usuarios")
                .whereEqualTo("rol", "supervisor")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> supervisores = new ArrayList<>();
                        List<String> supervisoresIds = new ArrayList<>(); // Lista para almacenar los IDs de los supervisores
                        for (DocumentSnapshot document : task.getResult()) {
                            Usuario supervisor = document.toObject(Usuario.class);
                            String nombreCompleto = supervisor.getNombre() + " " + supervisor.getApellido();
                            supervisores.add(nombreCompleto);
                            supervisoresIds.add(supervisor.getIdUsuario()); // Agregar el ID del supervisor a la lista
                        }
                        // Configurar el Spinner con la lista de supervisores disponibles
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, supervisores);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.spinnerSupervisores.setAdapter(adapter);
                        binding.spinnerSupervisores.setVisibility(View.VISIBLE);
                        binding.cardViewResponsable.setVisibility(View.GONE);

                        // Manejar la selección de un supervisor
                        binding.spinnerSupervisores.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                if (!isSpinnerInitial) {
                                    String selectedSupervisorId = supervisoresIds.get(position); // Obtener el ID del supervisor seleccionado
                                    mostrarConfirmacionAsignacionSupervisor(selectedSupervisorId);
                                }
                                isSpinnerInitial = false; // Cambiar el estado después de la primera vez
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parentView) {
                                // No hacer nada
                            }
                        });
                    } else {
                        Log.d("msg-test", "Error getting documents: ", task.getException());
                    }
                });
    }


    private void setupSpinner(Spinner spinner, int arrayId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                arrayId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setEnabled(false);
        spinner.setAdapter(adapter);
    }

    private void toggleEditMode(boolean enable) {
        binding.editDepartamento.setEnabled(enable);
        binding.editProvincia.setEnabled(enable);
        binding.editDistrito.setEnabled(enable);
        //binding.editLatitud.setEnabled(enable);
        //binding.editLongitud.setEnabled(enable);
        binding.spinnerTipoZona.setEnabled(enable);
        binding.spinnerOperadora.setEnabled(enable);

        // Mostrar el botón de guardar solo cuando esté en modo edición
        binding.btnGuardar.setVisibility(enable ? View.VISIBLE : View.GONE);
        // Ocultar el botón de editar cuando esté en modo edición
        binding.btnEditar.setVisibility(enable ? View.GONE : View.VISIBLE);
    }

    private void saveChanges() {
        String departamento = binding.editDepartamento.getText().toString();
        String provincia = binding.editProvincia.getText().toString();
        String distrito = binding.editDistrito.getText().toString();
        String latitud = binding.editLatitud.getText().toString();
        String longitud = binding.editLongitud.getText().toString();
        String tipoZona = binding.spinnerTipoZona.getSelectedItem().toString();
        String operadora = binding.spinnerOperadora.getSelectedItem().toString();

        // Actualizar los valores en el objeto sitio
        sitio.setDepartamento(departamento);
        sitio.setProvincia(provincia);
        sitio.setDistrito(distrito);
        sitio.setLatitud(latitud);
        sitio.setLongitud(longitud);
        sitio.setTipoDeZona(tipoZona);
        sitio.setOperadora(operadora);

        // Guardar los cambios en la base de datos
        db.collection("sitios")
                .document(sitio.getIdSitio())
                .set(sitio)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AdminInformacionSitioActivity.this, "Cambios guardados con éxito", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("AdminInformacionSitio", "Error al guardar cambios", e);
                        Toast.makeText(AdminInformacionSitioActivity.this, "Error al guardar cambios", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void mostrarConfirmacionAsignacionSupervisor(String selectedSupervisorId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Está seguro que quiere designar a " + selectedSupervisorId + " como supervisor de este sitio?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Actualizar la base de datos con el ID del supervisor seleccionado
                        actualizarEncargado(selectedSupervisorId);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // No hacer nada
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void actualizarEncargado(String selectedSupervisorId) {
        // Actualizar el campo 'encargado' del sitio en la base de datos
        db.collection("sitios")
                .document(sitio.getIdSitio())
                .update("encargado", selectedSupervisorId)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Mostrar el cardView del supervisor asignado
                        binding.cardViewResponsable.setVisibility(View.VISIBLE);
                        binding.elegirSupervisor.setVisibility(View.GONE);
                        Toast.makeText(AdminInformacionSitioActivity.this, "Supervisor asignado con éxito", Toast.LENGTH_SHORT).show();
                        mostrarInformacionEncargado(selectedSupervisorId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("msg-test", "Error updating document", e);
                    }
                });
    }
}

