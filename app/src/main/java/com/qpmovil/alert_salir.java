package com.qpmovil;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Window;

import com.qpmovil.R;

public class alert_salir
{
    Dialog dialogo;


    public alert_salir(final Context contexto)
    {
        dialogo = new Dialog(contexto);
    }

    public void  Mostrar_Alert()
    {

        dialogo.requestWindowFeature(Window.FEATURE_RIGHT_ICON);
        dialogo.setCancelable(false);
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.setContentView(R.layout.alert_salir);
        dialogo.show();
    }

    public void Cerrar_alert()
    {
        dialogo.dismiss();

    }

}
