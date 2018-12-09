package com.example.android.buyshare.Database;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class Grupo {

    private String nome;
    private List<User> membrosGrupo;

    public Grupo(){
    }

    public Grupo(String nome){
        this.nome = nome;
        this.membrosGrupo = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public List<User> getMembrosGrupo() {
        return membrosGrupo;
    }
}
