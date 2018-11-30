package com.example.android.buyshare.BD;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface MyDataAcessObject {

    //Operações de Utilizador

    @Insert
    public void adicionarUtilizador(User user);

    @Query("Select * from users where numeroTel=:numT" )
    public User selectUtilizador(String numT);



}
