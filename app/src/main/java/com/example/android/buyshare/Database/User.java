package com.example.android.buyshare.Database;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.android.buyshare.LoginActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class User {

    private String nome;
    private String password;
    private String numeroTlm;
    private String email;
    private static ArrayList<String> amigos;

    public User(){
    }

    public User(String nome,String password, String numeroTlm, String email){
        this.nome = nome;
        this.password = password;
        this.numeroTlm = numeroTlm;
        this.email = email;
        this.amigos=new ArrayList<>();
        amigos.add("");


    }

    public String getPassword(){
        return password;
    }

    public String getNumeroTlm(){
        return numeroTlm;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<String> getAmigos (){
        return amigos;
    }

/*
    public static void writeNewUser(String userId, String name, String pass, String nTel, String email) {
        User user = new User(name, pass, nTel, email);

        LoginActivity.mDatabase.child("users").child(userId).setValue(user);
    }*/


}
