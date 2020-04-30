package com.qpmovil;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;



public class alert_animacionespera

{
    Dialog dialogo;

    public alert_animacionespera(Context contexto)

    {
        dialogo = new Dialog(contexto);
    }


    public void  Mostrar_Alert()
    {
        dialogo.requestWindowFeature(Window.FEATURE_RIGHT_ICON);
        dialogo.setCancelable(true);
        dialogo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogo.setContentView(R.layout.alert_animacionespera);
        dialogo.show();

    }


    public void Cerrar_Alert()
    {
        dialogo.dismiss();

    }

}
