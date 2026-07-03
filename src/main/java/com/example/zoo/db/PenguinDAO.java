package com.example.zoo.db;

import java.sql.*;

/**
 * DAO for PENGUIN, which extends the base Animals table.
 * PENGUIN(AnimalID PK/FK -> Animals, PenguinName, PenguinHeight, PenguinRank)
 */
public class PenguinDAO {

    public void insertPenguin(int animalId, int age, int happiness, int zooId,
                               String name, double height, int rank) throws SQLException {

        String insertAnimalSQL = "INSERT INTO Animals (AnimalID, Age, Happiness, ZooId, AnimalType) VALUES (?, ?, ?, ?, ?)";
        String insertPenguinSQL = "INSERT INTO PENGUIN (AnimalID, PenguinName, PenguinHeight, PenguinRank) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement(insertAnimalSQL)) {
                    ps.setInt(1, animalId);
                    ps.setInt(2, age);
                    ps.setInt(3, happiness);
                    ps.setInt(4, zooId);
                    ps.setString(5, "Penguin");
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = conn.prepareStatement(insertPenguinSQL)) {
                    ps.setInt(1, animalId);
                    ps.setString(2, name);
                    ps.setDouble(3, height);
                    ps.setInt(4, rank);
                    ps.executeUpdate();
                }

                conn.commit();
                System.out.println("Penguin inserted successfully (AnimalID " + animalId + ").");
            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Insert failed, transaction rolled back.");
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public void findPenguinById(int animalId) throws SQLException {
        String sql = "SELECT a.AnimalID, a.Age, a.Happiness, p.PenguinName, p.PenguinHeight, p.PenguinRank " +
                "FROM Animals a JOIN PENGUIN p ON p.AnimalID = a.AnimalID WHERE a.AnimalID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, animalId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println(rs.getString("penguinname") +
                            " (ID " + rs.getInt("animalid") + ") - Rank " +
                            rs.getInt("penguinrank") + ", " +
                            rs.getDouble("penguinheight") + "cm" +
                            ", Age: " + rs.getInt("age") +
                            ", Happiness: " + rs.getInt("happiness"));
                } else {
                    System.out.println("No penguin found with AnimalID " + animalId);
                }
            }
        }
    }

    public void updatePenguinRank(int animalId, int newRank) throws SQLException {
        String sql = "UPDATE PENGUIN SET PenguinRank = ? WHERE AnimalID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newRank);
            ps.setInt(2, animalId);
            int rows = ps.executeUpdate();
            System.out.println("Updated " + rows + " row(s).");
        }
    }

    public void deletePenguin(int animalId) throws SQLException {
        String deletePenguinSQL = "DELETE FROM PENGUIN WHERE AnimalID = ?";
        String deleteAnimalSQL = "DELETE FROM Animals WHERE AnimalID = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement(deletePenguinSQL)) {
                    ps.setInt(1, animalId);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement(deleteAnimalSQL)) {
                    ps.setInt(1, animalId);
                    ps.executeUpdate();
                }
                conn.commit();
                System.out.println("Penguin deleted successfully (AnimalID " + animalId + ").");
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}
