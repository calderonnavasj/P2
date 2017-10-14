package com.example.josue.p2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by Elka on 13/10/2017.
 */

public class funcionAlbunes {

    public static void agregaCarpeta2(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final EditText textoBusqueda = new EditText(context);
        builder.setTitle("Ingrese el nombre de la carpeta nueva por favor: ");   // Título
        builder.setView(textoBusqueda);
        builder.setPositiveButton("Crear", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //texto ingresado por el usuario
                String texto = textoBusqueda.getText().toString();
                albums.nombre = texto;
                File file = new File(context.getExternalFilesDir(null), "Carpetas/"+ albums.nombreSeleccionado+"/"+texto);
                if (!file.exists()&& texto!=null &&texto!=" ") {
                    file.mkdir();
                    Toast.makeText(context, "Carpeta creada exitosamente!!", LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Carpeta no creada, ya existe o hay espacios en blanco!!", LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
         builder.create();
        builder.show();
    }

    public static void agregaCarpeta(final Context context){
        File file = new File(context.getExternalFilesDir(null), "Carpetas/"+ albums.nombre);
        if (!file.exists()&& albums.nombre!=null &&albums.nombre!=" ") {
            file.mkdir();
            Toast.makeText(context, "Carpeta creada exitosamente!!", LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Carpeta no creada, ya existe o hay espacios en blanco!!", LENGTH_SHORT).show();
        }
    }

  public static   void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }

    public static void eliminaCarpeta(final Context context, final File file2){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle("Eliminando Carpeta")
                .setMessage("Estás seguro que quieres eliminar la carpeta?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteRecursive(file2);
                        //muestra un toast informando la accion realizada
                        Toast.makeText(context, "La carpeta "+albums.nombreSeleccionado+" esta eliminada", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context,MainActivity.class);
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}