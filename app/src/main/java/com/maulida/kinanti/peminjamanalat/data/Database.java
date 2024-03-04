package com.maulida.kinanti.peminjamanalat.data;

public class Database {
    private String id_pinjam;
    private String nama_pinjam;
    private String no_hp;
    private String ktp;
    private String tgl_pinjam;
    private String kode_barang;
    private String status;

    public Database(String id_pinjam, String nama_pinjam, String no_hp, String ktp, String tgl_pinjam, String kode_barang, String status) {
        this.id_pinjam = id_pinjam;
        this.nama_pinjam = nama_pinjam;
        this.no_hp = no_hp;
        this.ktp = ktp;
        this.tgl_pinjam = tgl_pinjam;
        this.kode_barang = kode_barang;
        this.status = status;
    }

    public String getIdPinjam() {
        return id_pinjam;
    }

    public void setIdPinjam(String id_pinjam) {
        this.id_pinjam = id_pinjam;
    }

    public String getNamaPinjam() {
        return nama_pinjam;
    }

    public void setNamaPinjam(String nama_pinjam) {
        this.nama_pinjam = nama_pinjam;
    }

    public String getNoHp() {
        return no_hp;
    }

    public void setNoHp(String no_hp) {
        this.no_hp = no_hp;
    }


    public String getKtp() {
        return ktp;
    }

    public void setKtp(String ktp) {
        this.ktp = ktp;
    }

    public String getTglPinjam() {
        return tgl_pinjam;
    }

    public void setTglPinjam(String tgl_pinjam) {
        this.tgl_pinjam = tgl_pinjam;
    }

    public String getKodeBarang() {
        return kode_barang;
    }

    public void setKodeBarang(String kode_barang) {
        this.kode_barang = kode_barang;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
