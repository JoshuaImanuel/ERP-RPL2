package com.erp;

import com.erp.model.Barang;
import com.erp.model.HibernateUtil;
import com.erp.model.JasperUtil;
import com.erp.model.Karyawan;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class Main extends JFrame {

    private JTextField txtNip;
    private JTextField txtNamaKaryawan;
    private JTextField txtJabatan;
    private DefaultTableModel modelTabelKaryawan;

    private JTextField txtKodeBarang;
    private JTextField txtNamaBarang;
    private JTextField txtStokBarang;
    private DefaultTableModel modelTabelBarang;

    public Main() {
        setTitle("Sistem Informasi ERP-RPL2 - Integrasi JasperReports");
        setSize(860, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                HibernateUtil.shutdown();
                dispose();
                System.exit(0);
            }
        });

        JPanel panelUtama = new JPanel(new BorderLayout(10, 10));
        panelUtama.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel lblJudul = new JLabel("APLIKASI MANAJEMEN DATA & PELAPORAN", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblJudul.setBorder(new EmptyBorder(5, 0, 10, 0));
        panelUtama.add(lblJudul, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabbedPane.addTab("Data & Laporan Karyawan", buatPanelKaryawan());
        tabbedPane.addTab("Data & Laporan Barang", buatPanelBarang());

        panelUtama.add(tabbedPane, BorderLayout.CENTER);
        setContentPane(panelUtama);

        muatDataTabel("karyawan", modelTabelKaryawan);
        muatDataTabel("barang", modelTabelBarang);
    }

    private JPanel buatPanelKaryawan() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel panelForm = new JPanel(new GridLayout(3, 2, 10, 8));
        panelForm.setBorder(BorderFactory.createTitledBorder("Input Data Karyawan"));

        panelForm.add(new JLabel("Nomor Induk Pegawai (NIP):"));
        txtNip = new JTextField();
        panelForm.add(txtNip);

        panelForm.add(new JLabel("Nama Lengkap Karyawan:"));
        txtNamaKaryawan = new JTextField();
        panelForm.add(txtNamaKaryawan);

        panelForm.add(new JLabel("Jabatan:"));
        txtJabatan = new JTextField();
        panelForm.add(txtJabatan);

        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        JButton btnSimpan = new JButton("Simpan Karyawan");
        JButton btnSegarkan = new JButton("Muat Ulang Data");
        JButton btnCetakJasper = new JButton("Cetak Laporan Karyawan (Jasper)");
        JButton btnEksporPdf = new JButton("Ekspor Laporan ke PDF");

        btnCetakJasper.setBackground(new Color(33, 115, 70));
        btnCetakJasper.setForeground(Color.WHITE);

        btnSimpan.addActionListener(e -> simpanKaryawan());
        btnSegarkan.addActionListener(e -> muatDataTabel("karyawan", modelTabelKaryawan));
        btnCetakJasper.addActionListener(e -> prosesCetakLaporan("src/main/resources/laporan_karyawan.jrxml"));
        btnEksporPdf.addActionListener(e -> prosesEksporPdf("src/main/resources/laporan_karyawan.jrxml", "Laporan_Karyawan.pdf"));

        panelTombol.add(btnSimpan);
        panelTombol.add(btnSegarkan);
        panelTombol.add(btnCetakJasper);
        panelTombol.add(btnEksporPdf);

        JPanel panelAtas = new JPanel(new BorderLayout());
        panelAtas.add(panelForm, BorderLayout.CENTER);
        panelAtas.add(panelTombol, BorderLayout.SOUTH);

        modelTabelKaryawan = new DefaultTableModel();
        JTable tabelKaryawan = new JTable(modelTabelKaryawan);
        tabelKaryawan.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(tabelKaryawan);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Karyawan Tersimpan (MySQL)"));

        panel.add(panelAtas, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buatPanelBarang() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel panelForm = new JPanel(new GridLayout(3, 2, 10, 8));
        panelForm.setBorder(BorderFactory.createTitledBorder("Input Data Barang"));

        panelForm.add(new JLabel("Kode Barang:"));
        txtKodeBarang = new JTextField();
        panelForm.add(txtKodeBarang);

        panelForm.add(new JLabel("Nama Barang:"));
        txtNamaBarang = new JTextField();
        panelForm.add(txtNamaBarang);

        panelForm.add(new JLabel("Jumlah Stok:"));
        txtStokBarang = new JTextField();
        panelForm.add(txtStokBarang);

        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        JButton btnSimpan = new JButton("Simpan Barang");
        JButton btnSegarkan = new JButton("Muat Ulang Data");
        JButton btnCetakJasper = new JButton("Cetak Laporan Barang (Jasper)");
        JButton btnEksporPdf = new JButton("Ekspor Laporan ke PDF");

        btnCetakJasper.setBackground(new Color(33, 115, 70));
        btnCetakJasper.setForeground(Color.WHITE);

        btnSimpan.addActionListener(e -> simpanBarang());
        btnSegarkan.addActionListener(e -> muatDataTabel("barang", modelTabelBarang));
        btnCetakJasper.addActionListener(e -> prosesCetakLaporan("src/main/resources/laporan_barang.jrxml"));
        btnEksporPdf.addActionListener(e -> prosesEksporPdf("src/main/resources/laporan_barang.jrxml", "Laporan_Barang.pdf"));

        panelTombol.add(btnSimpan);
        panelTombol.add(btnSegarkan);
        panelTombol.add(btnCetakJasper);
        panelTombol.add(btnEksporPdf);

        JPanel panelAtas = new JPanel(new BorderLayout());
        panelAtas.add(panelForm, BorderLayout.CENTER);
        panelAtas.add(panelTombol, BorderLayout.SOUTH);

        modelTabelBarang = new DefaultTableModel();
        JTable tabelBarang = new JTable(modelTabelBarang);
        tabelBarang.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(tabelBarang);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Barang Tersimpan (MySQL)"));

        panel.add(panelAtas, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void simpanKaryawan() {
        String nip = txtNip.getText().trim();
        String nama = txtNamaKaryawan.getText().trim();
        String jabatan = txtJabatan.getText().trim();

        if (nip.isEmpty() || nama.isEmpty() || jabatan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seluruh kolom data karyawan wajib diisi.",
                    "Validasi Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Karyawan karyawan = new Karyawan(nip, nama, jabatan);
            session.persist(karyawan);
            transaction.commit();

            txtNip.setText("");
            txtNamaKaryawan.setText("");
            txtJabatan.setText("");
            muatDataTabel("karyawan", modelTabelKaryawan);
            JOptionPane.showMessageDialog(this, "Data karyawan berhasil disimpan ke basis data.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data karyawan: " + e.getMessage(),
                    "Kesalahan Simpan", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void simpanBarang() {
        String kode = txtKodeBarang.getText().trim();
        String nama = txtNamaBarang.getText().trim();
        String stokTeks = txtStokBarang.getText().trim();

        if (kode.isEmpty() || nama.isEmpty() || stokTeks.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seluruh kolom data barang wajib diisi.",
                    "Validasi Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int stok = Integer.parseInt(stokTeks);
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();
                Barang barang = new Barang(kode, nama, stok);
                session.persist(barang);
                transaction.commit();

                txtKodeBarang.setText("");
                txtNamaBarang.setText("");
                txtStokBarang.setText("");
                muatDataTabel("barang", modelTabelBarang);
                JOptionPane.showMessageDialog(this, "Data barang berhasil disimpan ke basis data.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Kolom jumlah stok harus berupa angka bulat.",
                    "Validasi Input", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data barang: " + e.getMessage(),
                    "Kesalahan Simpan", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void muatDataTabel(String namaTabel, DefaultTableModel model) {
        model.setRowCount(0);
        model.setColumnCount(0);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.doWork(connection -> {
                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT * FROM " + namaTabel)) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int jumlahKolom = metaData.getColumnCount();

                    for (int i = 1; i <= jumlahKolom; i++) {
                        model.addColumn(metaData.getColumnLabel(i).toUpperCase());
                    }

                    while (rs.next()) {
                        Object[] baris = new Object[jumlahKolom];
                        for (int i = 1; i <= jumlahKolom; i++) {
                            baris[i - 1] = rs.getObject(i);
                        }
                        model.addRow(baris);
                    }
                }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data tabel " + namaTabel + ": " + e.getMessage(),
                    "Kesalahan Basis Data", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void prosesCetakLaporan(String pathJrxml) {
        File berkasLaporan = new File(pathJrxml);
        if (!berkasLaporan.exists()) {
            JOptionPane.showMessageDialog(this,
                    "Berkas templat laporan belum ditemukan pada jalur:\n" + berkasLaporan.getAbsolutePath()
                    + "\n\nSilakan simpan berkas desain .jrxml dari iReport ke dalam folder src/main/resources/.",
                    "Templat Laporan Tidak Ditemukan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JasperUtil.tampilkanLaporan(pathJrxml);
    }

    private void prosesEksporPdf(String pathJrxml, String namaFileOutput) {
        File berkasLaporan = new File(pathJrxml);
        if (!berkasLaporan.exists()) {
            JOptionPane.showMessageDialog(this,
                    "Berkas templat laporan belum ditemukan pada jalur:\n" + berkasLaporan.getAbsolutePath(),
                    "Templat Laporan Tidak Ditemukan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JasperUtil.eksporKePdf(pathJrxml, namaFileOutput);
        JOptionPane.showMessageDialog(this,
                "Laporan berhasil diekspor menjadi berkas:\n" + new File(namaFileOutput).getAbsolutePath(),
                "Ekspor PDF Berhasil", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            Main aplikasi = new Main();
            aplikasi.setVisible(true);
        });
    }
}