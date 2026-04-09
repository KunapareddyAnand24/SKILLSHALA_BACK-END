
package com.placement.debug;

import java.sql.*;

public class CheckUsers {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/placement_db";
        String user = "root";
        String password = ""; // Assuming empty password based on common local setups or prompt them if it fails

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to the database!");
            String query = "SELECT id, name, email, role FROM users";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                
                System.out.println("ID | Name | Email | Role");
                System.out.println("-------------------------");
                while (rs.next()) {
                    System.out.printf("%d | %s | %s | %s\n", 
                        rs.getLong("id"), 
                        rs.getString("name"), 
                        rs.getString("email"), 
                        rs.getString("role"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error connecting: " + e.getMessage());
        }
    }
}
