package com.example.josue.p2;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

        nombreCarpetas();
        ListView listV= (ListView) rootView.findViewById(R.id.listView);
        ArrayAdapter<String> fileList = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listCarpetas);
        listV.setAdapter(fileList);

        rootView.findViewById(R.id.addCarpeta).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File file = new File(getActivity().getExternalFilesDir(null), "Carpetas/"+rootView.findViewById(R.id.carpetaCrear));
                if (!file.exists()) {
                    file.mkdir();
                    Toast.makeText(getActivity(), "Carpeta creada exitosamente!!", LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "Carpeta no creada, ya existe!!", LENGTH_SHORT).show();
                }
                Toast.makeText(getActivity(), file.toString(), LENGTH_SHORT).show();
            }
        });
        return rootView;

    }




    public void nombreCarpetas() {

        listCarpetas = new ArrayList<String>();

        File f = new File(Environment.getExternalStorageDirectory() + "Carpetas");
        File[] files = f.listFiles();

        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                listCarpetas.add(file.getName());
            }
        }
    }


    /*public void addAlbum(View view){
        //File nuevaCarpeta = new File(getFilesDir(), "nuevaCarpeta");
        //nuevaCarpeta.mkdirs();

        Toast.makeText(getActivity(),"Text!",Toast.LENGTH_SHORT).show();
        // Comprobamos si la carpeta está ya creada
        File f = new File(Environment.getExternalStorageDirectory() + "/nuevaCarpeta");

        // Si la carpeta no está creada, la creamos.
        if(!f.isDirectory()) {
            String newFolder = "/nuevaCarpeta"; //nuevaCarpeta es el nombre de la Carpeta que vamos a crear
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File myNewFolder = new File(extStorageDirectory + newFolder);
            myNewFolder.mkdir(); //creamos la carpeta
        }else{
            Toast.makeText(getActivity().getApplicationContext() , "Ya existe", Toast.LENGTH_SHORT).show();
        }
    }*/


}
