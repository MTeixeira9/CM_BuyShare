package com.example.android.buyshare;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.example.android.buyshare.Database.Grupo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class FragAddMembrosGrupos extends Fragment {

    private String userTlm, nomeLista, position;
    private ArrayAdapter<String> mAdapter;
    private DatabaseReference mDatabase;
    private ValueEventListener mListener;
    private Map<String, String> amigos;
    private LinearLayout linearLayout;
    private String nomeGrupo;

    public FragAddMembrosGrupos() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.frag_add_membros_grupos, container, false);


        userTlm = getActivity().getIntent().getStringExtra("userTlm");
        nomeLista = getActivity().getIntent().getStringExtra("nameL");
        position = getActivity().getIntent().getStringExtra("position");

        amigos = new HashMap<>();
        nomeGrupo = "";
        linearLayout = v.findViewById(R.id.linearLayoutGrupo);


        mDatabase = FirebaseDatabase.getInstance().getReference("grupos");

        mListener = mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    String nome = String.valueOf(singleSnapshot.child("nome").getValue());

                    if(nome != null){
                        CheckBox cb = new CheckBox(getContext());
                        cb.setTextSize(18);
                        cb.setText(nome);
                        linearLayout.addView(cb);
                    }

                    /*
                    Grupo g = dataSnapshot.getValue(Grupo.class);
                    nomeGrupo = g.getNome();*/

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }) ;



        return v;
    }




}
