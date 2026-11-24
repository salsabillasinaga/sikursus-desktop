package view;

import dao.PendaftaranDAO;
import java.awt.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import report.ReportService;
import util.ComboItem;
import util.EnumUtil;
import util.Theme;

/**
 * Pertemuan 13: CRUD pendaftaran + laporan.
 * - Cetak Terpilih : ID dari kolom 0 baris JTable -> P_ID_PENDAFTARAN
 * - Cetak Semua    : laporan_pendaftaran_semua (tanpa parameter)
 */
public class PendaftaranFrame extends JFrame {

    // Cadangan jika kolom status bukan ENUM (nilai asli dibaca dari database)
    private static final String[] STATUS_CADANGAN = {"AKTIF", "SELESAI", "BATAL"};

    private JComboBox<ComboItem> cmbPeserta, cmbKelas;
    private JComboBox<String> cmbStatus;
    private JTextField txtTanggal, txtCari;
    private JTable tblPendaftaran;
    private DefaultTableModel model;

    private int idTerpilih = -1;
    private List<Object[]> dataList = new ArrayList<Object[]>();

    private final PendaftaranDAO dao = new PendaftaranDAO();

    public PendaftaranFrame() {
        setTitle("SIKURSUS - Data Pendaftaran");
        setSize(1100, 760);
        Theme.styleFrame(this);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        initComponents();
        loadCombo();
        tampilData("");
    }

