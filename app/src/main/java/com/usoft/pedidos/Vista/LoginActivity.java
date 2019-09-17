package com.usoft.pedidos.Vista;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.jeevandeshmukh.glidetoastlib.GlideToast;
import com.usoft.pedidos.Interface.LoginInterface;
import com.usoft.pedidos.Presentador.LoginPresentador;
import com.usoft.pedidos.R;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity implements LoginInterface.Vista {

    LoginInterface.Presentador presentador;

    @BindView(R.id.login_button)  MaterialButton login;
    @BindView(R.id.usuario) EditText usuario;
    @BindView(R.id.clave) EditText clave;
    @BindView(R.id.logologin) ImageView logoLogin;
    ProgressBar progressBar;
    AlertDialog alertDialog;
    SharedPreferences sharedPref;
    MaterialButton enviarConexion;
    TextView errorEmpresa;
    EditText empresaText;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custompopup);
        progressBar = dialog.findViewById(R.id.progressBar);
        enviarConexion = dialog.findViewById(R.id.enviarempresa);
        empresaText = dialog.findViewById(R.id.empresa);
        errorEmpresa = dialog.findViewById(R.id.textoError);
        errorEmpresa.setVisibility(View.INVISIBLE);
        ButterKnife.bind(this);
        alertDialog = new  SpotsDialog.Builder().setContext(this).build();
        presentador = new LoginPresentador(this);
        sharedPref = this.getSharedPreferences("datosesion",Context.MODE_PRIVATE);
        boolean sesion = sharedPref.getBoolean("sesion",false);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUsuario(usuario.getText().toString(), clave.getText().toString());
            }
        });
        /*if(sesion){
            login();
        }*/
        mostrarDialogoConexion();
    }

    public void mostrarDialogoConexion(){

        enviarConexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorEmpresa.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                enviarConexion.setEnabled(false);
                empresaText.setEnabled(false);
                presentador.verificarEmpresa(empresaText.getText().toString());
            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if(i == KeyEvent.KEYCODE_BACK){
                    finish();
                    return true;
                }
                return false;
            }
        });
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public void loginUsuario(String usuario, String clave) {
        alertDialog.setMessage("Espere por favor...");
        alertDialog.show();
        presentador.verificarUsuario(usuario, clave);
    }


    @Override
    public void mostrarExito(String usuari) {
        String usu = usuario.getText().toString();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("sesion", true);
        editor.putString("usuario",usu);
        editor.commit();
        alertDialog.dismiss();
        login();
    }

    @Override
    public void resultadoConexion(boolean b, String mensaje) {
        progressBar.setVisibility(View.INVISIBLE);
        if(b){
           dialog.dismiss();
        }else{
            empresaText.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            enviarConexion.setEnabled(true);
            empresaText.setEnabled(true);
           errorEmpresa.setVisibility(View.VISIBLE);
           errorEmpresa.setText(mensaje);
        }
    }


    private void login(){
        String usu = sharedPref.getString("usuario", "");
        Intent intent = new Intent(this, PedidoActivity.class);
        if(usuario.getText().toString().equalsIgnoreCase("admin") || usu.equalsIgnoreCase("admin")){
            intent.putExtra("admin",true);
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void mostrarError(String error) {
        alertDialog.dismiss();
        runOnUiThread(new Runnable() {
            public void run() {
                new GlideToast.makeToast(LoginActivity.this,error, GlideToast.LENGTHTOOLONG, GlideToast.FAILTOAST, GlideToast.TOP).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
