package com.example.android.buyshare.Database;

public class User {

    public String nome;
    public String password;
    public String numeroTlm;
    public String email;


    public User(){
    }

    public User(String nome,String password, String numeroTlm, String email ){
        this.nome = nome;
        this.password = password;
        this.numeroTlm = numeroTlm;
        this.email = email;
    }

}
