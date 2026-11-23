package com.erp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "barang")
public class Barang {

    @Id
    @Column(name = "kode", length = 30)
    private String kode;

    @Column(name = "nama", length = 100)
    private String nama;

    @Column(name = "kategori", length = 50)
    private String kategori;

    @Column(name = "harga")
    private Double harga;

    @Column(name = "stok")
    private Integer stok;

    public Barang() {
    }

    public Barang(String kode, String nama, Integer stok) {
        this.kode = kode;
        this.nama = nama;
        this.kategori = "Umum";
        this.harga = 0.0;
        this.stok = stok;
    }

    public Barang(String kode, String nama, String kategori, Double harga, Integer stok) {
        this.kode = kode;
        this.nama = nama;
        this.kategori = kategori;
        this.harga = harga;
        this.stok = stok;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKategori() {
        return kategori != null ? kategori : "Umum";
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public double getHarga() {
        return harga != null ? harga : 0.0;
    }

    public void setHarga(Double harga) {
        this.harga = harga;
    }

    public int getStok() {
        return stok != null ? stok : 0;
    }

    public void setStok(Integer stok) {
        this.stok = stok;
    }

    public void tampilkanInfo() {
        System.out.println("Kode: " + kode + " | Nama: " + nama + " | Kategori: " + getKategori() + " | Harga: " + getHarga() + " | Stok: " + getStok());
    }

    @Override
    public String toString() {
        return kode + " - " + nama + " (Stok: " + getStok() + ")";
    }
}