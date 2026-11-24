package view;

import java.awt.*;
import javax.swing.*;
import report.ReportService;
import util.Theme;

/**
 * Pertemuan 13 (latihan): laporan pembayaran berdasarkan rentang tanggal.
 * Input format yyyy-MM-dd, dikirim sebagai java.sql.Date.
 */
public class LaporanPembayaranFrame extends JFrame {

    private JTextField txtTanggalAwal, txtTanggalAkhir;

    public LaporanPembayaranFrame() {
        setTitle("SIKURSUS - Laporan Pembayaran");
        setSize(640, 420);
        Theme.styleFrame(this);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        JPanel form = Theme.createCard(
                new GridBagLayout(), "PERIODE PEMBAYARAN");

        java.time.LocalDate hariIni = java.time.LocalDate.now();

        txtTanggalAwal = new JTextField(
                hariIni.withDayOfMonth(1).toString());
        txtTanggalAkhir = new JTextField(hariIni.toString());

        txtTanggalAwal.setToolTipText("Format: yyyy-MM-dd, contoh 2026-11-01");
        txtTanggalAkhir.setToolTipText("Format: yyyy-MM-dd, contoh 2026-11-30");

        Theme.addField(form, 0, 0,
                "Tanggal Awal (yyyy-MM-dd)", txtTanggalAwal);
        Theme.addField(form, 0, 1,
                "Tanggal Akhir (yyyy-MM-dd)", txtTanggalAkhir);

        JButton btnCetak = Theme.createButton(
                "Tampilkan Laporan", Theme.BTN_SUCCESS);
        JButton btnReset = Theme.createButton("Reset", Theme.BTN_NEUTRAL);

        Theme.addButtonRow(form, 1, 2, btnCetak, btnReset);

        JPanel catatan = Theme.createCard(
                new BorderLayout(), "CATATAN");
        JLabel lbl = new JLabel("<html>Laporan hanya memuat transaksi "
                + "pembayaran pada rentang tanggal di atas "
                + "(termasuk tanggal awal dan akhir).<br>"
                + "Contoh periode: 2026-11-01 sampai 2026-11-30.</html>");
        lbl.setFont(Theme.font(Font.PLAIN, 12));
        lbl.setForeground(Theme.MUTED);
        lbl.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        catatan.add(lbl, BorderLayout.CENTER);

        setContentPane(Theme.createPage(
                "LAPORAN PEMBAYARAN",
                "Cetak transaksi pembayaran berdasarkan rentang tanggal",
                form, catatan));

        btnCetak.addActionListener(e -> cetak());
        btnReset.addActionListener(e -> {
            java.time.LocalDate now = java.time.LocalDate.now();
            txtTanggalAwal.setText(now.withDayOfMonth(1).toString());
            txtTanggalAkhir.setText(now.toString());
        });
    }

    private void cetak() {
        String awalStr = txtTanggalAwal.getText().trim();
        String akhirStr = txtTanggalAkhir.getText().trim();

        // Validasi 1 & 2: tidak boleh kosong
        if (awalStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Tanggal awal tidak boleh kosong.");
            txtTanggalAwal.requestFocus();
            return;
        }
        if (akhirStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Tanggal akhir tidak boleh kosong.");
            txtTanggalAkhir.requestFocus();
            return;
        }

        java.sql.Date tanggalAwal;
        java.sql.Date tanggalAkhir;
        try {
            tanggalAwal = java.sql.Date.valueOf(awalStr);
            tanggalAkhir = java.sql.Date.valueOf(akhirStr);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    "Format tanggal harus yyyy-MM-dd, contoh 2026-11-01.",
                    "Format Salah", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validasi 3: awal tidak boleh setelah akhir
        if (tanggalAwal.after(tanggalAkhir)) {
            JOptionPane.showMessageDialog(this,
                    "Tanggal awal tidak boleh setelah tanggal akhir.",
                    "Periode Salah", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validasi 4 (tidak ada transaksi) ditangani ReportService:
        // tampil pesan jelas, aplikasi tetap stabil.
        ReportService.cetakPembayaran(tanggalAwal, tanggalAkhir);
    }
}