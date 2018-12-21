package com.example.android.buyshare.Database;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class Lista {

    private String idL;
    private String nomeLista;
    private ArrayList<String> produtos;
    private ArrayList<Double> arrayCusto;
    private String criadorLista;
    private ArrayList <String> membrosGrupo;
    private boolean arquivada;

    public Lista(){
    }

    public Lista(String idL, String criadorLista, String nomeLista, ArrayList<String> produtos ){
        this.idL = idL;
        this.nomeLista = nomeLista;
        this.produtos = produtos;
        this.criadorLista = criadorLista;
        membrosGrupo = new ArrayList<>();
        membrosGrupo.add(criadorLista);
        arquivada = false;

        arrayCusto = new ArrayList<>();
    }

    public String getIdL() {
        return idL;
    }

    public String getNomeLista() { return nomeLista;}

    public ArrayList<String> getProdutos() { return produtos;}

    public String getCriadorLista() {return criadorLista;}

    public ArrayList<String> getMembrosGrupo() { return membrosGrupo; }

    public boolean isArquivada() {
        return arquivada;
    }

    public ArrayList<Double> getCustoFinal() { return arrayCusto; }
}
