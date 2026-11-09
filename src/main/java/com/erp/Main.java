package com.erp;

import com.erp.model.Barang;
import com.erp.model.Karyawan;
import com.erp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean berjalan = true;

        while (berjalan) {
            System.out.println("\n=========================================");
            System.out.println("=== SISTEM MANAJEMEN DATA ===");
            System.out.println("=========================================");
            System.out.println("1. Tambah Data Karyawan");
            System.out.println("2. Tampilkan Semua Karyawan");
            System.out.println("3. Tambah Data Barang");
            System.out.println("4. Tampilkan Semua Barang");
            System.out.println("5. Keluar Aplikasi");
            System.out.print("Pilihan Anda (1-5): ");

            int pilihan = scanner.nextInt();
            scanner.nextLine();

            switch (pilihan) {
                case 1:
                    tambahKaryawan(scanner);
                    break;
                case 2:
                    tampilkanSemuaKaryawan();
                    break;
                case 3:
                    tambahBarang(scanner);
                    break;
                case 4:
                    tampilkanSemuaBarang();
                    break;
                case 5:
                    berjalan = false;
                    System.out.println("\nAplikasi selesai dijalankan.");
                    break;
                default:
                    System.out.println("\nPilihan tidak valid! Masukkan angka 1 sampai 5.");
            }
        }

        scanner.close();
        HibernateUtil.shutdown();
    }

    private static void tambahKaryawan(Scanner scanner) {
        System.out.println("\n--- INPUT DATA KARYAWAN ---");
        System.out.print("Masukkan NIP     : ");
        String nip = scanner.nextLine();
        System.out.print("Masukkan Nama    : ");
        String nama = scanner.nextLine();
        System.out.print("Masukkan Jabatan : ");
        String jabatan = scanner.nextLine();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Karyawan karyawan = new Karyawan(nip, nama, jabatan);
            session.persist(karyawan);
            transaction.commit();
            System.out.println("-> Data Karyawan berhasil disimpan ke MySQL!");
        } catch (Exception e) {
            System.err.println("-> Gagal menyimpan data karyawan: " + e.getMessage());
        }
    }

    private static void tampilkanSemuaKaryawan() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Karyawan> daftarKaryawan = session.createQuery("from Karyawan", Karyawan.class).list();
            System.out.println("\n=========================================");
            System.out.println("=== DAFTAR KARYAWAN DALAM DATABASE ===");
            System.out.println("=========================================");
            if (daftarKaryawan.isEmpty()) {
                System.out.println("Belum ada data karyawan yang tersimpan.");
            } else {
                for (Karyawan k : daftarKaryawan) {
                    k.tampilkanInfo();
                }
            }
            System.out.println("=========================================");
        } catch (Exception e) {
            System.err.println("-> Gagal mengambil data karyawan: " + e.getMessage());
        }
    }

    private static void tambahBarang(Scanner scanner) {
        System.out.println("\n--- INPUT DATA BARANG ---");
        System.out.print("Masukkan Kode Barang : ");
        String kode = scanner.nextLine();
        System.out.print("Masukkan Nama Barang : ");
        String nama = scanner.nextLine();
        System.out.print("Masukkan Jumlah Stok : ");
        int stok = scanner.nextInt();
        scanner.nextLine();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Barang barang = new Barang(kode, nama, stok);
            session.persist(barang);
            transaction.commit();
            System.out.println("-> Data Barang berhasil disimpan ke Database!");
        } catch (Exception e) {
            System.err.println("-> Gagal menyimpan data barang: " + e.getMessage());
        }
    }

    private static void tampilkanSemuaBarang() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Barang> daftarBarang = session.createQuery("from Barang", Barang.class).list();
            System.out.println("\n=========================================");
            System.out.println("=== DAFTAR BARANG DALAM DATABAS ===");
            System.out.println("=========================================");
            if (daftarBarang.isEmpty()) {
                System.out.println("Belum ada data barang yang tersimpan.");
            } else {
                for (Barang b : daftarBarang) {
                    b.tampilkanInfo();
                }
            }
            System.out.println("=========================================");
        } catch (Exception e) {
            System.err.println("-> Gagal mengambil data barang: " + e.getMessage());
        }
    }
}