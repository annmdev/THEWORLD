package com.example.r.theworld.presentation.loader;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;

public class AssetsData {

    private AssetManager assetManager;

    public AssetsData(AssetManager assetManager){
        this.assetManager = assetManager;
    }

    public Drawable getDrawable(String path){
        Drawable drawable = null;
        try{
            InputStream inputStream = assetManager.open(path);
            drawable = Drawable.createFromStream(inputStream, null);
        }catch (IOException ex){

        }
        return drawable;
    }


}
