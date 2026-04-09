
package com.placement.debug;

import java.sql.*;

public class FixUsers {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/placement_db?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "root";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("[FIX] Connected to database.");
            
            // 1. Identify users with empty email
            String selectQuery = "SELECT id, name, role FROM users WHERE email IS NULL OR email = '' OR email = 'null'";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(selectQuery)) {
                
                while (rs.next()) {
                    long id = rs.getLong("id");
                    String name = rs.getString("name");
                    String role = rs.getString("role");
                    
                    // 2. Restore a fallback email based on name
                    String fallbackEmail = (name != null ? name.toLowerCase().replaceAll("\\s+", "") : "user" + id) + "@gmail.com";
                    System.out.println("[FIX] Found corrupted user ID: " + id + " | Name: " + name + " | Role: " + role);
                    System.out.println("[FIX] Restoring email to: " + fallbackEmail);
                    
                    try (PreparedStatement upStmt = conn.prepareStatement("UPDATE users SET email = ? WHERE id = ?")) {
                        upStmt.setString(1, fallbackEmail);
                        upStmt.setLong(2, id);
                        upStmt.executeUpdate();
                    }
                }
            }
            
            // 3. List all users for diagnostic
            System.out.println("\n[DB STATUS] Current User Table:");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT id, name, email, role FROM users")) {
                while (rs.next()) {
                    System.out.printf("%d | %s | %s | %s\n", rs.getLong("id"), rs.getString("name"), rs.getString("email"), rs.getString("role"));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("[ERROR] DB Fix failed: " + e.getMessage());
        }
    }
}
