package com.erp.model;

public class Barang {
    private String kodeBarang;
    private String namaBarang;
    private int stok;
    
public Barang(String kodeBarang, String namaBarang, int stok){
    this.kodeBarang = kodeBarang;
    this.namaBarang = namaBarang;
    this.stok = stok;
    }

    public String getKodeBarang(){return kodeBarang;}
    public String getNamaBarang(){return namaBarang;}
    public int getStok(){return stok;}
    
    public void tampilkanInfo(){
        System.out.println("Kode: " + kodeBarang +" | Nama: " + namaBarang + " | Stok: " + stok);}
}
