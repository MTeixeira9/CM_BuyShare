package com.example.android.buyshare.Database;

import android.util.Pair;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@IgnoreExtraProperties
public class Lista {

    private String idL;
    private String nomeLista;
    private HashMap<String,HashMap<String, Double> > produtoCusto;
    private String criadorLista;
    private ArrayList <String> membrosLista;
    private boolean partilhada, finalizada;
    private ArrayList <String> quemEliminou, quemArquivou;
    private String quemFinalizou;

    public Lista(){
    }

    public Lista(String idL, String criadorLista, String nomeLista, HashMap<String,HashMap<String, Double> > produtoCusto ){
        this.idL = idL;
        this.nomeLista = nomeLista;
        this.criadorLista = criadorLista;
        membrosLista = new ArrayList<>();
        membrosLista.add(criadorLista);
        partilhada = false;
        quemEliminou = new ArrayList<>();
        quemEliminou.add("");
        quemArquivou = new ArrayList<>();
        quemArquivou.add("");
        this.produtoCusto = produtoCusto;
        finalizada = false;
        quemFinalizou = "";
    }

    public String getIdL() {
        return idL;
    }

    public String getNomeLista() { return nomeLista;}

    public String getCriadorLista() {return criadorLista;}

    public ArrayList<String> getMembrosLista() { return membrosLista; }

    public boolean isPartilhada() { return partilhada; }

    public ArrayList<String> getQuemEliminou() { return quemEliminou; }

    public ArrayList<String> getQuemArquivou() { return quemArquivou; }

    public HashMap<String, HashMap<String, Double>> getProdutoCusto() {return produtoCusto; }

    public String getQuemFinalizou() {
        return quemFinalizou;
    }

    public boolean isFinalizada() {
        return finalizada;
    }
}
