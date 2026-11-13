
package dao;

import config.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KelasDAO {

    public List<Object[]> getAll(String keyword) {
        List<Object[]> data = new ArrayList<Object[]>();

        String sql = "SELECT k.id_kelas, k.nama_kelas, "
                   + "u.nama_kursus, i.nama AS nama_instruktur, "
                   + "k.tanggal_mulai, k.kapasitas, "
                   + "k.id_kursus, k.id_instruktur "
                   + "FROM kelas k "
                   + "JOIN kursus u ON k.id_kursus = u.id_kursus "
                   + "JOIN instruktur i ON k.id_instruktur = i.id_instruktur "
                   + "WHERE k.nama_kelas LIKE ? "
                   + "OR u.nama_kursus LIKE ? "
                   + "OR i.nama LIKE ? "
                   + "ORDER BY k.id_kelas DESC";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            String key = "%" + keyword + "%";

            p.setString(1, key);
            p.setString(2, key);
            p.setString(3, key);

            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    data.add(new Object[]{
                        r.getInt("id_kelas"),
                        r.getString("nama_kelas"),
                        r.getString("nama_kursus"),
                        r.getString("nama_instruktur"),
                        r.getDate("tanggal_mulai"),
                        r.getInt("kapasitas"),
                        r.getInt("id_kursus"),
                        r.getInt("id_instruktur")
                    });
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    public List<Object[]> getKursus() {
        List<Object[]> data = new ArrayList<Object[]>();

        String sql = "SELECT id_kursus, nama_kursus "
                   + "FROM kursus ORDER BY nama_kursus";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {

            while (r.next()) {
                data.add(new Object[]{
                    r.getInt("id_kursus"),
                    r.getString("nama_kursus")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    public List<Object[]> getInstruktur() {
        List<Object[]> data = new ArrayList<Object[]>();

        String sql = "SELECT id_instruktur, nama "
                   + "FROM instruktur ORDER BY nama";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {

            while (r.next()) {
                data.add(new Object[]{
                    r.getInt("id_instruktur"),
                    r.getString("nama")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    public boolean insert(int idKursus, int idInstruktur,
                          String namaKelas, String tanggalMulai,
                          int kapasitas) {

        String sql = "INSERT INTO kelas "
                   + "(id_kursus, id_instruktur, nama_kelas, "
                   + "tanggal_mulai, kapasitas) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setInt(1, idKursus);
            p.setInt(2, idInstruktur);
            p.setString(3, namaKelas);
            p.setDate(4, Date.valueOf(tanggalMulai));
            p.setInt(5, kapasitas);

            return p.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(int idKelas, int idKursus,
                          int idInstruktur, String namaKelas,
                          String tanggalMulai, int kapasitas) {

        String sql = "UPDATE kelas SET id_kursus=?, "
                   + "id_instruktur=?, nama_kelas=?, "
                   + "tanggal_mulai=?, kapasitas=? "
                   + "WHERE id_kelas=?";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setInt(1, idKursus);
            p.setInt(2, idInstruktur);
            p.setString(3, namaKelas);
            p.setDate(4, Date.valueOf(tanggalMulai));
            p.setInt(5, kapasitas);
            p.setInt(6, idKelas);

            return p.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int idKelas) {
        String sql = "DELETE FROM kelas WHERE id_kelas=?";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setInt(1, idKelas);
            return p.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
