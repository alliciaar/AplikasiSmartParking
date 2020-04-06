package com.alice.parkingapp.Model;

public class Slot {
    String id, status, waktu;

    public Slot() {
    }

    public Slot(String id, String status, String waktu) {
        this.id = id;
        this.status = status;
        this.waktu = waktu;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }
}
