package com.erp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Entity
@Table(name = "barang")
public class Barang {

    @Id
    @Column(name = "kode_barang")
    private String kodeBarang;

    @Column(name = "nama_barang")
    private String namaBarang;

    @Column(name = "stok")
    private int stok;

    public Barang() {
    }

    public Barang(String kodeBarang, String namaBarang, int stok) {
        this.kodeBarang = kodeBarang;
        this.namaBarang = namaBarang;
        this.stok = stok;
    }

    public String getKodeBarang() {
        return kodeBarang;
    }

    public void setKodeBarang(String kodeBarang) {
        this.kodeBarang = kodeBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public void tampilkanInfo() {
        System.out.println("Kode: " + kodeBarang + " | Nama: " + namaBarang + " | Stok: " + stok);
    }
}