package com.qpmovil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import java.sql.Connection;


public class Fragment_datosConexion extends Fragment {


    private EditText Edit_Servidor;
    private EditText Edit_Instancia;
    private EditText Edit_Usuario;
    private EditText Edit_password;
    private EditText Edit_Base;
    private View menu;
    private Button ingresar, testConexion,IngresarQR, salir;
    public static Connection conexionSesion;
    public static String codigoSucurdal;
    private String datos;//para borde_carrito_unidades QR
    alert_animacionespera animacionespera; // para la animacion de espera


    public SharedPreferences preference;

    public Fragment_datosConexion() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        menu = inflater.inflate(R.layout.fragment_datos_conexion, container, false);
        Edit_Servidor = menu.findViewById(R.id.Edit_Servidor);
        Edit_Instancia = menu.findViewById(R.id.Edit_instancia);    //SE DECLARA LA VINCULACION CON LA PARTE GRAFICA
        Edit_Base = menu.findViewById(R.id.Edit_base);
        Edit_Usuario = menu.findViewById(R.id.Edit_usuario);
        Edit_password = menu.findViewById(R.id.Edit_password);
        ingresar = menu.findViewById(R.id.btn_guardar);
        testConexion = menu.findViewById(R.id.btn_test);
        IngresarQR = menu.findViewById(R.id.bt_IngresarQR);

        Preferencias ManejarPreferencias = new Preferencias(menu.getContext());


        Edit_Servidor.setText(ManejarPreferencias.getSrv());
        Edit_Instancia.setText(ManejarPreferencias.getInst());
        Edit_Base.setText(ManejarPreferencias.getBase());
        Edit_Usuario.setText(ManejarPreferencias.getUsu());
        Edit_password.setText(ManejarPreferencias.getPass());

        StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(politica);

        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AsyncGuardar hilo2 = new AsyncGuardar(menu.getContext());
                hilo2.execute();

            }
        }); // Pongo en escucha el boton Ingresar

        testConexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              PruebaAsync Hilo = new PruebaAsync(menu.getContext());
              Hilo.execute();

            }
        }); // Pongo en escucha el boton TestConexion

        IngresarQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingresarConQR();
            }
        }); // Pongo en escucha el boton TestConexion


        return menu;


    }

    public Boolean Guardar(Context contexto) {

        Preferencias ManejarPreferencias = new Preferencias(menu.getContext());

        ManejarPreferencias.setSrv(Edit_Servidor.getText().toString().trim());
        ManejarPreferencias.setBase(Edit_Base.getText().toString().trim());
        ManejarPreferencias.setInst(Edit_Instancia.getText().toString().trim());
        ManejarPreferencias.setUsu(Edit_Usuario.getText().toString().trim());
        ManejarPreferencias.setPass(Edit_password.getText().toString().trim());

        return crearConexion(contexto);

    }


    private class PruebaAsync extends AsyncTask<Boolean,Integer,Boolean> {
        Boolean condicion = false;
        Context contextoGlobal;
        private  PruebaAsync(Context contexto)
        {
            contextoGlobal = contexto;

        }


        @Override
        protected void onPreExecute() {
            animacionespera = new alert_animacionespera(menu.getContext());
            animacionespera.Mostrar_Alert();

        }

        @Override
        protected Boolean doInBackground(Boolean... booleans) {

            condicion = Test(menu,contextoGlobal);
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
            if (condicion) {

                Toast.makeText(menu.getContext(), "El test se realizó correctamente", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(menu.getContext(), "El test falló, vuelva a intentarlo", Toast.LENGTH_LONG).show();
            }

        }

    }

    private class AsyncGuardar extends AsyncTask<Boolean,Integer,Boolean> {
        Boolean condicion = false;
        Context contexto;


        private AsyncGuardar(Context context)
        {
            contexto = context;

        }


        @Override
        protected void onPreExecute() {
            animacionespera = new alert_animacionespera(menu.getContext());
            animacionespera.Mostrar_Alert();
        }

        @Override
        protected Boolean doInBackground(Boolean... booleans) {
           condicion = Guardar(contexto);
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
        if(condicion)
        {
            Toast.makeText(menu.getContext(),"Conexión guardada correctamente",Toast.LENGTH_LONG).show();

        }else {

            Toast.makeText(menu.getContext(),"Error de conexión",Toast.LENGTH_LONG).show();
            }

        }

    }



    public boolean Test(View vista,Context contexto)  // Cambie de VOID a  BOOLEAN
    {

        Boolean condicion;  // AGREGUE NUEVA
        condicion = Boolean.FALSE;
        try {

            ConexionBD testConexion = new ConexionBD();
            // String ruta =  Edit_Servidor.getText().toString() + Edit_Instancia.getText().toString() +  Edit_Base.getText().toString() + Edit_Usuario.getText().toString() + Edit_password.getText().toString();

            condicion = testConexion.TestConexion((contexto),
                    Edit_Servidor.getText().toString().trim(),
                    Edit_Instancia.getText().toString().trim(),
                    Edit_Base.getText().toString().trim(),
                    Edit_Usuario.getText().toString().trim(),
                    Edit_password.getText().toString().trim());
//            //probar con this
//            if (condicion == Boolean.TRUE)//
//            {
//                Toast.makeText(getActivity().getApplicationContext(), "Test de conexion correctamente", Toast.LENGTH_LONG).show();
//
//            } else {
//                Toast.makeText(getActivity().getApplicationContext(), "Error por favor verifique los datos", Toast.LENGTH_LONG).show();
//            }
        } catch (Exception e) {
            //  Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();

        }
        return condicion;

    }


    public boolean crearConexion(Context contexto) {

        ConexionBD conexione = new ConexionBD();
        Preferencias ManejarPreferencias = new Preferencias(contexto);

    try {
           conexionSesion = conexione.conectar(contexto,
            ManejarPreferencias.getSrv(),
            ManejarPreferencias.getInst(),
            ManejarPreferencias.getBase(),
            ManejarPreferencias.getUsu(),
            ManejarPreferencias.getPass());

    if (conexionSesion != null)
    {
             return true;
    } else {

        return false;
        }
    }catch (Exception e)
        {  e.printStackTrace();
        return  false;}


    }

    @Override
    public void onResume() {
        Log.e("DEBUG", "OnPause of loginFragment");
        Preferencias ManejarPreferencias = new Preferencias(menu.getContext());

        Edit_Servidor.setText(ManejarPreferencias.getSrv());
        Edit_Instancia.setText(ManejarPreferencias.getInst());
        Edit_Base.setText(ManejarPreferencias.getBase());
        Edit_Usuario.setText(ManejarPreferencias.getUsu());
        Edit_password.setText(ManejarPreferencias.getPass());
        super.onResume();

    }

    public void onPause() {
        Log.e("DEBUG", "OnPause of loginFragment");
        super.onPause();
    }



    public void ingresarConQR() {
        final Fragment FragAPasar = this;//estoy probando ingresar con QR
        new lec_barras(getActivity(), FragAPasar);

    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult resultadoCam = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        Preferencias ManejarPreferencias = new Preferencias(menu.getContext());

        if (resultadoCam != null) {
            if (resultadoCam.getContents() == null) {
                Toast.makeText(menu.getContext(), "Se ha cancelado el escaneo", Toast.LENGTH_LONG).show();
            } else {
                datos = String.valueOf(resultadoCam.getContents());
                boolean condicion = cargarDatosConexionQR(datos);

                if (condicion) {

                    Guardar(menu.getContext());
//                    AsyncGuardar hilo2 = new AsyncGuardar(menu.getContext());
//                    hilo2.execute();   agregar hilos pincha y lee de a uno el QR!!

                } else {
                    Toast.makeText(menu.getContext(), "Error en los datos del QR generado.", Toast.LENGTH_LONG).show();
                }

            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



    public  boolean cargarDatosConexionQR(String datosQR) {

        String[] cadena = datosQR.split("&");
        if (cadena.length == 5) {
            if(cadena[0]!=null) Edit_Servidor.setText(cadena[0]); else return false;
            /*las dos siguientes lineas son por si la instancia viene vacia*/
            if(cadena[1]!=null) Edit_Instancia.setText(cadena[1]); else Edit_Instancia.setText(null);
            if(cadena[2]!=null) Edit_Base.setText(cadena[2]); else return false;
            if(cadena[3]!=null) Edit_Usuario.setText(cadena[3]); else return false;
            if(cadena[4]!=null) Edit_password.setText(cadena[4]); else return false;

            return true;
        }
        else return false;
    }


}


