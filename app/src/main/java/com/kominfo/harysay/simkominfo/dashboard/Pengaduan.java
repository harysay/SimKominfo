package com.kominfo.harysay.simkominfo.dashboard;

public class Pengaduan {
    private String nomor;
    private String tanggal_pengaduan;
    private String kecamatan;
    private String opd;
    private String keterangan;
    private String status_perbaikan;

    public Pengaduan(String nomor, String tanggal_pengaduan, String kecamatan, String opd, String keterangan, String status_perbaikan) {
        this.nomor = nomor;
        this.tanggal_pengaduan = tanggal_pengaduan;
        this.kecamatan = kecamatan;
        this.opd = opd;
        this.keterangan = keterangan;
        this.status_perbaikan = status_perbaikan;
    }


    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public String getTanggal_pengaduan() {
        return tanggal_pengaduan;
    }

    public void setTanggal_pengaduan(String tanggal_pengaduan) {
        this.tanggal_pengaduan = tanggal_pengaduan;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public String getOpd() {
        return opd;
    }

    public void setOpd(String opd) {
        this.opd = opd;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getStatus_perbaikan() {
        return status_perbaikan;
    }

    public void setStatus_perbaikan(String status_perbaikan) {
        this.status_perbaikan = status_perbaikan;
    }
}
