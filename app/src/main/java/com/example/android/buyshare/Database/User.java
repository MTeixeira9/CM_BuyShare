package com.example.android.buyshare.Database;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.android.buyshare.LoginActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class User {

    private String nome;
    private String password;
    private String numeroTlm;
    private String email;
    //private static List<User> amigos;
    //private static User currUser;


    public User(){
    }

    public User(String nome,String password, String numeroTlm, String email){
        this.nome = nome;
        this.password = password;
        this.numeroTlm = numeroTlm;
        this.email = email;
        //this.amigos=new ArrayList<>();

    }

    public String getPassword(){
        return password;
    }

    public String getNumeroTlm(){
        return numeroTlm;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }
/*

    public static void writeNewUser(String userId, String name, String pass, String nTel, String email) {
        Log.d("11111111111111111 " + userId + " " + name, pass + " " + nTel + " " + email );
        User user = new User(name, pass, nTel, email);

        LoginActivity.mDatabase.child("users").child(userId).setValue(user);
    }
*/

    public static String readUser(String numTel) {


        return "";
    }

    public static void addNewAmigo(DatabaseReference dbr, String numTel){
        //User u = readUser(dbr, numTel);
        //amigos.add(u);
    }

}
