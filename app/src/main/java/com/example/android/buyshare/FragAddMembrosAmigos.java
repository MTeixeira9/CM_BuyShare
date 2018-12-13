package com.example.android.buyshare;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class FragAddMembrosAmigos extends Fragment {

    private String userTlm;
    private Da

    public FragAddMembrosAmigos() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         View v =  inflater.inflate(R.layout.frag_add_membros_amigos, container, false);

        userTlm = getActivity().getIntent().getStringExtra("userTlm");





         return v;
    }


}
