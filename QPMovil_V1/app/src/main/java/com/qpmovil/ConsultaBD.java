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
            resultado = ObtenerStatement(contexto).executeQuery("" +
                    "IF (SELECT OBJECT_ID(N'tempdb..#tempCItems')) IS NOT NULL \n" +
                    "       DROP TABLE #tempCItems \n" +
                    "       \n" +
                    "SELECT 0  id\n" +
                    "       , dbo.PVItems.CODITM\n" +
                    "       , PVItems.DESCRIPCION\n" +
                    "       , PVItems.CODMADRE\n" +
                    "       , STKACTUAL = isnull(dbo.PVItemsAcum.STKACTUAL,0)\n" +
                    "       , Codtal, Codcol \n" +
                    "       ,  NOMBRECOL   \n" +
                    "       INTO #tempCItems   \n" +
                    "FROM PVItems   \n" +
                    "       LEFT JOIN  PVItemsAcum ON dbo.PVItems.CODITM = dbo.PVItemsAcum.CODITM \n" +
                    "and codsuc = \n" +
                    "       (      select INTVALOR\n" +
                    "        from pvconfig\n" +
                    "        where PROPIEDAD = 'CodSuc')\n" +
                    "WHERE PVItems.CODMADRE like '%"+buscar+"%' /* <<-----  PARAMETRO */    \n" +
                    "\n" +
                    "INSERT INTO #tempCItems \n" +
                    "SELECT  ROW_NUMBER() OVER (ORDER BY PVItems.CODITM) as id \n" +
                    "       , PVItems.CODITM\n" +
                    "       , PVItems.DESCRIPCION\n" +
                    "       , PVItems.CODMADRE\n" +
                    "       , STKACTUAL = isnull(PVItemsAcum.STKACTUAL,0)\n" +
                    "       , codtal\n" +
                    "       , codcol \n" +
                    "       , PVItems.NOMBRECOL \n" +
                    "FROM PVItems                           \n" +
                    "       JOIN PVItemsAcum ON dbo.PVItems.CODITM = dbo.PVItemsAcum.CODITM \n" +
                    "and codsuc = \n" +
                    "       (      select INTVALOR\n" +
                    "        from pvconfig\n" +
                    "        where PROPIEDAD = 'CodSuc')                              \n" +
                    "Where PVItems.CODMADRE in (select codmadre from #tempCItems) \n" +
                    "       and pvitems.coditm not in (select coditm from #tempCItems)  \n" +
                    "\n" +
                    "select (sum(stkactual) - dbo.FN_STOCK_SENIADO(t.CODMADRE)\n" +
                    "                     - dbo.FN_STOCK_EN_BULTOS(t.CodMADRE)\n" +
                    "                     - dbo.FN_STOCK_PE(t.codMADRE,1)) as STKACTUAL, t.CODMADRE, DESCRIPCION\n" +
                    "from #tempCItems t\n" +
                    "where STKACTUAL>0\n" +
                    "group by t.CODMADRE,DESCRIPCION\n" +
                    "order by STKACTUAL DESC --creo que yasta no le aplique el isnull deberiamos que onda eso...");
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
            resultado = ObtenerStatement(contexto).executeQuery("IF (SELECT OBJECT_ID(N'tempdb..#tempCItems')) IS NOT NULL \n" +
                    "       DROP TABLE #tempCItems \n" +
                    "       \n" +
                    "SELECT 0  id\n" +
                    "       , dbo.PVItems.CODITM\n" +
                    "       , PVItems.DESCRIPCION\n" +
                    "       , PVItems.CODMADRE\n" +
                    "       , STKACTUAL = isnull(dbo.PVItemsAcum.STKACTUAL,0)\n" +
                    "       , Codtal, Codcol \n" +
                    "       ,  NOMBRECOL   \n" +
                    "       INTO #tempCItems   \n" +
                    "FROM PVItems   \n" +
                    "       LEFT JOIN  PVItemsAcum ON dbo.PVItems.CODITM = dbo.PVItemsAcum.CODITM \n" +
                    "and codsuc = \n" +
                    "       (      select INTVALOR\n" +
                    "        from pvconfig\n" +
                    "        where PROPIEDAD = 'CodSuc')\n" +
                    "WHERE PVItems.descripcion like '%"+buscar+"%' /* <<-----  PARAMETRO */    \n" +
                    "\n" +
                    "INSERT INTO #tempCItems \n" +
                    "SELECT  ROW_NUMBER() OVER (ORDER BY PVItems.CODITM) as id \n" +
                    "       , PVItems.CODITM\n" +
                    "       , PVItems.DESCRIPCION\n" +
                    "       , PVItems.CODMADRE\n" +
                    "       , STKACTUAL = isnull(PVItemsAcum.STKACTUAL,0)\n" +
                    "       , codtal\n" +
                    "       , codcol \n" +
                    "       , PVItems.NOMBRECOL \n" +
                    "FROM PVItems                           \n" +
                    "       JOIN PVItemsAcum ON dbo.PVItems.CODITM = dbo.PVItemsAcum.CODITM \n" +
                    "and codsuc = \n" +
                    "       (      select INTVALOR\n" +
                    "        from pvconfig\n" +
                    "        where PROPIEDAD = 'CodSuc')                              \n" +
                    "Where PVItems.CODMADRE in (select codmadre from #tempCItems) \n" +
                    "       and pvitems.coditm not in (select coditm from #tempCItems)  \n" +
                    "\n" +
                    "select (sum(stkactual) - dbo.FN_STOCK_SENIADO(t.CODMADRE)\n" +
                    "                     - dbo.FN_STOCK_EN_BULTOS(t.CodMADRE)\n" +
                    "                     - dbo.FN_STOCK_PE(t.codMADRE,1)) as STKACTUAL, t.CODMADRE, DESCRIPCION\n" +
                    "from #tempCItems t\n" +
                    "where STKACTUAL>0\n" +
                    "group by t.CODMADRE,DESCRIPCION\n" +
                    "order by STKACTUAL DESC --creo que yasta no le aplique el isnull deberiamos que onda eso...");

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
            resultado = ObtenerStatement(contexto).executeQuery( "SELECT I.CODCOL, I.CODTAL, I.DESCRIPCION, a.STKACTUAL, P.PRECIO, I.CODITM, I.CODMADRE, i.NOMBRECOL\n" +
                    "FROM PVListasPrecios P  \n" +
                    "\tjoin PVItemsAcum as a on p.CODITM=a.CODITM\n" +
                    "\tJOIN pvitems I ON P.CODITM = I.CODITM  \n" +
                    "\tLEFT JOIN (SELECT precio1 = 'Ver'\n" +
                    "\t\t\t\t, precio\n" +
                    "\t\t\t\t, codmadre\n" +
                    "\t\t\t\t, CODLIS  \n" +
                    "\t\t\tFROM PVListasPrecios L       \n" +
                    "\t\t\t\tLEFT JOIN pvitems I ON L.CODITM=I.CODITM            \n" +
                    "\t\t\tWhere precio > 0            \n" +
                    "GROUP BY precio, CODMADRE, CODLIS) I2 ON I.CODMADRE = I2.CODMADRE \n" +
                    "\t\t\tand P.precio <> I2.precio  \n" +
                    "\t\t\tand P.CODLIS = i2.CODLIS \n" +
                    "WHERE I."+columna+" = '"+valorAbuscar+"' /* <<-----  PARAMETRO */ \n" +
                    "\t\tand P.PRECIO > 0\n" +
                    "\t\tand p.CODLIS = \n" +
                    "\t(\t\t\tselect CODLIS\n" +
                    "            \tfrom PVListasPrecios\n" +
                    "            \twhere codlis like (select STRVALOR from PVConfig where propiedad='listapreciopublico')\n" +
                    "            \tgroup by codlis )");



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
