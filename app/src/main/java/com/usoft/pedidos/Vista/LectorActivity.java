package com.usoft.pedidos.Vista;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.button.MaterialButton;
import com.usoft.pedidos.BuildConfig;
import com.usoft.pedidos.Dom.Articulo;
import com.usoft.pedidos.Interface.LectorInterface;
import com.usoft.pedidos.Presentador.LectorPresentador;
import com.usoft.pedidos.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.brightec.kbarcode.Barcode;
import uk.co.brightec.kbarcode.BarcodeView;

import static android.Manifest.permission.CAMERA;

public class LectorActivity extends AppCompatActivity implements LifecycleOwner, LectorInterface.Vista {

    private static final int REQUEST_CAMERA = 1;
    @BindView(R.id.view_barcode) BarcodeView barcodeView;
    @BindView(R.id.enable_camera) MaterialButton escaner;
    @BindView(R.id.search)
    ImageButton search;
    @BindView(R.id.limpiar) ImageButton limpiar;
    @BindView(R.id.estadocamara) ImageButton estadoCamara;
    @BindView(R.id.codigo)
    EditText codigoManual;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.androidversion2) TextView androidversion;
    @BindView(R.id.versionname2) TextView versionname;
    private LectorInterface.Presentador presentador;
    ProgressDialog pDialog;
    int [] formatos = {Barcode.FORMAT_EAN_8, Barcode.FORMAT_EAN_13};
    Boolean showcaseLector = false;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lector_activity);
        ButterKnife.bind(this);
        androidversion.setText(android.os.Build.VERSION.RELEASE.substring(0, 1));
        versionname.setText(BuildConfig.VERSION_NAME);
        setSupportActionBar(toolbar);
        presentador = new LectorPresentador(this);
        sharedPref = getSharedPreferences("datosesion", Context.MODE_PRIVATE);
        showcaseLector = sharedPref.getBoolean("showcaselector", false);
        pDialog = new ProgressDialog(this);
        if(!showcaseLector){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("showcaselector", true);
            editor.commit();
        }
        limpiar.setVisibility(View.INVISIBLE);
        limpiar.setEnabled(false);
        limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codigoManual.setText("");
            }
        });
        codigoManual.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length() != 0){
                    limpiar.setEnabled(true);
                    limpiar.setVisibility(View.VISIBLE);
                }else{
                    limpiar.setEnabled(true);
                    limpiar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Oculto teclado cuando selecciona un articulo*/
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                enviarCodigo(codigoManual.getText().toString());
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(LectorActivity.this, LinearLayoutManager.HORIZONTAL, false);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        escaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barcodeView.start();
                DrawableCompat.setTint(estadoCamara.getDrawable(), ContextCompat.getColor(LectorActivity.this, R.color.green_600));
            }
        });
        getLifecycle().addObserver(barcodeView);
        barcodeView.setScaleType(BarcodeView.CENTER_INSIDE);
        barcodeView.setMinBarcodeWidth(300);
        barcodeView.getBarcodes().observe(LectorActivity.this, new Observer<List<Barcode>>() {
            @Override
            public void onChanged(@NonNull List<Barcode> barcodes) {
                for (Barcode barcode : barcodes) {
                    enviarCodigo(barcode.getDisplayValue());
                }
                barcodeView.pause();

                DrawableCompat.setTint(estadoCamara.getDrawable(), ContextCompat.getColor(LectorActivity.this, R.color.red_600));

            }
        });
        int currentApiVersion = Build.VERSION.SDK_INT;
        if(currentApiVersion >=  Build.VERSION_CODES.M)
        {
            if(checkPermission()) {
                barcodeView.pause();
            }
            else   {
                requestPermission();
            }
        }
    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(LectorActivity.this, CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(LectorActivity.this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted){
                        Toast.makeText(LectorActivity.this, "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(LectorActivity.this, "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LectorActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void enviarCodigo(String codigo) {
        pDialog.showProgressDialog("Obteniendo articulo");
        presentador.enviarCodigo(codigo);
    }

    @Override
    public void mostrarArticulo(Articulo articulo, String imagen, String ms) {
        pDialog.finishDialog();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("articulo", articulo);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    public void mostrarError(String error, String codarticulo) {
        pDialog.finishDialog();
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.salirmenu){
            SharedPreferences sharedPref = LectorActivity.this.getSharedPreferences("datosesion",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("sesion",false);
            editor.commit();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        barcodeView.release();
        DrawableCompat.setTint(estadoCamara.getDrawable(), ContextCompat.getColor(LectorActivity.this, R.color.red_600));
    }


}
