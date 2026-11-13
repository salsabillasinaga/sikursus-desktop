package view;

import dao.PesertaDAO;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Peserta;
import util.Theme;

public class PesertaFrame extends JFrame {

    private final PesertaDAO dao = new PesertaDAO();

    private int selectedId = -1;

    private JTextField txtNim, txtNama, txtProdi, txtNoHp, txtEmail, txtCari;
    private JComboBox<String> cmbJenisKelamin;

    private JTable tblPeserta;
    private DefaultTableModel model;

    public PesertaFrame() {

        setTitle("Master Data Peserta - SIKURSUS");
        setSize(1100, 760);
        Theme.styleFrame(this);
        setMinimumSize(new Dimension(900, 650));

        buildForm();
        loadTable();
        clearForm();
    }

    // ==========================================
    // TAMPILAN UTAMA
    // ==========================================

    private void buildForm() {
        setContentPane(Theme.createPage(
            "MASTER DATA PESERTA",
            "Kelola data peserta kursus",
            createFormPanel(),
            createTablePanel()
        ));
    }

    // ==========================================
    // FORM PESERTA
    // ==========================================

    private JPanel createFormPanel() {

        JPanel panel = Theme.createCard(new GridBagLayout(), "FORM PESERTA");

        txtNim = new JTextField();
        txtNama = new JTextField();
        txtProdi = new JTextField();
        txtNoHp = new JTextField();
        txtEmail = new JTextField();
        cmbJenisKelamin = new JComboBox<>(new String[]{"L", "P"});

        Theme.addField(panel, 0, 0, "NIM", txtNim);
        Theme.addField(panel, 0, 1, "Nama Peserta", txtNama);
        Theme.addField(panel, 1, 0, "Jenis Kelamin", cmbJenisKelamin);
        Theme.addField(panel, 1, 1, "Program Studi", txtProdi);
        Theme.addField(panel, 2, 0, "Nomor HP", txtNoHp);
        Theme.addField(panel, 2, 1, "Email", txtEmail);

        JButton btnSimpan = Theme.createButton("Simpan", Theme.BTN_SAVE);
        JButton btnUbah = Theme.createButton("Ubah", Theme.BTN_EDIT);
        JButton btnHapus = Theme.createButton("Hapus", Theme.BTN_DELETE);
        JButton btnReset = Theme.createButton("Reset", Theme.BTN_NEUTRAL);
        JButton btnCetak = Theme.createButton("Cetak Semua", Theme.BTN_SUCCESS);

        Theme.addButtonRow(panel, 3, 4,
                btnSimpan, btnUbah, btnHapus, btnReset, btnCetak);

        btnSimpan.addActionListener(e -> save());
        btnUbah.addActionListener(e -> update());
        btnHapus.addActionListener(e -> delete());
        btnReset.addActionListener(e -> {
            clearForm();
            loadTable();
        });
        btnCetak.addActionListener(e -> printData());

        return panel;
    }

    // ==========================================
    // PANEL TABEL DAN PENCARIAN
    // ==========================================

    private JPanel createTablePanel() {

        JPanel panel = Theme.createCard(new BorderLayout(0, 10), "DATA PESERTA");

        txtCari = new JTextField();
        JButton btnCari = Theme.createButton("Cari", Theme.BTN_EDIT);
        JButton btnSemua = Theme.createButton("Tampilkan Semua", Theme.BTN_NEUTRAL);

        model = new DefaultTableModel(
            new String[]{"ID", "NIM", "Nama", "JK", "Prodi", "No. HP", "Email"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblPeserta = new JTable(model);
        Theme.styleTable(tblPeserta);
        Theme.fixedWidth(tblPeserta, 0, 50);
        Theme.fixedWidth(tblPeserta, 3, 50);

        panel.add(Theme.createSearchBar(txtCari, btnCari, btnSemua),
                BorderLayout.NORTH);
        panel.add(Theme.createScroll(tblPeserta), BorderLayout.CENTER);

        btnCari.addActionListener(e -> search());
        txtCari.addActionListener(e -> search());
        btnSemua.addActionListener(e -> {
            txtCari.setText("");
            loadTable();
        });

        tblPeserta.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                pickData();
            }
        });

