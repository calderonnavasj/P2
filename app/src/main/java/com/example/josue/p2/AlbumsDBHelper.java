package com.example.josue.p2;

/**
 * Created by Elka on 10/10/2017.
 */

public class AlbumsDBHelper {

    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="presupuestapp.db";
    private static final String create = "CREATE TABLE "+ UsuarioReaderContract.Usuario.TABLE_NAME
            + " ( " + UsuarioReaderContract.Usuario.COLUMN_LOGIN +" TEXT PRIMARY KEY, "+ UsuarioReaderContract.Usuario.COLUMN_NAME
            + " TEXT,"+UsuarioReaderContract.Usuario.COLUMN_PASSWORD + " TEXT)";
    private static final String upgrade ="DROP TABLE IF EXISTS " + UsuarioReaderContract.Usuario.TABLE_NAME;
}
