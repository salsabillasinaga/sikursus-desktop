
package view;

import dao.UserDAO;
import model.User;
import util.Theme;
import util.UserSession;

import java.awt.*;
import javax.swing.*;

public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JCheckBox cbTampilkanPassword;
    private JButton btnLogin;

    public LoginFrame() {

        setTitle("Login - SIKURSUS");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {

        // PANEL UTAMA
        JPanel panelUtama = new JPanel(new BorderLayout());
        panelUtama.setBackground(Theme.CREAM);

        // PANEL KIRI
        JPanel panelKiri = new JPanel();
        panelKiri.setBackground(Theme.DARK_BROWN);
        panelKiri.setPreferredSize(new Dimension(330, 550));
        panelKiri.setLayout(new GridBagLayout());

        JPanel panelBrand = new JPanel();
        panelBrand.setOpaque(false);
        panelBrand.setLayout(new BoxLayout(
                panelBrand, BoxLayout.Y_AXIS
        ));

        JLabel lblNama = new JLabel("SIKURSUS");
        lblNama.setFont(new Font("Serif", Font.BOLD, 38));
        lblNama.setForeground(Color.WHITE);
        lblNama.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblGaris = new JLabel(
                "________________________"
        );
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

        panelBrand.add(lblNama);
        panelBrand.add(Box.createVerticalStrut(10));
        panelBrand.add(lblGaris);
        panelBrand.add(Box.createVerticalStrut(10));
        panelBrand.add(lblDeskripsi);

        panelKiri.add(panelBrand);

        // PANEL KANAN
        JPanel panelKanan = new JPanel(new GridBagLayout());
        panelKanan.setBackground(Theme.CREAM);

        JPanel panelForm = new JPanel();
        panelForm.setBackground(Theme.IVORY);
        panelForm.setLayout(new GridBagLayout());

        panelForm.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                Theme.BORDER, 1
                        ),
                        BorderFactory.createEmptyBorder(
                                35, 40, 35, 40
                        )
                )
        );

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(7, 0, 7, 0);

        // JUDUL LOGIN
        JLabel lblJudul = new JLabel("Selamat Datang");
        lblJudul.setFont(
                new Font("Serif", Font.BOLD, 27)
        );
        lblJudul.setForeground(Theme.DARK_BROWN);
        lblJudul.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        gbc.gridy = 0;
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

        // USERNAME
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(Theme.font(Font.BOLD, 13));
        lblUsername.setForeground(Theme.TEXT);

        gbc.gridy = 2;
        gbc.insets = new Insets(7, 0, 5, 0);
        panelForm.add(lblUsername, gbc);

        txtUsername = new JTextField();
        Theme.styleTextField(txtUsername);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 15, 0);
        panelForm.add(txtUsername, gbc);

        // PASSWORD
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(Theme.font(Font.BOLD, 13));
        lblPassword.setForeground(Theme.TEXT);

        gbc.gridy = 4;
        gbc.insets = new Insets(7, 0, 5, 0);
        panelForm.add(lblPassword, gbc);

        txtPassword = new JPasswordField();
        Theme.stylePasswordField(txtPassword);

        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 5, 0);
        panelForm.add(txtPassword, gbc);

        // CHECKBOX TAMPILKAN PASSWORD
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
        cbTampilkanPassword.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        // Simpan karakter password asli
        char echoChar = txtPassword.getEchoChar();

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

        // TOMBOL LOGIN
        btnLogin = Theme.createButton(
                "LOGIN",
                Theme.BTN_SAVE
        );

        gbc.gridy = 7;
        gbc.insets = new Insets(5, 0, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelForm.add(btnLogin, gbc);

        // AKSI LOGIN
        btnLogin.addActionListener(e -> prosesLogin());

        // ENTER UNTUK LOGIN
        txtPassword.addActionListener(e -> prosesLogin());

        // MASUKKAN FORM KE PANEL KANAN
        panelKanan.add(panelForm);

        // GABUNGKAN PANEL
        panelUtama.add(panelKiri, BorderLayout.WEST);
        panelUtama.add(panelKanan, BorderLayout.CENTER);

        setContentPane(panelUtama);
    }

    // ==================================================
    // PROSES LOGIN
    // ==================================================

    private void prosesLogin() {

        String username = txtUsername.getText().trim();
        char[] passwordChars = txtPassword.getPassword();
        String password = new String(passwordChars);

        if (username.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Username dan password harus diisi!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            UserDAO userDAO = new UserDAO();

            User user = userDAO.login(
                    username,
                    password
            );

            if (user != null) {

                UserSession.setUser(user);

                JOptionPane.showMessageDialog(
                        this,
                        "Login berhasil. Selamat datang!",
                        "Informasi",
                        JOptionPane.INFORMATION_MESSAGE
                );

                DashboardFrame dashboard =
                        new DashboardFrame();

                dashboard.setVisible(true);

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Username atau password salah!",
                        "Login Gagal",
                        JOptionPane.ERROR_MESSAGE
                );

                txtPassword.setText("");
                txtUsername.requestFocus();
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Terjadi kesalahan saat login:\n"
                    + ex.getMessage(),
                    "Kesalahan",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
