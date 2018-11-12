package com.example.android.buyshare;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class FragEstimarCustoLista extends Fragment {

    Intent i;
    public FragEstimarCustoLista() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_estimar_custo_lista, container, false);
        Button custo = (Button) view.findViewById(R.id.custo);

        i = new Intent(getActivity(), PopUpEstimarCusto.class);

        custo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //POPUP
                startActivityForResult(i, 1);
            }
        });

        return view;
    }
}
