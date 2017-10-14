package com.example.josue.p2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Registro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText pass = (EditText) findViewById(R.id.passsword);
                EditText nombre = (EditText) findViewById(R.id.nombre);
                EditText usuario = (EditText) findViewById(R.id.NUsuario);
                try {
                    if (!pass.getText().toString().isEmpty() && !nombre.getText().toString().isEmpty() && !usuario.getText().toString().isEmpty()) {
                        UsuarioDBHelper helper = new UsuarioDBHelper(Registro.this);
                        SQLiteDatabase database = helper.getWritableDatabase();
                        String login = UsuarioReaderContract.Usuario.COLUMN_LOGIN;
                        String table = UsuarioReaderContract.Usuario.TABLE_NAME;
                        String Nusuario = usuario.getText().toString();
                        Cursor c = database.rawQuery("SELECT " + login + " FROM " + table + " WHERE " + login + "='" + Nusuario + "'", null);
                        if (c.getCount()>0) {
                            Toast.makeText(Registro.this, "Este correo ya ha sido usado. Elija otro.", Toast.LENGTH_SHORT).show();
                        } else {
                            ContentValues values = new ContentValues();
                            values.put(UsuarioReaderContract.Usuario.COLUMN_LOGIN, usuario.getText().toString());
                            values.put(UsuarioReaderContract.Usuario.COLUMN_NAME, nombre.getText().toString());
                            values.put(UsuarioReaderContract.Usuario.COLUMN_PASSWORD, pass.getText().toString());
                            long insert = database.insert(UsuarioReaderContract.Usuario.TABLE_NAME, null, values);
                            database.close();
                            Toast.makeText(Registro.this, "Usuario " + usuario.getText().toString() + " agregado éxito.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        Toast.makeText(Registro.this, "Rellene todos los campos.", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(Registro.this, "Error: "+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
