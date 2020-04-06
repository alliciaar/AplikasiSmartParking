package com.alice.parkingapp.Class;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class Session {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;

    //key
    public String KEY_LOKASI_LATTITUDE = "LATTITUDE";
    public String KEY_LOKASI_LONGITUDE = "LONGITUDE";

    //key for is user login
    private String ISLOGIN="login";
    private final int MODE_PRIVATE = 0;
    private final String SHARE_NAME="sesi";


    // function store user details
    public void storeLogin(String lattitude, String longitude)
    {
        editor.putString(KEY_LOKASI_LATTITUDE,lattitude);
        editor.putString(KEY_LOKASI_LONGITUDE,longitude);
        editor.putBoolean(ISLOGIN,true);
        editor.commit();
    }

    public HashMap getDetailLogin(){
        HashMap<String,String> map = new HashMap<>();
        map.put(KEY_LOKASI_LATTITUDE, prefs.getString(KEY_LOKASI_LATTITUDE,null));
        map.put(KEY_LOKASI_LONGITUDE, prefs.getString(KEY_LOKASI_LONGITUDE,null));
        return map;
    }


    public Session(Context ctx) {
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences(SHARE_NAME, MODE_PRIVATE);
        editor = prefs.edit();

    }

    public boolean setLoggedIn (boolean loggedin) {
        editor.putBoolean("loggedInmode", loggedin);
        editor.commit();
        return loggedin;
    }

    public boolean loggedin() {
        return prefs.getBoolean("loggedInmode", false);
    }

}
