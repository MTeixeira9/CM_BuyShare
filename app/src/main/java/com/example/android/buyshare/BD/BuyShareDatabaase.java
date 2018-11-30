package com.example.android.buyshare.BD;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = User.class, version = 1)
public abstract class BuyShareDatabaase extends RoomDatabase {
    public abstract MyDataAcessObject myDataAcessObject();
}
