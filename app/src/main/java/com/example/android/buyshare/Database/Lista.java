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
    private boolean arquivada, partilhada;

    public Lista(){
    }

    public Lista(String idL, String criadorLista, String nomeLista, HashMap<String,Double > produtoCusto ){
        this.idL = idL;
        this.nomeLista = nomeLista;
        this.criadorLista = criadorLista;
        membrosGrupo = new ArrayList<>();
        membrosGrupo.add(criadorLista);
        arquivada = false;
        partilhada = false;
        this.produtoCusto = produtoCusto;
        produtoCusto = new HashMap<>();
    }

    public String getIdL() {
        return idL;
    }

    public String getNomeLista() { return nomeLista;}

    public String getCriadorLista() {return criadorLista;}

    public ArrayList<String> getMembrosGrupo() { return membrosGrupo; }

    public boolean isArquivada() {
        return arquivada;
    }

    public boolean isPartilhada() {
        return partilhada;
    }

    public HashMap<String, Double> getProdutoCusto() {return produtoCusto; }
}
