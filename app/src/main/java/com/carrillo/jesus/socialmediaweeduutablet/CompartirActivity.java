package com.carrillo.jesus.socialmediaweeduutablet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompartirActivity extends AppCompatActivity {
    private TextView textTerminosYCondiciones,paraCompartir;
    private Activity mActivity;
    private EditText editTextEmail;
    private Button btn_CancelarTerminos, btn_AceptarTerminos,btn_AceptarEmail,btn_Compartir,btn_CancelarCompartir;
    private ImageView imageViewEmail;
    private CheckBox checkTerminosYCondiciones;
    private LayoutInflater layoutInflater;
    private View popupView;
    private PopupWindow popupWindow;
    private String ruta;
    private int width;
    private int height;
    private boolean flagPopUpOpen=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //pantalla Completa y sin ActionBar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_compartir);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        ////////////////////////////////////
        mActivity=this;
        ruta=getIntent().getExtras().getString("ruta");
        width=getIntent().getExtras().getInt("width");
        height=getIntent().getExtras().getInt("height");
        btn_CancelarCompartir=(Button)findViewById(R.id.buttonCancelarCompartir);
        btn_Compartir=(Button)findViewById(R.id.buttonCompartir);
        checkTerminosYCondiciones=(CheckBox)findViewById(R.id.checkBoxTerminosYCondiciones);
        paraCompartir = (TextView) findViewById(R.id.textViewParaCompartir);
        textTerminosYCondiciones = (TextView) findViewById(R.id.textViewTerminosYCondiciones);
        editTextEmail=(EditText)findViewById(R.id.editTextEmail);
        assert editTextEmail!=null;
        editTextEmail.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {

                InputMethodManager inputManager = (InputMethodManager)mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(editTextEmail.getWindowToken(), 0);
                return true;

            }// end onEditorAction.
        });



        assert btn_Compartir != null;
        btn_Compartir.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
             if(checkTerminosYCondiciones.isChecked()){
                 if(editTextEmail.getText().toString().isEmpty()){
                 showToastShort("Se compartiría la foto sin enviar Email"+ruta);
                 }else{
                     Pattern mPattern = Pattern.compile("^.+@([a-z0-9]+[-.])+[a-z0-9]+$");

                     Matcher matcher = mPattern.matcher(editTextEmail.getText().toString());
                     if(!matcher.find())
                     {
                         showToastShort("Email no valido");
                     }else{
                         showToastShort("Se compartiría la foto"+ruta+"con el email:"+editTextEmail.getText().toString());
                     }
                 }
             }else{
                 showToastShort(getString(R.string.text_terminos_rechazados));
             }
            }
        });
        assert btn_CancelarCompartir !=null;
        btn_CancelarCompartir.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        ///Borrar imagen en preview tambien hay que quitarlo en el onback y en cancelar
                        File f=new File(ruta);
                        f.delete();
                        ///
                        Intent intent = new Intent(mActivity, SocialMediaMainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
        );
        assert editTextEmail !=null;
        editTextEmail.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        if(popupView!=null) {
                            if (popupView.isEnabled()) {
                                popupWindow.dismiss();
                                flagPopUpOpen = false;
                            }
                        }
                        }
                }
        );

        assert textTerminosYCondiciones != null;
        textTerminosYCondiciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flagPopUpOpen) {
                    flagPopUpOpen=true;
                    layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    popupView = layoutInflater.inflate(R.layout.pop_up_terminos_y_condiciones, null);
                    popupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.WRAP_CONTENT,
                            RadioGroup.LayoutParams.WRAP_CONTENT);
                    btn_AceptarTerminos = (Button) popupView.findViewById(R.id.buttonAceptarTerminos);
                    btn_AceptarTerminos.setOnClickListener(new Button.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            checkTerminosYCondiciones.setChecked(true);
                            popupWindow.dismiss();
                            flagPopUpOpen=false;
                        }
                    });


                    btn_CancelarTerminos = (Button) popupView.findViewById(R.id.buttonCancelarTerminos);
                    btn_CancelarTerminos.setOnClickListener(new Button.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            checkTerminosYCondiciones.setChecked(false);
                            popupWindow.dismiss();
                            flagPopUpOpen=false;
                        }
                    });

                    popupWindow.showAsDropDown(paraCompartir, 0, 0);


                }
            }
        });
        imageViewEmail = (ImageView) findViewById(R.id.imageViewInfoEmail);
        assert imageViewEmail != null;
        imageViewEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flagPopUpOpen) {
                    flagPopUpOpen=true;
                    layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    popupView = layoutInflater.inflate(R.layout.pop_up_informacion_email, null);
                    popupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.WRAP_CONTENT,
                            RadioGroup.LayoutParams.WRAP_CONTENT);
                    btn_AceptarEmail = (Button) popupView.findViewById(R.id.buttonAceptarEmail);
                    btn_AceptarEmail.setOnClickListener(new Button.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                            flagPopUpOpen=false;
                        }
                    });
                    popupWindow.showAsDropDown(paraCompartir, 0, 0);
                }
            }
        });


    }

    private void showToastShort(final String text) {
        final Activity activity = mActivity;
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
