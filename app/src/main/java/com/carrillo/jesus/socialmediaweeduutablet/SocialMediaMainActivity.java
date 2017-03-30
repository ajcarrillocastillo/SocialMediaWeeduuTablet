package com.carrillo.jesus.socialmediaweeduutablet;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import android.support.design.widget.Snackbar;

public class SocialMediaMainActivity extends AppCompatActivity {
    private Activity mActivity=this;
    private Button butFoto;
    private Boolean errorPermisos=false;
    private View mLayout;

    private static final int REQUEST_CAMERA_PERMISSION=200,REQUEST_STORAGE_PERMISSION=300;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //pantalla Completa y sin ActionBar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_social_media_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mLayout = findViewById(R.id.activity_social_media_main);

        // Here, thisActivity is the current activity

        ////////////////
        /////////////Conseguir keyHash=  IJ4LFuoo1xdXXoWhqtXtBZuP9+o=

/*
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.carrillo.jesus.socialmediaweeduutablet",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        */
        /////////////

         butFoto= (Button) findViewById(R.id.buttonTomarFoto);
        Button butComoFunciona= (Button) findViewById(R.id.buttonComoFunciona);

        butComoFunciona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ComoFuncionaActivity.class));

            }
        });
        butonCamera();

    }
  /*  private void verifyPermission()
    {
        //WRITE_EXTERNAL_STORAGE tiene implícito READ_EXTERNAL_STORAGE
        //porque pertenecen al mismo grupo de permisos
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        }
        else {
            //saveComments();
        }
    }


    private void requestPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            showSnackBar();
        }else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        }
    }

    private void showSnackBar() {

        Snackbar.make(mLayout,"no tiene permisos",Snackbar.LENGTH_LONG)
                .setAction("Opciones", new View.OnClickListener() {
                    @Override public void onClick(View view){
                        openSettings();
                    }}).show();
    }
    public void openSettings(){
        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);

    }
*/



private void permisos(){

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showSnackBarOpciones("Permisos de almacenamiento necesario para hacer fotos");

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_STORAGE_PERMISSION);

            }
        }


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                showSnackBarOpciones("Permisos de la camara necesario para hacer fotos");

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_CAMERA_PERMISSION);

            }
        }
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            butonCamera();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION: {
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    // close the app
                    butonCamera();
                } else {
                    butonCamera();
                }
                return;
            }
            case REQUEST_STORAGE_PERMISSION:{
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    // close the app
                    butonCamera();
                } else {
                    butonCamera();
                }
                return;

            }
        }
    }


    private void butonCamera(){
        errorPermisos=false;
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                errorPermisos=true;
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            errorPermisos=true;
        }
        if(!errorPermisos){
            butFoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), CameraActivity.class));
                }
            });
        }else{
            butFoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    permisos();
                }
            });
        }
        /*
            butFoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), CameraActivity.class));
                }
            });
        }else{
            butFoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    permisos();
                }
            });
        }*/
   }

   /* private void showToastShort(final String text) {
        final Activity activity = mActivity;
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }*/
    private void showSnackBar(String text) {

        Snackbar.make(mLayout,text,Snackbar.LENGTH_LONG).show();
    }

    private void showSnackBarOpciones(String text) {

        Snackbar.make(mLayout,text,Snackbar.LENGTH_LONG)
                .setAction("Opciones", new View.OnClickListener() {
                    @Override public void onClick(View view){
                        openPermissionSettings(mActivity);
                    }}).show();
    }

    public static void openPermissionSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + activity.getPackageName()));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
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
