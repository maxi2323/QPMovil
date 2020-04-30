package com.qpmovil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;


public class Fragment_Home extends Fragment {

    private View vista;
    private ImageButton IB_facebook;
    private ImageButton IB_youtube;
    private ImageButton IB_instagram;
    private ImageButton IB_linkedin;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        vista =inflater.inflate(R.layout.fragment_home, container, false);

        IB_facebook = vista.findViewById(R.id.IB_facebook);
        IB_youtube = vista.findViewById(R.id.IB_youtube);
        IB_instagram = vista.findViewById(R.id.IB_instagram);
        IB_linkedin = vista.findViewById(R.id.IB_linkedin);



        IB_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.instagram.com/bas_software");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });


        IB_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.youtube.com/channel/UCa28NKQsTfUx_HOEL839LAg/featured");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);

            }
        });

        IB_linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.linkedin.com/company/3682103");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);

            }
        });

        IB_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.facebook.com/buenosairessoftware");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);



            }
        });



        return vista;
    }

}

