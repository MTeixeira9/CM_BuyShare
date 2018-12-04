package com.example.android.buyshare.Database;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class Grupo {

    private String nome;
    private List<User> membrosGrupo;

    public Grupo(){
    }

    public Grupo(String nome, List<User> membrosGrupo){
        this.nome = nome;
        this.membrosGrupo = membrosGrupo;
    }
}
