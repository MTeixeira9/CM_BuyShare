package com.example.android.buyshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class FragAddMembrosGrupos extends Fragment implements IOnBackPressed {

    private String userTlm, nomeLista, position, idL;
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
        idL = getActivity().getIntent().getStringExtra("idL");

        amigos = new HashMap<>();
        nomeGrupo = "";
        linearLayout = v.findViewById(R.id.linearLayoutGrupo);


        mDatabase = FirebaseDatabase.getInstance().getReference("grupos");

        mListener = mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    String nome = String.valueOf(singleSnapshot.child("nome").getValue());

                    if(!nome.equals("")){
                        CheckBox cb = new CheckBox(getContext());
                        cb.setTextSize(18);
                        cb.setText(nome);
                        linearLayout.addView(cb);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }) ;

        return v;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getActivity(), MostraLista.class);
        i.putExtra("userTlm", userTlm);
        i.putExtra("nameL", nomeLista);
        i.putExtra("position", position);
        i.putExtra("idL", idL);
        startActivity(i);
    }
}
