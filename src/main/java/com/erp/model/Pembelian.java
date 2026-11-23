package com.erp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "pembelian")
public class Pembelian {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "no_faktur_beli", length = 40)
    private String noFakturBeli;

    @Column(name = "tanggal")
    private Date tanggal;

    @Column(name = "pemasok", length = 100)
    private String pemasok;

    @Column(name = "kode_barang", length = 30)
    private String kodeBarang;

    @Column(name = "nama_barang", length = 100)
    private String namaBarang;

    @Column(name = "jumlah")
    private int jumlah;

    @Column(name = "harga_beli")
    private double hargaBeli;

    @Column(name = "total_nilai")
    private double totalNilai;

    public Pembelian() {
    }

    public Pembelian(String noFakturBeli, Date tanggal, String pemasok, String kodeBarang,
                     String namaBarang, int jumlah, double hargaBeli, double totalNilai) {
        this.noFakturBeli = noFakturBeli;
        this.tanggal = tanggal;
        this.pemasok = pemasok;
        this.kodeBarang = kodeBarang;
        this.namaBarang = namaBarang;
        this.jumlah = jumlah;
        this.hargaBeli = hargaBeli;
        this.totalNilai = totalNilai;
    }

    public int getId() {
        return id;
    }

    public String getNoFakturBeli() {
        return noFakturBeli;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public String getPemasok() {
        return pemasok;
    }

    public String getKodeBarang() {
        return kodeBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public int getJumlah() {
        return jumlah;
    }

    public double getHargaBeli() {
        return hargaBeli;
    }

    public double getTotalNilai() {
        return totalNilai;
    }
}