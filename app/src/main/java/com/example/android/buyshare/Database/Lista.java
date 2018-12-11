package com.example.android.buyshare.Database;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class Lista {

    private String nomeLista;
    private ArrayList<String> produtos;
    private String criadorLista;


    public Lista(){
    }

    public Lista(String criadorLista, String nomeLista, ArrayList<String> produtos  ){
        this.nomeLista = nomeLista;
        this.produtos = produtos;
        this.criadorLista = criadorLista;
    }

    public String getNomeLista() { return nomeLista;}

    public void setNomeLista(String nomeLista) { this.nomeLista = nomeLista; }

    public ArrayList<String> getProdutos() { return produtos;}

    public void setProdutos(ArrayList<String> produtos) { this.produtos = produtos;}

    public String getCriadorLista() {return criadorLista;}

    public void setCriadorLista(String criadorLista) { this.criadorLista = criadorLista; }
}
