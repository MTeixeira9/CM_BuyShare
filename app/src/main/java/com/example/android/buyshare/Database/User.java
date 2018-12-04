package com.example.android.buyshare.Database;

import com.example.android.buyshare.LoginActivity;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
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

    public static void writeNewUser(String userId, String name, String pass, String nTel, String email) {
        User user = new User(name, pass, nTel, email);

        LoginActivity.mDatabase.child("users").child(userId).setValue(user);
    }

    public static void readUser(String name) {
        /*User user = new User(name, pass, nTel, email);

        LoginActivity.mDatabase.child("users").child(userId).setValue(user);*/
    }

}
