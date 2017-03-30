package com.carrillo.jesus.socialmediaweeduutablet;

/**
 * Created by jesus on 30/03/2017.
 */


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by jesus on 30/03/2017.
 */

// gestionamos la desgarga de datos de la base de datos de forma asincrona y si sale bien abriremos ver acontecimiento
public class ConexionAsincTask extends AsyncTask<String, String, String> {
    String image;
    String email;
    String ruta;
    int width, height;
    Context mContext;
    HttpURLConnection urlConnection;
    ProgressBar barraCarga;

    boolean existeAcontecimiento = true;

    /*public ConexionAsincTask(Context myContext, String image,String email ,ProgressBar barraCarga) {
        this.image = image;
        this.email=email;
        this.myContext=myContext;



    }*/
    public ConexionAsincTask(Context myContext, String ruta, int width, int height, String email, ProgressBar barraCarga) {
        this.ruta = ruta;
        this.width = width;
        this.height = height;
        this.email = email;
        this.mContext = myContext;
        this.barraCarga = barraCarga;


    }

    //accion antes de cargar
    @Override
    protected void onPreExecute() {


    }

    @Override
    protected String doInBackground(String... args) {
        image = getStringFromBitmap(recogerBitmapRuta(ruta, width, height));
        Log.e("imagen", image);
        String result = "error no hemos recogido nada";
        String query = "http://serverapppepe.hol.es/api/v1/compartir";
        String json = "{\"image\":\"" + image + "\",\"email\":\"" + email + "\"}";
        try {
            URL url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");

            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.close();

            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            result = IOUtils.toString(in, "UTF-8");


            in.close();
            conn.disconnect();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //   return jsonObject;
        return result;
    }

    //acciones despues de ejecutar
    @Override
    protected void onPostExecute(String result) {

        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
        //JSONObject jsonObject = new JSONObject(result);
        if (this.existeAcontecimiento) {

            //((Activity) myContext).finish();
        } else {
            //le pasamos la vista del boton y mandamos el mensaje
          /*  Snackbar.make(boton, "No Existe Acontecimiento", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();*/

        }

        barraCarga.setVisibility(View.GONE);

    }

    private String getStringFromBitmap(Bitmap bitmapPicture) {
 /*
 * This functions converts Bitmap picture to a string which can be
 * JSONified.
 * */
        final int COMPRESSION_QUALITY = 100;
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
                byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }

    private Bitmap recogerBitmapRuta(String ruta, int width, int height) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        //Se puede pasar por parametros si se van a usar otras resoluciones
        opt.outWidth = width;
        opt.outHeight = height;
        Bitmap imageBmp = BitmapFactory.decodeFile(ruta, opt);
        return imageBmp;
    }
}
