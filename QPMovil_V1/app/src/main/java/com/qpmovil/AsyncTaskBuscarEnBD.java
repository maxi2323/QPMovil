package com.qpmovil;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


public class AsyncTaskBuscarEnBD extends AsyncTask<Boolean,Integer,Boolean> {
    boolean condicion;
    Context contextoPriv;
    alert_animacionespera animacionespera;
    bandera bandera = new bandera(0) ;
    Fragment_Busqueda stock = new Fragment_Busqueda();
    Fragment_ConsultaItem barra = new Fragment_ConsultaItem();
    String consul;


    public AsyncTaskBuscarEnBD(Context contexto, String consulta)
    {
        contextoPriv = contexto;
        consul=consulta;
    }

    @Override
    protected void onPreExecute() {
        animacionespera = new alert_animacionespera(contextoPriv);
        animacionespera.Mostrar_Alert();
        bandera.valor=0;// reinicio el tipo de error

    }

    @Override
    protected Boolean doInBackground(Boolean... booleans) {

        // StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(politica);
        condicion = stock.BuscaPorCodigoODescripcion(consul);
        return condicion;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
//            super.onPostExecute(aBoolean);
        if(bandera.getValor()==1){
            Toast.makeText(contextoPriv, "Error de conexion, intente nuevamente", Toast.LENGTH_LONG).show();
        }
        if(bandera.getValor()==2){
            Toast.makeText(contextoPriv, "No se encontraron resultados", Toast.LENGTH_LONG).show();
        }
        if(bandera.getValor()==3){
            Toast.makeText(contextoPriv, "No se pudo generar la conexion nuevamente, verifique la red y los datos de conexion", Toast.LENGTH_LONG).show();
        }
        Log.i("info", String.valueOf(bandera.getValor()));
        //if(!condicion) Toast.makeText(menu.getContext(), "", Toast.LENGTH_LONG).show();
        animacionespera.Cerrar_Alert();
        stock.MostrarPorPantalla(contextoPriv);

    }

}
