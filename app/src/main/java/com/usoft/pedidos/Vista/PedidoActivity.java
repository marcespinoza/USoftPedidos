package com.usoft.pedidos.Vista;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.usoft.pedidos.Adapter.AutoCompleteArticuloAdapter;
import com.usoft.pedidos.Adapter.AutoCompleteClienteAdapter;
import com.usoft.pedidos.Adapter.RecyclerPedido;
import com.usoft.pedidos.BuildConfig;
import com.usoft.pedidos.Dom.Articulo;
import com.usoft.pedidos.Dom.Cliente;
import com.usoft.pedidos.Dom.Pedido;
import com.usoft.pedidos.Interface.PedidoInterface;
import com.usoft.pedidos.Presentador.PedidoPresentador;
import com.usoft.pedidos.R;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;

public class PedidoActivity extends AppCompatActivity implements PedidoInterface.Vista, RecyclerPedido.TotalInterface, CustomDialog.MyDialogCloseListener {

    private PedidoInterface.Presentador presentador;
    @BindView(R.id.desart) AutoCompleteTextView desart;
    @BindView(R.id.cliente) AutoCompleteTextView cliente;
    @BindView(R.id.codart) EditText codart;
    @BindView(R.id.codcli) TextView codcli;
    @BindView(R.id.observacion) EditText observacion;
    @BindView(R.id.agregar) ImageButton agregar;
    @BindView(R.id.clean) ImageButton limpiar;
    @BindView(R.id.searchcliente) ImageButton buscarcliente;
    @BindView(R.id.cleancliente) ImageButton limpiarCliente;
    @BindView(R.id.searcharticulo) ImageButton buscararticulo;
    @BindView(R.id.recyclerpedidos) RecyclerView recyclerPedido;
    @BindView(R.id.precio) EditText precio;
    @BindView(R.id.cantped) EditText cantPed;
    @BindView(R.id.listapedido) TextView tituloLista;
    @BindView(R.id.totallinea) TextView totallinea;
    @BindView(R.id.enviar)  MaterialButton enviarBoton;
    @BindView(R.id.coordinator)  CoordinatorLayout coordinator;
    @BindView(R.id.toolbar)  Toolbar toolbar;
    @BindView(R.id.androidversion2) TextView androidversion;
    @BindView(R.id.versionname2) TextView versionname;
    @BindView(R.id.scan) ImageButton scan;
    AlertDialog alertDialogArticulos;
    AlertDialog alertDialogClientes;
    AlertDialog alertDialogNroPedido;
    AlertDialog enviarPedido;
    RecyclerPedido adapter;
    private ArrayList<Pedido> lPedidos = new ArrayList<>();
    private BigDecimal precioArt;
    boolean flagClientes = true;
    SharedPreferences sharedPref;
    boolean showcase;
    ProgressDialog pdialog;
    String  precioUnitario, codigoPersona;
    BigDecimal totalParcial;
    static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.pedido_activity, null);
        setContentView(view);
        ButterKnife.bind(this);
        androidversion.setText(android.os.Build.VERSION.RELEASE.substring(0, 1));
        versionname.setText(BuildConfig.VERSION_NAME);
        setSupportActionBar(toolbar);
        totallinea.setText("0");
        recyclerPedido.setLayoutManager(new LinearLayoutManager(PedidoActivity.this));
        adapter = new RecyclerPedido(getApplicationContext(), lPedidos, this);
        recyclerPedido.setAdapter(adapter);
        pdialog = new ProgressDialog(this);
        sharedPref = getSharedPreferences("datosesion", Context.MODE_PRIVATE);
        showcase = sharedPref.getBoolean("showcase", false);
        alertDialogClientes = new  SpotsDialog.Builder().setContext(PedidoActivity.this).build();
        alertDialogArticulos = new  SpotsDialog.Builder().setContext(PedidoActivity.this).build();
        alertDialogNroPedido = new  SpotsDialog.Builder().setContext(PedidoActivity.this).build();
        enviarPedido = new SpotsDialog.Builder().setContext(getApplicationContext()).build();
        cantPed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {     }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length() != 0){
                    String cantPedido = charSequence.toString();
                    BigDecimal precioUnitario = new BigDecimal(precio.getText().toString());
                    BigDecimal cant = new BigDecimal(cantPedido);
                    BigDecimal subtotal = precioUnitario.multiply(cant);
                    totalParcial = subtotal;
                }else{
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {      }
        });
        precio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String texto = editable.toString();
                if(texto.length() != 0 && cantPed.getText().length()!=0 && !texto.startsWith(".")){
                    String cantPedido = cantPed.getText().toString();
                    BigDecimal precioUnitario = new BigDecimal(texto);
                    BigDecimal cant = new BigDecimal(cantPedido);
                    BigDecimal subtotal = precioUnitario.multiply(cant);
                    totalParcial = subtotal;
                }else{
                }
            }
        });
        buscarcliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                obtenerClientes();
            }
        });
        buscararticulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Oculto teclado cuando selecciona un articulo*/
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                obtenerArticulos();
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LectorActivity.class);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
        desart.setEnabled(false);
        limpiar.setEnabled(false);
        limpiarCliente.setEnabled(false);
        limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpiarDetalle();
            }
        });
        limpiarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Articulo> art = new ArrayList<>();
                desart.setAdapter(new AutoCompleteArticuloAdapter(getApplicationContext(), R.layout.autocompletearticulo_row, art));
                limpiarDetalle();
                cliente.setText("");
                codcli.setText("");
            }
        });
        desart.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length() != 0){
                    limpiar.setEnabled(true);
                }else{
                    limpiar.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        enviarBoton.setEnabled(false);
        enviarBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!lPedidos.isEmpty()) {
                    obtenernPedido();
                }
            }
        });
        cliente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!cliente.getText().toString().equals("")){
                    desart.setEnabled(true);
                    limpiarCliente.setEnabled(true);
                }else{
                    desart.setEnabled(false);
                    limpiarCliente.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cantPed.getText().toString().isEmpty()){
                    mostrarSnack("Ingrese cantidad", R.color.red_400);
                }else if(precio.getText().toString().isEmpty()){
                    mostrarSnack("Ingrese precio", R.color.red_400);
                }else if(!desart.getText().toString().equals("")){
                    Pedido pedido = new Pedido();
                    pedido.setNro_cuenta(codcli.getText().toString());
                    pedido.setCodigoarticulo(codart.getText().toString());
                    pedido.setPreciosugerido(precioUnitario);
                    pedido.setPrecio(precio.getText().toString());
                    pedido.setCantidad(cantPed.getText().toString());
                    pedido.setDescripcion(desart.getText().toString());
                    pedido.setObservacion(observacion.getText().toString());
                    pedido.setTotal(String.valueOf(totalParcial));
                    lPedidos.add(pedido);
                    BigDecimal total = new BigDecimal(totallinea.getText().toString());
                    BigDecimal totalfinal = totalParcial.add(total);
                    totallinea.setText(String.valueOf(totalfinal));
                    adapter.notifyDataSetChanged();
                    limpiarDetalle();
                    tituloLista.setText("Lista de pedidos");
                    enviarBoton.setEnabled(true);
                    desart.requestFocus();
                }
            }
        });
        cliente.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(cliente, InputMethodManager.SHOW_IMPLICIT);
        presentador = new PedidoPresentador(this);

    }


    private void enviarPedido(String nropedido){
        presentador.enviarLista(lPedidos, nropedido, codigoPersona);
        limpiarEncabezado();
        lPedidos.clear();
        enviarBoton.setEnabled(false);
        adapter.notifyDataSetChanged();
    }

    private void obtenerClientes(){
        ArrayList<Articulo> art = new ArrayList<>();
        desart.setAdapter(new AutoCompleteArticuloAdapter(getApplicationContext(), R.layout.autocompletearticulo_row, art));
        runOnUiThread(new Runnable() {
            public void run() {
                pdialog.showProgressDialog("Obteniendo clientes..");
            }
        });
        presentador.obtenerClientes(cliente.getText().toString());
    }

    private void obtenerArticulos(){
        alertDialogArticulos.setMessage("Obteniendo articulos..");
        alertDialogArticulos.show();
        presentador.articulos(desart.getText().toString(), codcli.getText().toString());
    }

    private void obtenernPedido(){
        pdialog.showProgressDialog("Enviando pedido...");
        presentador.getNroPedido();
    }

    @Override
    public void articulosList(ArrayList articulos) {
        alertDialogArticulos.dismiss();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                desart.setAdapter(new AutoCompleteArticuloAdapter(getApplicationContext(), R.layout.autocompletearticulo_row, articulos));
                desart.showDropDown();
                desart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Articulo articulo = (Articulo) adapterView.getItemAtPosition(i);
                        setArticulo(articulo);
                    }
                });
            }
        });
    }

    private void limpiarEncabezado(){
        observacion.setText("");
        totallinea.setText("0");
    }

    private void limpiarDetalle(){
        desart.setText("");
        codart.setText("");
        precio.setText("");
        cantPed.setText("");
    }

    public void setArticulo(Articulo articulo){
        codart.setText(articulo.getCodart());
        precioArt = new BigDecimal(articulo.getPrecio());
        precioUnitario = String.valueOf(precioArt);
        precio.setText(String.valueOf(precioArt));
        cantPed.setText("1");
        cantPed.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(cantPed, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void clientesList(ArrayList clientes) {
        pdialog.finishDialog();
        flagClientes = false;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!showcase){
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("showcase", true);
                    showcase = true;
                    editor.commit();
                new GuideView.Builder(getApplicationContext())
                        .setTitle("Importante")
                        .setContentText("Luego de enviar un pedido, deslice hacia abajo en cualquier lugar de la pantalla para actualizar el número de pedido o lista de clientes.")
                        .setDismissType(DismissType.anywhere) //optional - default dismissible by TargetView
                        .build()
                        .show();
                }
                cliente.setThreshold(1);
                cliente.setAdapter(new AutoCompleteClienteAdapter(getApplicationContext(), R.layout.autocompletecliente_row, clientes));
                cliente.showDropDown();
                cliente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Cliente clientes = (Cliente) adapterView.getItemAtPosition(i);
                        cliente.setText(clientes.getNomcli());
                        codcli.setText(clientes.getNrocuenta());
                        codigoPersona = clientes.getPersona();
                        desart.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(desart, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });
    }

    @Override
    public void mostraError(String error) {
        if (alertDialogClientes.isShowing()){alertDialogClientes.dismiss();}
        if (alertDialogArticulos.isShowing()){alertDialogArticulos.dismiss();}
        if (pdialog.showing()){pdialog.finishDialog();}
        mostrarSnack(error, R.color.orange_400);
    }

    @Override
    public void confirmaPedido(String contador) {
        pdialog.finishDialog();
        mostrarSnack("Pedido enviado correctamente.", R.color.green_400);
        cliente.requestFocus();
    }

    @Override
    public void mostrarNroPedido(String nropedido) {
        runOnUiThread( new Runnable() {
            @Override
            public void run() {
                enviarPedido(nropedido);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void descontarTotal(String total) {
        BigDecimal total_ = new BigDecimal(total);
        BigDecimal totalActual = new BigDecimal(totallinea.getText().toString());
        BigDecimal totalfinal = totalActual.subtract(total_);
        totallinea.setText(String.valueOf(totalfinal));
    }


    @Override
    public void handleDialogClose(DialogInterface dialog) {
        obtenernPedido();
    }

    public void mostrarSnack(String mensaje, int color){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar snackbar = Snackbar.make(coordinator, mensaje, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(getResources().getColor(R.color.white));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(getResources().getColor(color));
                snackbar.show();
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
            SharedPreferences sharedPref = PedidoActivity.this.getSharedPreferences("datosesion",Context.MODE_PRIVATE);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Articulo articulo = (Articulo) data.getSerializableExtra("articulo");
            desart.setText(articulo.getDescripcion());
            setArticulo(articulo);
        }
    }
}
