package view;

import dao.KelasDAO;
import java.awt.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import util.Theme;

public class KelasFrame extends JFrame {

    private JTextField txtNama, txtTanggal, txtKapasitas, txtCari;
    private JComboBox<ComboItem> cmbKursus, cmbInstruktur;
    private JTable tabel;
    private DefaultTableModel model;

    private int idTerpilih = -1;

    // Menyimpan data lengkap (termasuk ID kursus & instruktur) agar
    // tidak perlu query ulang setiap kali baris dipilih.
    private List<Object[]> dataKelas = new ArrayList<>();

    private final KelasDAO dao = new KelasDAO();

    public KelasFrame() {
        setTitle("SIKURSUS - Data Kelas");
        setSize(1050, 700);
        Theme.styleFrame(this);

        initComponents();
        loadCombo();
        tampilData("");
    }

    private void initComponents() {

        // ---------- FORM ----------
        JPanel form = Theme.createCard(new GridBagLayout(), "FORM KELAS");

        txtNama = new JTextField();
        txtTanggal = new JTextField();
        txtTanggal.setToolTipText("Contoh: 2026-01-31");
        txtKapasitas = new JTextField();
        cmbKursus = new JComboBox<ComboItem>();
        cmbInstruktur = new JComboBox<ComboItem>();

        Theme.addField(form, 0, 0, "Nama Kelas", txtNama);
        Theme.addField(form, 0, 1, "Kapasitas", txtKapasitas);
        Theme.addField(form, 1, 0, "Program Kursus", cmbKursus);
        Theme.addField(form, 1, 1, "Instruktur", cmbInstruktur);
        Theme.addField(form, 2, 0, "Tanggal Mulai (YYYY-MM-DD)", txtTanggal);

        JButton btnSimpan = Theme.createButton("Simpan", Theme.BTN_SAVE);
        JButton btnUbah = Theme.createButton("Ubah", Theme.BTN_EDIT);
        JButton btnHapus = Theme.createButton("Hapus", Theme.BTN_DELETE);
        JButton btnReset = Theme.createButton("Reset", Theme.BTN_NEUTRAL);

        Theme.addButtonRow(form, 3, 4, btnSimpan, btnUbah, btnHapus, btnReset);

        // ---------- TABEL ----------
        JPanel tablePanel = Theme.createCard(
            new BorderLayout(0, 10), "DAFTAR KELAS");

        txtCari = new JTextField();
        JButton btnCari = Theme.createButton("Cari", Theme.BTN_EDIT);
        JButton btnSemua = Theme.createButton("Tampilkan Semua", Theme.BTN_NEUTRAL);

        model = new DefaultTableModel(
            new String[]{
                "ID", "Nama Kelas", "Kursus", "Instruktur",
                "Tanggal Mulai", "Kapasitas"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabel = new JTable(model);
        Theme.styleTable(tabel);
        Theme.fixedWidth(tabel, 0, 50);
        Theme.fixedWidth(tabel, 5, 90);

        tablePanel.add(Theme.createSearchBar(txtCari, btnCari, btnSemua),
                BorderLayout.NORTH);
        tablePanel.add(Theme.createScroll(tabel), BorderLayout.CENTER);

        setContentPane(Theme.createPage(
            "DATA KELAS",
            "Kelola kelas, program kursus, dan instruktur",
            form, tablePanel
        ));

        // ---------- EVENT ----------
        btnSimpan.addActionListener(e -> simpan());
        btnUbah.addActionListener(e -> ubah());
        btnHapus.addActionListener(e -> hapus());
        btnReset.addActionListener(e -> reset());
        btnCari.addActionListener(e -> tampilData(txtCari.getText().trim()));
        txtCari.addActionListener(e -> tampilData(txtCari.getText().trim()));
        btnSemua.addActionListener(e -> {
            txtCari.setText("");
            tampilData("");
        });

        tabel.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabel.getSelectedRow() >= 0) {
                pilihData();
            }
        });
    }

    private void loadCombo() {

        cmbKursus.removeAllItems();
        cmbInstruktur.removeAllItems();

        for (Object[] row : dao.getKursus()) {
            cmbKursus.addItem(new ComboItem((Integer) row[0], row[1].toString()));
        }

        for (Object[] row : dao.getInstruktur()) {
            cmbInstruktur.addItem(new ComboItem((Integer) row[0], row[1].toString()));
        }
    }

    private void tampilData(String keyword) {

        model.setRowCount(0);
        dataKelas = dao.getAll(keyword);

        for (Object[] row : dataKelas) {
            model.addRow(new Object[]{
                row[0], row[1], row[2], row[3], row[4], row[5]
            });
        }
    }

    private void pilihData() {

        int row = tabel.getSelectedRow();

        idTerpilih = Integer.parseInt(model.getValueAt(row, 0).toString());

        txtNama.setText(Theme.str(model.getValueAt(row, 1)));
        txtTanggal.setText(Theme.str(model.getValueAt(row, 4)));
        txtKapasitas.setText(Theme.str(model.getValueAt(row, 5)));

        for (Object[] item : dataKelas) {
            if (Integer.parseInt(item[0].toString()) == idTerpilih) {
                pilihCombo(cmbKursus, Integer.parseInt(item[6].toString()));
                pilihCombo(cmbInstruktur, Integer.parseInt(item[7].toString()));
                break;
            }
        }
    }

    private void pilihCombo(JComboBox<ComboItem> combo, int id) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).getId() == id) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private boolean validasi() {

        if (txtNama.getText().trim().isEmpty()
                || txtTanggal.getText().trim().isEmpty()
                || txtKapasitas.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                "Semua data kelas wajib diisi.",
                "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (cmbKursus.getSelectedItem() == null
                || cmbInstruktur.getSelectedItem() == null) {

            JOptionPane.showMessageDialog(this,
                "Data kursus dan instruktur harus tersedia terlebih dahulu.",
                "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            Date.valueOf(txtTanggal.getText().trim());

            int kapasitas = Integer.parseInt(txtKapasitas.getText().trim());

            if (kapasitas <= 0) {
                JOptionPane.showMessageDialog(this,
                    "Kapasitas harus lebih dari 0.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
                return false;
            }

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                "Tanggal harus berformat YYYY-MM-DD dan kapasitas harus berupa angka.",
                "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private void simpan() {

        if (!validasi()) return;

        ComboItem kursus = (ComboItem) cmbKursus.getSelectedItem();
        ComboItem instruktur = (ComboItem) cmbInstruktur.getSelectedItem();

        boolean sukses = dao.insert(
            kursus.getId(),
            instruktur.getId(),
            txtNama.getText().trim(),
            txtTanggal.getText().trim(),
            Integer.parseInt(txtKapasitas.getText().trim())
        );

        if (sukses) {
            JOptionPane.showMessageDialog(this, "Data kelas berhasil disimpan.");
            reset();
            tampilData("");
        } else {
            JOptionPane.showMessageDialog(this,
                "Gagal menyimpan data kelas.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ubah() {

        if (idTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin diubah.");
            return;
        }

        if (!validasi()) return;

        ComboItem kursus = (ComboItem) cmbKursus.getSelectedItem();
        ComboItem instruktur = (ComboItem) cmbInstruktur.getSelectedItem();

        boolean sukses = dao.update(
            idTerpilih,
            kursus.getId(),
            instruktur.getId(),
            txtNama.getText().trim(),
            txtTanggal.getText().trim(),
            Integer.parseInt(txtKapasitas.getText().trim())
        );

        if (sukses) {
            JOptionPane.showMessageDialog(this, "Data kelas berhasil diubah.");
            reset();
            tampilData("");
        } else {
            JOptionPane.showMessageDialog(this,
                "Gagal mengubah data kelas.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapus() {

        if (idTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus.");
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(
            this, "Hapus data kelas ini?",
            "Konfirmasi", JOptionPane.YES_NO_OPTION
        );

        if (konfirmasi == JOptionPane.YES_OPTION) {

            if (dao.delete(idTerpilih)) {
                JOptionPane.showMessageDialog(this, "Data kelas berhasil dihapus.");
                reset();
                tampilData("");
            } else {
                JOptionPane.showMessageDialog(this,
                    "Data kelas tidak dapat dihapus karena mungkin sudah digunakan pada pendaftaran.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void reset() {

        idTerpilih = -1;
        tabel.clearSelection();

        txtNama.setText("");
        txtTanggal.setText("");
        txtKapasitas.setText("");

        if (cmbKursus.getItemCount() > 0) cmbKursus.setSelectedIndex(0);
        if (cmbInstruktur.getItemCount() > 0) cmbInstruktur.setSelectedIndex(0);

        txtNama.requestFocus();
    }

    // Item combo box yang menyimpan ID dan nama
    private static class ComboItem {

        private final int id;
        private final String nama;

        public ComboItem(int id, String nama) {
            this.id = id;
            this.nama = nama;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return nama;
        }
    }
}