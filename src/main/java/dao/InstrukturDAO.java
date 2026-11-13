
package dao;

import config.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InstrukturDAO {

    public List<Object[]> getAll(String keyword) {
        List<Object[]> data = new ArrayList<Object[]>();

        String sql = "SELECT * FROM instruktur "
                   + "WHERE nama LIKE ? OR keahlian LIKE ? "
                   + "OR no_hp LIKE ? OR email LIKE ? "
                   + "ORDER BY id_instruktur DESC";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            String key = "%" + keyword + "%";

            for (int i = 1; i <= 4; i++) {
                p.setString(i, key);
            }

            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    data.add(new Object[]{
                        r.getInt("id_instruktur"),
                        r.getString("nama"),
                        r.getString("keahlian"),
                        r.getString("no_hp"),
                        r.getString("email")
                    });
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    public boolean insert(String nama, String keahlian,
                          String noHp, String email) {

        String sql = "INSERT INTO instruktur "
                   + "(nama, keahlian, no_hp, email) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setString(1, nama);
            p.setString(2, keahlian);
            p.setString(3, noHp);
            p.setString(4, email);

            return p.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(int id, String nama, String keahlian,
                          String noHp, String email) {

        String sql = "UPDATE instruktur SET nama=?, keahlian=?, "
                   + "no_hp=?, email=? WHERE id_instruktur=?";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setString(1, nama);
            p.setString(2, keahlian);
            p.setString(3, noHp);
            p.setString(4, email);
            p.setInt(5, id);

            return p.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM instruktur WHERE id_instruktur=?";

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
