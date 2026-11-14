package view;

import dao.UserDAO;
import java.sql.SQLException;
import model.User;
import util.Theme;
import util.UserSession;

import java.awt.*;
import java.util.Arrays;
import javax.swing.*;

public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JCheckBox cbTampilkanPassword;

    private JButton btnLogin;
    private JButton btnKeluar;

    private final UserDAO userDAO = new UserDAO();

    public LoginFrame() {

        setTitle("Login - SIKURSUS");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                keluarAplikasi();
            }
        });
    }

    private void initComponents() {

        // ==========================================
        // PANEL UTAMA
        // ==========================================

        JPanel panelUtama = new JPanel(new BorderLayout());
        panelUtama.setBackground(Theme.CREAM);

        // ==========================================
        // PANEL KIRI / BRANDING
        // ==========================================

        JPanel panelKiri = Theme.gradientPanel(
                Theme.DARK_BROWN, Theme.SIDEBAR_DARK, new GridBagLayout());
        panelKiri.setPreferredSize(new Dimension(330, 550));

        JPanel panelBrand = new JPanel();
        panelBrand.setOpaque(false);
        panelBrand.setLayout(
                new BoxLayout(panelBrand, BoxLayout.Y_AXIS)
        );

        JComponent logo = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setColor(Theme.ACCENT);
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                g2.setColor(Theme.DARK_BROWN);
                g2.fillOval(6, 6, getWidth() - 13, getHeight() - 13);
                g2.setColor(Theme.LIGHT_BROWN);
                g2.setFont(new Font("Serif", Font.BOLD, 34));
                FontMetrics fm = g2.getFontMetrics();
                String t = "S";
                g2.drawString(t,
                        (getWidth() - fm.stringWidth(t)) / 2,
                        (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
                g2.dispose();
            }
        };
        Dimension logoSize = new Dimension(76, 76);
        logo.setPreferredSize(logoSize);
        logo.setMinimumSize(logoSize);
        logo.setMaximumSize(logoSize);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblNama = new JLabel("SIKURSUS");
        lblNama.setFont(new Font("Serif", Font.BOLD, 38));
        lblNama.setForeground(Color.WHITE);
        lblNama.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblGaris = new JLabel(
                "───────────────"
        );
        lblGaris.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lblGaris.setForeground(Theme.LIGHT_BROWN);
        lblGaris.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDeskripsi = new JLabel(
                "Sistem Informasi Kursus"
        );
        lblDeskripsi.setFont(
                new Font("SansSerif", Font.PLAIN, 14)
        );
        lblDeskripsi.setForeground(Theme.CREAM);
        lblDeskripsi.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblKeterangan = new JLabel(
                "Belajar, Berkembang, dan Berkarya"
        );
        lblKeterangan.setFont(
                new Font("SansSerif", Font.ITALIC, 11)
        );
        lblKeterangan.setForeground(Theme.LIGHT_BROWN);
        lblKeterangan.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelBrand.add(logo);
        panelBrand.add(Box.createVerticalStrut(16));
        panelBrand.add(lblNama);
        panelBrand.add(Box.createVerticalStrut(12));
        panelBrand.add(lblGaris);
        panelBrand.add(Box.createVerticalStrut(12));
        panelBrand.add(lblDeskripsi);
        panelBrand.add(Box.createVerticalStrut(8));
        panelBrand.add(lblKeterangan);

        panelKiri.add(panelBrand);

        // ==========================================
        // PANEL KANAN
        // ==========================================

        JPanel panelKanan = new JPanel(new GridBagLayout());
        panelKanan.setBackground(Theme.CREAM);

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Theme.IVORY);

        panelForm.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                Theme.BORDER, 1
                        ),
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createMatteBorder(
                                        5, 0, 0, 0, Theme.ACCENT
                                ),
                                BorderFactory.createEmptyBorder(
                                        30, 40, 35, 40
                                )
                        )
                )
        );

        panelForm.setPreferredSize(new Dimension(420, 410));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(7, 0, 7, 0);

        // ==========================================
        // JUDUL
        // ==========================================

        JLabel lblJudul = new JLabel("Selamat Datang");
        lblJudul.setFont(new Font("Serif", Font.BOLD, 27));
        lblJudul.setForeground(Theme.DARK_BROWN);
        lblJudul.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 5, 0);
        panelForm.add(lblJudul, gbc);

        JLabel lblSubjudul = new JLabel(
                "Silakan login untuk melanjutkan"
        );

        lblSubjudul.setFont(
                new Font("SansSerif", Font.PLAIN, 12)
        );
        lblSubjudul.setForeground(Theme.TEXT);
        lblSubjudul.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 25, 0);
        panelForm.add(lblSubjudul, gbc);

        // ==========================================
        // USERNAME
        // ==========================================

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(Theme.font(Font.BOLD, 13));
        lblUsername.setForeground(Theme.TEXT);

        gbc.gridy = 2;
        gbc.insets = new Insets(5, 0, 5, 0);
        panelForm.add(lblUsername, gbc);

        txtUsername = new JTextField();
        Theme.styleTextField(txtUsername);
        txtUsername.setPreferredSize(new Dimension(300, 38));

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 15, 0);
        panelForm.add(txtUsername, gbc);

        // ==========================================
        // PASSWORD
        // ==========================================

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(Theme.font(Font.BOLD, 13));
        lblPassword.setForeground(Theme.TEXT);

        gbc.gridy = 4;
        gbc.insets = new Insets(5, 0, 5, 0);
        panelForm.add(lblPassword, gbc);

        txtPassword = new JPasswordField();
        Theme.stylePasswordField(txtPassword);
        txtPassword.setPreferredSize(new Dimension(300, 38));

        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 5, 0);
        panelForm.add(txtPassword, gbc);

        // ==========================================
        // CHECKBOX TAMPILKAN PASSWORD
        // ==========================================

        cbTampilkanPassword = new JCheckBox(
                "Tampilkan password"
        );

        cbTampilkanPassword.setFont(
                Theme.font(Font.PLAIN, 11)
        );

        cbTampilkanPassword.setForeground(
                Theme.MID_BROWN
        );

        cbTampilkanPassword.setBackground(
                Theme.IVORY
        );

        cbTampilkanPassword.setFocusPainted(false);
        cbTampilkanPassword.setBorderPainted(false);
        cbTampilkanPassword.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        final char echoChar = txtPassword.getEchoChar();

        cbTampilkanPassword.addActionListener(e -> {

            if (cbTampilkanPassword.isSelected()) {
                txtPassword.setEchoChar((char) 0);
            } else {
                txtPassword.setEchoChar(echoChar);
            }
        });

        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 20, 0);
        panelForm.add(cbTampilkanPassword, gbc);

        // ==========================================
        // PANEL TOMBOL
        // KEDUA TOMBOL BERADA DI TENGAH
        // ==========================================

        JPanel panelTombol = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 15, 0)
        );

        panelTombol.setOpaque(false);

        // Tombol Keluar
        btnKeluar = Theme.createButton(
                "KELUAR",
                Theme.MID_BROWN
        );

        btnKeluar.setPreferredSize(
                new Dimension(130, 40)
        );

        // Tombol Login
        btnLogin = Theme.createButton(
                "LOGIN",
                Theme.BTN_SAVE
        );

        btnLogin.setPreferredSize(
                new Dimension(130, 40)
        );

        panelTombol.add(btnKeluar);
        panelTombol.add(btnLogin);

        gbc.gridy = 7;
        gbc.insets = new Insets(5, 0, 0, 0);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        panelForm.add(panelTombol, gbc);

        // ==========================================
        // AKSI TOMBOL
        // ==========================================

        btnLogin.addActionListener(e -> prosesLogin());

        btnKeluar.addActionListener(e -> keluarAplikasi());

        txtPassword.addActionListener(e -> prosesLogin());

        txtUsername.addActionListener(e -> txtPassword.requestFocus());

        getRootPane().setDefaultButton(btnLogin);

        // ==========================================
        // GABUNGKAN PANEL
        // ==========================================

        panelKanan.add(panelForm);

        panelUtama.add(panelKiri, BorderLayout.WEST);
        panelUtama.add(panelKanan, BorderLayout.CENTER);

        setContentPane(panelUtama);
    }

    // ==============================================
    // PROSES LOGIN
    // ==============================================

    private void prosesLogin() {

        String username = txtUsername.getText().trim();
        char[] password = txtPassword.getPassword();

        if (username.isEmpty() || password.length == 0) {
            Theme.showMessage(
                    this,
                    "Username dan password wajib diisi.",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE
            );
            Arrays.fill(password, '\0');
            return;
        }

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        btnLogin.setEnabled(false);

        try {
            User user = userDAO.authenticate(username, password);

            if (user == null) {
                Theme.showMessage(
                        this,
                        "Login gagal. Periksa username, password, atau status user.",
                        "Login Gagal",
                        JOptionPane.WARNING_MESSAGE
                );
                txtPassword.setText("");
                txtPassword.requestFocus();
                return;
            }

            // Session harus di-set SEBELUM DashboardFrame dibuat.
            UserSession.setCurrentUser(user);
            dispose();

            SwingUtilities.invokeLater(() -> {
                DashboardFrame dashboard = new DashboardFrame();
                dashboard.setVisible(true);
            });

        } catch (SQLException ex) {
            Theme.showMessage(
                    this,
                    "Tidak dapat mengakses database: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        } finally {
            Arrays.fill(password, '\0');
            btnLogin.setEnabled(true);
            setCursor(Cursor.getDefaultCursor());
        }
    }

    // ==============================================
    // KELUAR APLIKASI
    // ==============================================

    private void keluarAplikasi() {

        int pilihan = JOptionPane.showConfirmDialog(
                this,
                "Apakah Anda yakin ingin keluar dari aplikasi?",
                "Konfirmasi Keluar",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (pilihan == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    // ==============================================
    // MAIN
    // ==============================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}