package com.example.josue.p2;

import android.content.ContentValues;
import android.content.Intent;
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
                if(!pass.getText().toString().isEmpty()&&!nombre.getText().toString().isEmpty()&&!usuario.getText().toString().isEmpty()){
                    UsuarioDBHelper helper= new UsuarioDBHelper(Registro.this);
                    SQLiteDatabase database = helper.getWritableDatabase();
                    ContentValues values= new ContentValues();
                    values.put(UsuarioReaderContract.Usuario.COLUMN_LOGIN, usuario.getText().toString());
                    values.put(UsuarioReaderContract.Usuario.COLUMN_NAME, nombre.getText().toString());
                    values.put(UsuarioReaderContract.Usuario.COLUMN_PASSWORD, pass.getText().toString());
                    long insert = database.insert(UsuarioReaderContract.Usuario.TABLE_NAME, null, values);
                    database.close();
                    Toast.makeText(Registro.this, "Usuario agregado exito.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent("com.example.josue.p2.Registro");
                    startActivity(intent);
                }else{
                    Toast.makeText(Registro.this, "Rellene todos los campos.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
