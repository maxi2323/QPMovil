package com.qpmovil;


import android.content.Context;
import android.os.StrictMode;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.DriverManager;


public class ConexionBD

{
    Connection cnn;
    private boolean FlagTest;

    public Connection conectar(Context contexto,String srv,String instancia,String base,String usuario, String password)
    {
        cnn=null;
        FlagTest = Boolean.FALSE;

        try{
            StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(politica);

            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
    //============= Spliteo datos del usuario para hacer conexion por seguridad de windows o SQL ============

            if( usuario.indexOf("#")!=-1) {
                String[] parts = usuario.split("#");
                String part1 = parts[0];
                String part2 = parts[1];
                // String aux = ;
                cnn = DriverManager.getConnection("jdbc:jtds:sqlserver://"+srv +  ";instance="+instancia+";databaseName="+base+";domain="+part1+";useNTLMv2=true" + ";user="+part2+";password="+password+";");

            }
            else{
                cnn = DriverManager.getConnection("jdbc:jtds:sqlserver://"+srv +  ";instance="+instancia+";databaseName="+base+";user="+usuario+";password="+password+";");
            }

            if (!cnn.isClosed())
            {
                FlagTest = Boolean.TRUE;
            }

//            if(cnn.isValid(10))
//            {
//                FlagTest = Boolean.TRUE;
//
//            }
//

        }
        catch (Exception e){
  //          Toast.makeText(contexto,"Error al crear la conexión",Toast.LENGTH_LONG).show();// VER QUE DEVUELVO PARA EL ERROR
            return null;
        }


        return cnn;
    }

    public Boolean TestConexion(Context contexto, String srv, String instancia, String base, String usuario, String password)
    {
    try
         {
           conectar(contexto,srv,instancia,base,usuario,password);
             if (FlagTest == Boolean.TRUE)

             {
                 return  Boolean.TRUE;
             }
             else
             {
                 return  Boolean.FALSE;
             }
         }
    catch (Exception e )
        {
          Toast.makeText(contexto,e.getMessage(),Toast.LENGTH_LONG).show();
            return Boolean.FALSE;
        }

    }


}