    private void initComponents() {
        // ---------- form ----------
        JPanel form = Theme.createCard(
                new GridBagLayout(), "INFORMASI PENDAFTARAN");

        cmbPeserta = new JComboBox<ComboItem>();
        cmbKelas = new JComboBox<ComboItem>();
        cmbStatus = new JComboBox<String>(
                EnumUtil.ambil("pendaftaran", "status", STATUS_CADANGAN));
        txtTanggal = new JTextField(java.time.LocalDate.now().toString());
        txtTanggal.setToolTipText("Contoh: 2026-11-24");

        Theme.addField(form, 0, 0, "Pilih Peserta", cmbPeserta);
        Theme.addField(form, 0, 1, "Pilih Kelas", cmbKelas);
        Theme.addField(form, 1, 0,
                "Tanggal Daftar (yyyy-MM-dd)", txtTanggal);
        Theme.addField(form, 1, 1, "Status Pendaftaran", cmbStatus);

        JButton btnSimpan = Theme.createButton("Simpan", Theme.BTN_SAVE);
        JButton btnUbah = Theme.createButton("Ubah", Theme.BTN_EDIT);
        JButton btnHapus = Theme.createButton("Hapus", Theme.BTN_DELETE);
        JButton btnReset = Theme.createButton("Reset", Theme.BTN_NEUTRAL);
        JButton btnCetakTerpilih = Theme.createButton(
                "Cetak Terpilih", Theme.BTN_SUCCESS);
        JButton btnCetakSemua = Theme.createButton(
                "Cetak Semua", Theme.BTN_SUCCESS);

        Theme.addButtonRow(form, 2, 6, btnSimpan, btnUbah, btnHapus,
                btnReset, btnCetakTerpilih, btnCetakSemua);

        // ---------- tabel ----------
        JPanel tablePanel = Theme.createCard(
                new BorderLayout(0, 12), "DAFTAR PENDAFTARAN");

        txtCari = new JTextField();
        txtCari.setToolTipText("Cari NIM, nama, kelas, atau kursus");
        JButton btnCari = Theme.createButton("Cari", Theme.BTN_EDIT);

        model = new DefaultTableModel(
                new String[]{
                    "ID", "NIM", "Peserta", "Kelas",
                    "Kursus", "Tanggal", "Status"
                }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblPendaftaran = new JTable(model);
        Theme.styleTable(tblPendaftaran);
        tblPendaftaran.setAutoCreateRowSorter(true);
        Theme.fixedWidth(tblPendaftaran, 0, 60);

        tablePanel.add(Theme.createSearchBar(txtCari, btnCari),
                BorderLayout.NORTH);
        tablePanel.add(Theme.createScroll(tblPendaftaran),
                BorderLayout.CENTER);

        setContentPane(Theme.createPage(
                "DATA PENDAFTARAN",
                "Kelola pendaftaran peserta ke dalam kelas kursus.",
                form, tablePanel));

        // ---------- event ----------
        btnSimpan.addActionListener(e -> simpan());
        btnUbah.addActionListener(e -> ubah());
        btnHapus.addActionListener(e -> hapus());
        btnReset.addActionListener(e -> reset());
        btnCetakTerpilih.addActionListener(e -> cetakTerpilih());
        btnCetakSemua.addActionListener(e ->
                ReportService.cetakSemuaPendaftaran());

        btnCari.addActionListener(e -> tampilData(txtCari.getText().trim()));
        txtCari.addActionListener(e -> tampilData(txtCari.getText().trim()));

        tblPendaftaran.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()
                    && tblPendaftaran.getSelectedRow() >= 0) {
                pilihData();
            }
        });
    }

    private void loadCombo() {
        cmbPeserta.removeAllItems();
        cmbKelas.removeAllItems();

        for (Object[] row : dao.getPeserta()) {
            cmbPeserta.addItem(new ComboItem(
                    Integer.parseInt(row[0].toString()),
                    row[1].toString()));
        }
        for (Object[] row : dao.getKelas()) {
            cmbKelas.addItem(new ComboItem(
                    Integer.parseInt(row[0].toString()),
                    row[1].toString()));
        }
    }

    private void tampilData(String keyword) {
        model.setRowCount(0);
        dataList = dao.getAll(keyword);

        for (Object[] row : dataList) {
            model.addRow(new Object[]{
                row[0], row[1], row[2], row[3],
                row[4], row[5], row[6]
            });
        }
    }

    private void pilihData() {
        int row = tblPendaftaran.getSelectedRow();
        if (row < 0) return;

        row = tblPendaftaran.convertRowIndexToModel(row);
        idTerpilih = Integer.parseInt(model.getValueAt(row, 0).toString());

        txtTanggal.setText(Theme.str(model.getValueAt(row, 5)));
        pilihStatus(Theme.str(model.getValueAt(row, 6)));

        for (Object[] item : dataList) {
            if (Integer.parseInt(item[0].toString()) == idTerpilih) {
                pilihCombo(cmbPeserta, Integer.parseInt(item[7].toString()));
                pilihCombo(cmbKelas, Integer.parseInt(item[8].toString()));
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

    private void pilihStatus(String status) {
        for (int i = 0; i < cmbStatus.getItemCount(); i++) {
            if (cmbStatus.getItemAt(i).equalsIgnoreCase(status)) {
                cmbStatus.setSelectedIndex(i);
                return;
            }
        }
        // status dari database belum ada di daftar -> tambahkan
        cmbStatus.addItem(status);
        cmbStatus.setSelectedItem(status);
    }

    private boolean validasi() {
        if (cmbPeserta.getSelectedItem() == null
                || cmbKelas.getSelectedItem() == null) {
            Theme.showMessage(this,
                    "Data peserta dan kelas harus tersedia terlebih dahulu.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        String tanggal = txtTanggal.getText().trim();
        if (tanggal.isEmpty()) {
            Theme.showMessage(this, "Tanggal daftar wajib diisi.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            Date.valueOf(tanggal);
        } catch (IllegalArgumentException e) {
            Theme.showMessage(this,
                    "Tanggal harus berformat YYYY-MM-DD.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private void simpan() {
        if (!validasi()) return;

        ComboItem peserta = (ComboItem) cmbPeserta.getSelectedItem();
        ComboItem kelas = (ComboItem) cmbKelas.getSelectedItem();

        if (dao.sudahTerdaftar(peserta.getId(), kelas.getId(), -1)) {
            Theme.showMessage(this,
                    "Peserta tersebut sudah terdaftar pada kelas ini.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean sukses = dao.insert(peserta.getId(), kelas.getId(),
                txtTanggal.getText().trim(),
                cmbStatus.getSelectedItem().toString());

        if (sukses) {
            Theme.showMessage(this, "Data pendaftaran berhasil disimpan.",
                    "Berhasil", JOptionPane.INFORMATION_MESSAGE);
            reset();
        } else {
            Theme.showMessage(this,
                    "Gagal menyimpan pendaftaran.\n" + dao.getLastError(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ubah() {
        if (idTerpilih == -1) {
            Theme.showMessage(this, "Pilih data yang ingin diubah.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validasi()) return;

        ComboItem peserta = (ComboItem) cmbPeserta.getSelectedItem();
        ComboItem kelas = (ComboItem) cmbKelas.getSelectedItem();

        if (dao.sudahTerdaftar(peserta.getId(), kelas.getId(), idTerpilih)) {
            Theme.showMessage(this,
                    "Peserta tersebut sudah terdaftar pada kelas ini.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean sukses = dao.update(idTerpilih, peserta.getId(),
                kelas.getId(), txtTanggal.getText().trim(),
                cmbStatus.getSelectedItem().toString());

        if (sukses) {
            Theme.showMessage(this, "Data pendaftaran berhasil diubah.",
                    "Berhasil", JOptionPane.INFORMATION_MESSAGE);
            reset();
        } else {
            Theme.showMessage(this,
                    "Gagal mengubah pendaftaran.\n" + dao.getLastError(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapus() {
        if (idTerpilih == -1) {
            Theme.showMessage(this, "Pilih data yang ingin dihapus.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int konfirmasi = Theme.showConfirm(this,
                "Hapus data pendaftaran ini?", "Konfirmasi Hapus");

        if (konfirmasi == JOptionPane.YES_OPTION) {
            if (dao.delete(idTerpilih)) {
                Theme.showMessage(this,
                        "Data pendaftaran berhasil dihapus.",
                        "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                reset();
            } else {
                Theme.showMessage(this,
                        "Pendaftaran tidak dapat dihapus karena mungkin "
                      + "sudah memiliki data pembayaran.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void reset() {
        idTerpilih = -1;
        tblPendaftaran.clearSelection();

        txtTanggal.setText(java.time.LocalDate.now().toString());
        txtCari.setText("");

        if (cmbPeserta.getItemCount() > 0) cmbPeserta.setSelectedIndex(0);
        if (cmbKelas.getItemCount() > 0) cmbKelas.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);

        tampilData("");
    }

    private void cetakTerpilih() {
        int row = tblPendaftaran.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih satu data pendaftaran terlebih dahulu.");
            return;
        }

        // index tampilan -> index model (aman saat tabel di-sort)
        int modelRow = tblPendaftaran.convertRowIndexToModel(row);

        // Kolom pertama = indeks 0 (id_pendaftaran)
        int idPendaftaran = Integer.parseInt(
                model.getValueAt(modelRow, 0).toString());

        System.out.println("ID terpilih: " + idPendaftaran);

        ReportService.cetakPendaftaran(idPendaftaran);
    }
}