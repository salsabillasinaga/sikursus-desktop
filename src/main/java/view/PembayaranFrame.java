package view;

import dao.PembayaranDAO;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import report.ReportService;
import util.ComboItem;
import util.EnumUtil;
import util.Theme;

/**
 * Pertemuan 13 (latihan): CRUD pembayaran + laporan.
 * - Cari + Awal/Akhir : menyaring tabel berdasarkan kata kunci & tanggal
 * - Cetak Periode     : laporan_pembayaran (P_TANGGAL_AWAL, P_TANGGAL_AKHIR)
 * - Cetak Semua       : laporan_pembayaran_semua (tanpa parameter)
 */
public class PembayaranFrame extends JFrame {

    // Cadangan jika kolom bukan ENUM (nilai asli dibaca dari database)
    private static final String[] METODE_CADANGAN =
            {"TUNAI", "TRANSFER", "QRIS"};
    private static final String[] STATUS_CADANGAN =
            {"MENUNGGU", "LUNAS", "BATAL"};

    private JComboBox<ComboItem> cmbPendaftaran;
    private JComboBox<String> cmbMetode, cmbStatus;
    private JTextField txtTanggal, txtNominal;
    private JTextField txtCari, txtAwal, txtAkhir;
    private JTable tblPembayaran;
    private DefaultTableModel model;

    private int idTerpilih = -1;

    private final PembayaranDAO dao = new PembayaranDAO();

    public PembayaranFrame() {
        setTitle("SIKURSUS - Data Pembayaran");
        setSize(1150, 780);
        Theme.styleFrame(this);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        initComponents();
        loadCombo();
        tampilData();
    }

