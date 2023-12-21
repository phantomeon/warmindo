package com.android.warmindo;

public class TransactionModel {
    String idpelanggan, idpengguna, idpromosi, idtransaksi, kodemeja, metodepembayaran, namapelanggan, status, tanggal, waktu;
    Integer shift, total, totaldiskon;

    TransactionModel(){

    }

    public TransactionModel(String idpelanggan, String idpengguna, String idpromosi, String idtransaksi, String kodemeja, String metodepembayaran, String namapelanggan, String status, String tanggal, String waktu, Integer shift, Integer total, Integer totaldiskon) {
        this.idpelanggan = idpelanggan;
        this.idpengguna = idpengguna;
        this.idpromosi = idpromosi;
        this.idtransaksi = idtransaksi;
        this.kodemeja = kodemeja;
        this.metodepembayaran = metodepembayaran;
        this.namapelanggan = namapelanggan;
        this.status = status;
        this.tanggal = tanggal;
        this.waktu = waktu;
        this.shift = shift;
        this.total = total;
        this.totaldiskon = totaldiskon;
    }

    public String getIdpelanggan() {
        return idpelanggan;
    }

    public void setIdpelanggan(String idpelanggan) {
        this.idpelanggan = idpelanggan;
    }

    public String getIdpengguna() {
        return idpengguna;
    }

    public void setIdpengguna(String idpengguna) {
        this.idpengguna = idpengguna;
    }

    public String getIdpromosi() {
        return idpromosi;
    }

    public void setIdpromosi(String idpromosi) {
        this.idpromosi = idpromosi;
    }

    public String getIdtransaksi() {
        return idtransaksi;
    }

    public void setIdtransaksi(String idtransaksi) {
        this.idtransaksi = idtransaksi;
    }

    public String getKodemeja() {
        return kodemeja;
    }

    public void setKodemeja(String kodemeja) {
        this.kodemeja = kodemeja;
    }

    public String getMetodepembayaran() {
        return metodepembayaran;
    }

    public void setMetodepembayaran(String metodepembayaran) {
        this.metodepembayaran = metodepembayaran;
    }

    public String getNamapelanggan() {
        return namapelanggan;
    }

    public void setNamapelanggan(String namapelanggan) {
        this.namapelanggan = namapelanggan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public Integer getShift() {
        return shift;
    }

    public void setShift(Integer shift) {
        this.shift = shift;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getTotaldiskon() {
        return totaldiskon;
    }

    public void setTotaldiskon(Integer totaldiskon) {
        this.totaldiskon = totaldiskon;
    }
}
