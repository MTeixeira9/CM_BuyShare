package com.example.android.buyshare;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.android.buyshare.Database.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FragAddMembrosAmigos extends Fragment {

    private String userTlm, nomeLista, position;
    private ArrayAdapter<String> mAdapter;
    private DatabaseReference mDatabase;
    private ValueEventListener mListener;
    private Map<String, String> amigos;
    private LinearLayout linearLayout;



    public FragAddMembrosAmigos() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.frag_add_membros_amigos, container, false);

        userTlm = getActivity().getIntent().getStringExtra("userTlm");
        nomeLista = getActivity().getIntent().getStringExtra("nameL");
        position = getActivity().getIntent().getStringExtra("position");

        amigos = new HashMap<>();
        linearLayout = v.findViewById(R.id.linearLayoutAddMembros);

        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        mListener = mDatabase.child(userTlm).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);

                amigos = u.getAmigos();

                if(amigos != null){
                    for (Map.Entry<String, String> a : amigos.entrySet()) {
                        CheckBox cb = new CheckBox(getContext());
                        cb.setTextSize(18);
                        cb.setText(a.getValue() + ":"+ a.getKey());
                        linearLayout.addView(cb);


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return v;
    }


}
