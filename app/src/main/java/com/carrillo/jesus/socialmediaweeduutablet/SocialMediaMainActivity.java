package com.carrillo.jesus.socialmediaweeduutablet;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class SocialMediaMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //pantalla Completa y sin ActionBar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_social_media_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
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
        Button butFoto= (Button) findViewById(R.id.buttonTomarFoto);
        Button butComoFunciona= (Button) findViewById(R.id.buttonComoFunciona);
        butFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CameraActivity.class));

            }
        });
        butComoFunciona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ComoFuncionaActivity.class));

            }
        });
    }
}
