package com.example.zoo.db;

import java.sql.*;

/**
 * DAO for the ZooTable table.
 * ZooTable(ZooId PK, ZooName, City, Street, Number)
 */
public class ZooDAO {

    public void insertZoo(int zooId, String zooName, String city, String street, String number) throws SQLException {
        String sql = "INSERT INTO ZooTable (ZooId, ZooName, City, Street, Number) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, zooId);
            ps.setString(2, zooName);
            ps.setString(3, city);
            ps.setString(4, street);
            ps.setString(5, number);
            ps.executeUpdate();
        }
    }

    public void findZooById(int zooId) throws SQLException {
        String sql = "SELECT * FROM ZooTable WHERE ZooId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, zooId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("ZooId: " + rs.getInt("zooid") +
                            ", Name: " + rs.getString("zooname") +
                            ", City: " + rs.getString("city") +
                            ", Street: " + rs.getString("street") +
                            ", Number: " + rs.getString("number"));
                } else {
                    System.out.println("No zoo found with ZooId " + zooId);
                }
            }
        }
    }

    public void updateZoo(int zooId, String zooName, String city, String street, String number) throws SQLException {
        String sql = "UPDATE ZooTable SET ZooName = ?, City = ?, Street = ?, Number = ? WHERE ZooId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, zooName);
            ps.setString(2, city);
            ps.setString(3, street);
            ps.setString(4, number);
            ps.setInt(5, zooId);
            int rows = ps.executeUpdate();
            System.out.println("Updated " + rows + " row(s).");
        }
    }

    public void deleteZoo(int zooId) throws SQLException {
        // NOTE: Animals.ZooId is a foreign key to ZooTable.
        // A zoo can only be deleted once it has no animals referencing it.
        String sql = "DELETE FROM ZooTable WHERE ZooId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, zooId);
            int rows = ps.executeUpdate();
            System.out.println("Deleted " + rows + " row(s).");
        }
    }
}
