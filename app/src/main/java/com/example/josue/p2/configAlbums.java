package com.example.josue.p2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;


public class configAlbums extends Fragment {

    public configAlbums() {
        // Required empty public constructor
    }

    public static ArrayList<String>listCarpetas ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_config_albums, container, false);
        final ListView listV= (ListView) rootView.findViewById(R.id.listViewSho);
        nombreCarpetas(listV);
        rootView.findViewById(R.id.addCarpeta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText text = (EditText)rootView.findViewById(R.id.carpetaCrear);
                albums.nombre  = text.getText().toString();
                addAlbum(v);

            }
        });
        listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //optiene el nombre de la carpeta selccionada.
                albums.nombreSeleccionado=listV.getItemAtPosition(position).toString();
                //carga la siguiente activity de la galería
                Intent intent = new Intent("com.example.josue.p2.Fotografia");
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void nombreCarpetas(ListView listV) {
        //se crea un arraylist para el almacenamiento de las carpetas.
        listCarpetas = new ArrayList<String>();
        //se crea el argchivo con el nombre que tiene la carpeta donde contienen las carpetas.
        File f = new File(getActivity().getExternalFilesDir(null),"Carpetas");
        File[] files = f.listFiles();
        //recorre toda la carpeta para tomar todos sus datos.
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                listCarpetas.add(file.getName());
            }
        }if(listCarpetas.size()>0){//si la carpeta es vacía no hace nada, de lo contrario debe llenar el listview
            ArrayAdapter<String>adapter = new ArrayAdapter<String>(getActivity(),R.layout.filelist,R.id.nombre_fila_lista,listCarpetas);
            listV.setAdapter(adapter);
        }
    }

    public void addAlbum(View view){
        //llama al metodo que crea la carpeta
        funcionAlbunes.agregaCarpeta(getActivity());
        //refresca el fragment en el que se encuentra
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
}
