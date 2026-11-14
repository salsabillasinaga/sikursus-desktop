package view;

import dao.KursusDAO;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import util.Theme;

public class KursusFrame extends JFrame {

    private JTextField txtKode, txtNama, txtBiaya, txtCari;
    private JComboBox<String> cmbLevel;
    private JTable tabel;
    private DefaultTableModel model;
    private int idTerpilih = -1;

    private final KursusDAO dao = new KursusDAO();

    public KursusFrame() {
        setTitle("SIKURSUS - Data Kursus");
        setSize(950, 650);
        Theme.styleFrame(this);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        initComponents();
        tampilData("");
    }

    private void initComponents() {
        JPanel form = Theme.createCard(
                new GridBagLayout(), "FORM KURSUS");

        txtKode = new JTextField();
        txtNama = new JTextField();
        txtBiaya = new JTextField();

        cmbLevel = new JComboBox<>(
                new String[]{"BASIC", "INTERMEDIATE", "ADVANCED"});

        Theme.addField(form, 0, 0, "Kode Kursus", txtKode);
        Theme.addField(form, 0, 1, "Nama Kursus", txtNama);
        Theme.addField(form, 1, 0, "Level", cmbLevel);
        Theme.addField(form, 1, 1, "Biaya (Rp)", txtBiaya);

        JButton btnSimpan = Theme.createButton("Simpan", Theme.BTN_SAVE);
        JButton btnUbah = Theme.createButton("Ubah", Theme.BTN_EDIT);
        JButton btnHapus = Theme.createButton("Hapus", Theme.BTN_DELETE);
        JButton btnReset = Theme.createButton("Reset", Theme.BTN_NEUTRAL);

        Theme.addButtonRow(form, 2, 4,
                btnSimpan, btnUbah, btnHapus, btnReset);

        JPanel tablePanel = Theme.createCard(
                new BorderLayout(0, 12), "DATA KURSUS");

        txtCari = new JTextField();
        txtCari.setToolTipText("Cari kode, nama, atau level kursus");

        JButton btnCari = Theme.createButton("Cari", Theme.BTN_EDIT);

        model = new DefaultTableModel(
                new String[]{
                    "ID", "Kode", "Nama Kursus", "Level", "Biaya (Rp)"
                }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabel = new JTable(model);
        Theme.styleTable(tabel);
        tabel.setAutoCreateRowSorter(true);
        Theme.fixedWidth(tabel, 0, 50);

        NumberFormat nf = NumberFormat.getNumberInstance(
                new Locale("id", "ID"));

        tabel.getColumnModel().getColumn(4).setCellRenderer(
                new Theme.TableRenderer(
                        SwingConstants.RIGHT,
                        v -> v instanceof Number
                                ? "Rp " + nf.format(
                                        ((Number) v).doubleValue())
                                : Theme.str(v)
                )
        );

        tablePanel.add(
                Theme.createSearchBar(txtCari, btnCari),
                BorderLayout.NORTH);
        tablePanel.add(Theme.createScroll(tabel), BorderLayout.CENTER);

        setContentPane(Theme.createPage(
                "DATA KURSUS",
                "Kelola program dan biaya kursus",
                form, tablePanel));

        btnSimpan.addActionListener(e -> simpan());
        btnUbah.addActionListener(e -> ubah());
        btnHapus.addActionListener(e -> hapus());
        btnReset.addActionListener(e -> reset());

        btnCari.addActionListener(e ->
                tampilData(txtCari.getText().trim()));
        txtCari.addActionListener(e ->
                tampilData(txtCari.getText().trim()));

        tabel.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()
                    && tabel.getSelectedRow() >= 0) {
                pilihData();
            }
        });
    }

    private void tampilData(String keyword) {
        model.setRowCount(0);

        List<Object[]> data = dao.getAll(keyword);

        for (Object[] row : data) {
            model.addRow(row);
        }
    }

    private void pilihData() {
        int row = tabel.getSelectedRow();
        if (row < 0) return;

        row = tabel.convertRowIndexToModel(row);

        idTerpilih = Integer.parseInt(
                model.getValueAt(row, 0).toString());

        txtKode.setText(Theme.str(model.getValueAt(row, 1)));
        txtNama.setText(Theme.str(model.getValueAt(row, 2)));
        cmbLevel.setSelectedItem(
                Theme.str(model.getValueAt(row, 3)));
        txtBiaya.setText(Theme.str(model.getValueAt(row, 4)));
    }

    private boolean validasi() {
        if (txtKode.getText().trim().isEmpty()
                || txtNama.getText().trim().isEmpty()
                || txtBiaya.getText().trim().isEmpty()) {
            Theme.showMessage(this,
                    "Semua data kursus wajib diisi.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            double biaya = Double.parseDouble(
                    txtBiaya.getText().trim());

            if (biaya < 0) {
                Theme.showMessage(this,
                        "Biaya tidak boleh negatif.",
                        "Validasi", JOptionPane.WARNING_MESSAGE);
                return false;
            }

        } catch (NumberFormatException e) {
            Theme.showMessage(this,
                    "Biaya harus berupa angka.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private void simpan() {
        if (!validasi()) return;

        boolean sukses = dao.insert(
                txtKode.getText().trim(),
                txtNama.getText().trim(),
                cmbLevel.getSelectedItem().toString(),
                Double.parseDouble(txtBiaya.getText().trim()));

        if (sukses) {
            Theme.showMessage(this,
                    "Data kursus berhasil disimpan.",
                    "Berhasil", JOptionPane.INFORMATION_MESSAGE);
            reset();
        } else {
            Theme.showMessage(this,
                    "Gagal menyimpan. Periksa apakah kode kursus sudah digunakan.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ubah() {
        if (idTerpilih == -1) {
            Theme.showMessage(this,
                    "Pilih data yang ingin diubah.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validasi()) return;

        boolean sukses = dao.update(
                idTerpilih,
                txtKode.getText().trim(),
                txtNama.getText().trim(),
                cmbLevel.getSelectedItem().toString(),
                Double.parseDouble(txtBiaya.getText().trim()));

        if (sukses) {
            Theme.showMessage(this,
                    "Data kursus berhasil diubah.",
                    "Berhasil", JOptionPane.INFORMATION_MESSAGE);
            reset();
        } else {
            Theme.showMessage(this,
                    "Gagal mengubah data kursus.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapus() {
        if (idTerpilih == -1) {
            Theme.showMessage(this,
                    "Pilih data yang ingin dihapus.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int konfirmasi = Theme.showConfirm(
                this, "Hapus data kursus ini?",
                "Konfirmasi Hapus");

        if (konfirmasi == JOptionPane.YES_OPTION) {
            if (dao.delete(idTerpilih)) {
                Theme.showMessage(this,
                        "Data berhasil dihapus.",
                        "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                reset();
            } else {
                Theme.showMessage(this,
                        "Data gagal dihapus karena mungkin masih digunakan oleh kelas.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void reset() {
        idTerpilih = -1;
        tabel.clearSelection();

        txtKode.setText("");
        txtNama.setText("");
        txtBiaya.setText("");
        txtCari.setText("");

        cmbLevel.setSelectedIndex(0);

        tampilData("");
        txtKode.requestFocus();
    }
}