package com.erp.model;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Session;

public class JasperUtil {

    public static void tampilkanLaporan(String pathJrxml) {
        tampilkanLaporan(pathJrxml, new HashMap<>());
    }

    public static void tampilkanLaporan(String pathJrxml, Map<String, Object> parameters) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.doWork(connection -> {
                try {
                    File fileJrxml = new File(pathJrxml);
                    if (!fileJrxml.exists()) {
                        System.err.println("File laporan tidak ditemukan: " + fileJrxml.getAbsolutePath());
                        return;
                    }

                    JasperReport jasperReport = JasperCompileManager.compileReport(fileJrxml.getAbsolutePath());
                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

                    JasperViewer viewer = new JasperViewer(jasperPrint, false);
                    viewer.setTitle("Laporan Sistem ERP-RPL2");
                    viewer.setVisible(true);
                } catch (Exception e) {
                    System.err.println("Gagal memproses laporan JasperReports: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            System.err.println("Gagal membuka koneksi database untuk laporan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void eksporKePdf(String pathJrxml, String pathOutputPdf) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.doWork(connection -> {
                try {
                    JasperReport jasperReport = JasperCompileManager.compileReport(pathJrxml);
                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), connection);
                    JasperExportManager.exportReportToPdfFile(jasperPrint, pathOutputPdf);
                    System.out.println("Laporan berhasil diekspor ke PDF: " + pathOutputPdf);
                } catch (Exception e) {
                    System.err.println("Gagal mengekspor laporan ke PDF: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        }
    }
}