package com.kominfo.harysay.simkominfo.masterdata.spinner;

import java.util.List;

/**
 * Created by harysay on 03 Jan 2018.
 */

public class DaftarKecamatan {
    private String namaKecamatan,kodeKecamatan;

    public DaftarKecamatan() {

    }

    public DaftarKecamatan(String namKecamatan) {
        this.namaKecamatan = namKecamatan;
    }

    public String getKodeKecamatan() {
        return kodeKecamatan;
    }

    public  void setKodeKecamatan(String kodeKecamatan) {
        this.kodeKecamatan = kodeKecamatan;
    }

    public String getNamaKecamatan() {
        return namaKecamatan;
    }

    public  void setNamaKecamatan(String namakecamatan) {
        this.namaKecamatan = namakecamatan;
    }

}
