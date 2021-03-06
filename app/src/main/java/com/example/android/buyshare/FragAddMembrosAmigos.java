package com.example.android.buyshare;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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


public class FragAddMembrosAmigos extends Fragment implements IOnBackPressed {

    private String userTlm, nomeLista, position, idL, tipoLista;
    private ArrayAdapter<String> mAdapter;
    private DatabaseReference mDatabase, mDatabaseL;
    private Map<String, String> amigos;
    private LinearLayout linearLayout;
    private Button adicionar;
    private List<String> paraAdicionar;
    private ArrayList<String> membrosLista;
    private Lista l;

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
        tipoLista = getActivity().getIntent().getStringExtra("tipoL");


        amigos = new HashMap<>();
        linearLayout = v.findViewById(R.id.linearLayoutAddMembros);
        adicionar = v.findViewById(R.id.buttonAddAmigosFrag);

        membrosLista = new ArrayList<>();

        mDatabaseL = FirebaseDatabase.getInstance().getReference("listas");
        mDatabaseL.child(idL).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                l = dataSnapshot.getValue(Lista.class);

                mDatabase = FirebaseDatabase.getInstance().getReference("users");
                mDatabase.child(userTlm).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User u = dataSnapshot.getValue(User.class);
                        amigos = u.getAmigos();

                        if (amigos != null) {
                            for (Map.Entry<String, String> a : amigos.entrySet()) {
                                CheckBox cb = new CheckBox(getContext());
                                cb.setTextSize(18);
                                cb.setText(a.getValue() + " " + a.getKey());

                                if (l.getMembrosLista().contains(a.getKey())) {
                                    cb.setChecked(true);
                                    cb.setPaintFlags(cb.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                    cb.setTextColor(Color.GRAY);
                                }

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

                                            if(!l.getMembrosLista().contains(add[add.length - 1])) {
                                                paraAdicionar.add(add[add.length - 1]);
                                            }
                                        }

                                    }
                                }


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


                                Intent i = new Intent(getActivity(), MinhasListas.class);
                                i.putExtra("userTlm", userTlm);
                                i.putExtra("nameL", nomeLista);
                                i.putExtra("position", position);
                                i.putExtra("tipoL", tipoLista);

                                startActivity(i);

                            }
                        });

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return v;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        i.putExtra("tipoL", tipoLista);
        startActivity(i);
    }
}