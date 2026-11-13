
package dao;

import config.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KursusDAO {

    public List<Object[]> getAll(String keyword) {
        List<Object[]> data = new ArrayList<Object[]>();

        String sql = "SELECT * FROM kursus "
                   + "WHERE kode_kursus LIKE ? "
                   + "OR nama_kursus LIKE ? "
                   + "OR level LIKE ? "
                   + "ORDER BY id_kursus DESC";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            String key = "%" + keyword + "%";

            p.setString(1, key);
            p.setString(2, key);
            p.setString(3, key);

            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    data.add(new Object[]{
                        r.getInt("id_kursus"),
                        r.getString("kode_kursus"),
                        r.getString("nama_kursus"),
                        r.getString("level"),
                        r.getBigDecimal("biaya")
                    });
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    public boolean insert(String kode, String nama,
                          String level, double biaya) {

        String sql = "INSERT INTO kursus "
                   + "(kode_kursus, nama_kursus, level, biaya) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setString(1, kode);
            p.setString(2, nama);
            p.setString(3, level);
            p.setDouble(4, biaya);

            return p.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(int id, String kode, String nama,
                          String level, double biaya) {

        String sql = "UPDATE kursus SET kode_kursus=?, "
                   + "nama_kursus=?, level=?, biaya=? "
                   + "WHERE id_kursus=?";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setString(1, kode);
            p.setString(2, nama);
            p.setString(3, level);
            p.setDouble(4, biaya);
            p.setInt(5, id);

            return p.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM kursus WHERE id_kursus=?";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setInt(1, id);
            return p.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
