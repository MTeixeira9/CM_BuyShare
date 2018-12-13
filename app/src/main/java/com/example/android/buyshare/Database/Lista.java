package com.example.android.buyshare.Database;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class Lista {

    private String nomeLista;
    private ArrayList<String> produtos;
    private String criadorLista;
    private ArrayList <String> membrosGrupo;
    private boolean arquivada;

    public Lista(){
    }

    public Lista(String criadorLista, String nomeLista, ArrayList<String> produtos ){
        this.nomeLista = nomeLista;
        this.produtos = produtos;
        this.criadorLista = criadorLista;
        membrosGrupo = new ArrayList<>();
        membrosGrupo.add(criadorLista);
        arquivada = false;
    }

    public String getNomeLista() { return nomeLista;}

    public ArrayList<String> getProdutos() { return produtos;}

    public String getCriadorLista() {return criadorLista;}

    public ArrayList<String> getMembrosGrupo() { return membrosGrupo; }

    public boolean isArquivada() {
        return arquivada;
    }
}
