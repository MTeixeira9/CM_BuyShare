package com.example.android.buyshare.Database;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@IgnoreExtraProperties
public class Lista {

    private String idL;
    private String nomeLista;
    private HashMap<String, Double > produtoCusto;
    private String criadorLista;
    private ArrayList <String> membrosGrupo;
    private boolean partilhada;
    private ArrayList <String> quemEliminou, quemArquivou;

    public Lista(){
    }

    public Lista(String idL, String criadorLista, String nomeLista, HashMap<String,Double > produtoCusto ){
        this.idL = idL;
        this.nomeLista = nomeLista;
        this.criadorLista = criadorLista;
        membrosGrupo = new ArrayList<>();
        membrosGrupo.add(criadorLista);
        partilhada = false;
        quemEliminou = new ArrayList<>();
        quemEliminou.add("");
        quemArquivou = new ArrayList<>();
        quemArquivou.add("");
        this.produtoCusto = produtoCusto;
    }

    public String getIdL() {
        return idL;
    }

    public String getNomeLista() { return nomeLista;}

    public String getCriadorLista() {return criadorLista;}

    public ArrayList<String> getMembrosGrupo() { return membrosGrupo; }

    public boolean isPartilhada() { return partilhada; }

    public ArrayList<String> getQuemEliminou() { return quemEliminou; }

    public ArrayList<String> getQuemArquivou() { return quemArquivou; }

    public HashMap<String, Double> getProdutoCusto() {return produtoCusto; }
}
