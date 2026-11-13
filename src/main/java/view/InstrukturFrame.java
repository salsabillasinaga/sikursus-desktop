package view;

import dao.InstrukturDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import util.Theme;

public class InstrukturFrame extends JFrame {

    private JTextField txtNama, txtKeahlian, txtNoHp, txtEmail, txtCari;
    private JTable tabel;
    private DefaultTableModel model;
    private int idTerpilih = -1;

    private final InstrukturDAO dao = new InstrukturDAO();

    public InstrukturFrame() {
        setTitle("SIKURSUS - Data Instruktur");
        setSize(950, 650);
        Theme.styleFrame(this);
        initComponents();
        tampilData("");
    }

    private void initComponents() {

        // ---------- FORM ----------
        JPanel form = Theme.createCard(new GridBagLayout(), "FORM INSTRUKTUR");

        txtNama = new JTextField();
        txtKeahlian = new JTextField();
        txtNoHp = new JTextField();
        txtEmail = new JTextField();

        Theme.addField(form, 0, 0, "Nama Instruktur", txtNama);
        Theme.addField(form, 0, 1, "Keahlian", txtKeahlian);
        Theme.addField(form, 1, 0, "Nomor HP", txtNoHp);
        Theme.addField(form, 1, 1, "Email", txtEmail);

        JButton btnSimpan = Theme.createButton("Simpan", Theme.BTN_SAVE);
        JButton btnUbah = Theme.createButton("Ubah", Theme.BTN_EDIT);
        JButton btnHapus = Theme.createButton("Hapus", Theme.BTN_DELETE);
        JButton btnReset = Theme.createButton("Reset", Theme.BTN_NEUTRAL);

        Theme.addButtonRow(form, 2, 4, btnSimpan, btnUbah, btnHapus, btnReset);

        // ---------- TABEL ----------
        JPanel tablePanel = Theme.createCard(
            new BorderLayout(0, 10), "DAFTAR INSTRUKTUR");

        txtCari = new JTextField();
        JButton btnCari = Theme.createButton("Cari", Theme.BTN_EDIT);
        JButton btnSemua = Theme.createButton("Tampilkan Semua", Theme.BTN_NEUTRAL);

        model = new DefaultTableModel(
            new String[]{"ID", "Nama", "Keahlian", "No. HP", "Email"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabel = new JTable(model);
        Theme.styleTable(tabel);
        Theme.fixedWidth(tabel, 0, 50);

        tablePanel.add(Theme.createSearchBar(txtCari, btnCari, btnSemua),
                BorderLayout.NORTH);
        tablePanel.add(Theme.createScroll(tabel), BorderLayout.CENTER);

        setContentPane(Theme.createPage(
            "DATA INSTRUKTUR",
            "Kelola informasi instruktur kursus",
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
                txtNama.setText(Theme.str(model.getValueAt(row, 1)));
                txtKeahlian.setText(Theme.str(model.getValueAt(row, 2)));
                txtNoHp.setText(Theme.str(model.getValueAt(row, 3)));
                txtEmail.setText(Theme.str(model.getValueAt(row, 4)));
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
        if (txtNama.getText().trim().isEmpty()
                || txtKeahlian.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Nama dan keahlian wajib diisi.",
                "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String email = txtEmail.getText().trim();
        if (!email.isEmpty() && !email.contains("@")) {
            JOptionPane.showMessageDialog(this,
                "Format email tidak valid.",
                "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void simpan() {
        if (!validasi()) return;

        boolean sukses = dao.insert(
            txtNama.getText().trim(),
            txtKeahlian.getText().trim(),
            txtNoHp.getText().trim(),
            txtEmail.getText().trim()
        );

        if (sukses) {
            JOptionPane.showMessageDialog(this, "Data instruktur berhasil disimpan.");
            reset();
            tampilData("");
        } else {
            JOptionPane.showMessageDialog(this,
                "Gagal menyimpan data instruktur.",
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
            txtNama.getText().trim(),
            txtKeahlian.getText().trim(),
            txtNoHp.getText().trim(),
            txtEmail.getText().trim()
        );

        if (sukses) {
            JOptionPane.showMessageDialog(this, "Data instruktur berhasil diubah.");
            reset();
            tampilData("");
        } else {
            JOptionPane.showMessageDialog(this,
                "Gagal mengubah data.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapus() {
        if (idTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus.");
            return;
        }

        int konfirmasi = JOptionPane.showConfirmDialog(
            this, "Hapus data instruktur ini?",
            "Konfirmasi", JOptionPane.YES_NO_OPTION
        );

        if (konfirmasi == JOptionPane.YES_OPTION) {
            if (dao.delete(idTerpilih)) {
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");
                reset();
                tampilData("");
            } else {
                JOptionPane.showMessageDialog(this,
                    "Data gagal dihapus. Instruktur mungkin masih digunakan oleh kelas.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void reset() {
        idTerpilih = -1;
        tabel.clearSelection();
        txtNama.setText("");
        txtKeahlian.setText("");
        txtNoHp.setText("");
        txtEmail.setText("");
        txtNama.requestFocus();
    }
}