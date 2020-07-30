package com.qpmovil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Fragment_Busqueda extends Fragment {

    private EditText ET_BUSCA;
    private String consulta;
    private Button buscaXcodigo, buscaXdescripcion;
    private ListView mostrar;
    private List<Producto> listaProducto = new ArrayList<Producto>();
    private Producto producto = null;
    private View menu;
    private Fragment_datosConexion verificarConexion = new Fragment_datosConexion();
    alert_animacionespera animacionespera;
    bandera bandera = new bandera(0) ;

    Configuracion configuracion = new Configuracion();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        menu = inflater.inflate(R.layout.fragment_busqueda, container, false);
        asociarVariables(menu);
        final Fragment FragAPasar = this; //Con esto funciona el codigo de barras para el OnResultActivity
        boolean estado = true;
        AsyncCrearConexion hilo2 = new AsyncCrearConexion(menu.getContext());//analizar el impacto de colocarlo aca o en el siguiente IF

        //voy a probar poner la configuracion del codigo de lista en esta parte

        configuracion.Configuracion(getContext());

        try {
            if(Fragment_datosConexion.conexionSesion ==null)
            {

                hilo2.execute();
                Log.i("info", String.valueOf(AsyncCrearConexion.estado));
            }


            if (AsyncCrearConexion.estado)
            {
                buscaXdescripcion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(ValidarCampoVacio(ET_BUSCA))
                        {
                            consulta = "descripcion";
//                            AsyncTaskBuscarEnBD Hilo = new AsyncTaskBuscarEnBD(menu.getContext(),consulta);
// Como hago para poder llamar a un metodo de otra clase con su misma instancia, si no da error
                            AsyncConsulta Hilo = new AsyncConsulta(menu.getContext(),consulta);
                            OcultarTeclado(menu, getActivity());
                            Hilo.execute();
                        }

                    }
                }); // Pongo a la escucha el boton buscar por descripcion
                //-------------------------------------------------------------------
                // Pongo a la escucha el boton buscar por codigo
                buscaXcodigo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(ValidarCampoVacio(ET_BUSCA))
                        {
                            consulta = "codigo";
//                            AsyncTaskBuscarEnBD Hilo = new AsyncTaskBuscarEnBD(menu.getContext(),consulta);
// Como hago para poder llamar a un metodo de otra clase con su misma instancia, si no da error
                            AsyncConsulta Hilo = new AsyncConsulta(menu.getContext(),consulta);
                            OcultarTeclado(menu, getActivity());
                            Hilo.execute();

                        }
                    }
                });



                return menu;
            }
            else{
                 // Toast.makeText(getContext(), "Error en los datos de conexion.\nDebe cargar los datos de conexion correctamente", Toast.LENGTH_LONG).show();
            }

            
        } catch (Exception e) {

        }

        return menu;
    }


    public boolean BuscaPorCodigoODescripcion(String cod) { //metodo para buscar por codigo o descripcion


        ResultSet resultado = null;
        ConsultaBD datoBD = new ConsultaBD();
        consulta = String.valueOf(ET_BUSCA.getText());

        try{
            if(cod=="codigo")
                resultado = datoBD.TraerDatosCodigo(menu.getContext(), consulta, bandera, configuracion.codLista);
            else
                resultado = datoBD.TraerDatosDescricion(menu.getContext(), consulta, bandera,  configuracion.codLista);
   //         if(resultado!=null)  bandera.valor = 2;
            listaProducto = cargarLista(resultado);
            asociarVariables(menu);
            Log.i("info", "volvi de hacer la consulta y estoy dentro de BuscarPorCodigoODescripcion");
            return true;
            //          MostrarPorPantalla(menu.getContext());
        }catch (Exception e) {
//            Toast.makeText(menu.getContext(), "Error en la conexión, vuelva a intentarlo", Toast.LENGTH_LONG).show();
            if(resultado!=null)  bandera.valor = 2; // Error en la consulta, el valor ingresado por el usuario es incorrecto.
            if(!verificarConexion.crearConexion(getContext()))
                bandera.valor=3;//Si no se puede generar la conexion nuevamente arrojo el error pertinente
            return false;
        }

    }


    private List<Producto> cargarLista(ResultSet resultado) {
        listaProducto.clear();

        try {
            if (resultado.next()) {
                do {
                        producto = new Producto();
                        producto.setCodigo(resultado.getString("CODMADRE"));
                        producto.setDescripcion(resultado.getString("DESCRIPCION"));
                        producto.setStock(resultado.getString("STKACTUAL"));

                        listaProducto.add(producto);
                        Log.i("info", "estoy cargado la lista de productos y lo que estoy cargando es:"+ producto);

                } while (resultado.next());
            } else {
                bandera.valor = 2;
//                Toast.makeText(menu.getContext(), "No se encontraron resultados", Toast.LENGTH_LONG).show();
            }
        } catch (SQLException e) {
//            Toast.makeText(getContext(), "Error en cargar lista", Toast.LENGTH_LONG).show();
        }
        return listaProducto;
    }

    /*Asocia las variables*/
    private void asociarVariables(View menu) {
        ET_BUSCA = menu.findViewById(R.id.busca);
        mostrar = menu.findViewById(R.id.Lista);
        buscaXcodigo = menu.findViewById(R.id.buscaxbarras);//comento para el git
        buscaXdescripcion = menu.findViewById(R.id.buscaxitem);
    }

    public void MostrarPorPantalla(final Context contexto) {
        formato_ListView_Stock adaptador = new formato_ListView_Stock(getActivity(), listaProducto);
        mostrar.setAdapter(adaptador);

        mostrar.setOnItemClickListener(new AdapterView.OnItemClickListener() // Pongo en escucha la seleccion
        {                                                                   // de items en el listview
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(menu.getContext(), Detallado_stock.class);
                intent.putExtra("codigoMadre",listaProducto.get(i).getCodigo());

                startActivity(intent);
            }
        });
    }


    private boolean ValidarCampoVacio(TextView campoaEvaluar)
    {
        if(campoaEvaluar.getText().length() >= 3 )
        {
            return true;

        }else
        {
            Toast.makeText(menu.getContext() ,"El campo  debe tener al menos 3 caracteres ",Toast.LENGTH_LONG).show();
            return false;
        }
    }


    private void OcultarTeclado(View view, Activity actividad) {
        InputMethodManager imm = (InputMethodManager) actividad.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /*==================================================================================*/

    private class AsyncConsulta extends AsyncTask<Boolean,Integer,Boolean> {
        boolean condicion;
        Context contextoPriv;
//aaaaaaaa

        private AsyncConsulta(Context contexto,String consulta)
        {
            contextoPriv = contexto;
        }

        @Override
        protected void onPreExecute() {
            animacionespera = new alert_animacionespera(contextoPriv);
            animacionespera.Mostrar_Alert();
            bandera.valor=0;// reinicio el tipo de error
        }

        @Override
        protected Boolean doInBackground(Boolean... booleans) {
            StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(politica);
            condicion = BuscaPorCodigoODescripcion(consulta);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
//            super.onPostExecute(aBoolean);
            if(bandera.getValor()==1){
                Toast.makeText(menu.getContext(), "Error 1: "+ "\n" +"Error de conexion, intente nuevamente", Toast.LENGTH_LONG).show();
            }
            if(bandera.getValor()==2){
                Toast.makeText(menu.getContext(), "Error 2: "+ "\n" +"No se encontraron resultados", Toast.LENGTH_LONG).show();
            }
            if(bandera.getValor()==3){
                Toast.makeText(menu.getContext(), "Error 3: "+ "\n" +" No se pudo generar la conexión nuevamente, verifique la red y los datos de conexión", Toast.LENGTH_LONG).show();
            }
            Log.i("info", String.valueOf(bandera.getValor()));
            //if(!condicion) Toast.makeText(menu.getContext(), "", Toast.LENGTH_LONG).show();
            animacionespera.Cerrar_Alert();
            MostrarPorPantalla(contextoPriv);

        }

    }

}
