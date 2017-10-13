package com.example.josue.p2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;
import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.widget.Toast.LENGTH_SHORT;

public class Fotografia extends AppCompatActivity {

    private static final int IMAGE_CAPTURE = 102;
    private static final int VIDEO_CAPTURE = 101;
    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "MainActivity";

    CoordinatorLayout coordinatorLayout;
    FloatingActionButton btnSelectImage;
    AppCompatImageView imgView;
    private ImageAdapter imageAdapter;
    ArrayList<String> f = new ArrayList<String>();// list of file paths
    File[] listFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotografia);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Carpeta de prueba
        File file = new File(Fotografia.this.getExternalFilesDir(null), Variables.NombreAlbum);
        if (!file.exists()) {
            file.mkdir();
            Toast.makeText(Fotografia.this, "Carpeta creada exitosamente!!", LENGTH_SHORT).show();
        }else{
            Toast.makeText(Fotografia.this, "Carpeta no creada, ya existe!!", LENGTH_SHORT).show();
        }
        Toast.makeText(Fotografia.this, file.toString(), LENGTH_SHORT).show();

//-----------------------Mostrar galería--------------------------------------
        getFromSdcard();
        GridView imagegrid = (GridView) findViewById(R.id.PhoneImageGrid);
        imageAdapter = new ImageAdapter();
        imagegrid.setAdapter(imageAdapter);
//-----------------------Tomar fotografía-------------------------------------
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String FileName = "PIC_" + timeStamp + "_";

                File mediaFile =
                        new File(Fotografia.this.getExternalFilesDir(null)
                                + "/"+FileName+"mifoto.jpg");

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                Uri videoUri = Uri.fromFile(mediaFile);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);

                startActivityForResult(intent, IMAGE_CAPTURE);
            }
        });
//-----------------------Tomar vídeo-------------------------------------
        FloatingActionButton video = (FloatingActionButton) findViewById(R.id.fab2);
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String FileName = "MP4_" + timeStamp + "_";

                File mediaFile=new File(Fotografia.this.getExternalFilesDir(null)+"/"+FileName+"mivideo.mp4");

                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                Uri videoUri = Uri.fromFile(mediaFile);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                startActivityForResult(intent, VIDEO_CAPTURE);
            }
        });
//-----------------------importar vídeo-------------------------------------
        FloatingActionButton importar = (FloatingActionButton) findViewById(R.id.fab3);
        importar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen"), SELECT_PICTURE);
            }
        });
    }

    public void getFromSdcard()
    {
        File file= new File(this.getExternalFilesDir(null),Variables.NombreAlbum);

        if (file.isDirectory())
        {
            try{
                listFile = file.listFiles();
                for (int i = 0; i < listFile.length; i++)
                {
                    f.add(listFile[i].getAbsolutePath());
                }
            }catch (Exception e){
                Toast.makeText(this, "Error: " + e.toString()+" - ", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Dirección inválida:" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "El vídeo ha sido guardado", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Grabación cancelada.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "La grabación ha fallado.",Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "La imagen ha sido guardada.", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Captura cancelada",                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "La captura ha fallado.",Toast.LENGTH_LONG).show();
            }
        }
    }

    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ImageAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return f.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(
                        R.layout.gelleryitem, null);
                holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);

                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }


            Bitmap myBitmap = BitmapFactory.decodeFile(f.get(position));
            holder.imageview.setImageBitmap(myBitmap);
            return convertView;
        }
    }
    class ViewHolder {
        ImageView imageview;


    }
}