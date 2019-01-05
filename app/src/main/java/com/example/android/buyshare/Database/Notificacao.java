package com.example.android.buyshare.Database;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Notificacao {

    private String idN;
    private String idL;
    private String quemPagou;
    private String quemDeve;
    private Double quantia;

    public Notificacao(){

    }

    public Notificacao(String idN, String idL, String quemPagou, String quemDeve, Double quantia){
        this.idN = idN;
        this.idL = idL;
        this.quemPagou = quemPagou;
        this.quemDeve = quemDeve;
        this.quantia = quantia;
    }

    public String getIdL() {
        return idL;
    }

    public Double getQuantia() {
        return quantia;
    }

    public String getIdN() {
        return idN;
    }

    public String getQuemDeve() {
        return quemDeve;
    }

    public String getQuemPagou() {
        return quemPagou;
    }

}
