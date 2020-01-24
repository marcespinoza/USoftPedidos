package com.usoft.pedidos.Modelo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.usoft.pedidos.Dom.Articulo;
import com.usoft.pedidos.Interface.LectorInterface;
import com.usoft.pedidos.Presentador.LectorPresentador;
import com.usoft.pedidos.Vista.GlobalApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LectorModelo implements LectorInterface.Modelo {

    private LectorInterface.Presentador presentador;
    SharedPreferences sharedPref;
    SharedPreferences sharedPrefConexion;
    Context context;
    Articulo articulo;
    String urlServidor = "";

    public LectorModelo(LectorPresentador presentador) {
        this.presentador=presentador;
        context = GlobalApplication.getContext();
        sharedPref = context.getSharedPreferences("datosesion", Context.MODE_PRIVATE);
        sharedPrefConexion = context.getSharedPreferences("datosconexion", Context.MODE_PRIVATE);
    }


    @Override
    public void obtenerArticulo(String codart) {
        getArticulo(codart);
    }

    public void getArticulo(String codart) {
        urlServidor = sharedPrefConexion.getString("servidor","");
        String empresa = sharedPrefConexion.getString("empresa","");
        String url = urlServidor+"/getarticulo";
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS).build();
        RequestBody formBody = new FormBody.Builder()
                .add("codigo", codart)
                .add("empresa",empresa)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                presentador.mostrarError(e.getMessage(),"");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String mMessage = response.body().string();
                try {
                    JSONObject json = new JSONObject(mMessage);
                    String mensaje = json.getString("mensaje");
                    if (mensaje.equals("false")) {
                        presentador.mostrarError("No se encontró articulo","");
                    } else {
                        articulo = new Articulo();
                        articulo.setCodart(json.getString("codigoarticulo"));
                        articulo.setDescripcion(json.getString("descripcion"));
                        articulo.setDescripcioncorta(json.getString("descripcioncorta"));
                        articulo.setMarca(json.getString("marca"));
                        articulo.setDescripcionMaestro(json.getString("descripcionmaestro"));
                        articulo.setPrecio(json.getString("precio"));
                        String path = json.getString("ruta")+"/"+json.getString("imagen");
                        getImagenArticulo(path);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getImagenArticulo(String path){
        String milisegundos = sharedPrefConexion.getString("milisegundos","3000");
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS).build();

        RequestBody formBody;
        formBody = new FormBody.Builder()
                /*Encabezado*/
                .add("imagen", path)
                .build();

        Request request = new Request.Builder()
                .url("http://usoft.selfip.info:10701/api/index.php/api/articuloimagen")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("Errorimagen","error");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.i("Errorimagen","error");
                } else {
                    String imageString = response.body().string();
                    String img = null;
                    try {
                        JSONObject objeto = new JSONObject(imageString);
                        String mensaje = objeto.getString("1");
                        JSONObject objeto2 = new JSONObject(mensaje);
                        img = objeto2.getString("img");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    presentador.devolverArticulo(articulo, img, milisegundos);

                }
            }
        });
    }
}
