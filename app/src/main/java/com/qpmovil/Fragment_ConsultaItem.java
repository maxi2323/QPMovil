package com.qpmovil;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.sql.ResultSet;
import java.sql.SQLException;


public class Fragment_ConsultaItem extends Fragment {


    private String consulta, codmadre = null;
    private TextView ET_descripcion, ET_precio, ET_color, ET_stock, ET_talle, ET_codigo;
    private View menu;
    private ResultSet resultado = null;
    private Button buscarxbarras, buscarxitem, Stockdetallado, agregarAlCarrito;
    private EditText et_busca;
    Fragment_datosConexion verificarConexion = new Fragment_datosConexion();
    alert_animacionespera animacionespera;
    bandera banderaflag = new bandera(0) ;
    Configuracion configuracion = new Configuracion();
    private Producto producto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        menu = inflater.inflate(R.layout.fragment_consultaitem, container, false);
        final Fragment FragAPasar = this;
        boolean estado = true;
        asociarVariables(menu);
//        consulta = String.valueOf(et_busca.getText());
        AsyncCrearConexion hilo2 = new AsyncCrearConexion(menu.getContext());
//        final Producto producto = new Producto();

        configuracion.Configuracion(getContext());

        try {
           if (Fragment_datosConexion.conexionSesion == null) {

               hilo2.execute();

           }

               if (AsyncCrearConexion.estado) {

                   buscarxitem.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {

                           AsyncConsulta Hilo = new AsyncConsulta(menu.getContext());
                           Hilo.execute();
                           consulta = String.valueOf(et_busca.getText());
                       //    BuscaPorCodigo(consulta);
                       }
                   });

