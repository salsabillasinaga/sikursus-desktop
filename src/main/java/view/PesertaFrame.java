
package view;

import dao.PesertaDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Peserta;

public class PesertaFrame extends JFrame {

    private final PesertaDAO dao = new PesertaDAO();

    private int selectedId = -1;

    private JTextField txtNim;
    private JTextField txtNama;
    private JTextField txtProdi;
    private JTextField txtNoHp;
    private JTextField txtEmail;
    private JTextField txtCari;

    private JComboBox<String> cmbJenisKelamin;

    private JTable tblPeserta;
    private DefaultTableModel model;

    private JButton btnSimpan;
    private JButton btnUbah;
    private JButton btnHapus;
    private JButton btnReset;
    private JButton btnCari;
    private JButton btnCetak;

    // PALET WARNA SOFT MOCHA
    private final Color warnaLatar = new Color(246, 240, 232);
    private final Color warnaUtama = new Color(174, 132, 96);
    private final Color warnaUtamaGelap = new Color(112, 78, 55);
    private final Color warnaCream = new Color(255, 251, 245);
    private final Color warnaPutih = Color.WHITE;
    private final Color warnaTeks = new Color(75, 55, 42);
    private final Color warnaGaris = new Color(220, 205, 188);
    private final Color warnaBaris = new Color(250, 245, 238);

    public PesertaFrame() {

        setTitle("Master Data Peserta - SIKURSUS Desktop");
        setSize(1100, 780);
        setMinimumSize(new Dimension(900, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(warnaLatar);

        buildForm();
        configureTable();
        loadTable();
        clearForm();
    }

    // ==========================================
    // TAMPILAN UTAMA
    // ==========================================

    private void buildForm() {

        JPanel utama = new JPanel(new BorderLayout(14, 14));

        utama.setBorder(
                BorderFactory.createEmptyBorder(18, 20, 18, 20)
        );

        utama.setBackground(warnaLatar);

        JLabel lblJudul = new JLabel(
                "MASTER DATA PESERTA",
                SwingConstants.CENTER
        );

        lblJudul.setFont(
                new Font("SansSerif", Font.BOLD, 23)
        );

        lblJudul.setForeground(warnaCream);
        lblJudul.setOpaque(true);
        lblJudul.setBackground(warnaUtamaGelap);

        lblJudul.setPreferredSize(
                new Dimension(100, 72)
        );

        lblJudul.setBorder(
                BorderFactory.createEmptyBorder(12, 10, 12, 10)
        );

        utama.add(lblJudul, BorderLayout.NORTH);

        JPanel tengah = new JPanel(
                new BorderLayout(14, 14)
        );

        tengah.setBackground(warnaLatar);

        tengah.add(
                createFormPanel(),
                BorderLayout.NORTH
        );

        tengah.add(
                createTablePanel(),
                BorderLayout.CENTER
        );

        utama.add(tengah, BorderLayout.CENTER);

        setContentPane(utama);
    }

    // ==========================================
    // FORM PESERTA
    // ==========================================

    private JPanel createFormPanel() {

        JPanel panel = new JPanel(new GridBagLayout());

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                warnaGaris, 1, true
                        ),
                        BorderFactory.createTitledBorder(
                                BorderFactory.createEmptyBorder(
                                        10, 12, 10, 12
                                ),
                                "FORM PESERTA",
                                javax.swing.border.TitledBorder.LEADING,
                                javax.swing.border.TitledBorder.TOP,
                                new Font("SansSerif", Font.BOLD, 14),
                                warnaUtamaGelap
                        )
                )
        );

        panel.setBackground(warnaCream);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNim = new JTextField(30);
        txtNama = new JTextField(30);
        txtProdi = new JTextField(30);
        txtNoHp = new JTextField(30);
        txtEmail = new JTextField(30);

        cmbJenisKelamin = new JComboBox<>(
                new String[]{"L", "P"}
        );

        styleTextField(txtNim);
        styleTextField(txtNama);
        styleTextField(txtProdi);
        styleTextField(txtNoHp);
        styleTextField(txtEmail);

        styleComboBox(cmbJenisKelamin);

        addFormRow(panel, gbc, 0, "NIM", txtNim);
        addFormRow(panel, gbc, 1, "Nama Peserta", txtNama);
        addFormRow(panel, gbc, 2, "Jenis Kelamin", cmbJenisKelamin);
        addFormRow(panel, gbc, 3, "Program Studi", txtProdi);
        addFormRow(panel, gbc, 4, "Nomor HP", txtNoHp);
        addFormRow(panel, gbc, 5, "Email", txtEmail);

