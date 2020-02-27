package com.usoft.pedidos.Modelo;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import com.usoft.pedidos.Dom.Articulo;
import com.usoft.pedidos.Dom.Cliente;
import com.usoft.pedidos.Dom.Pedido;
import com.usoft.pedidos.Interface.PedidoInterface;
import com.usoft.pedidos.Presentador.PedidoPresentador;
import com.usoft.pedidos.Vista.GlobalApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PedidoModelo implements PedidoInterface.Modelo {

    PedidoInterface.Presentador presentador;
    Context context;
    SharedPreferences sharedPref, sharedPrefConexion;
    String urlservidor = "";

    public PedidoModelo(PedidoPresentador presentador) {
        this.presentador=presentador;
        context = GlobalApplication.getContext();
        sharedPref = context.getSharedPreferences("datosesion", Context.MODE_PRIVATE);
        sharedPrefConexion = context.getSharedPreferences("datosconexion", Context.MODE_PRIVATE);
        urlservidor = sharedPrefConexion.getString("servidor","");
    }

    public void getArticulos(String nombreArticulo, String codigoCliente){
        String empresa = sharedPrefConexion.getString("empresa","");
        OkHttpClient client = new OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS).build();

        RequestBody formBody;
        formBody = new FormBody.Builder()
                /*Encabezado*/
                .add("nombrearticulo", nombreArticulo)
                .add("codigocliente", codigoCliente)
                .add("empresa", empresa)
                .build();

        Request request = new Request.Builder()
                .url(urlservidor+"/getarticulos")
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                presentador.mostrarError("Error, no se pudo conectar al servidor");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    presentador.mostrarError("Error al obtener articulos");
                } else {
                    String mMessage = response.body().string();
                    JSONObject object;
                    ArrayList<Articulo> articulosList = new ArrayList<>();
                    JSONObject objeto = null;
                    try {
                        objeto = new JSONObject(mMessage);
                        String mensaje = objeto.getString("mensaje");
                        if(mensaje.equals("true")){
                            JSONArray json = (JSONArray) objeto.get("0");
                            for(int i=0; i < json.length(); i++){
                                object = json.getJSONObject(i);
                                Articulo articulo = new Articulo();
                                String desart = object.getString("descripcion");
                                String codart = object.getString("codigoarticulo");
                                String precio = object.getString("p_pesos");
                                articulo.setDesart(desart);
                                articulo.setCodart(codart);
                                articulo.setPrecio(precio);
                                articulo.setPrecio_original(precio);
                                Log.i("PRECIOO",""+precio);
                                if(precio.equals("null")){
                                  articulo.setPrecio("0");
                                }else{
                                  articulo.setPrecio(precio);
                                }
                                articulosList.add(articulo);
                            }
                            Collections.sort(articulosList, new Comparator<Articulo>() {
                                @Override
                                public int compare(Articulo a1, Articulo a2) {
                                    return a1.getDesart().compareToIgnoreCase(a2.getDesart());
                                }
                            });
                            presentador.devolverArticulos(articulosList);
                        }else{
                            presentador.mostrarError("No hay articulos con esa descripción.");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
    });
}

    public void getClientes(String cuenta){
        String empresa = sharedPrefConexion.getString("empresa","");
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS).build();
        RequestBody formBody;
        formBody = new FormBody.Builder()
                /*Encabezado*/
                .add("nrocuenta", cuenta)
                .add("empresa",empresa)
                .build();

        Request request = new Request.Builder()
                .url(urlservidor+"/getclientes")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                presentador.mostrarError("Error. Time out..");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    presentador.mostrarError("Error, no se pudo conectar al servidor");
                } else {
                    String mMessage = response.body().string();
                    JSONObject object;
                    ArrayList<Cliente> clientesList = new ArrayList<>();
                    JSONObject objeto = null;
                    try {
                        objeto = new JSONObject(mMessage);
                        String mensaje = objeto.getString("mensaje");
                        if(mensaje.equals("true")){
                            JSONArray json = (JSONArray) objeto.get("0");
                            for(int i=0; i < json.length(); i++){
                                object = json.getJSONObject(i);
                                Cliente cliente= new Cliente();
                                String nombre = object.getString("nombre");
                                String nro_cuenta = object.getString("nro_cuenta");
                                String persona = object.getString("codigopersona");
                                cliente.setNomcli(nombre);
                                cliente.setNrocuenta(nro_cuenta);
                                cliente.setPersona(persona);
                                clientesList.add(cliente);
                            }
                            Collections.sort(clientesList, new Comparator<Cliente>() {
                                @Override
                                public int compare(Cliente s1, Cliente s2) {
                                    return s1.getNomcli().compareToIgnoreCase(s2.getNomcli());
                                }
                            });
                            presentador.devolverClientes(clientesList);
                        }else{
                            presentador.mostrarError("Error, intente nuevamente");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    public void getNroPedido(){
        String empresa = sharedPrefConexion.getString("empresa","");
        String url = urlservidor+"/proximopedido";
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS).build();

        RequestBody formBody;
        formBody = new FormBody.Builder()
                /*Encabezado*/
                .add("empresa",empresa)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                presentador.mostrarError("Error, no se pudo conectar al servidor");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                   presentador.mostrarError("Error al obtener nro. de pedido. Intente de nuevo");
                } else {
                    String mMessage = response.body().string();
                    try {
                        JSONObject object = new JSONObject(mMessage);
                        String nropedido = object.getString("nropedido");
                        presentador.nroPedido(nropedido);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    public void enviarPedidos(ArrayList<Pedido> lPedidos, String np, String cc){
        String empresa = sharedPrefConexion.getString("empresa","");
        String usuario = sharedPref.getString("usuario","");
        String url = urlservidor+"/pedido";
        RequestBody formBody;
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String date2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS).build();

        for(int i = 0; i < lPedidos.size(); i++){
            formBody = new FormBody.Builder()
                    /*Encabezado*/
                    .add("nropedido", np)
                    .add("vendedor", cc)
                    .add("codigoarticulo", lPedidos.get(i).getCodigoarticulo())
                    .add("cantidad", lPedidos.get(i).getCantidad())
                    .add("precio", lPedidos.get(i).getPrecio())
                    .add("preciosugerido", lPedidos.get(i).getPreciosugerido())
                    .add("observacion", lPedidos.get(i).getObservacion())
                    .add("fecha", date)
                    .add("codcli", lPedidos.get(i).getNro_cuenta())
                    .add("fechaalta", date2)
                    .add("usuario", usuario)
                    .add("empresa",empresa)
                    .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

                 client.newCall(request).enqueue(new Callback() {
                     @Override
                     public void onFailure(Call call, IOException e) {
                         presentador.mostrarError("Error, no se pudo conectar al servidor");
                     }

                     @Override
                     public void onResponse(Call call, final Response response) throws IOException {
                         if (!response.isSuccessful()) {
                           Log.i("respuesta","respuesta"+response.toString());
                         }
                     }
                 });


        }
        int nropedido = Integer.parseInt(np);
        nropedido++;
        String npedido = String.valueOf(nropedido);
        updateNroPedido(npedido);
        presentador.confirmarPedido("Se ha enviado el pedido.");
    }

    public void updateNroPedido(String nropedido){
        String empresa = sharedPrefConexion.getString("empresa","");
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS).build();
        RequestBody formBody;
        formBody = new FormBody.Builder()
                /*Encabezado*/
                .add("proximo", nropedido)
                .add("empresa",empresa)
                .build();

        Request request = new Request.Builder()
                .url(urlservidor+"/updatenropedido")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                presentador.mostrarError("Error. Time out..");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {


                }
            }
        });
    }


    @Override
    public void enviarListaPedidos(ArrayList<Pedido> list, String np, String cc) {
        enviarPedidos(list, np, cc);
    }

    @Override
    public void obtenerNroPedido() {
        getNroPedido();
    }

    @Override
    public void obtenerClientes(String cuenta) {
        getClientes(cuenta);
    }


}
