package com.erp;

import com.erp.model.HibernateUtil;
import com.erp.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class LoginForm extends JFrame {

    private final JTextField txtUsername = new JTextField();
    private final JPasswordField txtPassword = new JPasswordField();

    public LoginForm() {
        setTitle("Login Aplikasi Inventaris & Penjualan");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        buatAkunDefault();

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblJudul = new JLabel("LOGIN SISTEM", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(lblJudul, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(3, 2, 8, 10));
        form.add(new JLabel("Nama User:"));
        form.add(txtUsername);
        form.add(new JLabel("Kata Sandi:"));
        form.add(txtPassword);
        form.add(new JLabel("Akun Demo:"));
        form.add(new JLabel("admin / 123  |  kasir / 123"));
        panel.add(form, BorderLayout.CENTER);

        JPanel tombolPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnBatal = new JButton("Batal");
        JButton btnMasuk = new JButton("Masuk");

        btnBatal.addActionListener(e -> System.exit(0));
        btnMasuk.addActionListener(e -> prosesLogin());
        txtPassword.addActionListener(e -> prosesLogin());

        tombolPanel.add(btnBatal);
        tombolPanel.add(btnMasuk);
        panel.add(tombolPanel, BorderLayout.SOUTH);

        setContentPane(panel);
    }

    private void buatAkunDefault() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long total = session.createQuery("select count(u) from User u", Long.class).uniqueResult();
            if (total != null && total == 0) {
                Transaction tx = session.beginTransaction();
                session.persist(new User("admin", "123", "Admin Gudang", "ADMIN_GUDANG"));
                session.persist(new User("kasir", "123", "Petugas Kasir", "KASIR"));
                tx.commit();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Koneksi database gagal: " + e.getMessage());
        }
    }

    private void prosesLogin() {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<User> hasil = session.createQuery(
                    "from User where username = :u and password = :p", User.class)
                    .setParameter("u", user)
                    .setParameter("p", pass)
                    .list();

            if (!hasil.isEmpty()) {
                dispose();
                new Main(hasil.get(0)).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Username atau kata sandi salah!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error login: " + e.getMessage());
        }
    }
}