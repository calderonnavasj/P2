package com.example.josue.p2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import java.io.File;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by Elka on 13/10/2017.
 */

public class funcionAlbunes {

    public static void agregaCarpeta(Context context){
        File file = new File(context.getExternalFilesDir(null), "Carpetas/"+ albums.nombre);
        if (!file.exists()&& albums.nombre!=null &&albums.nombre!=" ") {
            file.mkdir();
            Toast.makeText(context, "Carpeta creada exitosamente!!", LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Carpeta no creada, ya existe o hay espacios en blanco!!", LENGTH_SHORT).show();
        }
    }
    public static void eliminaCarpeta(final Context context, final File fil2){
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
                        File file2 = fil2;
                        file2.delete();//elimina el file
                        //muestra un toast informando la accion realizada
                        Toast.makeText(context, "La carpeta "+albums.nombreSeleccionado+" esta eliminada", Toast.LENGTH_LONG).show();
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
