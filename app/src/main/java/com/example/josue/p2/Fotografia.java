package com.example.josue.p2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.widget.Toast.LENGTH_SHORT;
import static com.example.josue.p2.R.id.imageView;

public class Fotografia extends AppCompatActivity {

    private static final int IMAGE_CAPTURE = 102;
    private static final int VIDEO_CAPTURE = 101;
    private static final int SELECT_PICTURE = 100;

    private static ArrayList<String> PathImages = new ArrayList<>();
    int contadorimagenes = 0;
    int contadorvideos;
    public static ArrayList<String> PathVideos = new ArrayList<>();// list of file paths of videos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotografia);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        obtenerImagenes();
        obtenerVideos();
        File file = new File(Fotografia.this.getExternalFilesDir(null), Variables.NombreAlbum);
        if (!file.exists()) {
            file.mkdir();
            Toast.makeText(Fotografia.this, "Carpeta creada exitosamente", LENGTH_SHORT).show();
        } else {
            //Toast.makeText(Fotografia.this, "Carpeta no creada, ya existe", LENGTH_SHORT).show();
        }

//-----------------------Mostrar galería--------------------------------------
        GridView gridview = (GridView) findViewById(R.id.PhoneImageGrid);
        ImageAdapter myImageAdapter = new ImageAdapter(this);
        gridview.setAdapter(myImageAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

            }
        });

        myAsyncTaskLoadFiles = new AsyncTaskLoadFiles(myImageAdapter);
        myAsyncTaskLoadFiles.execute();

        //gridview.setOnItemClickListener(myOnItemClickListener);

        //ImageAdapter imageAdapter = new ImageAdapter(Fotografia.this, FilePathStrings,FileNameStrings);
        //imagegrid.setAdapter(imageAdapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String FileName = "PIC_" + timeStamp + "_";
                File mediaFile = new File(Fotografia.this.getExternalFilesDir(null) + "/" + Variables.NombreAlbum + "/" + FileName + "mifoto.jpg");
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
                File mediaFile = new File(Fotografia.this.getExternalFilesDir(null) + "/" + Variables.NombreAlbum + "/" + FileName + "mivideo.mp4");
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                Uri videoUri = Uri.fromFile(mediaFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                startActivityForResult(intent, VIDEO_CAPTURE);
            }
        });
        //-----------------------Crear Carpeta Album-------------------------------------
        FloatingActionButton creaCarpeta = (FloatingActionButton) findViewById(R.id.fab5);
        creaCarpeta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                addAlbum();
                //refresca el activity para que tome la carpeta creada recientemente.
                Intent refresh = new Intent(Fotografia.this,Fotografia.class);
                startActivity(refresh);//Start the same Activity
                finish();
            }
        });
        //-----------------------Eliminar Carpeta Album-------------------------------------
        FloatingActionButton delCarpeta = (FloatingActionButton) findViewById(R.id.fab4);
        delCarpeta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                File file = new File(Fotografia.this.getExternalFilesDir(null), "Carpetas/"+ albums.nombreSeleccionado);
                if (file.exists()){
                    File file2 = new File(getExternalFilesDir(null),"Carpetas/"+albums.nombreSeleccionado);
                    funcionAlbunes.eliminaCarpeta(Fotografia.this,file2);
                    //vuelve al fragment de albums.
                    Intent intent = new Intent(Fotografia.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
//-----------------------importar vídeo-------------------------------------
        FloatingActionButton importar = (FloatingActionButton) findViewById(R.id.fab3);
        importar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen"), SELECT_PICTURE);
            }
        });
    }


    public void addAlbum(){
        //llama al metodo que crea la carpeta
        funcionAlbunes.agregaCarpeta(this);
        //refresca el activity en el que se encuentra

    }

    // array of supported extensions (use a List if you prefer)
    static final String[] EXTENSIONS = new String[]{
            "gif", "png", "bmp", "jpg", "jepg" // and other formats you need
    };
    // filter to identify images based on their extensions
    static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {
        @Override
        public boolean accept(final File dir, final String name) {
            for (final String ext : EXTENSIONS) {
                if (name.endsWith("." + ext)) {
                    return (true);
                }
            }
            return (false);
        }
    };

    public void obtenerImagenes() {
        try {
            File dir = new File(Fotografia.this.getExternalFilesDir(null) + "/" + Variables.NombreAlbum + "/");
            File[] filelist = dir.listFiles(IMAGE_FILTER);
            for (File f : filelist) { // do your stuff here
                PathImages.add(f.toString());
                contadorimagenes++;
            }
            //Toast.makeText(this, "Imágenes: " + contadorimagenes, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error de imagen: " + e.toString() + " - " + contadorimagenes, Toast.LENGTH_SHORT).show();
        }
    }


    static final String[] EXTENSIONS2 = new String[]{
            "mp4", "3gp" // and other formats you need
    };
    // filter to identify images based on their extensions
    static final FilenameFilter VIDEO_FILTER = new FilenameFilter() {

        @Override
        public boolean accept(final File dir, final String name) {
            for (final String ext : EXTENSIONS2) {
                if (name.endsWith("." + ext)) {
                    return (true);
                }
            }
            return (false);
        }
    };

    public void obtenerVideos() {
        try {
            File dir = new File(Fotografia.this.getExternalFilesDir(null) + "/" + Variables.NombreAlbum + "/");
            File[] filelist = dir.listFiles(VIDEO_FILTER);
            for (File f : filelist) { // do your stuff here
                PathVideos.add(f.toString());
                contadorvideos++;
            }
            //Toast.makeText(this, "Videos: " + contadorvideos, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error de videos: " + e.toString() + " - " + contadorvideos, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "La grabación ha fallado.", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                //ImageView mImage = (ImageView) findViewById(R.id.imageView2);
                //mImage.setImageBitmap(decodeSampledBitmapFromFile(Fotografia.this.getExternalFilesDir(null)+"/"+Variables.NombreAlbum+"/"+"mifoto.jpg", 500, 250));
                Toast.makeText(this, "La imagen ha sido guardada.", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Captura cancelada", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "La captura ha fallado.", Toast.LENGTH_LONG).show();
            }
        }
    }

    AsyncTaskLoadFiles myAsyncTaskLoadFiles;

    public class AsyncTaskLoadFiles extends AsyncTask<Void, String, Void> {

        File targetDirector;
        ImageAdapter myTaskAdapter;

        public AsyncTaskLoadFiles(ImageAdapter adapter) {
            myTaskAdapter = adapter;
        }

        @Override
        protected void onPreExecute() {
            targetDirector = new File(Fotografia.this.getExternalFilesDir(null) + "/" + Variables.NombreAlbum);
            myTaskAdapter.clear();

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            File[] files = targetDirector.listFiles();
            for (File file : files) {
                publishProgress(file.getAbsolutePath());
                if (isCancelled()) break;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            myTaskAdapter.add(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            myTaskAdapter.notifyDataSetChanged();
            super.onPostExecute(result);
        }

    }

    public class ImageAdapter extends BaseAdapter {

        private Context mContext;
        ArrayList<String> itemList = new ArrayList<String>();

        public ImageAdapter(Context c) {
            mContext = c;
        }

        void add(String path) {
            itemList.add(path);
        }

        void clear() {
            itemList.clear();
        }

        void remove(int index){
            itemList.remove(index);
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        public class ViewHolder {

            public CheckBox ItemCheck;
            int id;
        }


        public ViewHolder holder;
        private ImageView imageView;
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View myView = convertView;
            if (convertView == null) { // if it's not recycled, initialize some
                // attributes
                holder = new ViewHolder();
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(220, 220));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }
            if(itemList.get(position).contains(".jpg")) {
                Bitmap bm = decodeSampledBitmapFromUri(itemList.get(position), 220,
                        220);
                imageView.setImageBitmap(bm);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Toast.makeText(mContext, new File(itemList.get(position)).getName(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(new File(itemList.get(position))), "image/*");
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(mContext, "Error al abrir:" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else{
                imageView.setImageBitmap(ThumbnailUtils.createVideoThumbnail(itemList.get(position),0));
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Toast.makeText(mContext, new File(itemList.get(position)).getName(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(new File(itemList.get(position))), "video/*");
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(mContext, "Error al abrir: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            return imageView;
        }

        public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth,
                                                 int reqHeight) {

            Bitmap bm = null;
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth,
                    reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeFile(path, options);

            return bm;
        }

        public int calculateInSampleSize(

                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {
                if (width > height) {
                    inSampleSize = Math.round((float) height
                            / (float) reqHeight);
                } else {
                    inSampleSize = Math.round((float) width / (float) reqWidth);
                }
            }

            return inSampleSize;
        }

    }

}