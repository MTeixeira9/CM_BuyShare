package com.example.android.buyshare.Database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.buyshare.LoginActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

@IgnoreExtraProperties
public class User {

    private String nome;
    private String password;
    private String numeroTlm;
    private String email;
    private static List<User> amigos;
    private static User currUser;


    public User(){
    }

    public User(String nome,String password, String numeroTlm, String email){
        this.nome = nome;
        this.password = password;
        this.numeroTlm = numeroTlm;
        this.email = email;
        this.amigos=amigos;
    }

    public String getPassword(){
        return password;
    }

    public String getNumeroTlm(){
        return numeroTlm;
    }
    public static void writeNewUser(String userId, String name, String pass, String nTel, String email) {
        User user = new User(name, pass, nTel, email);

        LoginActivity.mDatabase.child("users").child(userId).setValue(user);
    }

    public static User readUser(DatabaseReference dbr, String numTel) {
        Query q = dbr.equalTo(numTel);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    currUser =singleSnapshot.getValue(User.class);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }

        });

        return currUser;
    }

    public static void addNewAmigo(DatabaseReference dbr, String numTel){
        User u = readUser(dbr, numTel);
        amigos.add(u);
    }

}
