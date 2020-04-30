package com.qpmovil;

import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.widget.Toast;



/* A partir de aca se crea el hilo para crear la conexion*/
public class AsyncCrearConexion extends AsyncTask<Boolean,Integer,Boolean> {

    public Context contexto;
    public alert_animacionespera animacionespera;
    public static boolean estado = true;
    public Fragment_datosConexion verificarConexion = new Fragment_datosConexion();

    public AsyncCrearConexion(Context context)
    {
        contexto = context;
    }



    @Override
    protected void onPreExecute() {
        animacionespera = new alert_animacionespera(contexto);
        animacionespera.Mostrar_Alert();
    }

    @Override
    protected Boolean doInBackground(Boolean... booleans) {
        StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(politica);
        estado = verificarConexion.crearConexion(contexto);
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
//            super.onPostExecute(aBoolean);

        animacionespera.Cerrar_Alert();
        if(estado)
        {
            Toast.makeText(contexto,"Conexión creada correctamente",Toast.LENGTH_LONG).show();
            estado = true;
        }else {
            Toast.makeText(contexto,"Error de conexión",Toast.LENGTH_LONG).show();
            estado = false;
        }

    }

}


// Finaliza la seccion del hilo
