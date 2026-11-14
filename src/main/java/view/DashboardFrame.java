package view;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.User;
import util.Theme;
import util.UserSession;
import util.WindowManager;

public class DashboardFrame extends JFrame {

    private JPanel mainPanel;
    private JPanel contentPanel;

    private JLabel lblUsername;
    private JLabel lblRole;
    private JLabel lblWelcome;
    private JLabel lblRoleDescription;

    private JPopupMenu popupMenu;
    private JScrollPane scrollPane;

    // Menu bar (Master, Transaksi, Laporan, Keluar)
    private JMenu menuMaster, menuTransaksi, menuLaporan, menuKeluar;
    private JMenuItem miPeserta, miInstruktur, miKursus, miKelas, miUser;
    private JMenuItem miPendaftaran, miPembayaran;
    private JMenuItem miLaporanPeserta, miLaporanPendaftaran;
    private JMenuItem miLogout, miTutup;

    private User currentUser;

    public DashboardFrame() {
        currentUser = UserSession.getCurrentUser();

        if (currentUser == null) {
            Theme.showMessage(
                    null,
                    "Session tidak ditemukan. Silakan login kembali.",
                    "Session Berakhir",
                    JOptionPane.WARNING_MESSAGE
            );

            SwingUtilities.invokeLater(() -> {
                LoginFrame login = new LoginFrame();
                login.setVisible(true);
            });
            dispose();
            return;
        }

        initComponents();
        applyRoleAccess();

        setTitle("Dashboard - SIKURSUS Desktop");
        setSize(1200, 750);
        setMinimumSize(new Dimension(1000, 650));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeApplication();
            }
        });
    }

    // =====================================================
    // INISIALISASI
    // =====================================================

    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.CREAM);

        createPopupMenu();
        createMenuBar();
        createMainPanel();

        add(mainPanel, BorderLayout.CENTER);

        attachPopupMenu(getContentPane());
    }

    // =====================================================
    // POPUP MENU
    // =====================================================

    private void createPopupMenu() {
        popupMenu = new JPopupMenu();
        popupMenu.setBackground(Theme.IVORY);
        popupMenu.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                new EmptyBorder(5, 5, 5, 5)
        ));

        JMenuItem itemTentang = new JMenuItem("Tentang Aplikasi");
        JMenuItem itemRefresh = new JMenuItem("Refresh Dashboard");

        stylePopupItem(itemTentang);
        stylePopupItem(itemRefresh);

        popupMenu.add(itemTentang);
        popupMenu.add(itemRefresh);

        itemTentang.addActionListener(e -> showAbout());
        itemRefresh.addActionListener(e -> refreshDashboard());
    }

    private void stylePopupItem(JMenuItem item) {
        item.setFont(Theme.font(Font.PLAIN, 12));
        item.setForeground(Theme.TEXT);
        item.setBackground(Theme.IVORY);
        item.setOpaque(true);
        item.setBorder(new EmptyBorder(7, 12, 7, 12));
        item.setPreferredSize(new Dimension(190, 32));
    }

    private void attachPopupMenu(Component component) {
        if (component instanceof JComponent) {
            ((JComponent) component).setComponentPopupMenu(popupMenu);
        }

        if (component instanceof Container) {
            Component[] children = ((Container) component).getComponents();

            for (Component child : children) {
                attachPopupMenu(child);
            }
        }
    }

    // =====================================================
    // TENTANG APLIKASI
    // =====================================================

    private void showAbout() {
        String message =
                "<html><div style='width:300px;'>"
                + "<h2 style='color:#5C4033;'>SIKURSUS Desktop</h2>"
                + "<p><b>Sistem Informasi Kursus dan Pelatihan</b></p>"
                + "<p>Aplikasi untuk membantu pengelolaan data peserta, "
                + "instruktur, kursus, kelas, pendaftaran, dan pembayaran.</p>"
                + "<hr>"
                + "<p>Versi Aplikasi: 1.0</p>"
                + "<p>Pengguna: " + currentUser.getUsername() + "</p>"
                + "<p>Role: " + currentUser.getRole() + "</p>"
                + "</div></html>";

        Theme.showMessage(
                this,
                message,
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

        lblRole.setText("ROLE: " + currentUser.getRole());

        lblWelcome.setText(
                "Selamat Datang, " + currentUser.getUsername() + "!"
        );

        applyRoleAccess();

        if (scrollPane != null) {
            scrollPane.getVerticalScrollBar().setValue(0);
        }

        contentPanel.revalidate();
        contentPanel.repaint();
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // =====================================================
    // MENU BAR (Master, Transaksi, Laporan, Keluar)
    // =====================================================

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Theme.DARK_BROWN);
        menuBar.setBorder(new EmptyBorder(2, 8, 2, 8));

        menuMaster = new JMenu("Master");
        menuTransaksi = new JMenu("Transaksi");
        menuLaporan = new JMenu("Laporan");
        menuKeluar = new JMenu("Keluar");

        for (JMenu m : new JMenu[]{
            menuMaster, menuTransaksi, menuLaporan, menuKeluar}) {
            m.setForeground(Color.WHITE);
            m.setFont(Theme.font(Font.BOLD, 12));
            m.setBorder(new EmptyBorder(6, 12, 6, 12));
        }

        miPeserta = new JMenuItem("Peserta");
        miInstruktur = new JMenuItem("Instruktur");
        miKursus = new JMenuItem("Kursus");
        miKelas = new JMenuItem("Kelas");
        miUser = new JMenuItem("Manajemen User");

        miPendaftaran = new JMenuItem("Pendaftaran");
        miPembayaran = new JMenuItem("Pembayaran");

        miLaporanPeserta = new JMenuItem("Laporan Peserta");
        miLaporanPendaftaran = new JMenuItem("Laporan Pendaftaran");

        miLogout = new JMenuItem("Logout");
        miTutup = new JMenuItem("Tutup Aplikasi");

        // Icon dimuat dari resource project (bukan path komputer).
        miPeserta.setIcon(loadIcon("/icons/peserta.png"));
        miInstruktur.setIcon(loadIcon("/icons/instruktur.png"));
        miKursus.setIcon(loadIcon("/icons/kursus.png"));
        miKelas.setIcon(loadIcon("/icons/kelas.png"));

        menuMaster.add(miPeserta);
        menuMaster.add(miInstruktur);
        menuMaster.add(miKursus);
        menuMaster.add(miKelas);

        // Manajemen User hanya ditampilkan untuk ADMIN.
        if (currentUser.isAdmin()) {
            menuMaster.addSeparator();
            menuMaster.add(miUser);
        }

        menuTransaksi.add(miPendaftaran);
        menuTransaksi.add(miPembayaran);

        menuLaporan.add(miLaporanPeserta);
        menuLaporan.add(miLaporanPendaftaran);

        menuKeluar.add(miLogout);
        menuKeluar.addSeparator();
        menuKeluar.add(miTutup);

        menuBar.add(menuMaster);
        menuBar.add(menuTransaksi);
        menuBar.add(menuLaporan);
        menuBar.add(menuKeluar);
        setJMenuBar(menuBar);

        miPeserta.addActionListener(e -> openPeserta());
        miInstruktur.addActionListener(e -> openInstruktur());
        miKursus.addActionListener(e -> openKursus());
        miKelas.addActionListener(e -> openKelas());
        miUser.addActionListener(e -> openUser());

        miPendaftaran.addActionListener(e ->
                showUnderDevelopment("Modul Pendaftaran"));
        miPembayaran.addActionListener(e ->
                showUnderDevelopment("Modul Pembayaran"));

        miLaporanPeserta.addActionListener(e -> showReportPlaceholder());
        miLaporanPendaftaran.addActionListener(e -> showReportPlaceholder());

        miLogout.addActionListener(e -> logout());
        miTutup.addActionListener(e -> closeApplication());
    }

    // Memuat icon dari classpath (src/icons/...). Aman jika file tidak ada.
    private ImageIcon loadIcon(String path) {
        URL url = getClass().getResource(path);
        return url == null ? null : new ImageIcon(url);
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
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(
                        0, 0, 2, 0, Theme.LIGHT_BROWN),
                new EmptyBorder(16, 28, 16, 28)));

        JLabel lblPageTitle = new JLabel("SIKURSUS - Dashboard");
        lblPageTitle.setFont(new Font("Serif", Font.BOLD, 25));
        lblPageTitle.setForeground(Theme.DARK_BROWN);

        JPanel userPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        userPanel.setOpaque(false);

        lblUsername = new JLabel(
                "Pengguna: " + currentUser.getUsername()
        );
        lblUsername.setFont(Theme.font(Font.BOLD, 12));
        lblUsername.setForeground(Theme.TEXT);

        lblRole = new JLabel("ROLE: " + currentUser.getRole());
        lblRole.setFont(Theme.font(Font.BOLD, 11));
        lblRole.setForeground(Theme.MID_BROWN);

        lblUsername.setHorizontalAlignment(SwingConstants.RIGHT);
        lblRole.setHorizontalAlignment(SwingConstants.RIGHT);
        userPanel.add(lblUsername);
        userPanel.add(lblRole);

        header.add(lblPageTitle, BorderLayout.WEST);
        header.add(userPanel, BorderLayout.EAST);
        mainPanel.add(header, BorderLayout.NORTH);

        // KONTEN
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Theme.CREAM);
        contentPanel.setBorder(new EmptyBorder(28, 28, 28, 28));

        lblWelcome = new JLabel(
                "Selamat Datang, " + currentUser.getUsername() + "!"
        );
        lblWelcome.setFont(new Font("Serif", Font.BOLD, 25));
        lblWelcome.setForeground(Theme.DARK_BROWN);
        lblWelcome.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblRoleDescription = new JLabel();
        lblRoleDescription.setFont(Theme.font(Font.PLAIN, 13));
        lblRoleDescription.setForeground(Theme.TEXT);
        lblRoleDescription.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(lblWelcome);
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(lblRoleDescription);
        contentPanel.add(Box.createVerticalStrut(25));

        JLabel lblSection = new JLabel("Ringkasan Aplikasi");
        lblSection.setFont(Theme.font(Font.BOLD, 16));
        lblSection.setForeground(Theme.DARK_BROWN);
        lblSection.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(lblSection);
        contentPanel.add(Box.createVerticalStrut(15));

        // Kartu ringkasan.
        int jumlahKartu = currentUser.isAdmin() ? 5 : 4;

        JPanel cardPanel = new JPanel(new GridLayout(0, 2, 16, 16));
        cardPanel.setOpaque(false);
        cardPanel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, jumlahKartu == 5 ? 330 : 230)
        );
        cardPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        cardPanel.add(createInfoCard(
                "MASTER PESERTA",
                "Pengelolaan data peserta kursus",
                "Buka Data Peserta",
                this::openPeserta,
                true
        ));

        cardPanel.add(createInfoCard(
                "MASTER INSTRUKTUR",
                "Pengelolaan data instruktur",
                "Buka Data Instruktur",
                this::openInstruktur,
                currentUser.isAdmin()
        ));

        cardPanel.add(createInfoCard(
                "MASTER KURSUS",
                "Pengelolaan informasi kursus",
                "Buka Data Kursus",
                this::openKursus,
                currentUser.isAdmin()
        ));

        cardPanel.add(createInfoCard(
                "MASTER KELAS",
                "Pengelolaan kelas pelatihan",
                "Buka Data Kelas",
                this::openKelas,
                currentUser.isAdmin()
        ));

        if (currentUser.isAdmin()) {
            cardPanel.add(createInfoCard(
                    "MANAJEMEN USER",
                    "Mengelola akun Admin dan Operator",
                    "Buka Manajemen User",
                    this::openUser,
                    true
            ));
        }

        contentPanel.add(cardPanel);
        contentPanel.add(Box.createVerticalStrut(25));

        // INFORMASI HAK AKSES
        JPanel roleInfo = new JPanel(new BorderLayout(10, 8));
        roleInfo.setBackground(Theme.IVORY);
        roleInfo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0, 5, 0, 0, Theme.MID_BROWN),
                        new EmptyBorder(16, 18, 16, 20)
                )
        ));

        JLabel lblInfoTitle = new JLabel("Informasi Hak Akses");
        lblInfoTitle.setFont(Theme.font(Font.BOLD, 14));
        lblInfoTitle.setForeground(Theme.DARK_BROWN);

        JLabel lblInfoText = new JLabel();
        lblInfoText.setFont(Theme.font(Font.PLAIN, 12));
        lblInfoText.setForeground(Theme.TEXT);

        if (currentUser.isAdmin()) {
            lblInfoText.setText(
                    "<html>Anda login sebagai ADMIN. "
                    + "Anda dapat mengelola data Master, Transaksi, "
                    + "Laporan, dan akun pengguna melalui Manajemen User."
                    + "</html>"
            );
        } else {
            lblInfoText.setText(
                    "<html>Anda login sebagai OPERATOR. "
                    + "Anda dapat mengakses menu Peserta, Transaksi, "
                    + "dan Laporan sesuai hak akses yang diberikan."
                    + "</html>"
            );
        }

        roleInfo.add(lblInfoTitle, BorderLayout.NORTH);
        roleInfo.add(lblInfoText, BorderLayout.CENTER);
        roleInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(roleInfo);

        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Theme.CREAM);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JLabel footer = new JLabel(
                "SIKURSUS Desktop | Sistem Informasi Manajemen Kursus & Pelatihan"
        );
        footer.setFont(Theme.font(Font.PLAIN, 10));
        footer.setForeground(Theme.MUTED);
        footer.setBorder(new EmptyBorder(10, 28, 12, 28));

        mainPanel.add(footer, BorderLayout.SOUTH);
    }

    // =====================================================
    // KARTU INFORMASI
    // =====================================================

    private JPanel createInfoCard(
            String title,
            String description,
            String buttonText,
            Runnable action,
            boolean enabled) {

        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(Theme.IVORY);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0, 5, 0, 0,
                                enabled ? Theme.ACCENT : Theme.BORDER),
                        new EmptyBorder(16, 16, 16, 16)
                )
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(Theme.font(Font.BOLD, 13));
        lblTitle.setForeground(Theme.DARK_BROWN);

        JLabel lblDescription = new JLabel(
                "<html><div style='width:230px;'>"
                + description
                + "</div></html>"
        );
        lblDescription.setFont(Theme.font(Font.PLAIN, 11));
        lblDescription.setForeground(Theme.TEXT);

        JButton button = Theme.createButton(
                buttonText, Theme.BTN_SAVE
        );
        button.addActionListener(e -> action.run());
        button.setEnabled(enabled);

        // Tombol berada di kiri kartu.
        JPanel bottom = new JPanel(new FlowLayout(
                FlowLayout.LEFT, 0, 0
        ));
        bottom.setOpaque(false);
        bottom.add(button);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblDescription, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);

        return card;
    }

    // =====================================================
    // HAK AKSES
    // =====================================================

    private void applyRoleAccess() {
        boolean admin = currentUser.isAdmin();

        // Menu bar: item master selain Peserta hanya untuk ADMIN.
        miPeserta.setEnabled(true);
        miInstruktur.setEnabled(admin);
        miKursus.setEnabled(admin);
        miKelas.setEnabled(admin);
        miUser.setEnabled(admin);

        menuMaster.setEnabled(true);
        menuTransaksi.setEnabled(true);
        menuLaporan.setEnabled(true);
        menuKeluar.setEnabled(true);

        if (admin) {
            lblRoleDescription.setText(
                    "Anda masuk sebagai Administrator. "
                    + "Anda memiliki akses pengelolaan penuh."
            );
            lblRole.setForeground(Theme.BTN_SUCCESS);
        } else {
            lblRoleDescription.setText(
                    "Anda masuk sebagai Operator. "
                    + "Gunakan menu yang tersedia sesuai hak akses Anda."
            );
            lblRole.setForeground(Theme.MID_BROWN);
        }
    }

    // =====================================================
    // MENU MASTER
    // =====================================================

    private void openPeserta() {
        WindowManager.open(PesertaFrame.class, PesertaFrame::new);
    }

    private void openInstruktur() {
        if (!currentUser.isAdmin()) {
            showAccessDenied();
            return;
        }
        WindowManager.open(InstrukturFrame.class, InstrukturFrame::new);
    }

    private void openKursus() {
        if (!currentUser.isAdmin()) {
            showAccessDenied();
            return;
        }
        WindowManager.open(KursusFrame.class, KursusFrame::new);
    }

    private void openKelas() {
        if (!currentUser.isAdmin()) {
            showAccessDenied();
            return;
        }
        WindowManager.open(KelasFrame.class, KelasFrame::new);
    }

    private void openUser() {
        if (!currentUser.isAdmin()) {
            showAccessDenied();
            return;
        }
        WindowManager.open(UserFrame.class, UserFrame::new);
    }

    // =====================================================
    // PESAN
    // =====================================================

    private void showAccessDenied() {
        Theme.showMessage(
                this,
                "Maaf, menu ini hanya dapat diakses oleh ADMIN.",
                "Akses Ditolak",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void showReportPlaceholder() {
        Theme.showMessage(
                this,
                "Modul laporan diaktifkan pada Pertemuan 12 dengan Jaspersoft Studio.",
                "Informasi",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showUnderDevelopment(String module) {
        Theme.showMessage(
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
        int confirm = Theme.showConfirm(
                this,
                "Apakah Anda yakin ingin logout?",
                "Konfirmasi Logout"
        );

        if (confirm == JOptionPane.YES_OPTION) {
            closeChildWindows();
            UserSession.clear();
            dispose();

            SwingUtilities.invokeLater(() -> {
                LoginFrame login = new LoginFrame();
                login.setVisible(true);
            });
        }
    }

    // =====================================================
    // TUTUP APLIKASI
    // =====================================================

    private void closeApplication() {
        int confirm = Theme.showConfirm(
                this,
                "Apakah Anda yakin ingin menutup aplikasi SIKURSUS?",
                "Konfirmasi Keluar"
        );

        if (confirm == JOptionPane.YES_OPTION) {
            closeChildWindows();
            UserSession.clear();
            dispose();
            System.exit(0);
        }
    }

    private void closeChildWindows() {
        WindowManager.closeAll();
    }
}