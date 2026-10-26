package com.erp;

import com.erp.model.Barang;
import com.erp.model.Karyawan;
import java.util.ArrayList;
import java.util.Scanner;

public class Main{
    public static void main(String[] args){
        ArrayList<Barang> daftarBarang = new ArrayList<>();
        ArrayList<Karyawan> daftarKaryawan = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        int pilihan = 0;
        
        while (pilihan !=3){
        System.out.println("=== Sistem ERP-MASTER DATA BARANG ===");
        System.out.println("1. Tambah Barang");
        System.out.println("2. Lihat Daftar Barang");
        System.out.println("3. Tambah Karyawan");
        System.out.println("4. Lihat Daftar Karyawan");
        System.out.println("5. Keluar");
        System.out.print("Pilih Menu (1-5): ");
        pilihan = input.nextInt();
        input.nextLine(); // membersihkan buffer
        
        switch (pilihan){
            case 1:
                System.out.print("Masukan Kode Barang : ");
                String kode = input.nextLine();
                System.out.print("Masukan nama Barang : ");
                String namaBarang = input.nextLine();
                System.out.print("Masukan Jumlah Stok : ");
                int stok = input.nextInt();
                
                Barang barangBaru = new Barang(kode, namaBarang, stok);
                daftarBarang.add(barangBaru);
                System.out.println("-> Barang berhasil ditambahkan!");
                break;
                
            case 2:
                System.out.println("\n-- DAFTAR STOK BARANG ---");
                if (daftarBarang.isEmpty()){
                    System.out.println("Belum ada data barang.");
                } else {
                    for (Barang b : daftarBarang) {
                        b.tampilkanInfo();
                    }
                }
                break;
            case 3:
                System.out.print("Masukan NIP Karyawan : ");
                String nip = input.nextLine();
                System.out.print("Masukan Nama Karyawan : ");
                String namaKaryawan = input.nextLine();
                System.out.print("Masukan Jabatan : ");
                String jabatan = input.nextLine();
                
                daftarKaryawan.add(new Karyawan(nip, namaKaryawan, jabatan));
                System.out.println("-> Karyawan berhasil ditambahkan!");
                break;
            case 4:
                System.out.println("\n--- Daftar Karyawan ---");
                if (daftarKaryawan.isEmpty()){
                    System.out.println("Belum ada data karyawan.");
                }else{
                    for (Karyawan k : daftarKaryawan){
                        k.tampilkanInfo();
                    }
                }
                break;
            case 5:
                System.out.println("Keluar dari Program. Terimakasih!");
                break;
                
            default:
                System.out.println("Pilihan tidak valid!");
        }
         
    }
    input.close();    
    }
    
}