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
        initComponents();
        tampilData("");
    }

    private void initComponents() {

        // ---------- FORM ----------
        JPanel form = Theme.createCard(new GridBagLayout(), "FORM KURSUS");

        txtKode = new JTextField();
        txtNama = new JTextField();
        txtBiaya = new JTextField();
        cmbLevel = new JComboBox<String>(
            new String[]{"BASIC", "INTERMEDIATE", "ADVANCED"}
        );

        Theme.addField(form, 0, 0, "Kode Kursus", txtKode);
        Theme.addField(form, 0, 1, "Nama Kursus", txtNama);
        Theme.addField(form, 1, 0, "Level", cmbLevel);
        Theme.addField(form, 1, 1, "Biaya (Rp)", txtBiaya);

        JButton btnSimpan = Theme.createButton("Simpan", Theme.BTN_SAVE);
        JButton btnUbah = Theme.createButton("Ubah", Theme.BTN_EDIT);
        JButton btnHapus = Theme.createButton("Hapus", Theme.BTN_DELETE);
        JButton btnReset = Theme.createButton("Reset", Theme.BTN_NEUTRAL);

        Theme.addButtonRow(form, 2, 4, btnSimpan, btnUbah, btnHapus, btnReset);

        // ---------- TABEL ----------
        JPanel tablePanel = Theme.createCard(
            new BorderLayout(0, 10), "DAFTAR KURSUS");

        txtCari = new JTextField();
        JButton btnCari = Theme.createButton("Cari", Theme.BTN_EDIT);
        JButton btnSemua = Theme.createButton("Tampilkan Semua", Theme.BTN_NEUTRAL);

        model = new DefaultTableModel(
            new String[]{"ID", "Kode", "Nama Kursus", "Level", "Biaya (Rp)"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabel = new JTable(model);
        Theme.styleTable(tabel);
        Theme.fixedWidth(tabel, 0, 50);

        // Format rupiah (hanya tampilan, data asli tetap angka)
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("id", "ID"));
        tabel.getColumnModel().getColumn(4).setCellRenderer(
            new Theme.TableRenderer(SwingConstants.RIGHT, v ->
                (v instanceof Number)
                    ? "Rp " + nf.format(((Number) v).doubleValue())
                    : v.toString())
        );

        tablePanel.add(Theme.createSearchBar(txtCari, btnCari, btnSemua),
                BorderLayout.NORTH);
        tablePanel.add(Theme.createScroll(tabel), BorderLayout.CENTER);

        setContentPane(Theme.createPage(
            "DATA KURSUS",
            "Kelola program dan biaya kursus",
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
                int row = tabel.getSelectedRow();

                idTerpilih = Integer.parseInt(model.getValueAt(row, 0).toString());
                txtKode.setText(Theme.str(model.getValueAt(row, 1)));
                txtNama.setText(Theme.str(model.getValueAt(row, 2)));
                cmbLevel.setSelectedItem(Theme.str(model.getValueAt(row, 3)));
                txtBiaya.setText(Theme.str(model.getValueAt(row, 4)));
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

    private boolean validasi() {
        if (txtKode.getText().trim().isEmpty()
                || txtNama.getText().trim().isEmpty()
                || txtBiaya.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this,
                "Semua data kursus wajib diisi.",
                "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            double biaya = Double.parseDouble(txtBiaya.getText().trim());

            if (biaya < 0) {
                JOptionPane.showMessageDialog(this,
                    "Biaya tidak boleh negatif.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
                return false;
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
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
            Double.parseDouble(txtBiaya.getText().trim())
        );

        if (sukses) {
            JOptionPane.showMessageDialog(this, "Data kursus berhasil disimpan.");
            reset();
            tampilData("");
        } else {
            JOptionPane.showMessageDialog(this,
                "Gagal menyimpan. Periksa apakah kode kursus sudah digunakan.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ubah() {
        if (idTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin diubah.");
            return;
        }
        if (!validasi()) return;

        boolean sukses = dao.update(
            idTerpilih,
            txtKode.getText().trim(),
            txtNama.getText().trim(),
            cmbLevel.getSelectedItem().toString(),
            Double.parseDouble(txtBiaya.getText().trim())
        );

        if (sukses) {
            JOptionPane.showMessageDialog(this, "Data kursus berhasil diubah.");
            reset();
            tampilData("");
        } else {
            JOptionPane.showMessageDialog(this,
                "Gagal mengubah data kursus.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapus() {
        if (idTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus.");
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(
            this, "Hapus data kursus ini?",
            "Konfirmasi", JOptionPane.YES_NO_OPTION
        );

        if (konfirmasi == JOptionPane.YES_OPTION) {
            if (dao.delete(idTerpilih)) {
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");
                reset();
                tampilData("");
            } else {
                JOptionPane.showMessageDialog(this,
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
        cmbLevel.setSelectedIndex(0);
        txtKode.requestFocus();
    }
}