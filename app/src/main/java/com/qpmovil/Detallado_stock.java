package com.qpmovil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Detallado_stock extends AppCompatActivity {


    private List<Producto> listaProducto = new ArrayList<Producto>();
    private Producto producto = null;
    private ListView mostrar;
    private ImageButton atras;
    private Fragment_datosConexion verificarconexion = new Fragment_datosConexion();
    alert_animacionespera animacionespera;
    private String codmadre;
    ResultSet resultado= null;
    Configuracion configuracion = new Configuracion();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detallado_stock);

            TextView Cod_madre = findViewById(R.id.det_codmadre);

            StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(politica);

            Bundle bundle = getIntent().getExtras();

            codmadre = bundle.getString("codigoMadre");


            mostrar = findViewById(R.id.det_codigoItemStock);
            Cod_madre.setText(codmadre);

            configuracion.Configuracion(getApplicationContext());

            atras = findViewById(R.id.IB_volver);

            atras.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            AsyncConsulta hilo2 = new AsyncConsulta(this);
            hilo2.execute();


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




        private void MostrarPorPantalla(final Context contexto)
        {
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

                        Log.i("info",resultado.getString("CODITM"));
                        producto.setCodigo(resultado.getString("CODITM"));

                        Log.i("info",resultado.getString("DESCRIPCION"));
                        producto.setDescripcion(resultado.getString("DESCRIPCION"));



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

            }

        }


    }

