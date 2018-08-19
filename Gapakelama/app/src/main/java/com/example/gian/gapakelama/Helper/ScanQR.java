package com.example.gian.gapakelama.Helper;

/**
 * Created by gian on 05/03/2018.
 */

public class ScanQR {

    private String no_meja, status;

    public ScanQR(String no_meja, String status) {
        this.no_meja = no_meja;
        this.status = status;
    }

    public String getNo_meja() {
        return no_meja;
    }

    public String getStatus() { return status; }


}
