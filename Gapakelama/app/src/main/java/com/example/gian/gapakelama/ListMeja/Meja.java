package com.example.gian.gapakelama.ListMeja;

/**
 * Created by gian on 27/09/2018.
 */

public class Meja {

    private String status;
    private String id_user;
    private String starttime;
    private String nomeja;

    public Meja(String nomeja, String status, String id_user, String starttime) {
        this.nomeja = nomeja;
        this.id_user = id_user;
        this.status = status;
        this.starttime = starttime;
    }

    public String getNomeja() {
        return nomeja;
    }

    public String getId_user() {
        return id_user;
    }

    public String getStarttime() { return starttime; }

    public String getStatus() {
        return status;
    }
}
