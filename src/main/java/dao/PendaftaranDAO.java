package dao;

import config.Database;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PendaftaranDAO {

    private String lastError = "";

    public String getLastError() {
        return lastError;
    }

    /**
     * Data JTable. Urutan kolom:
     * 0 id_pendaftaran, 1 nim, 2 nama, 3 kelas, 4 kursus,
     * 5 tanggal_daftar, 6 status, 7 id_peserta, 8 id_kelas
     */
    public List<Object[]> getAll(String keyword) {
        List<Object[]> data = new ArrayList<Object[]>();

        String sql = "SELECT p.id_pendaftaran, ps.nim, "
                   + "ps.nama AS nama_peserta, kls.nama_kelas, "
                   + "krs.nama_kursus, p.tanggal_daftar, "
                   + "p.status AS status_pendaftaran, "
                   + "p.id_peserta, p.id_kelas "
                   + "FROM pendaftaran p "
                   + "JOIN peserta ps ON p.id_peserta = ps.id_peserta "
                   + "JOIN kelas kls ON p.id_kelas = kls.id_kelas "
                   + "JOIN kursus krs ON kls.id_kursus = krs.id_kursus "
                   + "WHERE ps.nim LIKE ? OR ps.nama LIKE ? "
                   + "OR kls.nama_kelas LIKE ? OR krs.nama_kursus LIKE ? "
                   + "ORDER BY p.id_pendaftaran DESC";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            String key = "%" + keyword + "%";
            for (int i = 1; i <= 4; i++) {
                p.setString(i, key);
            }

            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    data.add(new Object[]{
                        r.getInt("id_pendaftaran"),
                        r.getString("nim"),
                        r.getString("nama_peserta"),
                        r.getString("nama_kelas"),
                        r.getString("nama_kursus"),
                        r.getDate("tanggal_daftar"),
                        r.getString("status_pendaftaran"),
                        r.getInt("id_peserta"),
                        r.getInt("id_kelas")
                    });
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            lastError = e.getMessage();
        }

        return data;
    }

    /** Isi combo peserta: {id_peserta, "nim - nama"} */
    public List<Object[]> getPeserta() {
        List<Object[]> data = new ArrayList<Object[]>();
        String sql = "SELECT id_peserta, CONCAT(nim, ' - ', nama) AS teks "
                   + "FROM peserta ORDER BY nama";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {

            while (r.next()) {
                data.add(new Object[]{
                    r.getInt("id_peserta"), r.getString("teks")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            lastError = e.getMessage();
        }
        return data;
    }

    /** Isi combo kelas: {id_kelas, "nama_kelas - nama_kursus"} */
    public List<Object[]> getKelas() {
        List<Object[]> data = new ArrayList<Object[]>();
        String sql = "SELECT k.id_kelas, "
                   + "CONCAT(k.nama_kelas, ' - ', u.nama_kursus) AS teks "
                   + "FROM kelas k JOIN kursus u ON k.id_kursus = u.id_kursus "
                   + "ORDER BY k.nama_kelas";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {

            while (r.next()) {
                data.add(new Object[]{
                    r.getInt("id_kelas"), r.getString("teks")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            lastError = e.getMessage();
        }
        return data;
    }

    /** true jika peserta sudah terdaftar di kelas tsb (selain id tertentu). */
    public boolean sudahTerdaftar(int idPeserta, int idKelas,
                                  int kecualiId) {
        String sql = "SELECT COUNT(*) FROM pendaftaran "
                   + "WHERE id_peserta = ? AND id_kelas = ? "
                   + "AND id_pendaftaran <> ?";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setInt(1, idPeserta);
            p.setInt(2, idKelas);
            p.setInt(3, kecualiId);

            try (ResultSet r = p.executeQuery()) {
                return r.next() && r.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            lastError = e.getMessage();
            return false;
        }
    }

    public boolean insert(int idPeserta, int idKelas,
                          String tanggalDaftar, String status) {
        String sql = "INSERT INTO pendaftaran "
                   + "(id_peserta, id_kelas, tanggal_daftar, status) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setInt(1, idPeserta);
            p.setInt(2, idKelas);
            p.setDate(3, Date.valueOf(tanggalDaftar));
            p.setString(4, status);

            return p.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            lastError = e.getMessage();
            return false;
        }
    }

    public boolean update(int idPendaftaran, int idPeserta, int idKelas,
                          String tanggalDaftar, String status) {
        String sql = "UPDATE pendaftaran SET id_peserta=?, id_kelas=?, "
                   + "tanggal_daftar=?, status=? "
                   + "WHERE id_pendaftaran=?";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setInt(1, idPeserta);
            p.setInt(2, idKelas);
            p.setDate(3, Date.valueOf(tanggalDaftar));
            p.setString(4, status);
            p.setInt(5, idPendaftaran);

            return p.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            lastError = e.getMessage();
            return false;
        }
    }

    public boolean delete(int idPendaftaran) {
        String sql = "DELETE FROM pendaftaran WHERE id_pendaftaran=?";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setInt(1, idPendaftaran);
            return p.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            lastError = e.getMessage();
            return false;
        }
    }
}