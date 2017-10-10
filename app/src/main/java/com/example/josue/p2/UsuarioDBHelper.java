package com.example.josue.p2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by josue on 9/10/2017.
 */

public class UsuarioDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="presupuestapp.db";
    private static final String create = "CREATE TABLE "+ UsuarioReaderContract.Usuario.TABLE_NAME
            + " (" + UsuarioReaderContract.Usuario.COLUMN_LOGIN +" TEXT PRIMARY KEY, "+ UsuarioReaderContract.Usuario.COLUMN_NAME
            + " TEXT,"+UsuarioReaderContract.Usuario.COLUMN_PASSWORD + " TEXT)";
    private static final String upgrade ="DROP TABLE IF EXISTS "+UsuarioReaderContract.Usuario.TABLE_NAME;

    public UsuarioDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
