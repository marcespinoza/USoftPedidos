package com.usoft.pedidos.Vista;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.usoft.pedidos.R;

public class CustomDialog extends DialogFragment {

    private String mensaje = "";
    private TextView custom_text_dialog;
    private MaterialButton aceptarbtn;
    private MyDialogCloseListener closeListener;

    public interface MyDialogCloseListener {
        void handleDialogClose(DialogInterface dialog);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.custom_dialog, container, false);
        custom_text_dialog = rootView.findViewById(R.id.custom_text_dialog);
        return rootView;
    }

    public void DismissListener(MyDialogCloseListener closeListener) {
        this.closeListener = closeListener;
    }

    public static CustomDialog getInstanceFor(String mensaje){
        CustomDialog fragment=new CustomDialog();
        fragment.mensaje = mensaje;
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        custom_text_dialog.setText(mensaje);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(closeListener != null) {
            closeListener.handleDialogClose(null);
        }

    }

}
