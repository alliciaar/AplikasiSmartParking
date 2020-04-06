package com.alice.parkingapp.Model;

public class Pesanan {
    String id, slot, start, finish, status, pemesan, plat;

    public Pesanan() {
    }

    public Pesanan(String id, String slot, String start, String finish, String status, String pemesan, String plat) {
        this.id = id;
        this.slot = slot;
        this.start = start;
        this.finish = finish;
        this.status = status;
        this.pemesan = pemesan;
        this.plat = plat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPemesan() {
        return pemesan;
    }

    public void setPemesan(String pemesan) {
        this.pemesan = pemesan;
    }

    public String getPlat() {
        return plat;
    }

    public void setPlat(String plat) {
        this.plat = plat;
    }
}
