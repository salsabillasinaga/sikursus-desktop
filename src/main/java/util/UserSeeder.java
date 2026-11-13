package util;

import dao.UserDAO;

public class UserSeeder {

    public static void main(String[] args) {

        UserDAO dao = new UserDAO();

        if (!dao.usernameExists("admin")) {
            boolean admin = dao.insertUser(
                "admin",
                "Admin@123",
                "ADMIN"
            );

            System.out.println(
                admin ? "Akun admin berhasil dibuat."
                      : "Akun admin gagal dibuat."
            );
        } else {
            System.out.println("Akun admin sudah tersedia.");
        }

        if (!dao.usernameExists("operator")) {
            boolean operator = dao.insertUser(
                "operator",
                "Operator@123",
                "OPERATOR"
            );

            System.out.println(
                operator ? "Akun operator berhasil dibuat."
                         : "Akun operator gagal dibuat."
            );
        } else {
            System.out.println("Akun operator sudah tersedia.");
        }
    }
}