    private void initComponents() {
        // ================= FORM =================
        JPanel form = Theme.createCard(
                new GridBagLayout(), "INFORMASI PEMBAYARAN");

        cmbPendaftaran = new JComboBox<ComboItem>();
        cmbMetode = new JComboBox<String>(
                EnumUtil.ambil("pembayaran", "metode", METODE_CADANGAN));
        cmbStatus = new JComboBox<String>(
                EnumUtil.ambil("pembayaran", "status", STATUS_CADANGAN));

        txtTanggal = new JTextField(java.time.LocalDate.now().toString());
        txtTanggal.setToolTipText("Contoh: 2026-11-24");
        txtNominal = new JTextField();
        txtNominal.setToolTipText("Angka saja, contoh: 500000");

        // baris 1: 3 kolom
        Theme.addField(form, 0, 0, "Pendaftaran", cmbPendaftaran);
        Theme.addField(form, 0, 1, "Tanggal Bayar (yyyy-MM-dd)", txtTanggal);
        Theme.addField(form, 0, 2, "Nominal Pembayaran (Rp)", txtNominal);
        // baris 2
        Theme.addField(form, 1, 0, "Metode Pembayaran", cmbMetode);
        Theme.addField(form, 1, 1, "Status Pembayaran", cmbStatus);

        JButton btnSimpan = Theme.createButton("Simpan", Theme.BTN_SAVE);
        JButton btnUbah = Theme.createButton("Ubah", Theme.BTN_EDIT);
        JButton btnHapus = Theme.createButton("Hapus", Theme.BTN_DELETE);
        JButton btnReset = Theme.createButton("Reset", Theme.BTN_NEUTRAL);
        JButton btnPeriode = Theme.createButton(
                "Cetak Periode", Theme.BTN_SUCCESS);
        JButton btnCetakSemua = Theme.createButton(
                "Cetak Semua", Theme.BTN_SUCCESS);

        // baris tombol (melebar 3 kolom)
        JPanel bar = new JPanel();
        bar.setLayout(new BoxLayout(bar, BoxLayout.X_AXIS));
        bar.setOpaque(false);
        JButton[] tombol = {btnSimpan, btnUbah, btnHapus, btnReset,
            btnPeriode, btnCetakSemua};
        bar.add(Box.createHorizontalGlue());
        for (int i = 0; i < tombol.length; i++) {
            if (i > 0) bar.add(Box.createHorizontalStrut(10));
            bar.add(tombol[i]);
        }
        bar.add(Box.createHorizontalGlue());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(14, 0, 0, 0);
        form.add(bar, gbc);

        // ================= TABEL =================
        JPanel tablePanel = Theme.createCard(
                new BorderLayout(0, 12), "RIWAYAT PEMBAYARAN");

        txtCari = new JTextField();
        txtCari.setToolTipText("Cari NIM, nama, kelas, atau metode");
        txtAwal = new JTextField(9);
        txtAwal.setToolTipText("Filter tanggal awal (yyyy-MM-dd), boleh kosong");
        txtAkhir = new JTextField(9);
        txtAkhir.setToolTipText("Filter tanggal akhir (yyyy-MM-dd), boleh kosong");
        JButton btnCari = Theme.createButton("Cari", Theme.BTN_EDIT);

        tablePanel.add(buatBarFilter(btnCari), BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{
                    "ID", "Pendaftaran", "Peserta", "Kelas",
                    "Tanggal", "Nominal", "Metode", "Status"
                }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblPembayaran = new JTable(model);
        Theme.styleTable(tblPembayaran);
        tblPembayaran.setAutoCreateRowSorter(true);
        Theme.fixedWidth(tblPembayaran, 0, 50);
        Theme.fixedWidth(tblPembayaran, 1, 100);

        tablePanel.add(Theme.createScroll(tblPembayaran),
                BorderLayout.CENTER);

        setContentPane(Theme.createPage(
                "DATA PEMBAYARAN",
                "Kelola pembayaran, status transaksi, dan laporan keuangan kursus.",
                form, tablePanel));

        // ================= EVENT =================
        btnSimpan.addActionListener(e -> simpan());
        btnUbah.addActionListener(e -> ubah());
        btnHapus.addActionListener(e -> hapus());
        btnReset.addActionListener(e -> reset());
        btnPeriode.addActionListener(e -> cetakPeriode());
        btnCetakSemua.addActionListener(e ->
                ReportService.cetakSemuaPembayaran());

        btnCari.addActionListener(e -> tampilData());
        txtCari.addActionListener(e -> tampilData());

        tblPembayaran.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()
                    && tblPembayaran.getSelectedRow() >= 0) {
                pilihData();
            }
        });
    }

    /** Baris: Cari [....] [Cari]   Awal [....]   Akhir [....] */
    private JPanel buatBarFilter(JButton btnCari) {
        Theme.styleTextField(txtCari);
        Theme.styleTextField(txtAwal);
        Theme.styleTextField(txtAkhir);

        JPanel bar = new JPanel(new GridBagLayout());
        bar.setOpaque(false);

        GridBagConstraints g = new GridBagConstraints();
        g.gridy = 0;
        g.insets = new Insets(0, 0, 0, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx = 0; g.weightx = 0;
        bar.add(label("Cari"), g);

        g.gridx = 1; g.weightx = 1;
        bar.add(txtCari, g);

        g.gridx = 2; g.weightx = 0;
        bar.add(btnCari, g);

        g.gridx = 3; g.insets = new Insets(0, 14, 0, 8);
        bar.add(label("Awal"), g);

        g.gridx = 4; g.insets = new Insets(0, 0, 0, 8);
        bar.add(txtAwal, g);

        g.gridx = 5; g.insets = new Insets(0, 6, 0, 8);
        bar.add(label("Akhir"), g);

        g.gridx = 6; g.insets = new Insets(0, 0, 0, 0);
        bar.add(txtAkhir, g);

        return bar;
    }

    private JLabel label(String teks) {
        JLabel l = new JLabel(teks);
        l.setFont(Theme.font(Font.BOLD, 12));
        l.setForeground(Theme.MID_BROWN);
        return l;
    }

    private void loadCombo() {
        cmbPendaftaran.removeAllItems();
        for (Object[] row : dao.getPendaftaran()) {
            cmbPendaftaran.addItem(new ComboItem(
                    Integer.parseInt(row[0].toString()),
                    row[1].toString()));
        }
    }

    // ---------- periode (Awal / Akhir) ----------
    /** Validasi Awal & Akhir. Mengembalikan {awal, akhir} atau null. */
    private Date[] bacaPeriode() {
        String awalStr = txtAwal.getText().trim();
        String akhirStr = txtAkhir.getText().trim();

        if (awalStr.isEmpty()) {
            Theme.showMessage(this, "Tanggal awal tidak boleh kosong.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            txtAwal.requestFocus();
            return null;
        }
        if (akhirStr.isEmpty()) {
            Theme.showMessage(this, "Tanggal akhir tidak boleh kosong.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            txtAkhir.requestFocus();
            return null;
        }

        Date awal;
        Date akhir;
        try {
            awal = Date.valueOf(awalStr);
            akhir = Date.valueOf(akhirStr);
        } catch (IllegalArgumentException e) {
            Theme.showMessage(this,
                    "Format tanggal harus yyyy-MM-dd, contoh 2026-11-01.",
                    "Format Salah", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        if (awal.after(akhir)) {
            Theme.showMessage(this,
                    "Tanggal awal tidak boleh setelah tanggal akhir.",
                    "Periode Salah", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        return new Date[]{awal, akhir};
    }

    private void cetakPeriode() {
        Date[] periode = bacaPeriode();
        if (periode == null) return;

        ReportService.cetakPembayaran(periode[0], periode[1]);
    }

    // ---------- tabel ----------
    private void tampilData() {
        Date awal = null;
        Date akhir = null;

        // Filter tanggal dipakai hanya jika Awal/Akhir diisi
        if (!txtAwal.getText().trim().isEmpty()
                || !txtAkhir.getText().trim().isEmpty()) {
            Date[] periode = bacaPeriode();
            if (periode == null) return;
            awal = periode[0];
            akhir = periode[1];
        }

        model.setRowCount(0);
        List<Object[]> data = dao.getAll(
                txtCari.getText().trim(), awal, akhir);
        for (Object[] row : data) {
            model.addRow(row);
        }
    }

    private void pilihData() {
        int row = tblPembayaran.getSelectedRow();
        if (row < 0) return;

        row = tblPembayaran.convertRowIndexToModel(row);
        idTerpilih = Integer.parseInt(model.getValueAt(row, 0).toString());

        int idDaftar = Integer.parseInt(model.getValueAt(row, 1).toString());
        for (int i = 0; i < cmbPendaftaran.getItemCount(); i++) {
            if (cmbPendaftaran.getItemAt(i).getId() == idDaftar) {
                cmbPendaftaran.setSelectedIndex(i);
                break;
            }
        }

        txtTanggal.setText(Theme.str(model.getValueAt(row, 4)));
        txtNominal.setText(Theme.str(model.getValueAt(row, 5)));
        pilihString(cmbMetode, Theme.str(model.getValueAt(row, 6)));
        pilihString(cmbStatus, Theme.str(model.getValueAt(row, 7)));
    }

    private void pilihString(JComboBox<String> combo, String nilai) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).equalsIgnoreCase(nilai)) {
                combo.setSelectedIndex(i);
                return;
            }
        }
        combo.addItem(nilai);
        combo.setSelectedItem(nilai);
    }

    // ---------- CRUD ----------
    private BigDecimal validasi() {
        if (cmbPendaftaran.getSelectedItem() == null) {
            Theme.showMessage(this,
                    "Belum ada data pendaftaran. Isi data pendaftaran dahulu.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        String tanggal = txtTanggal.getText().trim();
        String nominalStr = txtNominal.getText().trim();

        if (tanggal.isEmpty() || nominalStr.isEmpty()) {
            Theme.showMessage(this,
                    "Tanggal bayar dan nominal wajib diisi.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        try {
            Date.valueOf(tanggal);
        } catch (IllegalArgumentException e) {
            Theme.showMessage(this, "Tanggal harus berformat yyyy-MM-dd.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        if (!nominalStr.matches("\\d+(\\.\\d{1,2})?")) {
            Theme.showMessage(this,
                    "Nominal harus berupa angka, contoh: 500000.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        BigDecimal nominal = new BigDecimal(nominalStr);
        if (nominal.compareTo(BigDecimal.ZERO) <= 0) {
            Theme.showMessage(this, "Nominal harus lebih dari 0.",
                    "Validasi", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        return nominal;
    }

    private void simpan() {
        BigDecimal nominal = validasi();
        if (nominal == null) return;

        ComboItem daftar = (ComboItem) cmbPendaftaran.getSelectedItem();

        boolean sukses = dao.insert(daftar.getId(),
                txtTanggal.getText().trim(), nominal,
                cmbMetode.getSelectedItem().toString(),
                cmbStatus.getSelectedItem().toString());

        if (sukses) {
            Theme.showMessage(this, "Data pembayaran berhasil disimpan.",
                    "Berhasil", JOptionPane.INFORMATION_MESSAGE);
            reset();
        } else {
            Theme.showMessage(this,
                    "Gagal menyimpan pembayaran.\n" + dao.getLastError(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ubah() {
        if (idTerpilih == -1) {
            Theme.showMessage(this, "Pilih data yang ingin diubah.",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        BigDecimal nominal = validasi();
        if (nominal == null) return;

        ComboItem daftar = (ComboItem) cmbPendaftaran.getSelectedItem();

        boolean sukses = dao.update(idTerpilih, daftar.getId(),
                txtTanggal.getText().trim(), nominal,
                cmbMetode.getSelectedItem().toString(),
                cmbStatus.getSelectedItem().toString());

        if (sukses) {
            Theme.showMessage(this, "Data pembayaran berhasil diubah.",
                    "Berhasil", JOptionPane.INFORMATION_MESSAGE);
            reset();
        } else {
            Theme.showMessage(this,
                    "Gagal mengubah pembayaran.\n" + dao.getLastError(),
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
                "Hapus data pembayaran ini?", "Konfirmasi Hapus");

        if (konfirmasi == JOptionPane.YES_OPTION) {
            if (dao.delete(idTerpilih)) {
                Theme.showMessage(this, "Data pembayaran berhasil dihapus.",
                        "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                reset();
            } else {
                Theme.showMessage(this,
                        "Gagal menghapus pembayaran.\n" + dao.getLastError(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void reset() {
        idTerpilih = -1;
        tblPembayaran.clearSelection();

        txtTanggal.setText(java.time.LocalDate.now().toString());
        txtNominal.setText("");
        txtCari.setText("");
        txtAwal.setText("");
        txtAkhir.setText("");

        // data pendaftaran baru mungkin sudah ditambah di form lain
        loadCombo();
        cmbMetode.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);

        tampilData();
    }
}