        return panel;
    }

    // ==========================================
    // MENGAMBIL DATA FORM
    // ==========================================

    private Peserta getFormData() {

        Peserta p = new Peserta();

        p.setId(selectedId < 0 ? 0 : selectedId);
        p.setNim(txtNim.getText().trim());
        p.setNama(txtNama.getText().trim());
        p.setJenisKelamin(cmbJenisKelamin.getSelectedItem().toString());
        p.setProdi(txtProdi.getText().trim());
        p.setNoHp(txtNoHp.getText().trim());
        p.setEmail(txtEmail.getText().trim());

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

            JOptionPane.showMessageDialog(this,
                    "Semua kolom harus diisi!",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    // ==========================================
    // CREATE
    // ==========================================

    private void save() {

        if (!validateForm()) return;

        try {
            if (dao.insert(getFormData())) {
                JOptionPane.showMessageDialog(this,
                        "Data peserta berhasil disimpan!");
                clearForm();
                loadTable();
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
            JOptionPane.showMessageDialog(this,
                    "Pilih data peserta dari tabel terlebih dahulu!");
            return;
        }

        if (!validateForm()) return;

        try {
            if (dao.update(getFormData())) {
                JOptionPane.showMessageDialog(this,
                        "Data peserta berhasil diubah!");
                clearForm();
                loadTable();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Data peserta tidak ditemukan!");
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
            JOptionPane.showMessageDialog(this,
                    "Pilih data peserta yang ingin dihapus!");
            return;
        }

        int jawab = JOptionPane.showConfirmDialog(
                this,
                "Apakah kamu yakin ingin menghapus data peserta ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION
        );

        if (jawab != JOptionPane.YES_OPTION) return;

        try {
            if (dao.delete(selectedId)) {
                JOptionPane.showMessageDialog(this,
                        "Data peserta berhasil dihapus!");
                clearForm();
                loadTable();
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
    // SEARCH
    // ==========================================

    private void search() {
        try {
            fillTable(dao.search(txtCari.getText().trim()));
        } catch (SQLException ex) {
            showError("Mencari data peserta", ex);
        }
    }

    // ==========================================
    // MEMILIH DATA TABEL
    // ==========================================

    private void pickData() {

        int row = tblPeserta.getSelectedRow();

        if (row < 0) return;

        selectedId = Integer.parseInt(tblPeserta.getValueAt(row, 0).toString());

        txtNim.setText(Theme.str(tblPeserta.getValueAt(row, 1)));
        txtNama.setText(Theme.str(tblPeserta.getValueAt(row, 2)));
        cmbJenisKelamin.setSelectedItem(Theme.str(tblPeserta.getValueAt(row, 3)));
        txtProdi.setText(Theme.str(tblPeserta.getValueAt(row, 4)));
        txtNoHp.setText(Theme.str(tblPeserta.getValueAt(row, 5)));
        txtEmail.setText(Theme.str(tblPeserta.getValueAt(row, 6)));
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
    }

    // ==========================================
    // CETAK DATA
    // ==========================================

    private void printData() {

        try {
            if (tblPeserta.print()) {
                JOptionPane.showMessageDialog(this,
                        "Data peserta berhasil dicetak!");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Gagal mencetak data: " + ex.getMessage(),
                    "Cetak Data", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==========================================
    // PESAN ERROR
    // ==========================================

    private void showValidationError(IllegalArgumentException ex) {
        JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Validasi", JOptionPane.WARNING_MESSAGE);
    }

    private void showError(String action, SQLException ex) {
        JOptionPane.showMessageDialog(this,
                "Gagal " + action + ":\n" + ex.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
    }

    // ==========================================
    // MAIN (untuk uji coba form ini saja)
    // ==========================================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PesertaFrame().setVisible(true));
    }
}