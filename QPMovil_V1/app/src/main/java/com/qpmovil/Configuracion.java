package com.qpmovil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.qpmovil.ConsultaBD;
import com.qpmovil.R;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Configuracion extends Fragment {

    String codigoLocal, codLista=null;
//    ConsultaBD traerDatosBD = new ConsultaBD();
    ResultSet resultado;
    EditText ET_CodLista;
    Button guardaConfig;
    private View menu;

    private SharedPreferences preference;

    public String getCodigoLocal() {
        return codigoLocal;
    }

    public void setCodigoLocal(String codigoLocal) {
        this.codigoLocal = codigoLocal;
    }

    public String getCodLista() {
        return codLista;
    }

    public void setCodLista(String codLista) {
        this.codLista = codLista;
    }



    public void Configuracion(Context contexto) {

        preference = contexto.getSharedPreferences("preferencias", Context.MODE_PRIVATE);

        codLista = preference.getString("preferencias", "");

        ConsultaBD traerDatosBD = new ConsultaBD();

        if (codLista.isEmpty()) {

            try {
                resultado = traerDatosBD.TraerListaPrecio(contexto);
                Log.i("info", "Estoy despues de traerListaPrecios");
                try {
                    if (resultado.next()) {
                        codLista = resultado.getString("STRVALOR");
                        Log.i("info", "estoy despues hacer la consulta y leer el resultSet"+ codLista);
                        SharedPreferences.Editor obj_editor = getPreference().edit();
                        Log.i("info", codLista);
                        obj_editor.putString("preferencias", codLista);
                        obj_editor.commit();

                    } else {
                        Toast.makeText(menu.getContext(), "No se encontraron resultados", Toast.LENGTH_LONG).show();
                    }
                } catch (SQLException e) {
                    Toast.makeText(getContext(), "Error al buscar STRVALOR", Toast.LENGTH_LONG).show();

//                return true;
                    //          MostrarPorPantalla(menu.getContext());
                } catch (Exception e) {
//            Toast.makeText(menu.getContext(), "Error en la conexión, vuelva a intentarlo", Toast.LENGTH_LONG).show();
//                if(resultado!=null)  bandera.valor = 2; // Error en la consulta, el valor ingresado por el usuario es incorrecto.
//                if(!verificarConexion.crearConexion(getContext()))
//                    bandera.valor=3;//Si no se puede generar la conexion nuevamente arrojo el error pertinente
//                return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        menu = inflater.inflate(R.layout.activity_configuracion, container, false);

        ET_CodLista = menu.findViewById(R.id.et_CodLista);
        preference = menu.getContext().getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        ET_CodLista.setText(preference.getString("preferencias",""));
        guardaConfig = menu.findViewById(R.id.GuardarConfiguracion);
        codLista = ET_CodLista.getText().toString().trim();
        Log.i("info", codLista);
        super.onCreate(savedInstanceState);

        guardaConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Guardar();
            }

        });

        return menu;
    }

    public void Guardar() {

        SharedPreferences.Editor obj_editor = getPreference().edit();
        codLista = ET_CodLista.getText().toString().trim();
        Log.i("info", codLista);
        obj_editor.putString("preferencias", codLista);
        obj_editor.commit();
        Toast.makeText(menu.getContext(),"Se guardo correctamente la preferencia ", Toast.LENGTH_SHORT).show();

    }

    public SharedPreferences getPreference() {
        return preference;
    }

    public void setPreference(SharedPreferences preference) {
        this.preference = preference;
    }
}
