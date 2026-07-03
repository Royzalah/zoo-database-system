package com.example.zoo.db;

import java.sql.*;

/**
 * DAO for PREDATOR, which extends the base Animals table.
 * PREDATOR(AnimalID PK/FK -> Animals, PredatorName, PredatorWeight, PredatorGender, PredatorSpecies)
 *
 * Inserting or deleting a predator touches TWO tables (Animals + PREDATOR),
 * so both operations run inside a single transaction: either both rows
 * are written/removed, or neither is - the database is never left
 * with a "ghost" Animals row that has no PREDATOR details.
 */
public class PredatorDAO {

    public void insertPredator(int animalId, int age, int happiness, int zooId,
                                String predatorName, double weight, String gender, String species) throws SQLException {

        String insertAnimalSQL = "INSERT INTO Animals (AnimalID, Age, Happiness, ZooId, AnimalType) VALUES (?, ?, ?, ?, ?)";
        String insertPredatorSQL = "INSERT INTO PREDATOR (AnimalID, PredatorName, PredatorWeight, PredatorGender, PredatorSpecies) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement(insertAnimalSQL)) {
                    ps.setInt(1, animalId);
                    ps.setInt(2, age);
                    ps.setInt(3, happiness);
                    ps.setInt(4, zooId);
                    ps.setString(5, "Predator");
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = conn.prepareStatement(insertPredatorSQL)) {
                    ps.setInt(1, animalId);
                    ps.setString(2, predatorName);
                    ps.setDouble(3, weight);
                    ps.setString(4, gender);
                    ps.setString(5, species);
                    ps.executeUpdate();
                }

                conn.commit();
                System.out.println("Predator inserted successfully (AnimalID " + animalId + ").");
            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Insert failed, transaction rolled back.");
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public void findPredatorById(int animalId) throws SQLException {
        String sql = "SELECT a.AnimalID, a.Age, a.Happiness, a.ZooId, " +
                "p.PredatorName, p.PredatorWeight, p.PredatorGender, p.PredatorSpecies " +
                "FROM Animals a JOIN PREDATOR p ON p.AnimalID = a.AnimalID " +
                "WHERE a.AnimalID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, animalId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println(rs.getString("predatorname") +
                            " (ID " + rs.getInt("animalid") + ") - " +
                            rs.getString("predatorspecies") + ", " +
                            rs.getDouble("predatorweight") + "kg, " +
                            rs.getString("predatorgender") +
                            ", Age: " + rs.getInt("age") +
                            ", Happiness: " + rs.getInt("happiness"));
                } else {
                    System.out.println("No predator found with AnimalID " + animalId);
                }
            }
        }
    }

    public void updatePredator(int animalId, double newWeight, int newHappiness) throws SQLException {
        String updatePredatorSQL = "UPDATE PREDATOR SET PredatorWeight = ? WHERE AnimalID = ?";
        String updateAnimalSQL = "UPDATE Animals SET Happiness = ? WHERE AnimalID = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement(updatePredatorSQL)) {
                    ps.setDouble(1, newWeight);
                    ps.setInt(2, animalId);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement(updateAnimalSQL)) {
                    ps.setInt(1, newHappiness);
                    ps.setInt(2, animalId);
                    ps.executeUpdate();
                }
                conn.commit();
                System.out.println("Predator updated successfully (AnimalID " + animalId + ").");
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public void deletePredator(int animalId) throws SQLException {
        String deletePredatorSQL = "DELETE FROM PREDATOR WHERE AnimalID = ?";
        String deleteAnimalSQL = "DELETE FROM Animals WHERE AnimalID = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Child row (PREDATOR) must be deleted before the parent row (Animals)
                try (PreparedStatement ps = conn.prepareStatement(deletePredatorSQL)) {
                    ps.setInt(1, animalId);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement(deleteAnimalSQL)) {
                    ps.setInt(1, animalId);
                    ps.executeUpdate();
                }
                conn.commit();
                System.out.println("Predator deleted successfully (AnimalID " + animalId + ").");
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}
