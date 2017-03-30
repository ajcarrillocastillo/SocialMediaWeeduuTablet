package com.carrillo.jesus.socialmediaweeduutablet;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class PreviewActivity extends AppCompatActivity {
    private static final String TAG = "PreviewActivity";
    private Button repetir, aceptar,cancelar;
    private CameraActivity cameraActivityView=new CameraActivity();
    private Activity mActivity;
    private String ruta;
    private int width;
    private int height;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //pantalla Completa y sin ActionBar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_preview);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        ///////////////////////////
        mActivity=this;
        repetir = (Button) findViewById(R.id.buttonRepetir);
        aceptar =(Button) findViewById(R.id.buttonAceptarPreview);
        cancelar=(Button)findViewById(R.id.buttonCancelar);
        ruta=getIntent().getExtras().getString("ruta");
        width=getIntent().getExtras().getInt("width");
        height=getIntent().getExtras().getInt("height");
        assert repetir != null;
        repetir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///Borrar imagen en compartir tambien hay que quitarlo si pulsa cancelar
                File f=new File(ruta);
                f.delete();
                ///
                /* directly go to FirstActivity, finish all intermediate ones.*/
                Intent intent = new Intent(mActivity, SocialMediaMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, CompartirActivity.class);
                intent.putExtra("ruta", ruta);
                intent.putExtra("width",width);
                intent.putExtra("height",height);
                startActivity(intent);

            }
        });

        //Recoger Imagen
        BitmapFactory.Options opt = new BitmapFactory.Options();
        //Se puede pasar por parametros si se van a usar otras resoluciones
        opt.outWidth=width;
        opt.outHeight=height;
        Bitmap imageBmp= BitmapFactory.decodeFile(ruta,opt);
        /////////////////
        ImageView IV = (ImageView) findViewById(R.id.imageViewPreview);
        IV.setImageBitmap(imageBmp);
    }
        @Override
    public void onBackPressed(){
            ///Borrar imagen en cancelar y en la vista compartir tambien hay que quitarlo si pulsa cancelar
            File f=new File(ruta);
            f.delete();
            ///
            super.onBackPressed();
        }

    @Override
    protected void onResume() {
        super.onResume();
        fullScreenCall();
    }

    public void fullScreenCall() {

        //for new api versions.
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

    }
}
