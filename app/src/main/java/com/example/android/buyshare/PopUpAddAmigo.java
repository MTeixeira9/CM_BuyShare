package com.example.android.buyshare;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class PopUpAddAmigo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.acitivity_popupaddamigo);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        //ola

        getWindow().setLayout((int) (width*.8),(int) (height*.32));
        
    }
}
