package com.erp;

import com.erp.model.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends JFrame {

    private final User userAktif;

    // 1. Komponen Manajemen Gudang
    private final JTextField txtKode = new JTextField();
    private final JTextField txtNama = new JTextField();
    private final JComboBox<String> cmbKategori = new JComboBox<>(new String[]{"Elektronik", "ATK", "Bahan Baku", "Konsumsi", "Umum"});
    private final JTextField txtHarga = new JTextField("0");
    private final JTextField txtStok = new JTextField("0");
    private final JTextField txtCari = new JTextField(15);
    private final DefaultTableModel modelBarang = new DefaultTableModel(new String[]{"Kode", "Nama Barang", "Kategori", "Harga", "Stok"}, 0);

    // 2. Komponen Modul Pembelian
    private final JComboBox<String> cmbPemasok = new JComboBox<>(new String[]{"PT Sumber Makmur", "CV Mitra Logistik", "PT Nusantara Distribusi"});
    private final JComboBox<Barang> cmbBarangBeli = new JComboBox<>();
    private final JTextField txtJumlahBeli = new JTextField("1");
    private final JTextField txtHargaBeli = new JTextField("0");
    private final JLabel lblTotalBeli = new JLabel("Rp 0");
    private final DefaultTableModel modelBeli = new DefaultTableModel(new String[]{"No Faktur", "Tanggal", "Pemasok", "Barang", "Jumlah", "Total Nilai"}, 0);

    // 3. Komponen Modul Penjualan (Kasir)
    private final JComboBox<Barang> cmbBarangJual = new JComboBox<>();
    private final JTextField txtJumlahJual = new JTextField("1", 5);
    private final DefaultTableModel modelKeranjang = new DefaultTableModel(new String[]{"Kode", "Nama Barang", "Harga", "Jumlah", "Subtotal"}, 0);
    private final JLabel lblTotalJual = new JLabel("Total Faktur: Rp 0");

    // 4. Komponen Pelaporan Terpusat
    private final JTextField txtTglAwal = new JTextField(LocalDate.now().withDayOfMonth(1).toString(), 10);
    private final JTextField txtTglAkhir = new JTextField(LocalDate.now().toString(), 10);
    private final JTextArea areaLaporan = new JTextArea();

    public Main() {
        this(new User("admin", "123", "Admin Gudang", "ADMIN_GUDANG"));
    }

    public Main(User userAktif) {
        this.userAktif = userAktif;
        setTitle("Aplikasi Inventaris & Penjualan - " + userAktif.getNamaLengkap());
        setSize(920, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Baris Atas: Judul & Logout
        JPanel header = new JPanel(new BorderLayout());
        JLabel judul = new JLabel("APLIKASI INVENTARIS & PENJUALAN");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JPanel kanan = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        kanan.add(new JLabel("User: " + userAktif.getNamaLengkap() + " (" + userAktif.getRole() + ")"));
        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginForm().setVisible(true);
        });
        kanan.add(btnLogout);

        header.add(judul, BorderLayout.WEST);
        header.add(kanan, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        // Pembagian Hak Akses Berdasarkan Peran Admin Gudang dan Kasir
        JTabbedPane tabs = new JTabbedPane();
        if ("ADMIN_GUDANG".equalsIgnoreCase(userAktif.getRole())) {
            tabs.addTab("Manajemen Gudang", panelGudang());
            tabs.addTab("Modul Pembelian", panelPembelian());
            tabs.addTab("Modul Penjualan (Kasir)", panelPenjualan());
            tabs.addTab("Pelaporan Terpusat", panelLaporan());
        } else {
            tabs.addTab("Modul Penjualan (Kasir)", panelPenjualan());
            tabs.addTab("Pelaporan Terpusat", panelLaporan());
        }

        root.add(tabs, BorderLayout.CENTER);
        setContentPane(root);
        muatSemuaData();
    }

    // ==================== 1. PANEL MANAJEMEN GUDANG ====================
    private JPanel panelGudang() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 5));
        form.setBorder(BorderFactory.createTitledBorder("Data Barang"));
        form.add(new JLabel("Kode Barang:")); form.add(txtKode);
        form.add(new JLabel("Nama Barang:")); form.add(txtNama);
        form.add(new JLabel("Kategori:")); form.add(cmbKategori);
        form.add(new JLabel("Harga Jual (Rp):")); form.add(txtHarga);
        form.add(new JLabel("Jumlah Stok:")); form.add(txtStok);

        JPanel aksi = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnTambah = new JButton("Tambah");
        JButton btnUbah = new JButton("Ubah");
        JButton btnHapus = new JButton("Hapus");
        JButton btnReset = new JButton("Reset");

        btnTambah.addActionListener(e -> simpanBarang(true));
        btnUbah.addActionListener(e -> simpanBarang(false));
        btnHapus.addActionListener(e -> hapusBarang());
        btnReset.addActionListener(e -> resetFormBarang());

        aksi.add(btnTambah);
        aksi.add(btnUbah);
        aksi.add(btnHapus);
        aksi.add(btnReset);
        aksi.add(new JLabel("   Cari Cepat:"));
        aksi.add(txtCari);

        txtCari.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                muatDataBarang(txtCari.getText().trim());
            }
        });

        JPanel atas = new JPanel(new BorderLayout());
        atas.add(form, BorderLayout.CENTER);
        atas.add(aksi, BorderLayout.SOUTH);

        JTable tabel = new JTable(modelBarang);
        tabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int r = tabel.getSelectedRow();
                if (r >= 0) {
                    txtKode.setText(modelBarang.getValueAt(r, 0).toString());
                    txtKode.setEditable(false);
                    txtNama.setText(modelBarang.getValueAt(r, 1).toString());
                    cmbKategori.setSelectedItem(modelBarang.getValueAt(r, 2).toString());
                    txtHarga.setText(modelBarang.getValueAt(r, 3).toString());
                    txtStok.setText(modelBarang.getValueAt(r, 4).toString());
                }
            }
        });

        panel.add(atas, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabel), BorderLayout.CENTER);
        return panel;
    }

    // ==================== 2. PANEL MODUL PEMBELIAN ====================
    private JPanel panelPembelian() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        JPanel form = new JPanel(new GridLayout(5, 2, 8, 5));
        form.setBorder(BorderFactory.createTitledBorder("Entri Pembelian ke Pemasok (Auto Tambah Stok)"));
        cmbPemasok.setEditable(true);
        form.add(new JLabel("Pemasok:")); form.add(cmbPemasok);
        form.add(new JLabel("Pilih Barang:")); form.add(cmbBarangBeli);
        form.add(new JLabel("Jumlah Beli:")); form.add(txtJumlahBeli);
        form.add(new JLabel("Harga Beli Satuan:")); form.add(txtHargaBeli);
        form.add(new JLabel("Total Nilai Pembelian:")); form.add(lblTotalBeli);

        KeyAdapter hitungTotal = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    int qty = Integer.parseInt(txtJumlahBeli.getText().trim());
                    double hrg = Double.parseDouble(txtHargaBeli.getText().trim());
                    lblTotalBeli.setText(String.format("Rp %,.0f", qty * hrg));
                } catch (Exception ex) {
                    lblTotalBeli.setText("Rp 0");
                }
            }
        };
        txtJumlahBeli.addKeyListener(hitungTotal);
        txtHargaBeli.addKeyListener(hitungTotal);

        JButton btnSimpanBeli = new JButton("Simpan Pembelian & Tambah Stok");
        btnSimpanBeli.addActionListener(e -> prosesPembelian());

        JPanel atas = new JPanel(new BorderLayout());
        atas.add(form, BorderLayout.CENTER);
        atas.add(btnSimpanBeli, BorderLayout.SOUTH);

        panel.add(atas, BorderLayout.NORTH);
        panel.add(new JScrollPane(new JTable(modelBeli)), BorderLayout.CENTER);
        return panel;
    }

    // ==================== 3. PANEL MODUL PENJUALAN (KASIR) ====================
    private JPanel panelPenjualan() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        JPanel atas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        atas.setBorder(BorderFactory.createTitledBorder("Kasir Penjualan (Auto Kurang Stok)"));
        atas.add(new JLabel("Barang:"));
        atas.add(cmbBarangJual);
        atas.add(new JLabel("Jumlah:"));
        atas.add(txtJumlahJual);

        JButton btnMasukKeranjang = new JButton("Tambah ke Keranjang");
        JButton btnHapusKeranjang = new JButton("Hapus Baris");
        JTable tabelKeranjang = new JTable(modelKeranjang);

        btnMasukKeranjang.addActionListener(e -> tambahKeranjang());
        btnHapusKeranjang.addActionListener(e -> {
            int r = tabelKeranjang.getSelectedRow();
            if (r >= 0) {
                modelKeranjang.removeRow(r);
                hitungTotalKeranjang();
            }
        });

        atas.add(btnMasukKeranjang);
        atas.add(btnHapusKeranjang);

        JPanel bawah = new JPanel(new BorderLayout());
        lblTotalJual.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JButton btnCetakFaktur = new JButton("Proses Transaksi & Cetak Faktur");
        btnCetakFaktur.addActionListener(e -> prosesPenjualan());

        bawah.add(lblTotalJual, BorderLayout.WEST);
        bawah.add(btnCetakFaktur, BorderLayout.EAST);

        panel.add(atas, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabelKeranjang), BorderLayout.CENTER);
        panel.add(bawah, BorderLayout.SOUTH);
        return panel;
    }

    // ==================== 4. PANEL PELAPORAN TERPUSAT ====================
    private JPanel panelLaporan() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        JPanel filter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filter.setBorder(BorderFactory.createTitledBorder("Filter Rentang Tanggal (YYYY-MM-DD)"));
        filter.add(new JLabel("Dari:"));
        filter.add(txtTglAwal);
        filter.add(new JLabel("Sampai:"));
        filter.add(txtTglAkhir);

        JButton btnTampil = new JButton("Tampilkan Ringkasan");
        JButton btnCetakBarang = new JButton("Cetak Stok Barang");
        JButton btnCetakBeli = new JButton("Cetak Pembelian");
        JButton btnCetakJual = new JButton("Cetak Penjualan");

        btnTampil.addActionListener(e -> tampilkanRingkasan());
        btnCetakBarang.addActionListener(e -> cetakJasper("src/main/resources/laporan_barang.jrxml", false));
        btnCetakBeli.addActionListener(e -> cetakJasper("src/main/resources/laporan_pembelian.jrxml", true));
        btnCetakJual.addActionListener(e -> cetakJasper("src/main/resources/laporan_penjualan.jrxml", true));

        filter.add(btnTampil);
        filter.add(btnCetakBarang);
        filter.add(btnCetakBeli);
        filter.add(btnCetakJual);

        areaLaporan.setEditable(false);
        areaLaporan.setFont(new Font("Consolas", Font.PLAIN, 13));

        panel.add(filter, BorderLayout.NORTH);
        panel.add(new JScrollPane(areaLaporan), BorderLayout.CENTER);
        return panel;
    }

    // ==================== LOGIKA DATABASE & TRANSAKSI ====================
    private void muatSemuaData() {
        muatDataBarang("");
        muatRiwayatPembelian();
        tampilkanRingkasan();
    }

    private void muatDataBarang(String cari) {
        modelBarang.setRowCount(0);
        cmbBarangBeli.removeAllItems();
        cmbBarangJual.removeAllItems();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Barang> list = cari.isEmpty()
                    ? session.createQuery("from Barang", Barang.class).list()
                    : session.createQuery("from Barang where lower(kode) like :c or lower(nama) like :c or lower(kategori) like :c", Barang.class)
                            .setParameter("c", "%" + cari.toLowerCase() + "%").list();

            for (Barang b : list) {
                modelBarang.addRow(new Object[]{b.getKode(), b.getNama(), b.getKategori(), String.format("%.0f", b.getHarga()), b.getStok()});
                cmbBarangBeli.addItem(b);
                cmbBarangJual.addItem(b);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat barang: " + e.getMessage());
        }
    }

    private void simpanBarang(boolean baru) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            String kode = txtKode.getText().trim();
            String nama = txtNama.getText().trim();
            String kat = cmbKategori.getSelectedItem().toString();
            double harga = Double.parseDouble(txtHarga.getText().trim());
            int stok = Integer.parseInt(txtStok.getText().trim());

            if (baru) {
                session.persist(new Barang(kode, nama, kat, harga, stok));
            } else {
                Barang b = session.get(Barang.class, kode);
                if (b != null) {
                    b.setNama(nama);
                    b.setKategori(kat);
                    b.setHarga(harga);
                    b.setStok(stok);
                    session.merge(b);
                }
            }
            tx.commit();
            resetFormBarang();
            muatSemuaData();
            JOptionPane.showMessageDialog(this, "Data barang berhasil disimpan.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal simpan barang: " + e.getMessage());
        }
    }

    private void hapusBarang() {
        String kode = txtKode.getText().trim();
        if (kode.isEmpty()) return;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Barang b = session.get(Barang.class, kode);
            if (b != null) session.remove(b);
            tx.commit();
            resetFormBarang();
            muatSemuaData();
        }
    }

    private void resetFormBarang() {
        txtKode.setText("");
        txtKode.setEditable(true);
        txtNama.setText("");
        txtHarga.setText("0");
        txtStok.setText("0");
    }

    private void prosesPembelian() {
        Barang b = (Barang) cmbBarangBeli.getSelectedItem();
        if (b == null) return;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            int qty = Integer.parseInt(txtJumlahBeli.getText().trim());
            double harga = Double.parseDouble(txtHargaBeli.getText().trim());
            double total = qty * harga;
            String faktur = "PO-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());

            Transaction tx = session.beginTransaction();
            session.persist(new Pembelian(faktur, Date.valueOf(LocalDate.now()), cmbPemasok.getSelectedItem().toString(),
                    b.getKode(), b.getNama(), qty, harga, total));

            Barang barangDb = session.get(Barang.class, b.getKode());
            barangDb.setStok(barangDb.getStok() + qty);
            session.merge(barangDb);
            tx.commit();

            muatSemuaData();
            JOptionPane.showMessageDialog(this, "Pembelian tersimpan & stok " + b.getNama() + " bertambah " + qty);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal simpan pembelian: " + e.getMessage());
        }
    }

    private void muatRiwayatPembelian() {
        modelBeli.setRowCount(0);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Pembelian> list = session.createQuery("from Pembelian order by id desc", Pembelian.class).list();
            for (Pembelian p : list) {
                modelBeli.addRow(new Object[]{p.getNoFakturBeli(), p.getTanggal(), p.getPemasok(), p.getNamaBarang(), p.getJumlah(), p.getTotalNilai()});
            }
        }
    }

    private void tambahKeranjang() {
        Barang b = (Barang) cmbBarangJual.getSelectedItem();
        if (b == null) return;
        int qty = Integer.parseInt(txtJumlahJual.getText().trim());
        if (qty <= 0 || qty > b.getStok()) {
            JOptionPane.showMessageDialog(this, "Stok tidak mencukupi! Sisa stok: " + b.getStok());
            return;
        }
        modelKeranjang.addRow(new Object[]{b.getKode(), b.getNama(), b.getHarga(), qty, b.getHarga() * qty});
        hitungTotalKeranjang();
    }

    private void hitungTotalKeranjang() {
        double total = 0;
        for (int i = 0; i < modelKeranjang.getRowCount(); i++) {
            total += Double.parseDouble(modelKeranjang.getValueAt(i, 4).toString());
        }
        lblTotalJual.setText(String.format("Total Faktur: Rp %,.0f", total));
    }

    private void prosesPenjualan() {
        if (modelKeranjang.getRowCount() == 0) return;
        String noFaktur = "INV-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        Date tgl = Date.valueOf(LocalDate.now());
        double totalFaktur = 0;
        for (int i = 0; i < modelKeranjang.getRowCount(); i++) {
            totalFaktur += Double.parseDouble(modelKeranjang.getValueAt(i, 4).toString());
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            for (int i = 0; i < modelKeranjang.getRowCount(); i++) {
                String kode = modelKeranjang.getValueAt(i, 0).toString();
                String nama = modelKeranjang.getValueAt(i, 1).toString();
                double harga = Double.parseDouble(modelKeranjang.getValueAt(i, 2).toString());
                int qty = Integer.parseInt(modelKeranjang.getValueAt(i, 3).toString());
                double sub = Double.parseDouble(modelKeranjang.getValueAt(i, 4).toString());

                Barang b = session.get(Barang.class, kode);
                b.setStok(b.getStok() - qty);
                session.merge(b);

                session.persist(new Penjualan(noFaktur, tgl, userAktif.getNamaLengkap(), kode, nama, qty, harga, sub, totalFaktur));
            }
            tx.commit();
        }

        modelKeranjang.setRowCount(0);
        hitungTotalKeranjang();
        muatSemuaData();

        File jrxml = new File("src/main/resources/faktur_penjualan.jrxml");
        if (jrxml.exists()) {
            Map<String, Object> param = new HashMap<>();
            param.put("no_faktur", noFaktur);
            JasperUtil.tampilkanLaporan(jrxml.getPath(), param);
        } else {
            JOptionPane.showMessageDialog(this, "Faktur " + noFaktur + " berhasil diterbitkan!\nTotal: Rp " + String.format("%,.0f", totalFaktur) + "\nStok gudang otomatis berkurang.");
        }
    }

    private void tampilkanRingkasan() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Date awal = Date.valueOf(txtTglAwal.getText().trim());
            Date akhir = Date.valueOf(txtTglAkhir.getText().trim());

            Long jmlStok = session.createQuery("select coalesce(sum(b.stok), 0) from Barang b", Long.class).uniqueResult();
            Double totBeli = session.createQuery("select coalesce(sum(p.totalNilai), 0) from Pembelian p where p.tanggal between :a and :b", Double.class)
                    .setParameter("a", awal).setParameter("b", akhir).uniqueResult();
            Double totJual = session.createQuery("select coalesce(sum(j.subtotal), 0) from Penjualan j where j.tanggal between :a and :b", Double.class)
                    .setParameter("a", awal).setParameter("b", akhir).uniqueResult();

            areaLaporan.setText(
                    "RINGKASAN OPERASIONAL & PERSEDIAAN (" + awal + " s/d " + akhir + ")\n"
                    + "-------------------------------------------------------------\n"
                    + "Total Unit Stok Gudang Saat Ini : " + jmlStok + " Unit\n"
                    + String.format("Total Pembelian Barang Masuk    : Rp %,.0f\n", totBeli)
                    + String.format("Total Penjualan Barang Keluar   : Rp %,.0f\n", totJual)
            );
        } catch (Exception e) {
            areaLaporan.setText("Pastikan format tanggal YYYY-MM-DD.");
        }
    }

    private void cetakJasper(String path, boolean pakaiTanggal) {
        File f = new File(path);
        if (!f.exists()) {
            JOptionPane.showMessageDialog(this, "File laporan belum ada di: " + f.getAbsolutePath());
            return;
        }
        Map<String, Object> param = new HashMap<>();
        if (pakaiTanggal) {
            param.put("tgl_awal", Date.valueOf(txtTglAwal.getText().trim()));
            param.put("tgl_akhir", Date.valueOf(txtTglAkhir.getText().trim()));
        }
        JasperUtil.tampilkanLaporan(path, param);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}