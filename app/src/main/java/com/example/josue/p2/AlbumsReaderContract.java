package com.example.josue.p2;

import android.provider.BaseColumns;

/**
 * Created by Elka on 10/10/2017.
 */

public class AlbumsReaderContract {
    private AlbumsReaderContract(){}

    public class Albums implements BaseColumns {
        public static final String TABLE_NAME = "NOMBRE";
    }
}
