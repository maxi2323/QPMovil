package com.qpmovil;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Stock_detallado {

    private List<Producto> listaProducto = new ArrayList<Producto>();
    private Producto producto = null;
    private ListView mostrar;
    ResultSet resultado= null;

    private Fragment_datosConexion verificarconexion = new Fragment_datosConexion();
    alert_animacionespera animacionespera;
    private String codmadre;
    Dialog dialogo;
    Configuracion configuracion = new Configuracion();


    public Stock_detallado(final Context contexto, String CodigoMadre){//Constructor


        dialogo = new Dialog(contexto);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setCancelable(true);
        dialogo.setContentView(R.layout.stock_detallado);
        TextView Cod_madre = dialogo.findViewById(R.id.det_codmadre);


        StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(politica);

        mostrar = dialogo.findViewById(R.id.det_codigoItemStock);
        Cod_madre.setText(CodigoMadre);
        codmadre = CodigoMadre;
        configuracion.Configuracion(contexto);

        AsyncConsulta hilo2 = new AsyncConsulta(contexto);
        hilo2.execute();
        //stockbuscarBD(contexto, CodigoMadre);

        dialogo.show();

    }

    private void Mostrar_alertStock()
    {
        dialogo.show();

    }


    private boolean stockbuscarBD(Context context, String codmadre){
        ConsultaBD datos = new ConsultaBD();
        try{

            resultado = datos.TraerStockDetallado(context, codmadre, configuracion.codLista);

            cargarLista(resultado);
       //     MostrarPorPantalla(context); pruebo sacandolo de aca para que el background no toque la parte grafica

        } catch(Exception e) {
//            Toast.makeText(context, "Error de conexión, inténtelo nuevamente", Toast.LENGTH_LONG).show();
       //     verificarconexion.crearConexion(contextoglobal);
        }
        return true;
    }




    private void MostrarPorPantalla(final Context contexto) {
        /*En este metodo se desplega la lista del producto en base a su codigo madre y el stock detallado
         utilizando la clase formato_dialog_stcok y su respectivo XML*/

        formato_dialog_stock adaptador = new formato_dialog_stock(contexto, listaProducto);
        mostrar.setAdapter(adaptador);

    }


    private List<Producto> cargarLista(ResultSet resultado) {
        listaProducto.clear();
        Log.i("info","cargando lista");
        try {
            if (resultado.next()) {
                do {
                        producto = new Producto();
                        Log.i("info",resultado.getString("PRECIO"));
                        producto.setPrecio(resultado.getString("PRECIO"));

                        Log.i("info",resultado.getString("STKACTUAL"));
                        producto.setStock(resultado.getString("STKACTUAL"));

                        Log.i("info",resultado.getString("CODTAL"));
                        producto.setTalle(resultado.getString("CODTAL"));


                        //si nombreCOL tiene info lo cargo en color, sino cargo el código de color
                        if (resultado.getString("NOMBRECOL") != null)
                        {
                            Log.i("info",resultado.getString("NOMBRECOL"));
                            producto.setColor(resultado.getString("NOMBRECOL"));

                        }
                        else{ Log.i("info",resultado.getString("CODCOL"));
                            producto.setColor(resultado.getString("CODCOL"));}

                        listaProducto.add(producto);

                } while (resultado.next());


            } else {
 //               Toast.makeText(menu.getContext(), "No se encontraron resultados", Toast.LENGTH_LONG).show();
            }
        } catch (SQLException e) {
   //         Toast.makeText(getContext(), "Error en cargar lista", Toast.LENGTH_LONG).show();
   //         verificarconexion.crearConexion(contexto2);
        }
        return listaProducto;
    }


    /*==================================================================================*/

    private class AsyncConsulta extends AsyncTask<Boolean,Integer,Boolean> {
        Boolean condicion = false;
        Context contextoPriv;



        private AsyncConsulta(Context contexto)
        {
            contextoPriv = contexto;

        }



        @Override
        protected void onPreExecute() {
            animacionespera = new alert_animacionespera(contextoPriv);
            animacionespera.Mostrar_Alert();

        }

        @Override
        protected Boolean doInBackground(Boolean... booleans) {

           // StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            //StrictMode.setThreadPolicy(politica);
            condicion = stockbuscarBD(contextoPriv,codmadre);

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
            MostrarPorPantalla(contextoPriv);
            Mostrar_alertStock();

        }

    }




}