                   buscarxbarras.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {

                          new lec_barras(getActivity(), FragAPasar);


                       }

                   });

                   Stockdetallado.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {

                           try{
                               if (producto.getCodigo() != null) {
                                   //   new Stock_detallado(menu.getContext(), codmadre);
                                   Intent intent = new Intent(menu.getContext(), Detallado_stock.class);
                                   intent.putExtra("codigoMadre", producto.getCodigo() );

                                   startActivity(intent);

                               } else {
                                   Toast.makeText(menu.getContext(), "Debe cargar un código de ítem primero", Toast.LENGTH_LONG).show();
                               }
                           }catch(Exception e) {

                                }

                           }

                   });

                   agregarAlCarrito.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {

                           try{
                               alert_agregar_carrito alerta = new alert_agregar_carrito(view.getContext(), producto);
                               alerta.Mostrar_alertCarrito();
                           }catch (Exception e){

                           }



                       }
                   });


               } //else
                 //  Toast.makeText(getContext(), "Error de conexión.\nDebe cargar los datos de conexión correctamente.", Toast.LENGTH_LONG).show();

             //else { verificarConexion.crearConexion(menu.getContext());}

        } catch (Exception e) {

            Toast.makeText(menu.getContext(), "Error", Toast.LENGTH_LONG).show();

        }

        return menu;
    }


    private void asociarVariables(View menu) {
        ET_descripcion = menu.findViewById(R.id.et_desc);
        ET_color = menu.findViewById(R.id.et_Color);
        ET_talle = menu.findViewById(R.id.et_Talle);
        ET_stock = menu.findViewById(R.id.et_stock);
        ET_precio = menu.findViewById(R.id.et_precio);
        ET_codigo = menu.findViewById(R.id.et_codigo);
        buscarxbarras = menu.findViewById(R.id.buscaxbarras);
        buscarxitem = menu.findViewById(R.id.buscaxitem);
        et_busca = menu.findViewById(R.id.busca);
        Stockdetallado = menu.findViewById(R.id.stockdetallado);
        agregarAlCarrito = menu.findViewById(R.id.btt_agregar_carrito);

    }

    /*private boolean cargarProducto(ResultSet resultado) {

        try {
            if (resultado.next()) {
                do {
                    producto = new Producto();
                    producto.setCodItm(resultado.getString(("CODITM")));
                    producto.setCodigo(resultado.getString("CODMADRE"));
                    producto.setDescripcion(resultado.getString("DESCRIPCION"));
                    producto.setStock(resultado.getString("STKACTUAL"));
                    producto.setPrecio(resultado.getString("PRECIO"));
                    if (resultado.getString("NOMBRECOL") != null)
                        producto.setColor(resultado.getString("NOMBRECOL"));
                    else producto.setColor(resultado.getString("CODCOL"));
                    producto.setTalle(resultado.getString("CODTAL"));


                    Log.i("info", "estoy cargado la lista de productos y lo que estoy cargando es:"+ producto);

                } while (resultado.next());
            } else {
                banderaflag.valor = 2;
                return false;
//                Toast.makeText(menu.getContext(), "No se encontraron resultados", Toast.LENGTH_LONG).show();
            }
        } catch (SQLException e) {
//            Toast.makeText(getContext(), "Error en cargar lista", Toast.LENGTH_LONG).show();
        }
            return true;
    }*/

    private void mostrarPantalla(Producto producto) {
        try {
            Log.i("info","Empieza el mostrar por pantalla");

            if (producto!=null) {
                Log.i("info","despues del next de mostrar pantalla");

                Log.i("info","Entro a mostrar por pantalla");


              //  codmadre = producto.getCodigo();// string para guardar el codigo madre
                ET_descripcion.setText(producto.getDescripcion());
                ET_codigo.setText(producto.getCodigo());
                et_busca.setText(producto.getCodItm());
                ET_color.setText(producto.getColor());
                ET_talle.setText(producto.getTalle());
                ET_stock.setText(producto.getStock());
                ET_precio.setText(producto.getPrecio());

            } else {
                Toast.makeText(menu.getContext(), "No se encontraron resultados " + consulta, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error en cargar lista", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult resultadoCam = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (resultadoCam != null) {
            if (resultadoCam.getContents() == null) {
//                Toast.makeText(menu.getContext(), "Se ha cancelado el escaneo", Toast.LENGTH_LONG).show();

            } else {
                Log.i("info",resultadoCam.getContents());
                consulta = String.valueOf(resultadoCam.getContents());

            //    consulta = consulta.replace(.""%","); para tratar de cambiarlo en el metodo de busqueda

                AsyncConsulta Hilo = new AsyncConsulta(menu.getContext());
                //     OcultarTeclado(menu, getActivity());
                Hilo.execute();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }


    }

    public void onResume() {
        Log.e("DEBUG", "OnPause of loginFragment");
        //      Toast.makeText(getContext(), "Resume", Toast.LENGTH_LONG).show();
        super.onResume();
    }

    public void onPause() {
        Log.e("DEBUG", "OnPause of loginFragment");
        //      Toast.makeText(getContext(), "Pausa", Toast.LENGTH_LONG).show();

        super.onPause();
    }


    public Boolean BuscaPorCodigo(String consulta) { //metodo para buscar por codigo de item
        StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(politica);
        ConsultaBD datoBD = new ConsultaBD();


            try {
                resultado = datoBD.ObtenerValorCodigo(menu.getContext(), consulta, configuracion.codLista,banderaflag);

                producto = new Producto();
                AsociarProducto(resultado,producto);

                resultado = null;

                resultado = datoBD.ConsultarStock(menu.getContext(),producto.getCodItm());

                AsociarStock(resultado,producto);

                resultado = null;

                resultado = datoBD.ConsultarPrecioItem(menu.getContext(),producto.getCodItm());
                AsociarPrecio(resultado,producto);

                return true;
            } catch (Exception e) {
//            Toast.makeText(menu.getContext(), "Error en la conexión, vuelva a intentarlo", Toast.LENGTH_LONG).show();
                if(resultado!=null)  banderaflag.valor = 2; // Error en la consulta, el valor ingresado por el usuario es incorrecto.
                if(!verificarConexion.crearConexion(getContext()))
                    banderaflag.valor=3;//Si no se puede generar la conexion nuevamente arrojo el error pertinente
                return false;
            }

    }
    public void AsociarPrecio(ResultSet resultado, Producto producto )
    {

        try {

            if (resultado.next())
            {
                do {
                    producto.setPrecio(resultado.getString("PRECIO"));

                } while (resultado.next());


            }else {
                banderaflag.valor = 2;

            }
        }catch (SQLException e)
        {

        }


    }

    public void AsociarStock(ResultSet resultado, Producto producto)
    {
        try {

            if (resultado.next())
            {
                do {
                    producto.setStock(resultado.getString("STKACTUAL"));

                } while (resultado.next());


            }else {
                banderaflag.valor = 2;

            }
        }catch (SQLException e)
        {

        }

    }

    public void AsociarProducto(ResultSet resultado,Producto producto)
    {

        try {
            // falta parsear del codigo para hacer las dos consultas del precio y del stock
            if (resultado.next())
            {
                do {
                    producto.setCodItm(resultado.getString(("CODITM")));
                    producto.setCodigo(resultado.getString("CODMADRE"));
                    producto.setDescripcion(resultado.getString("DESCRIPCION"));
                    if (resultado.getString("NOMBRECOL") != null)
                        producto.setColor(resultado.getString("NOMBRECOL"));
                    else producto.setColor(resultado.getString("CODCOL"));
                    producto.setTalle(resultado.getString("CODTAL"));

                    Log.i("info", "estoy cargado la lista de productos y lo que estoy cargando es:"+ producto);

                } while (resultado.next());


            }else {
                banderaflag.valor = 2;

            }
        }catch (SQLException e)
        {

        }



    }




    /*==================================================================================*/

    private class AsyncConsulta extends AsyncTask<Boolean,Integer,Boolean> {

        Context contextoPriv;
        boolean condicion;


        private AsyncConsulta(Context contexto)
        {
            contextoPriv = contexto;

            boolean condicion;

        }

        @Override
        protected void onPreExecute() {
            animacionespera = new alert_animacionespera(contextoPriv);
            animacionespera.Mostrar_Alert();
            banderaflag.valor=0;// reinicio el tipo de error

        }

        @Override
        protected Boolean doInBackground(Boolean... booleans) {

            StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(politica);
            condicion = BuscaPorCodigo(consulta);
           // cargarProducto(resultado);
            return condicion;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
//            super.onPostExecute(aBoolean);
            animacionespera.Cerrar_Alert();

            mostrarPantalla(producto);
            if(banderaflag.getValor()==1){
                Toast.makeText(menu.getContext(), "Error 1: "+ "\n" +"Error de conexion, intente nuevamente", Toast.LENGTH_LONG).show();
            }
            if(banderaflag.getValor()==2){
                Toast.makeText(menu.getContext(), "Error 2: "+ "\n" +"No se encontraron resultados", Toast.LENGTH_LONG).show();
            }
            if(banderaflag.getValor()==3){
                Toast.makeText(menu.getContext(), "Error 3: "+ "\n" +" No se pudo generar la conexión nuevamente, verifique la red y los datos de conexión", Toast.LENGTH_LONG).show();
            }
            Log.i("info", String.valueOf(banderaflag.getValor()));
            //if(!condicion) Toast.makeText(menu.getContext(), "", Toast.LENGTH_LONG).show();


        }

    }

}


