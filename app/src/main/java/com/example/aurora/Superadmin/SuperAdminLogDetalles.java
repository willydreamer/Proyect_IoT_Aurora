package com.example.aurora.Superadmin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.aurora.Bean.Log;
import com.example.aurora.Bean.Usuario;
import com.example.aurora.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SuperAdminLogDetalles#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SuperAdminLogDetalles extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String ARG_LOG = "log";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView fecha;
    TextView idUsuario;
    TextView titulo;
    TextView acti;
    TextView sitio;
    TextView descrip;

    EditText editActividad, editDescription,editTimestamp,editUsuario,editSitio;

    private Log logu;

    public SuperAdminLogDetalles() {
        // Required empty public constructor
    }


    public static SuperAdminLogDetalles newInstance(Log logu) {
        SuperAdminLogDetalles fragment = new SuperAdminLogDetalles();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LOG, logu);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            logu = (Log) getArguments().getSerializable(ARG_LOG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_super_admin_log_detalles, container, false);

         fecha = view.findViewById(R.id.timestamp);
         idUsuario = view.findViewById(R.id.user);
         titulo = view.findViewById(R.id.activitytext);
         acti = view.findViewById(R.id.acti);
         sitio = view.findViewById(R.id.sitionombre);
         descrip = view.findViewById(R.id.descriptiontext);
         editActividad = view.findViewById(R.id.editActividad);
         editDescription = view.findViewById(R.id.editDescription);
         editTimestamp = view.findViewById(R.id.editTimestamp);
         editUsuario = view.findViewById(R.id.editUsuario);
         editSitio = view.findViewById(R.id.editSitio);


        android.util.Log.d("SuperAdmin", "Nombre: " + logu.getActividad());
        android.util.Log.d("SuperAdmin", "Apellido: " + logu.getDescription());

        if (logu != null) {
            String olafecha = "" + logu.getTimestamp();
            String olausuario;
            if(logu.getUsuario()!= null) {
                olausuario = "" + logu.getUsuario().getNombre() + " " +logu.getUsuario().getApellido();
            }else {
                olausuario = "Sin Usuario";
            }
            String olasitio;
            if(logu.getSitio()!= null) {
                olasitio = "" + logu.getSitio().getIdSitio();
            }else {
                olasitio ="Sin Sitio";
            }

            String olatitulo = titulo.getText().toString().trim() + " " + logu.getActividad();
            String olaacti = "" + logu.getActividad();
            String oladescrip = "" + logu.getDescription();
            editTimestamp.setText(olafecha);
            editUsuario.setText(olausuario);
            titulo.setText(olatitulo);
            editActividad.setText(olaacti);
            editSitio.setText(olasitio);
            editDescription.setText(oladescrip);
        }

        return view;
    }
}