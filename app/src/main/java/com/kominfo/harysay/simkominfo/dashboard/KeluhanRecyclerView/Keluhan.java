package com.kominfo.harysay.simkominfo.dashboard.KeluhanRecyclerView;

/**
 * Created by Lincoln on 15/01/16.
 */
public class Keluhan {
    private String opd, keterangan, tanggal;

    public Keluhan() {
    }

    public Keluhan(String opd, String keterangan, String tanggal) {
        this.opd = opd;
        this.keterangan = keterangan;
        this.tanggal = tanggal;
    }

    public String getOpd() {
        return opd;
    }

    public void setOpd(String nameOpd) {
        this.opd = nameOpd;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
