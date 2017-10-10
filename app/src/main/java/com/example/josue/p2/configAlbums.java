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
import android.widget.Toast;

import java.io.File;


public class configAlbums extends Fragment {

    public configAlbums() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_config_albums, container, false);
    }

    public void addAlbum(View view){
        //File nuevaCarpeta = new File(getFilesDir(), "nuevaCarpeta");
        //nuevaCarpeta.mkdirs();


        // Comprobamos si la carpeta está ya creada
        File f = new File(Environment.getExternalStorageDirectory() + "/nuevaCarpeta");

        // Si la carpeta no está creada, la creamos.
        if(!f.isDirectory()) {
            String newFolder = "/nuevaCarpeta"; //nuevaCarpeta es el nombre de la Carpeta que vamos a crear
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File myNewFolder = new File(extStorageDirectory + newFolder);
            myNewFolder.mkdir(); //creamos la carpeta
        }else{
            Toast.makeText(this.getContext(), "", Toast.LENGTH_SHORT).show();
        }
    }


}
