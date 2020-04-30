package com.qpmovil;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;


public class lec_barras
{

    public lec_barras(Activity actividad, Fragment Fragmento)
    {
        IntentIntegrator intent = new IntentIntegrator(actividad);
        intent.forSupportFragment(Fragmento).setCameraId(0).setBeepEnabled(true).initiateScan();

    }



}




