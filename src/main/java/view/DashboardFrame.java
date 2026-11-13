
package view;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.User;
import util.Theme;
import util.UserSession;

public class DashboardFrame extends JFrame {

    private JPanel sidebar;
    private JPanel mainPanel;
    private JPanel contentPanel;

    private JLabel lblUsername;
    private JLabel lblRole;
    private JLabel lblWelcome;
    private JLabel lblRoleDescription;

    private JButton btnPeserta;
    private JButton btnInstruktur;
    private JButton btnKursus;
    private JButton btnKelas;
    private JButton btnPendaftaran;
    private JButton btnPembayaran;
    private JButton btnLaporan;
    private JButton btnLogout;

    private JPopupMenu popupMenu;
    private JScrollPane scrollPane;

    private final Map<Class<? extends JFrame>, JFrame> openedWindows =
            new HashMap<Class<? extends JFrame>, JFrame>();

    private User currentUser;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public DashboardFrame() {

        currentUser = UserSession.getUser();

        if (currentUser == null) {

            JOptionPane.showMessageDialog(
                    null,
                    "Session tidak ditemukan. Silakan login kembali.",
                    "Session Berakhir",
                    JOptionPane.WARNING_MESSAGE
            );

            SwingUtilities.invokeLater(() ->
                    new LoginFrame().setVisible(true)
            );

            return;
        }

        initComponents();
        applyRoleAccess();

        setTitle("Dashboard - SIKURSUS Desktop");
        setSize(1200, 750);
        setMinimumSize(new Dimension(1000, 650));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // =====================================================
    // INISIALISASI
    // =====================================================

    private void initComponents() {

        setLayout(new BorderLayout());

        getContentPane().setBackground(Theme.CREAM);

        createPopupMenu();
        createSidebar();
        createMainPanel();

        add(sidebar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);

        // Popup klik kanan pada seluruh komponen dashboard
        attachPopupMenu(getContentPane());
    }

    // =====================================================
    // POPUP MENU
    // =====================================================

    private void createPopupMenu() {

        popupMenu = new JPopupMenu();

        popupMenu.setBackground(Theme.IVORY);

        popupMenu.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Theme.BORDER),
                        new EmptyBorder(5, 5, 5, 5)
                )
        );

        JMenuItem itemTentang = new JMenuItem("Tentang Aplikasi");
        JMenuItem itemRefresh = new JMenuItem("Refresh");

        stylePopupItem(itemTentang);
        stylePopupItem(itemRefresh);

        popupMenu.add(itemTentang);
        popupMenu.add(itemRefresh);

        itemTentang.addActionListener(e -> showAbout());

        itemRefresh.addActionListener(e -> refreshDashboard());
    }

    private void stylePopupItem(JMenuItem item) {

        item.setFont(
                new Font("SansSerif", Font.PLAIN, 12)
        );

        item.setForeground(Theme.TEXT);
        item.setBackground(Theme.IVORY);
        item.setOpaque(true);

        item.setBorder(
                new EmptyBorder(7, 12, 7, 12)
        );

        item.setPreferredSize(new Dimension(190, 30));
    }

    // Memasang popup pada seluruh komponen
    private void attachPopupMenu(Component component) {

        if (component instanceof JComponent) {

            ((JComponent) component).setComponentPopupMenu(popupMenu);
        }

        if (component instanceof Container) {

            Component[] children =
                    ((Container) component).getComponents();

            for (Component child : children) {
                attachPopupMenu(child);
            }
        }
    }

    // =====================================================
    // TENTANG APLIKASI
    // =====================================================

    private void showAbout() {

        JOptionPane.showMessageDialog(
                this,
                "<html><div style='width:300px;'>"
                + "<h2 style='color:#5C4033;'>SIKURSUS Desktop</h2>"
                + "<p><b>Sistem Informasi Kursus dan Pelatihan</b></p>"
                + "<p>Aplikasi ini digunakan untuk membantu "
                + "pengelolaan data peserta, instruktur, kursus, "
                + "kelas, pendaftaran, dan pembayaran.</p>"
                + "<hr>"
                + "<p>Versi Aplikasi: 1.0</p>"
                + "<p>Pengguna: " + currentUser.getUsername() + "</p>"
                + "<p>Role: " + currentUser.getRole() + "</p>"
                + "</div></html>",
                "Tentang Aplikasi",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =====================================================
    // REFRESH DASHBOARD
    // =====================================================

    private void refreshDashboard() {

        lblUsername.setText(
                "Pengguna: " + currentUser.getUsername()
        );

        lblRole.setText(
                "ROLE: " + currentUser.getRole()
        );

        lblWelcome.setText(
                "Selamat Datang, " + currentUser.getUsername() + "!"
        );

        applyRoleAccess();

        // Kembalikan posisi scroll ke atas
        if (scrollPane != null) {

            scrollPane.getVerticalScrollBar().setValue(0);
        }

        contentPanel.revalidate();
        contentPanel.repaint();

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // =====================================================
    // SIDEBAR
    // =====================================================

    private void createSidebar() {

        sidebar = new JPanel();

        sidebar.setPreferredSize(new Dimension(245, 0));
        sidebar.setBackground(Theme.DARK_BROWN);

        sidebar.setLayout(
                new BoxLayout(sidebar, BoxLayout.Y_AXIS)
        );

        sidebar.setBorder(
                new EmptyBorder(22, 15, 20, 15)
        );

        JLabel lblLogo = new JLabel("SIKURSUS");

        lblLogo.setFont(
                new Font("Serif", Font.BOLD, 27)
        );

        lblLogo.setForeground(Color.WHITE);
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSubtitle = new JLabel(
                "Sistem Informasi Kursus"
        );

        lblSubtitle.setFont(
                new Font("SansSerif", Font.PLAIN, 11)
        );

        lblSubtitle.setForeground(
                new Color(225, 210, 195)
        );

        lblSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(lblLogo);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(lblSubtitle);

        sidebar.add(Box.createVerticalStrut(30));

        addSidebarLabel("MENU UTAMA");

        btnPeserta = createSidebarButton("Data Peserta");
        btnInstruktur = createSidebarButton("Data Instruktur");
        btnKursus = createSidebarButton("Data Kursus");
        btnKelas = createSidebarButton("Data Kelas");

        sidebar.add(btnPeserta);
        sidebar.add(btnInstruktur);
        sidebar.add(btnKursus);
        sidebar.add(btnKelas);

        sidebar.add(Box.createVerticalStrut(20));

        addSidebarLabel("TRANSAKSI");

        btnPendaftaran = createSidebarButton("Pendaftaran");
        btnPembayaran = createSidebarButton("Pembayaran");

        sidebar.add(btnPendaftaran);
        sidebar.add(btnPembayaran);

        sidebar.add(Box.createVerticalStrut(20));

        addSidebarLabel("LAPORAN");

        btnLaporan = createSidebarButton("Laporan");

        sidebar.add(btnLaporan);

        sidebar.add(Box.createVerticalGlue());

        JSeparator separator = new JSeparator();

        separator.setForeground(
                new Color(130, 105, 88)
        );

        separator.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 1)
        );

        sidebar.add(separator);

        sidebar.add(Box.createVerticalStrut(15));

        btnLogout = createSidebarButton("Logout");

        btnLogout.setForeground(
                new Color(255, 210, 190)
        );

        sidebar.add(btnLogout);

        // Event tombol sidebar
        btnPeserta.addActionListener(e -> openPeserta());
        btnInstruktur.addActionListener(e -> openInstruktur());
        btnKursus.addActionListener(e -> openKursus());
        btnKelas.addActionListener(e -> openKelas());

        btnPendaftaran.addActionListener(e ->
                showUnderDevelopment("Modul Pendaftaran")
        );

        btnPembayaran.addActionListener(e ->
                showUnderDevelopment("Modul Pembayaran")
        );

        btnLaporan.addActionListener(e ->
                showUnderDevelopment("Modul Laporan")
        );

        btnLogout.addActionListener(e -> logout());
    }

    private void addSidebarLabel(String text) {

        JLabel label = new JLabel(text);

        label.setFont(
                new Font("SansSerif", Font.BOLD, 10)
        );

        label.setForeground(
                new Color(205, 175, 145)
        );

        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        label.setBorder(
                new EmptyBorder(0, 5, 10, 0)
        );

        sidebar.add(label);
    }

    private JButton createSidebarButton(String text) {

        JButton button = new JButton(text);

        button.setFont(
                new Font("SansSerif", Font.PLAIN, 13)
        );

        button.setForeground(Color.WHITE);
        button.setBackground(Theme.DARK_BROWN);

        button.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);

        button.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        button.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 43)
        );

        button.setPreferredSize(
                new Dimension(210, 43)
        );

        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        button.setBorder(
                new EmptyBorder(8, 12, 8, 8)
        );

        button.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {

                if (button.isEnabled()) {

                    button.setBackground(
                            new Color(115, 83, 65)
                    );

                    button.setOpaque(true);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {

                button.setOpaque(false);
            }
        });

        return button;
    }

    // =====================================================
    // PANEL UTAMA
    // =====================================================

    private void createMainPanel() {

        mainPanel = new JPanel(new BorderLayout());

        mainPanel.setBackground(Theme.CREAM);

        // HEADER
        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(Theme.IVORY);

        header.setBorder(
                new EmptyBorder(18, 28, 18, 28)
        );

        JLabel lblPageTitle = new JLabel("Dashboard");

        lblPageTitle.setFont(
                new Font("Serif", Font.BOLD, 25)
        );

        lblPageTitle.setForeground(Theme.DARK_BROWN);

        JPanel userPanel = new JPanel(
                new GridLayout(2, 1, 0, 4)
        );

        userPanel.setOpaque(false);

        lblUsername = new JLabel(
                "Pengguna: " + currentUser.getUsername()
        );

        lblUsername.setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );

        lblUsername.setForeground(Theme.TEXT);

        lblRole = new JLabel(
                "ROLE: " + currentUser.getRole()
        );

        lblRole.setFont(
                new Font("SansSerif", Font.BOLD, 11)
        );

        lblRole.setForeground(Theme.MID_BROWN);

        userPanel.add(lblUsername);
        userPanel.add(lblRole);

        header.add(lblPageTitle, BorderLayout.WEST);
        header.add(userPanel, BorderLayout.EAST);

        mainPanel.add(header, BorderLayout.NORTH);

        // KONTEN
        contentPanel = new JPanel();

        contentPanel.setLayout(
                new BoxLayout(contentPanel, BoxLayout.Y_AXIS)
        );

        contentPanel.setBackground(Theme.CREAM);

        contentPanel.setBorder(
                new EmptyBorder(28, 28, 28, 28)
        );

        lblWelcome = new JLabel(
                "Selamat Datang, " + currentUser.getUsername() + "!"
        );

        lblWelcome.setFont(
                new Font("Serif", Font.BOLD, 25)
        );

        lblWelcome.setForeground(Theme.DARK_BROWN);

        lblWelcome.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblRoleDescription = new JLabel();

        lblRoleDescription.setFont(
                new Font("SansSerif", Font.PLAIN, 13)
        );

        lblRoleDescription.setForeground(Theme.TEXT);

        lblRoleDescription.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(lblWelcome);
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(lblRoleDescription);

        contentPanel.add(Box.createVerticalStrut(25));

        // JUDUL RINGKASAN
        JLabel lblSection = new JLabel("Ringkasan Aplikasi");

        lblSection.setFont(
                new Font("SansSerif", Font.BOLD, 16)
        );

        lblSection.setForeground(Theme.DARK_BROWN);

        lblSection.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(lblSection);

        contentPanel.add(Box.createVerticalStrut(15));

        // KARTU DASHBOARD
        JPanel cardPanel = new JPanel(
                new GridLayout(2, 2, 16, 16)
        );

        cardPanel.setOpaque(false);

        cardPanel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 230)
        );

        cardPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        cardPanel.add(createInfoCard(
                "MASTER PESERTA",
                "Pengelolaan data peserta kursus",
                "Buka Data Peserta",
                this::openPeserta
        ));

        cardPanel.add(createInfoCard(
                "MASTER INSTRUKTUR",
                "Pengelolaan data instruktur",
                "Buka Data Instruktur",
                this::openInstruktur
        ));

        cardPanel.add(createInfoCard(
                "MASTER KURSUS",
                "Pengelolaan informasi kursus",
                "Buka Data Kursus",
                this::openKursus
        ));

        cardPanel.add(createInfoCard(
                "MASTER KELAS",
                "Pengelolaan kelas pelatihan",
                "Buka Data Kelas",
                this::openKelas
        ));

        contentPanel.add(cardPanel);

        contentPanel.add(Box.createVerticalStrut(25));

        // INFORMASI HAK AKSES
        JPanel roleInfo = new JPanel(
                new BorderLayout(10, 8)
        );

        roleInfo.setBackground(Theme.IVORY);

        roleInfo.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Theme.BORDER),
                        new EmptyBorder(18, 20, 18, 20)
                )
        );

        JLabel lblInfoTitle = new JLabel(
                "Informasi Hak Akses"
        );

        lblInfoTitle.setFont(
                new Font("SansSerif", Font.BOLD, 14)
        );

        lblInfoTitle.setForeground(Theme.DARK_BROWN);

        JLabel lblInfoText = new JLabel();

        lblInfoText.setFont(
                new Font("SansSerif", Font.PLAIN, 12)
        );

        lblInfoText.setForeground(Theme.TEXT);

        if (currentUser.isAdmin()) {

            lblInfoText.setText(
                    "<html>Anda login sebagai ADMIN. "
                    + "Seluruh menu Master, Transaksi, dan Laporan "
                    + "dapat digunakan.</html>"
            );

        } else {

            lblInfoText.setText(
                    "<html>Anda login sebagai OPERATOR. "
                    + "Anda dapat mengakses Peserta, Transaksi, dan Laporan. "
                    + "Menu Instruktur, Kursus, dan Kelas hanya tersedia "
                    + "untuk ADMIN.</html>"
            );
        }

        roleInfo.add(lblInfoTitle, BorderLayout.NORTH);
        roleInfo.add(lblInfoText, BorderLayout.CENTER);

        roleInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(roleInfo);

        scrollPane = new JScrollPane(contentPanel);

        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Theme.CREAM);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // FOOTER
        JLabel footer = new JLabel(
                "SIKURSUS Desktop  |  Sistem Informasi Manajemen Kursus & Pelatihan"
        );

        footer.setFont(
                new Font("SansSerif", Font.PLAIN, 10)
        );

        footer.setForeground(
                new Color(130, 110, 95)
        );

        footer.setBorder(
                new EmptyBorder(10, 28, 12, 28)
        );

        mainPanel.add(footer, BorderLayout.SOUTH);
    }

    // =====================================================
    // KARTU INFORMASI
    // =====================================================

    private JPanel createInfoCard(
            String title,
            String description,
            String buttonText,
            Runnable action) {

        JPanel card = new JPanel(
                new BorderLayout(0, 10)
        );

        card.setBackground(Theme.IVORY);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Theme.BORDER),
                        new EmptyBorder(18, 18, 18, 18)
                )
        );

        JLabel lblTitle = new JLabel(title);

        lblTitle.setFont(
                new Font("SansSerif", Font.BOLD, 13)
        );

        lblTitle.setForeground(Theme.DARK_BROWN);

        JLabel lblDescription = new JLabel(
                "<html>" + description + "</html>"
        );

        lblDescription.setFont(
                new Font("SansSerif", Font.PLAIN, 11)
        );

        lblDescription.setForeground(Theme.TEXT);

        JButton button = Theme.createButton(
                buttonText,
                Theme.BTN_SAVE
        );

        button.setPreferredSize(
                new Dimension(175, 38)
        );

        button.addActionListener(e -> action.run());

        JPanel bottom = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 0, 0)
        );

        bottom.setOpaque(false);
        bottom.add(button);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblDescription, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);

        return card;
    }

    // =====================================================
    // HAK AKSES ADMIN DAN OPERATOR
    // =====================================================

    private void applyRoleAccess() {

        boolean admin = currentUser.isAdmin();

        btnPeserta.setEnabled(true);
        btnInstruktur.setEnabled(admin);
        btnKursus.setEnabled(admin);
        btnKelas.setEnabled(admin);

        btnPendaftaran.setEnabled(true);
        btnPembayaran.setEnabled(true);
        btnLaporan.setEnabled(true);
        btnLogout.setEnabled(true);

        if (admin) {

            lblRoleDescription.setText(
                    "Anda masuk sebagai Administrator. "
                    + "Anda memiliki akses penuh untuk mengelola data aplikasi."
            );

            lblRole.setForeground(
                    new Color(111, 125, 91)
            );

        } else {

            lblRoleDescription.setText(
                    "Anda masuk sebagai Operator. "
                    + "Gunakan menu yang tersedia sesuai hak akses Anda."
            );

            lblRole.setForeground(
                    new Color(166, 119, 78)
            );
        }
    }

    // =====================================================
    // MEMBUKA FORM TANPA DUPLIKASI
    // =====================================================

    private <T extends JFrame> void openWindow(
            Class<T> type,
            Supplier<T> factory) {

        JFrame frame = openedWindows.get(type);

        if (frame == null || !frame.isDisplayable()) {

            frame = factory.get();

            frame.setDefaultCloseOperation(
                    WindowConstants.DISPOSE_ON_CLOSE
            );

            openedWindows.put(type, frame);

            JFrame trackedFrame = frame;

            frame.addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosed(WindowEvent e) {

                    openedWindows.remove(type, trackedFrame);
                }
            });
        }

        if ((frame.getExtendedState() & Frame.ICONIFIED)
                == Frame.ICONIFIED) {

            frame.setExtendedState(Frame.NORMAL);
        }

        frame.setVisible(true);
        frame.toFront();
        frame.requestFocus();
    }

    // =====================================================
    // MENU MASTER
    // =====================================================

    private void openPeserta() {

        openWindow(
                PesertaFrame.class,
                PesertaFrame::new
        );
    }

    private void openInstruktur() {

        if (!currentUser.isAdmin()) {
            showAccessDenied();
            return;
        }

        openWindow(
                InstrukturFrame.class,
                InstrukturFrame::new
        );
    }

    private void openKursus() {

        if (!currentUser.isAdmin()) {
            showAccessDenied();
            return;
        }

        openWindow(
                KursusFrame.class,
                KursusFrame::new
        );
    }

    private void openKelas() {

        if (!currentUser.isAdmin()) {
            showAccessDenied();
            return;
        }

        openWindow(
                KelasFrame.class,
                KelasFrame::new
        );
    }

    // =====================================================
    // PESAN AKSES
    // =====================================================

    private void showAccessDenied() {

        JOptionPane.showMessageDialog(
                this,
                "Maaf, menu ini hanya dapat diakses oleh ADMIN.",
                "Akses Ditolak",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void showUnderDevelopment(String module) {

        JOptionPane.showMessageDialog(
                this,
                module + " sedang dikembangkan.",
                "Informasi",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =====================================================
    // LOGOUT
    // =====================================================

    private void logout() {

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Apakah Anda yakin ingin logout?",
                "Konfirmasi Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {

            for (JFrame frame : openedWindows.values()) {
                frame.dispose();
            }

            openedWindows.clear();

            UserSession.clear();

            dispose();

            SwingUtilities.invokeLater(() ->
                    new LoginFrame().setVisible(true)
            );
        }
    }

    // =====================================================
    // TUTUP APLIKASI
    // =====================================================

    private void closeApplication() {

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Apakah Anda yakin ingin menutup aplikasi SIKURSUS?",
                "Konfirmasi Keluar",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            for (JFrame frame : openedWindows.values()) {
                frame.dispose();
            }

            openedWindows.clear();

            UserSession.clear();

            System.exit(0);
        }
    }
}
