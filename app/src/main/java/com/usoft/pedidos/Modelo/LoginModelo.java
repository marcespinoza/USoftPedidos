package com.usoft.pedidos.Modelo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.TextView;


import com.usoft.pedidos.Interface.LoginInterface;
import com.usoft.pedidos.Presentador.LoginPresentador;
import com.usoft.pedidos.Vista.GlobalApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginModelo implements LoginInterface.Modelo {

    LoginInterface.Presentador presentador;
    Context context;
    SharedPreferences sharedPref;
    String urlServidor;
    private static final String url = "jdbc:mysql://192.168.0.192:3306/myDB";
    private static final String user = "hitesh";
    private static final String pass = "1234";

    public LoginModelo(LoginPresentador presentador){
        this.presentador=presentador;
        context = GlobalApplication.getContext();
        sharedPref = context.getSharedPreferences("datosesion", Context.MODE_PRIVATE);
    }

    @Override
    public void enviarUsuario(String usuario, String clave) {
        loginRequest(usuario, clave);
    }

    @Override
    public void verificarEmpresa(String empresa) {
        new ConsultarEmpresa().execute(empresa);
    }

    public void enviarEmpresa(String emp){
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        }catch(Exception e){
            System.err.println("Cannot create connection");
        }
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://usoft.com.uy/usoft201_mobile?useUnicode=true", "usoft201_mobile", "orHfV3Cib5YoJZmEq4");
            String query = "SELECT * FROM empresas WHERE empresa = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, emp);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                String nombre = rs.getString(1);
            }
        }catch(Exception e){
            System.err.println("ErrorDB"+e.getMessage());
        }
    }

    class ConsultarEmpresa extends AsyncTask<String, Void, Boolean> {

        String empresa = "";

        @Override
        protected Boolean doInBackground(String... params) {
            String empresa = "";
            try{
                Class.forName("com.mysql.jdbc.Driver").newInstance();
            }catch(Exception e){
                System.err.println("Cannot create connection");
            }
            try{
                Connection connection = DriverManager.getConnection("jdbc:mysql://usoft.com.uy/usoft201_mobile?useUnicode=true", "usoft201_mobile", "orHfV3Cib5YoJZmEq4");
                String query = "SELECT * FROM empresas WHERE empresa like ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, empresa);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()){
                    empresa = rs.getString(2);
                    return true;
                }else{
                    empresa = "vacio";
                    return false;
                }
            }catch(Exception e){
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
           if(empresa.equals("vacio") && !result) {
               presentador.resultadoConexion(result, "No se encontró empresa.");
           }else if(!result){
               presentador.resultadoConexion(result, "Error de conexión.");
           }else{
               presentador.resultadoConexion(result, "");
           }
        }

    }

    private void loginRequest(String usuario, String contraseña) {
        urlServidor = sharedPref.getString("urlservidor","");
            String url = "http://aromacos.selfip.info:10701/api/index.php/api/getlogin";
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS).build();
            RequestBody formBody = new FormBody.Builder()
                    .add("usuario", usuario)
                    .add("clave", contraseña)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();


            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    presentador.mostrarError("Error, no se pudo conectar con el servidor.");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String mMessage = response.body().string();

                    try {
                        JSONObject json = new JSONObject(mMessage);
                        String mensaje = json.getString("mensaje");
                        if (mensaje.equals("false")) {
                            presentador.usuarioIncorrecto();
                        } else {
                            String usuario = json.getString("nombre");
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("usuario", usuario);
                            editor.commit();
                            presentador.devolverUsuario(usuario);
                        }
                    } catch (JSONException e) {
                        presentador.mostrarError("Error, intente nuevamente.");
                        e.printStackTrace();
                    }
                }
            });
      }
}
