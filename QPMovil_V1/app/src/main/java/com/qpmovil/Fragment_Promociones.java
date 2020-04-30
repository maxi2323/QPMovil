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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Fragment_Promociones extends Fragment {

    private String parametro;
    private EditText ET_parametro;
    private Spinner promo;
    private ListView mostrar;
    private List<Promocion> listaPromocion = new ArrayList<Promocion>();
    private Promocion promocion = null;
    private View menu;
    private Fragment_datosConexion verificarConexion = new Fragment_datosConexion();
    alert_animacionespera animacionespera;
    bandera bandera = new bandera(0) ;
    private Button btt_buscar;

    Configuracion configuracion = new Configuracion();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        menu = inflater.inflate(R.layout.fragment_promociones, container, false);
        asociarVariables(menu);
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
                parametro=promo.getItemAtPosition(promo.getSelectedItemPosition()).toString();
                btt_buscar.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        if(ValidarOpcion(parametro))
                        {
                            AsyncConsulta Hilo = new AsyncConsulta(menu.getContext(),parametro);
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
        //parametro = String.valueOf(ET_parametro.getText());
        //parametro = String.valueOf(cod.charAt(1));

        try{
            resultado = datoBD.EjecutarSP(menu.getContext(), cod, bandera);

            //         if(resultado!=null)  bandera.valor = 2;
            listaPromocion = cargarLista(resultado);
            //asociarVariables(menu);
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


    private List<Promocion> cargarLista(ResultSet resultado) {
        listaPromocion.clear();

        try {
            if (resultado.next()) {
                do {
                    promocion = new Promocion();
                    promocion.setCodigo(resultado.getString("codigo"));
                    promocion.setDescripcion(resultado.getString("descri"));
                    promocion.setTipo(resultado.getString("tipo"));
                    promocion.setVigencia(resultado.getString("Vigencia"));
                    promocion.setHora(resultado.getString("horarios"));
                    promocion.setDias(resultado.getString("dias"));
                    promocion.setExtras(resultado.getString("extras"));

                    listaPromocion.add(promocion);
                    Log.i("info", "estoy cargado la lista de productos y lo que estoy cargando es:"+ promocion);

                } while (resultado.next());
            } else {
                bandera.valor = 2;
//                Toast.makeText(menu.getContext(), "No se encontraron resultados", Toast.LENGTH_LONG).show();
            }
        } catch (SQLException e) {
//            Toast.makeText(getContext(), "Error en cargar lista", Toast.LENGTH_LONG).show();
        }
        return listaPromocion;
    }

    /*Asocia las variables*/
    private void asociarVariables(View menu) {
        promo = menu.findViewById(R.id.spinnerPromo);
        mostrar = menu.findViewById(R.id.lst_mostrar_promo);
        btt_buscar = menu.findViewById(R.id.btt_buscar_promo);
        parametro=promo.getItemAtPosition(promo.getSelectedItemPosition()).toString();
    }

    public void MostrarPorPantalla(final Context contexto) {
        formato_listView_promociones adaptador = new formato_listView_promociones(getActivity(), listaPromocion);
        mostrar.setAdapter(adaptador);

    }


    private boolean ValidarOpcion(String valor)
    {

        if(valor.charAt(0) == 'S' || valor.charAt(0) == 'D' || valor.charAt(0) == 'H' )
        {
            return true;

        }else
        {
            Toast.makeText(menu.getContext() ,"Debe ingresa S, D o H ",Toast.LENGTH_LONG).show();
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


        private AsyncConsulta(Context contexto,String parametro)
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
            condicion = BuscaPorCodigoODescripcion(parametro);
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
