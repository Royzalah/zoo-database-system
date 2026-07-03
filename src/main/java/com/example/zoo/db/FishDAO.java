package com.example.zoo.db;

import java.sql.*;

/**
 * DAO for FISH, which extends Animals, plus its multivalued FISH_COLORS table.
 * FISH(AnimalID PK/FK -> Animals, FishLength, FishPattern, FishSpecies)
 * FISH_COLORS(AnimalID FK -> FISH, FishColor, composite PK)
 *
 * A single fish touches THREE tables (Animals, FISH, FISH_COLORS - one row
 * per color), so the whole insert/delete runs inside one transaction.
 */
public class FishDAO {

    public void insertFish(int animalId, int age, int happiness, int zooId,
                            double length, String pattern, String species, String[] colors) throws SQLException {

        String insertAnimalSQL = "INSERT INTO Animals (AnimalID, Age, Happiness, ZooId, AnimalType) VALUES (?, ?, ?, ?, ?)";
        String insertFishSQL = "INSERT INTO FISH (AnimalID, FishLength, FishPattern, FishSpecies) VALUES (?, ?, ?, ?)";
        String insertColorSQL = "INSERT INTO FISH_COLORS (AnimalID, FishColor) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement(insertAnimalSQL)) {
                    ps.setInt(1, animalId);
                    ps.setInt(2, age);
                    ps.setInt(3, happiness);
                    ps.setInt(4, zooId);
                    ps.setString(5, "Fish");
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = conn.prepareStatement(insertFishSQL)) {
                    ps.setInt(1, animalId);
                    ps.setDouble(2, length);
                    ps.setString(3, pattern);
                    ps.setString(4, species);
                    ps.executeUpdate();
                }

                if (colors != null) {
                    try (PreparedStatement ps = conn.prepareStatement(insertColorSQL)) {
                        for (String color : colors) {
                            ps.setInt(1, animalId);
                            ps.setString(2, color);
                            ps.executeUpdate();
                        }
                    }
                }

                conn.commit();
                System.out.println("Fish inserted successfully (AnimalID " + animalId + ").");
            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Insert failed, transaction rolled back.");
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public void findFishById(int animalId) throws SQLException {
        String fishSQL = "SELECT a.AnimalID, a.Age, a.Happiness, f.FishLength, f.FishPattern, f.FishSpecies " +
                "FROM Animals a JOIN FISH f ON f.AnimalID = a.AnimalID WHERE a.AnimalID = ?";
        String colorsSQL = "SELECT FishColor FROM FISH_COLORS WHERE AnimalID = ?";

        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(fishSQL)) {
                ps.setInt(1, animalId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        System.out.println(rs.getString("fishspecies") +
                                " (ID " + rs.getInt("animalid") + ") - " +
                                rs.getDouble("fishlength") + "cm, " +
                                rs.getString("fishpattern") +
                                ", Age: " + rs.getInt("age") +
                                ", Happiness: " + rs.getInt("happiness"));
                    } else {
                        System.out.println("No fish found with AnimalID " + animalId);
                        return;
                    }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(colorsSQL)) {
                ps.setInt(1, animalId);
                try (ResultSet rs = ps.executeQuery()) {
                    System.out.print("Colors: ");
                    while (rs.next()) {
                        System.out.print(rs.getString("fishcolor") + " ");
                    }
                    System.out.println();
                }
            }
        }
    }

    public void deleteFish(int animalId) throws SQLException {
        String deleteColorsSQL = "DELETE FROM FISH_COLORS WHERE AnimalID = ?";
        String deleteFishSQL = "DELETE FROM FISH WHERE AnimalID = ?";
        String deleteAnimalSQL = "DELETE FROM Animals WHERE AnimalID = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Delete order matters: FISH_COLORS -> FISH -> Animals
                // (children before parents, to respect the foreign keys)
                try (PreparedStatement ps = conn.prepareStatement(deleteColorsSQL)) {
                    ps.setInt(1, animalId);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement(deleteFishSQL)) {
                    ps.setInt(1, animalId);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement(deleteAnimalSQL)) {
                    ps.setInt(1, animalId);
                    ps.executeUpdate();
                }
                conn.commit();
                System.out.println("Fish deleted successfully (AnimalID " + animalId + ").");
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}
