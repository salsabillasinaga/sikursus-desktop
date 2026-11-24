package report;

import config.Database;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 * Pusat pemanggilan laporan Jasper.
 * Pertemuan 12: laporan_peserta (tanpa parameter)
 * Pertemuan 13: laporan_pendaftaran (P_ID_PENDAFTARAN)
 *               laporan_pembayaran  (P_TANGGAL_AWAL, P_TANGGAL_AKHIR)
 */
public final class ReportService {

    private ReportService() {}

    // ---------- Pertemuan 12 ----------
    public static void tampilkanLaporanPeserta() {
        tampilkan("/report/laporan_peserta.jasper",
                new HashMap<String, Object>(),
                "Laporan Peserta",
                "Belum ada data peserta untuk dicetak.");
    }

    // ---------- Pertemuan 13: parameter Integer ----------
    public static void cetakPendaftaran(int idPendaftaran) {
        Map<String, Object> params = new HashMap<String, Object>();
        // key harus sama persis dengan nama parameter di Jasper
        params.put("P_ID_PENDAFTARAN", Integer.valueOf(idPendaftaran));

        tampilkan("/report/laporan_pendaftaran.jasper",
                params,
                "Laporan Pendaftaran",
                "Data laporan tidak ditemukan untuk ID pendaftaran "
                        + idPendaftaran + ".");
    }

    // ---------- Pertemuan 13: parameter java.sql.Date ----------
    public static void cetakPembayaran(java.sql.Date tanggalAwal,
                                       java.sql.Date tanggalAkhir) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("P_TANGGAL_AWAL", tanggalAwal);
        params.put("P_TANGGAL_AKHIR", tanggalAkhir);

        tampilkan("/report/laporan_pembayaran.jasper",
                params,
                "Laporan Pembayaran",
                "Tidak ada transaksi pembayaran pada periode "
                        + tanggalAwal + " s/d " + tanggalAkhir + ".");
    }

    // ---------- Pertemuan 13: laporan semua data (tanpa parameter) ----------
    public static void cetakSemuaPendaftaran() {
        tampilkan("/report/laporan_pendaftaran_semua.jasper",
                new HashMap<String, Object>(),
                "Laporan Pendaftaran",
                "Belum ada data pendaftaran untuk dicetak.");
    }

    public static void cetakSemuaPembayaran() {
        tampilkan("/report/laporan_pembayaran_semua.jasper",
                new HashMap<String, Object>(),
                "Laporan Pembayaran",
                "Belum ada data pembayaran untuk dicetak.");
    }

    // ---------- inti: isi report lalu tampilkan ----------
    private static void tampilkan(String resource,
                                  Map<String, Object> params,
                                  String judul,
                                  String pesanKosong) {
        // try-with-resources: Connection & InputStream otomatis ditutup,
        // sehingga file .jasper tidak terkunci saat Clean and Build.
        try (Connection conn = Database.getConnection();
             InputStream in = ReportService.class
                     .getResourceAsStream(resource)) {

            if (in == null) {
                throw new IllegalStateException(
                        "File " + resource + " tidak ditemukan di resource.");
            }

            JasperPrint print = JasperFillManager.fillReport(
                    in, params, conn);

            if (print.getPages().isEmpty()) {
                JOptionPane.showMessageDialog(null, pesanKosong,
                        judul, JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // false = menutup viewer TIDAK menutup seluruh aplikasi
            JasperViewer.viewReport(print, false);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Gagal membuka laporan: " + e.getMessage(),
                    judul, JOptionPane.ERROR_MESSAGE);
        }
    }
}