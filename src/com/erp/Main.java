package com.erp;

import com.erp.model.Barang;
import java.util.ArrayList;
import java.util.Scanner;

public class Main{
    public static void main(String[] args){
        ArrayList<Barang> daftarBarang = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        int pilihan = 0;
        
        while (pilihan !=3){
        System.out.println("=== Sistem ERP-MASTER DATA BARANG ===");
        System.out.println("1. Tambah Barang");
        System.out.println("2. Lihat Daftar Barang");
        System.out.println("3. Keluar");
        System.out.print("Pilih Menu (1-3): ");
        pilihan = input.nextInt();
        input.nextLine(); // membersihkan buffer
        
        switch (pilihan){
            case 1:
                System.out.print("Masukan Kode Barang : ");
                String kode = input.nextLine();
                System.out.print("Masukan nama Barang : ");
                String nama = input.nextLine();
                System.out.print("Masukan Jumlah Stok : ");
                int stok = input.nextInt();
                
                Barang barangBaru = new Barang(kode, nama, stok);
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
                System.out.println("Keluar dari Program. Terimakasih!");
                break;
                
            default:
                System.out.println("Pilihan tidak valid!");
        }
         
    }
    input.close();    
    }
    
}