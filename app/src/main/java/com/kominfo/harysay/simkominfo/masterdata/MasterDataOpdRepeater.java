package com.kominfo.harysay.simkominfo.masterdata;

/**
 * Created by harysay on 18/02/2018.
 */

public class MasterDataOpdRepeater {
    private String noUrut, idMstOpd, mstNamaOpd, mstNamaKecamatan, mstKodeKec;

    public MasterDataOpdRepeater() {
    }

    public MasterDataOpdRepeater(String noUrutx, String idMstOpdx, String mstNamaKecamatanx, String mstNamaOpd, String kodeKecamatan) {
        this.noUrut = noUrutx;
        this.idMstOpd = idMstOpdx;
        this.mstNamaOpd = mstNamaOpd;
        this.mstNamaKecamatan = mstNamaKecamatanx;
        this.mstKodeKec = kodeKecamatan;
    }

    public String getNoUrutOpd() {
        return noUrut;
    }

    public void setNoUrutOpd(String noUrutOpd) {
        this.noUrut = noUrutOpd;
    }

    public String getIdMstOpd() {
        return idMstOpd;
    }

    public void setIdMstOpd(String idMstOpd) {
        this.idMstOpd = idMstOpd;
    }

    public String getMstNamaKecamatan() {
        return mstNamaKecamatan;
    }

    public void setMstNamaKecamatan(String mstNamaKecamatan) {
        this.mstNamaKecamatan = mstNamaKecamatan;
    }

    public String getMstNamaOpd() {
        return mstNamaOpd;
    }

    public void setMstNamaOpd(String mstNamaOpd) {
        this.mstNamaOpd = mstNamaOpd;
    }

    public String getKodeKecamatan(){
        return  mstKodeKec;
    }

    public void setMstKodeKec(String mstKodeKec) {
        this.mstKodeKec = mstKodeKec;
    }
}
