package com.kominfo.harysay.simkominfo.masterdata;

/**
 * Created by harysay on 18/02/2018.
 */

public class MasterDataKendala {
    private String noUrut, idKendala, judulKendala, keterangan;

    public MasterDataKendala() {
    }

    public MasterDataKendala(String noUrutx, String idKendalax, String judulKendalax, String keteranganx) {
        this.noUrut = noUrutx;
        this.idKendala = idKendalax;
        this.judulKendala = judulKendalax;
        this.keterangan = keteranganx;
    }

    public String getNoUrut() {
        return noUrut;
    }

    public void setNoUrut(String noUrut) {
        this.noUrut = noUrut;
    }

    public String getIdKendala() {
        return idKendala;
    }

    public void setIdKendala(String idKendala) {
        this.idKendala = idKendala;
    }

    public String getJudulKendala() {
        return judulKendala;
    }

    public void setJudulKendala(String judulKendala) {
        this.judulKendala = judulKendala;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

}
