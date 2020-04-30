package com.qpmovil;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class ConsultaBD {
    Statement stmt = null;


    private ResultSet resultado;
    private String original, reemplazado;

    private Statement ObtenerStatement(Context contexto)   // Metodo privado para que de la conexion
    {

        try{
            Log.i("info","antes de crear el stament");
            stmt = Fragment_datosConexion.conexionSesion.createStatement();
            Log.i("info","despues de crear el stament");

        }catch (Exception e){

            Toast.makeText(contexto,e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        return stmt;
    }

    public ConsultaBD() {
        super();
    }


    public ResultSet EjecutarSP(Context contexto, String fecha, bandera bandera)
    {

        String SP = "Exec SP_TraerPromocionesVigentes" + "'" + fecha + "'";

        resultado=null;
        try{
            PreparedStatement prepStat = Fragment_datosConexion.conexionSesion.prepareStatement(SP);
           resultado = prepStat.executeQuery();

        }catch (SQLException e)
            {

            }

        return resultado;

    }


    public ResultSet TraerDatosCodigo(Context contexto, String buscar, bandera bandera, String codlista)

    {
        resultado=null;
        try {
            resultado = ObtenerStatement(contexto).executeQuery(
                    "\n" +
                            "select sum(stkactual) STKACTUAL, I.CODMADRE, DESCRIPCION\n" +
                            "from PVItemsAcum as A join PVItems as I on I.CODITM=A.CODITM join PVListasPrecios as P on p.CODITM=I.CODITM\n" +
                            "where i.CODMADRE like '%"+ buscar +"%' and a.STKACTUAL>0 AND i.itemprefi='b' and P.expiracion is null and p.CODLIS in (\n" +
                            "            select CODLIS\n" +
                            "\t\t\tfrom PVListasPrecios\n" +
                            "\t\t\twhere codlis like '"+codlista+"' \n" +
                            "\t\t\tgroup by codlis )\n" +
                            "\t\t\tand codSuc in ( select INTVALOR\n" +
                            "                            from pvconfig\n" +
                            "                            where PROPIEDAD = 'CodSuc')\n" +
                            "                            group by i.CODMADRE,DESCRIPCION\n" +
                            "                            order by STKACTUAL DESC");

            return resultado;

        } catch (SQLException e) {
            if(stmt!=null) {
                bandera.valor=1;
                bandera.setValor(1);// 1 significa que no hay conexion
            }

            Toast.makeText(contexto.getApplicationContext(), "Error al ejecutar la consulta", Toast.LENGTH_LONG).show();
            return null;
        }

    }

    public ResultSet TraerStockDetallado(Context contexto,String buscar,String codlista)

    {
        try {
//            Configuracion configuracion=new Configuracion();
//
//            configuracion.Configuracion(contexto);
            resultado=null;

            resultado = ObtenerStatement(contexto).executeQuery(
                    "select Precio, I.codcol,i.descripcion, i.coditm, codtal, A.STKACTUAL, i.NOMBRECOL\n" +
                            "from PVItems i join PVItemsAcum a on I.CODITM = A.CODITM join PVListasPrecios P on p.CODITM=I.CODITM\n" +
                            "where  i.CODMADRE = '"+buscar+"' and a.STKACTUAL>0 and i.itemprefi='b' and p.EXPIRACION is null and p.CODLIS = (\n" +
                            "            select CODLIS\n" +
                            "\t\t\tfrom PVListasPrecios\n" +
                            "\t\t\twhere codlis like '"+codlista+"' \n" +
                            "\t\t\tgroup by codlis )\n" +
                            "\t AND CODSUC = ( select INTVALOR\n" +
                            "                            from pvconfig\n" +
                            "                            where PROPIEDAD = 'CodSuc')\n" +
                            "order by I.CODCOL,I.CODTAL desc");

            Log.i("info","casi vuelve de traer stock");
            return resultado;
        } catch (SQLException e) {
            Toast.makeText(contexto.getApplicationContext(), "Error al ejecutar la consulta", Toast.LENGTH_LONG).show();
            return null;
        }

    }

    public ResultSet TraerDatosDescricion(Context contexto, String buscar, bandera bandera,String codlista)

    {
        try {
            resultado=null;
            resultado = ObtenerStatement(contexto).executeQuery("select sum(stkactual) STKACTUAL, I.CODMADRE, DESCRIPCION\n" +
                    "from PVItemsAcum as A join PVItems as I on I.CODITM=A.CODITM join PVListasPrecios as P on p.CODITM=I.CODITM\n" +
                    "where i.DESCRIPCION like '%"+ buscar+"%' and a.STKACTUAL>0 AND i.itemprefi='b' and P.expiracion is null and p.CODLIS in (\n" +
                    "            select CODLIS\n" +
                    "\t\t\tfrom PVListasPrecios\n" +
                    "\t\t\twhere codlis like '"+codlista+"' \n" +
                    "\t\t\tgroup by codlis )\n" +
                    "and codSuc in ( select INTVALOR\n" +
                    "\t\t\t\tfrom pvconfig\n" +
                    "\t\t\t\twhere PROPIEDAD = 'CodSuc')\n" +
                    "group by i.CODMADRE,DESCRIPCION\n" +
                    "order by STKACTUAL DESC");

            Log.i("info", "termine de hacer la consulta y el resultado es:"+resultado);
            return resultado;
        } catch (SQLException e) {
            if(stmt!=null) {
                bandera.valor=1;
                bandera.setValor(1);// 1 significa que no hay conexion
            }

            Toast.makeText(contexto.getApplicationContext(), "Error al ejecutar la consulta", Toast.LENGTH_LONG).show();
            return null;
        }

    }


    public ResultSet ConsultarPrecioSegunTabla(Context contexto, String valorAbuscar,String codlista, String columna){

        try{
            resultado = ObtenerStatement(contexto).executeQuery( "select I.CODCOL, I.CODTAL, I.DESCRIPCION, A.STKACTUAL, P.PRECIO, I.CODITM, I.CODMADRE, i.NOMBRECOL\n" +
                    "from PVItems i join PVItemsAcum a on I.CODITM = A.CODITM join PVListasPrecios P on p.CODITM=I.CODITM\n" +
                    "where  i." + columna + " = '" + valorAbuscar + "' and i.itemprefi='b' and a.CODSUC in (\n" +
                    "\t\t\tselect INTVALOR \n" +
                    "\t\t\tfrom pvconfig\n" +
                    "\t\t\twhere PROPIEDAD = 'CodSuc')\n" +
                    "and p.EXPIRACION is null and p.CODLIS = (\n" +
                    "            select CODLIS\n" +
                    "\t\t\tfrom PVListasPrecios\n" +
                    "\t\t\twhere codlis like '"+codlista+"' \n" +
                    "\t\t\tgroup by codlis )");



        } catch (SQLException e){
            Log.e("info", "estoy en el catch de ConsultarPrecioSegunTabla");

            //   Toast.makeText(contexto.getApplicationContext(), "Error al ejecutar la consulta", Toast.LENGTH_LONG).show();
            return null;

        }

        return resultado;
    }



    public void armarCodigoDeReemplazo(Context contexto, String buscar){

        String CaracterABuscarr, CaracterReemplazo;

        CaracterABuscarr = null;
        CaracterReemplazo = null;

        try {
            ResultSet rs = ObtenerStatement(contexto).executeQuery("select STRVALOR from PVCONFIG where PROPIEDAD = 'CAMCARBUS'");

            if (rs.next()) {
                CaracterABuscarr = rs.getString("STRVALOR");
            }

            rs = (ObtenerStatement(contexto).executeQuery("select STRVALOR" +
                    " from PVConfig where PROPIEDAD = 'CAMCARNEW'"));
            if (rs.next()) {
                CaracterReemplazo = rs.getString("STRVALOR");

            }
            if(CaracterABuscarr != null && CaracterReemplazo != null){
                reemplazado = buscar.replace(CaracterABuscarr,CaracterReemplazo);
                Log.i("info", CaracterABuscarr + "   " + CaracterReemplazo + " " + reemplazado + " " + buscar );
            }
        } catch (SQLException e){
            Log.e("info", "estoy en el catch de ArmarCodidoDeReemplazo");
        }
    }





    public ResultSet ConsultarPrecio(Context contexto, String buscar,String codlista)

    {
        try {
            original = buscar;
            reemplazado = buscar;
            armarCodigoDeReemplazo(contexto, buscar);

            //en este IF busco por el codigo con el caracter reemplazado==============================
            if ( reemplazado.compareTo(original) != 0 )
            {
                resultado = ConsultarPrecioSegunTabla(contexto, reemplazado, codlista, "coditm");
                Log.i("info","despues de la consulta");

                if (resultado.isBeforeFirst())
                {
                    Log.i("info"," Salio por caracter reemplazo coditm" );
                    return resultado;
                }
                // busco por codigo alternativo con el codigo original==================================

                if(!resultado.isBeforeFirst())
                {
                    resultado = ConsultarPrecioSegunTabla(contexto, original, codlista, "CODITMALTERNATIVO");
                    if (resultado.isBeforeFirst())
                    {
                        Log.i("info","Salio por el codigo original en codAlternativo");
                        return resultado;
                    } //devuelvo solo si hay resultados
                }
            }

            //en este IF busco si no hay reemplazo==================================================
            if( reemplazado.compareTo(original) == 0 )
            {
                Log.i("info","Recien compare a reemplazo con original y ahora vos a hacer la consulta");

                //Busco x codigo Orinal sobre la columna CODALTERNATIVO
                resultado = ConsultarPrecioSegunTabla(contexto, original, codlista, "CODITMALTERNATIVO");
                Log.i("info"," Hice ConsultarPrecioSegunTabla(contexto, original, codlista, \"CODITMALTERNATIVO\")");

                if (resultado.isBeforeFirst())
                {
                    Log.i("info","Salio por caracterbuscar = null y busco por codAlternativo");
                    return resultado;
                } //devuelvo solo si hay resultados

                //Busco x codigo Orinal sobre la columna CODITM=========================================
                if(!resultado.isBeforeFirst())
                {

                    resultado = ConsultarPrecioSegunTabla(contexto, original, codlista, "coditm");

                    if (resultado.isBeforeFirst())
                    {
                        Log.i("info","Salio por caracterbuscar null y busco por codItm");
                        return resultado;}
                }
            } return resultado;

        } catch (SQLException e) {

            Toast.makeText(contexto.getApplicationContext(), "Error al ejecutar la consulta", Toast.LENGTH_LONG).show();
            return null;
        }

    }


    public ResultSet TraerSucursal(Context contexto)

    {
        try {
            resultado=null;
            resultado = ObtenerStatement(contexto).executeQuery(
                    "SELECT INTVALOR FROM PVConfig where PROPIEDAD = 'CodSuc'");
            return resultado;
        } catch (SQLException e) {
            Toast.makeText(contexto.getApplicationContext(), "ERROR SQL", Toast.LENGTH_LONG).show();
            return null;
        }

    }

    public ResultSet TraerListaPrecio(Context contexto)

    {
        try {
            resultado=null;
            resultado = ObtenerStatement(contexto).executeQuery(
                    "select STRVALOR from PVConfig where propiedad='listapreciopublico'");
            return resultado;
        } catch (SQLException e) {
            Toast.makeText(contexto.getApplicationContext(), "ERROR SQL", Toast.LENGTH_LONG).show();
            return null;
        }

    }



}
