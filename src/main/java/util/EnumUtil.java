package util;

import config.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Membaca daftar nilai kolom ENUM langsung dari database,
 * sehingga isi ComboBox (status, metode) selalu sama dengan database.
 * Jika kolom bukan ENUM / gagal dibaca, dipakai nilai cadangan.
 */
public final class EnumUtil {

    private EnumUtil() {}

    public static String[] ambil(String tabel, String kolom,
                                 String[] cadangan) {
        String sql = "SELECT COLUMN_TYPE FROM INFORMATION_SCHEMA.COLUMNS "
                   + "WHERE TABLE_SCHEMA = DATABASE() "
                   + "AND TABLE_NAME = ? AND COLUMN_NAME = ?";

        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {

            p.setString(1, tabel);
            p.setString(2, kolom);

            try (ResultSet r = p.executeQuery()) {
                if (r.next()) {
                    String tipe = r.getString(1);

                    if (tipe != null
                            && tipe.toLowerCase().startsWith("enum(")) {

                        List<String> hasil = new ArrayList<String>();
                        Matcher m = Pattern
                                .compile("'((?:[^']|'')*)'")
                                .matcher(tipe);

                        while (m.find()) {
                            hasil.add(m.group(1).replace("''", "'"));
                        }

                        if (!hasil.isEmpty()) {
                            return hasil.toArray(new String[0]);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cadangan;
    }
}