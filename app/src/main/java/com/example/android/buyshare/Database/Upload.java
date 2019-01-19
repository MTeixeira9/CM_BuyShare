package com.example.android.buyshare.Database;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Upload {
    private String mName;
    private String mImageUrl;
    private String idFoto;

    public Upload() {
        //empty constructor needed
    }

    public Upload(String name, String imageUrl, String idFoto) {
        this.mName = name;
        this.mImageUrl = imageUrl;
        this.idFoto = idFoto;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getIdFoto() {
        return idFoto;
    }
}