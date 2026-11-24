package dao;

import config.Database;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PembayaranDAO {

    private String lastError = "";

    public String getLastError() {
        return lastError;
    }

    /**
     * Data JTable. Urutan kolom:
     * 0 id_pembayaran, 1 id_pendaftaran, 2 nama_peserta, 3 nama_kelas,
     * 4 tanggal_bayar, 5 nominal, 6 metode, 7 status
     *
     * keyword : cari NIM / nama / kelas / metode
     * awal, akhir : filter tanggal bayar; isi null untuk tanpa filter
     */
    public List<Object[]> getAll(String keyword, Date awal, Date akhir) {
        List<Object[]> data = new ArrayList<Object[]>();

        String sql = "SELECT pb.id_pembayaran, p.id_pendaftaran, "
                   + "ps.nama AS nama_peserta, kls.nama_kelas, "
                   + "pb.tanggal_bayar, pb.nominal, pb.metode, "
                   + "pb.status AS status_pembayaran "
                   + "FROM pembayaran pb "
                   + "JOIN pendaftaran p ON pb.id_pendaftaran = p.id_pendaftaran "
                   + "JOIN peserta ps ON p.id_peserta = ps.id_peserta "
                   + "JOIN kelas kls ON p.id_kelas = kls.id_kelas "
                   + "WHERE (ps.nim LIKE ? OR ps.nama LIKE ? "
                   + "OR kls.nama_kelas LIKE ? OR pb.metode LIKE ?) ";

        boolean pakaiTanggal = awal != null && akhir != null;
        if (pakaiTanggal) {
            sql += "AND pb.tanggal_bayar BETWEEN ? AND ? ";
        }
        sql += "ORDER BY pb.id_pembayaran DESC";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            String key = "%" + keyword + "%";
            for (int i = 1; i <= 4; i++) {
                p.setString(i, key);
            }
            if (pakaiTanggal) {
                p.setDate(5, awal);
                p.setDate(6, akhir);
            }

            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    BigDecimal nominal = r.getBigDecimal("nominal");
                    data.add(new Object[]{
                        r.getInt("id_pembayaran"),
                        r.getInt("id_pendaftaran"),
                        r.getString("nama_peserta"),
                        r.getString("nama_kelas"),
                        r.getDate("tanggal_bayar"),
                        nominal == null ? "" : nominal.toPlainString(),
                        r.getString("metode"),
                        r.getString("status_pembayaran")
                    });
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            lastError = e.getMessage();
        }

        return data;
    }

    /** Isi combo pendaftaran: {id, "#id - nama / kelas"} */
    public List<Object[]> getPendaftaran() {
        List<Object[]> data = new ArrayList<Object[]>();

        String sql = "SELECT p.id_pendaftaran, "
                   + "CONCAT('#', p.id_pendaftaran, ' - ', ps.nama, "
                   + "' / ', kls.nama_kelas) AS teks "
                   + "FROM pendaftaran p "
                   + "JOIN peserta ps ON p.id_peserta = ps.id_peserta "
                   + "JOIN kelas kls ON p.id_kelas = kls.id_kelas "
                   + "ORDER BY p.id_pendaftaran DESC";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {

            while (r.next()) {
                data.add(new Object[]{
                    r.getInt("id_pendaftaran"), r.getString("teks")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            lastError = e.getMessage();
        }
        return data;
    }

    public boolean insert(int idPendaftaran, String tanggalBayar,
                          BigDecimal nominal, String metode,
                          String status) {
        String sql = "INSERT INTO pembayaran "
                   + "(id_pendaftaran, tanggal_bayar, nominal, "
                   + "metode, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setInt(1, idPendaftaran);
            p.setDate(2, Date.valueOf(tanggalBayar));
            p.setBigDecimal(3, nominal);
            p.setString(4, metode);
            p.setString(5, status);

            return p.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            lastError = e.getMessage();
            return false;
        }
    }

    public boolean update(int idPembayaran, int idPendaftaran,
                          String tanggalBayar, BigDecimal nominal,
                          String metode, String status) {
        String sql = "UPDATE pembayaran SET id_pendaftaran=?, "
                   + "tanggal_bayar=?, nominal=?, metode=?, status=? "
                   + "WHERE id_pembayaran=?";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setInt(1, idPendaftaran);
            p.setDate(2, Date.valueOf(tanggalBayar));
            p.setBigDecimal(3, nominal);
            p.setString(4, metode);
            p.setString(5, status);
            p.setInt(6, idPembayaran);

            return p.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            lastError = e.getMessage();
            return false;
        }
    }

    public boolean delete(int idPembayaran) {
        String sql = "DELETE FROM pembayaran WHERE id_pembayaran=?";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setInt(1, idPembayaran);
            return p.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            lastError = e.getMessage();
            return false;
        }
    }
}