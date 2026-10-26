package com.erp.model;

public class Karyawan {
    private String nip;
    private String nama;
    private String jabatan;
    
    public Karyawan(String nip, String nama, String jabatan){
        this.nip = nip;
        this.nama = nama;
        this.jabatan = jabatan;
    }
    
    public String getNip() {return nip;}
    public String getNama() {return nama;}
    public String getJabatan() {return jabatan;}
    
    public void tampilkanInfo(){
        System.out.println("NIP: " + nip + " | Nama: " + nama + " | Jabatan: " + jabatan);
        }
}
