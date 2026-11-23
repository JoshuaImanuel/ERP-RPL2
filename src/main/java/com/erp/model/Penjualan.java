package com.erp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "penjualan")
public class Penjualan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "no_faktur", length = 40)
    private String noFaktur;

    @Column(name = "tanggal")
    private Date tanggal;

    @Column(name = "kasir", length = 100)
    private String kasir;

    @Column(name = "kode_barang", length = 30)
    private String kodeBarang;

    @Column(name = "nama_barang", length = 100)
    private String namaBarang;

    @Column(name = "jumlah")
    private int jumlah;

    @Column(name = "harga_jual")
    private double hargaJual;

    @Column(name = "subtotal")
    private double subtotal;

    @Column(name = "total_faktur")
    private double totalFaktur;

    public Penjualan() {
    }

    public Penjualan(String noFaktur, Date tanggal, String kasir, String kodeBarang,
                     String namaBarang, int jumlah, double hargaJual, double subtotal, double totalFaktur) {
        this.noFaktur = noFaktur;
        this.tanggal = tanggal;
        this.kasir = kasir;
        this.kodeBarang = kodeBarang;
        this.namaBarang = namaBarang;
        this.jumlah = jumlah;
        this.hargaJual = hargaJual;
        this.subtotal = subtotal;
        this.totalFaktur = totalFaktur;
    }

    public int getId() {
        return id;
    }

    public String getNoFaktur() {
        return noFaktur;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public String getKasir() {
        return kasir;
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

    public double getHargaJual() {
        return hargaJual;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getTotalFaktur() {
        return totalFaktur;
    }
}