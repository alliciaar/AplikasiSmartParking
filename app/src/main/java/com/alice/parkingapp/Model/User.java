package com.alice.parkingapp.Model;

public class User {
    String id, nama, plat;

    public User(String id, String nama, String plat) {
        this.id = id;
        this.nama = nama;
        this.plat = plat;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPlat() {
        return plat;
    }

    public void setPlat(String plat) {
        this.plat = plat;
    }
}
