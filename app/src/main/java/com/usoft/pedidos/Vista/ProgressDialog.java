package com.usoft.pedidos.Vista;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.usoft.pedidos.R;


public class ProgressDialog {

    AlertDialog dialog;
    TextView texto;

    public ProgressDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true); // if you want user to wait for some process to finish,
        View inflate = LayoutInflater.from(context).inflate(R.layout.animator_dialog, null);
        texto = inflate.findViewById(R.id.texto_progress_dialog);
        builder.setView(inflate);
        dialog = builder.create();
    }

    public  void showProgressDialog(String mensaje) {
        texto.setText(mensaje);
        dialog.show();
    }

    public  void finishDialog(){
        dialog.dismiss();
    }

    protected boolean showing(){
        return dialog.isShowing();
    }

}
