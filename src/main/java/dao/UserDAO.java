package dao;

import config.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.User;
import util.PasswordUtil;

public class UserDAO {

    public User login(String username, String password) {

        String sql = "SELECT * FROM users "
                   + "WHERE username = ? AND aktif = 1";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    String passwordHash =
                        rs.getString("password_hash");

                    if (PasswordUtil.verifyPassword(
                            password, passwordHash)) {

                        return new User(
                            rs.getInt("id_user"),
                            rs.getString("username"),
                            passwordHash,
                            rs.getString("role"),
                            rs.getBoolean("aktif")
                        );
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean usernameExists(String username) {

        String sql = "SELECT id_user FROM users WHERE username = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean insertUser(
            String username, String password, String role) {

        String sql = "INSERT INTO users "
                   + "(username, password_hash, role, aktif) "
                   + "VALUES (?, ?, ?, 1)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, PasswordUtil.hashPassword(password));
            ps.setString(3, role);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}