package com.erp.model;

import jakarta.persistance.Entity;
import jakarta.persistance.Table;
import jakarta.persistance.Id;
import jakarta.persistance.Column;

@Entity
@Table(name = "karyawan")
public class Karyawan {
    
    @Id
    @Column(name = "nip")
    private String nip;
    
    @Column(name = "nama")
    private String nama;
    
    @Column(name = "jabatan")
    private String jabatan;
}    
    public Karyawan(String nip, String nama, String jabatan){
        this.nip = nip;
        this.nama = nama;
        this.jabatan = jabatan;
    }
    public String getNip(){ 
        return nip;
    }
    public void setNip(String nip){
        this.nip = nip;
    }
    public String getNama() 
    {
        return nama;
    }
    public void setNama(String nama){
        this.nama = nama;
    }
    public String getJabatan() 
    {
        return jabatan;
    }
    public void setJabatan(String nama){
        this.jabatan = jabatan;
    }
    public void tampilkanInfo(){
        System.out.println("NIP: " + nip + " | Nama: " + nama + " | Jabatan: " + jabatan);
    }
}
