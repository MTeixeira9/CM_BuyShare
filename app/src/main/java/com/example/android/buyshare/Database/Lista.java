package com.example.android.buyshare.Database;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class Lista {

    private String nome;
    private List<String> produtos;

    public Lista(){

    }

    public Lista(String nome, List<String> produtos){
        this.nome = nome;
        this.produtos = produtos;
    }
}