        // PANEL TOMBOL
        JPanel panelTombol = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 5)
        );

        panelTombol.setBackground(warnaCream);

        btnSimpan = new JButton("Simpan");
        btnUbah = new JButton("Ubah");
        btnHapus = new JButton("Hapus");
        btnReset = new JButton("Reset");
        btnCetak = new JButton("Cetak Semua");

        styleButton(
                btnSimpan,
                new Color(166, 119, 78)
        );

        styleButton(
                btnUbah,
                new Color(125, 91, 67)
        );

        styleButton(
                btnHapus,
                new Color(169, 76, 63)
        );

        styleButton(
                btnReset,
                new Color(135, 119, 103)
        );

        styleButton(
                btnCetak,
                new Color(111, 125, 91)
        );

        panelTombol.add(btnSimpan);
        panelTombol.add(btnUbah);
        panelTombol.add(btnHapus);
        panelTombol.add(btnReset);
        panelTombol.add(btnCetak);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.CENTER;

        panel.add(panelTombol, gbc);

        // EVENT TOMBOL
        btnSimpan.addActionListener(e -> save());
        btnUbah.addActionListener(e -> update());
        btnHapus.addActionListener(e -> delete());
        btnReset.addActionListener(e -> clearForm());
        btnCetak.addActionListener(e -> printData());

        return panel;
    }

    // ==========================================
    // MENAMBAHKAN BARIS FORM
    // ==========================================

    private void addFormRow(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            String label,
            JComponent component) {

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.2;

        JLabel lbl = new JLabel(label);

        lbl.setFont(
                new Font("SansSerif", Font.BOLD, 13)
        );

        lbl.setForeground(warnaTeks);

        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;

        panel.add(component, gbc);
    }

    // ==========================================
    // PANEL TABEL DAN PENCARIAN
    // ==========================================

    private JPanel createTablePanel() {

        JPanel panel = new JPanel(
                new BorderLayout(10, 10)
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                warnaGaris, 1, true
                        ),
                        BorderFactory.createTitledBorder(
                                BorderFactory.createEmptyBorder(
                                        10, 12, 12, 12
                                ),
                                "DATA PESERTA",
                                javax.swing.border.TitledBorder.LEADING,
                                javax.swing.border.TitledBorder.TOP,
                                new Font("SansSerif", Font.BOLD, 14),
                                warnaUtamaGelap
                        )
                )
        );

        panel.setBackground(warnaCream);

        // PANEL PENCARIAN
        JPanel panelCari = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 5)
        );

        panelCari.setBackground(warnaCream);

        txtCari = new JTextField(25);
        styleTextField(txtCari);

        btnCari = new JButton("Cari");

        styleButton(
                btnCari,
                warnaUtamaGelap
        );

        JLabel lblCari = new JLabel("Pencarian");

        lblCari.setFont(
                new Font("SansSerif", Font.BOLD, 13)
        );

        lblCari.setForeground(warnaTeks);

        panelCari.add(lblCari);
        panelCari.add(txtCari);
        panelCari.add(btnCari);

        panel.add(
                panelCari,
                BorderLayout.NORTH
        );

        // KOLOM TABEL
        String[] kolom = {
            "ID",
            "NIM",
            "Nama",
            "JK",
            "Prodi",
            "No. HP",
            "Email"
        };

        model = new DefaultTableModel(kolom, 0) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {
                return false;
            }
        };

        tblPeserta = new JTable(model);

        configureTable();

        JScrollPane scroll = new JScrollPane(tblPeserta);

        scroll.setBorder(
                BorderFactory.createLineBorder(warnaGaris)
        );

        scroll.getViewport().setBackground(Color.WHITE);

        panel.add(
                scroll,
                BorderLayout.CENTER
        );

        // EVENT PENCARIAN
        btnCari.addActionListener(e -> search());

        txtCari.addActionListener(e -> search());

        // EVENT PILIH DATA
        tblPeserta.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        pickData();
                    }
                });

        return panel;
    }

    // ==========================================
    // STYLE TOMBOL
    // ==========================================

    private void styleButton(
            JButton button,
            Color color
    ) {

        button.setFont(
                new Font("SansSerif", Font.BOLD, 13)
        );

        button.setBackground(color);
        button.setForeground(Color.WHITE);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.setCursor(
                new java.awt.Cursor(
                        java.awt.Cursor.HAND_CURSOR
                )
        );

        button.setPreferredSize(
                new Dimension(122, 36)
        );

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                color.darker(), 1, true
                        ),
                        BorderFactory.createEmptyBorder(
                                5, 12, 5, 12
                        )
                )
        );
    }

    // ==========================================
    // STYLE INPUT TEXT FIELD
    // ==========================================

    private void styleTextField(JTextField field) {

        field.setFont(
                new Font("SansSerif", Font.PLAIN, 13)
        );

        field.setForeground(warnaTeks);
        field.setBackground(Color.WHITE);
        field.setCaretColor(warnaUtamaGelap);

        field.setPreferredSize(
                new Dimension(250, 34)
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                warnaGaris, 1, true
                        ),
                        BorderFactory.createEmptyBorder(
                                5, 9, 5, 9
                        )
                )
        );
    }

    // ==========================================
    // STYLE COMBO BOX
    // ==========================================

    private void styleComboBox(
            JComboBox<String> combo
    ) {

        combo.setFont(
                new Font("SansSerif", Font.PLAIN, 13)
        );

        combo.setForeground(warnaTeks);
        combo.setBackground(Color.WHITE);

        combo.setPreferredSize(
                new Dimension(250, 34)
        );
    }

    // ==========================================
    // MENGATUR TABEL
    // ==========================================

    private void configureTable() {

        if (tblPeserta == null) {
            return;
        }

        tblPeserta.setRowHeight(30);

        tblPeserta.setFont(
                new Font("SansSerif", Font.PLAIN, 12)
        );

        tblPeserta.setForeground(warnaTeks);
        tblPeserta.setBackground(Color.WHITE);
        tblPeserta.setGridColor(warnaGaris);

        tblPeserta.setSelectionBackground(
                new Color(230, 211, 190)
        );

        tblPeserta.setSelectionForeground(
                warnaUtamaGelap
        );

        tblPeserta.setShowVerticalLines(false);

        tblPeserta.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tblPeserta.getTableHeader()
                .setReorderingAllowed(false);

        tblPeserta.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS
        );

        JTableHeaderStyle();
    }

    // STYLE HEADER TABEL
    private void JTableHeaderStyle() {

        tblPeserta.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );

        tblPeserta.getTableHeader().setBackground(
                warnaUtamaGelap
        );

        tblPeserta.getTableHeader().setForeground(
                Color.WHITE
        );

        tblPeserta.getTableHeader().setPreferredSize(
                new Dimension(100, 36)
        );
    }

    // ==========================================
    // MENGAMBIL DATA FORM
    // ==========================================

    private Peserta getFormData() {

        Peserta p = new Peserta();

        p.setId(selectedId < 0 ? 0 : selectedId);

        p.setNim(
                txtNim.getText().trim()
        );

        p.setNama(
                txtNama.getText().trim()
        );

        p.setJenisKelamin(
                cmbJenisKelamin.getSelectedItem().toString()
        );

        p.setProdi(
                txtProdi.getText().trim()
        );

        p.setNoHp(
                txtNoHp.getText().trim()
        );

        p.setEmail(
                txtEmail.getText().trim()
        );

        return p;
    }

    // ==========================================
    // VALIDASI FORM
    // ==========================================

    private boolean validateForm() {

        if (txtNim.getText().trim().isEmpty()
                || txtNama.getText().trim().isEmpty()
                || txtProdi.getText().trim().isEmpty()
                || txtNoHp.getText().trim().isEmpty()
                || txtEmail.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Semua kolom harus diisi!",
                    "Validasi",
                    JOptionPane.WARNING_MESSAGE
            );

            return false;
        }

        return true;
    }

    // ==========================================
    // CREATE
    // ==========================================

    private void save() {

        if (!validateForm()) {
            return;
        }

        try {

            Peserta p = getFormData();

            if (dao.insert(p)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Data peserta berhasil disimpan!"
                );

                refreshData();
                clearForm();
            }

        } catch (IllegalArgumentException ex) {

            showValidationError(ex);

        } catch (SQLException ex) {

            showError("Menyimpan data", ex);
        }
    }

    // ==========================================
    // UPDATE
    // ==========================================

    private void update() {

        if (selectedId < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pilih data peserta dari tabel terlebih dahulu!"
            );

            return;
        }

        if (!validateForm()) {
            return;
        }

        try {

            Peserta p = getFormData();

            if (dao.update(p)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Data peserta berhasil diubah!"
                );

                refreshData();
                clearForm();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Data peserta tidak ditemukan!"
                );
            }

        } catch (IllegalArgumentException ex) {

            showValidationError(ex);

        } catch (SQLException ex) {

            showError("Mengubah data", ex);
        }
    }

    // ==========================================
    // DELETE
    // ==========================================

    private void delete() {

        if (selectedId < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pilih data peserta yang ingin dihapus!"
            );

            return;
        }

        int jawab = JOptionPane.showConfirmDialog(
                this,
                "Apakah kamu yakin ingin menghapus data peserta ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION
        );

        if (jawab != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            if (dao.delete(selectedId)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Data peserta berhasil dihapus!"
                );

                refreshData();
                clearForm();
            }

        } catch (SQLException ex) {

            showError("Menghapus data", ex);
        }
    }

    // ==========================================
    // READ
    // ==========================================

    private void loadTable() {

        try {

            fillTable(dao.findAll());

        } catch (SQLException ex) {

            showError("Memuat data peserta", ex);
        }
    }

    // ==========================================
    // MENGISI TABEL
    // ==========================================

    private void fillTable(List<Peserta> data) {

        model.setRowCount(0);

        for (Peserta p : data) {

            model.addRow(new Object[]{
                p.getId(),
                p.getNim(),
                p.getNama(),
                p.getJenisKelamin(),
                p.getProdi(),
                p.getNoHp(),
                p.getEmail()
            });
        }
    }

    // ==========================================
    // REFRESH DATA
    // ==========================================

    private void refreshData() {

        loadTable();
    }

    // ==========================================
    // SEARCH
    // ==========================================

    private void search() {

        try {

            fillTable(
                    dao.search(txtCari.getText().trim())
            );

        } catch (SQLException ex) {

            showError("Mencari data peserta", ex);
        }
    }

    // ==========================================
    // MEMILIH DATA TABEL
    // ==========================================

    private void pickData() {

        int row = tblPeserta.getSelectedRow();

        if (row < 0) {
            return;
        }

        selectedId = Integer.parseInt(
                tblPeserta.getValueAt(row, 0).toString()
        );

        txtNim.setText(
                String.valueOf(
                        tblPeserta.getValueAt(row, 1)
                )
        );

        txtNama.setText(
                String.valueOf(
                        tblPeserta.getValueAt(row, 2)
                )
        );

        cmbJenisKelamin.setSelectedItem(
                String.valueOf(
                        tblPeserta.getValueAt(row, 3)
                )
        );

        txtProdi.setText(
                String.valueOf(
                        tblPeserta.getValueAt(row, 4)
                )
        );

        txtNoHp.setText(
                String.valueOf(
                        tblPeserta.getValueAt(row, 5)
                )
        );

        txtEmail.setText(
                String.valueOf(
                        tblPeserta.getValueAt(row, 6)
                )
        );
    }

    // ==========================================
    // RESET FORM
    // ==========================================

    private void clearForm() {

        selectedId = -1;

        txtNim.setText("");
        txtNama.setText("");
        txtProdi.setText("");
        txtNoHp.setText("");
        txtEmail.setText("");
        txtCari.setText("");

        cmbJenisKelamin.setSelectedIndex(0);

        tblPeserta.clearSelection();

        txtNim.requestFocus();

        loadTable();
    }

    // ==========================================
    // CETAK DATA
    // ==========================================

    private void printData() {

        try {

            boolean berhasil = tblPeserta.print();

            if (berhasil) {

                JOptionPane.showMessageDialog(
                        this,
                        "Data peserta berhasil dicetak!"
                );
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Gagal mencetak data: " + ex.getMessage(),
                    "Cetak Data",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ==========================================
    // PESAN VALIDASI
    // ==========================================

    private void showValidationError(
            IllegalArgumentException ex
    ) {

        JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Validasi",
                JOptionPane.WARNING_MESSAGE
        );
    }

    // ==========================================
    // PESAN DATABASE
    // ==========================================

    private void showError(
            String action,
            SQLException ex
    ) {

        JOptionPane.showMessageDialog(
                this,
                "Gagal " + action + ":\n" + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    // ==========================================
    // MAIN
    // ==========================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new PesertaFrame().setVisible(true);

        });
    }
}
