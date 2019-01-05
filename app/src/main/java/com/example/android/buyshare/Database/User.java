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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@IgnoreExtraProperties
public class User {

    private String nome;
    private String password;
    private String numeroTlm;
    private String email;
    private Map<String,String> amigos;
    private List<String> grupos;
    private List<String> listas, notificacoes;

    public User(){
    }

    public User(String nome,String password, String numeroTlm, String email){
        this.nome = nome;
        this.password = password;
        this.numeroTlm = numeroTlm;
        this.email = email;
        amigos = new HashMap<>();
        grupos = new ArrayList<>();
        listas = new ArrayList<>();
        notificacoes = new ArrayList<>();
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

    public Map<String, String> getAmigos (){
        return amigos;
    }

    public List<String> getGrupos() {
        return grupos;
    }

    public List<String> getListas() {
        return listas;
    }

    public List<String> getNotificacoes() {
        return notificacoes;
    }
}
