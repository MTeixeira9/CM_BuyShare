package com.example.android.buyshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.buyshare.Database.Lista;
import com.example.android.buyshare.Database.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragAddMembrosAmigos extends Fragment {

    private String userTlm, nomeLista, position, idL;
    private ArrayAdapter<String> mAdapter;
    private DatabaseReference mDatabase, mDatabaseL;
    private ValueEventListener mListener, mListenerL;
    private Map<String, String> amigos;
    private LinearLayout linearLayout;
    private Button adicionar;
    private List<String> paraAdicionar;
    private ArrayList<String> membrosLista;

    public FragAddMembrosAmigos() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.frag_add_membros_amigos, container, false);

        userTlm = getActivity().getIntent().getStringExtra("userTlm");
        nomeLista = getActivity().getIntent().getStringExtra("nameL");
        position = getActivity().getIntent().getStringExtra("position");
        idL = getActivity().getIntent().getStringExtra("idL");




        amigos = new HashMap<>();
        linearLayout = v.findViewById(R.id.linearLayoutAddMembros);
        adicionar = v.findViewById(R.id.buttonAddAmigosFrag);


        membrosLista = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        mListener = mDatabase.child(userTlm).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);

                amigos = u.getAmigos();

                if (amigos != null) {
                    for (Map.Entry<String, String> a : amigos.entrySet()) {
                        CheckBox cb = new CheckBox(getContext());
                        cb.setTextSize(18);
                        cb.setText(a.getValue() + " " + a.getKey());
                        linearLayout.addView(cb);
                    }
                }

                //botao
                adicionar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        paraAdicionar = new ArrayList<>();
                        for (int a = 0; a <= linearLayout.getChildCount(); a++) {

                            View view = linearLayout.getChildAt(a);
                            if (view instanceof CheckBox) {
                                CheckBox c = (CheckBox) view;
                                //cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                //  @Override
                                //public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                if (c.isChecked()) {
                                    String[] add = c.getText().toString().split("\\s+");
                                    paraAdicionar.add(add[add.length-1]);
                                }

                            }
                        }


                        mDatabaseL = FirebaseDatabase.getInstance().getReference("listas");
                        mListenerL = mDatabaseL.child(idL).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                Lista l = dataSnapshot.getValue(Lista.class);



                                membrosLista = l.getMembrosLista();


                                for (String a : paraAdicionar) {
                                    if (!membrosLista.contains(a)) {
                                        membrosLista.add(a);

                                        if (membrosLista.size() > 0) {
                                            mDatabaseL.child(idL).child("partilhada").setValue(true);
                                        }
                                    }

                                    Toast.makeText(getContext(), "Adicionado com sucesso", Toast.LENGTH_LONG).show();

                                }

                                mDatabaseL.child(idL).child("membrosLista").setValue(membrosLista);


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        Intent i = new Intent(getActivity(), VerMembros.class );
                        i.putExtra("userTlm", userTlm);
                        i.putExtra("nameL", nomeLista);
                        i.putExtra("position", position);
                        i.putExtra("idL", idL);
                        startActivity(i);

                    }

                });

            }


            //@Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return v;


    }

    /*
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDatabase.removeEventListener(mListener);
        mDatabaseL.removeEventListener(mListenerL);
    }*/
}